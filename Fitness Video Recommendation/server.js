// Run a node.js web server for local development of a static web site.
// Start with "node server.js" and put pages in a "public" sub-folder.
// Visit the site at the address printed on the console.

// The server is configured to be platform independent.  URLs are made lower
// case, so the server is case insensitive even on Linux, and paths containing
// upper case letters are banned so that the file system is treated as case
// sensitive even on Windows.  All .html files are delivered as
// application/xhtml+xml for instant feedback on XHTML errors.  To improve the
// server, either add content negotiation (so old browsers can be tested) or
// switch to text/html and do validity checking another way (e.g. with vnu.jar).

// Choose a port, e.g. change the port to the default 80, if there are no
// privilege issues and port number 80 isn't already in use. Choose verbose to
// list banned files (with upper case letters) on startup.
"use strict"
var port = 8080;
var verbose = true;

// Load the library modules, and define the global constants.
// See http://en.wikipedia.org/wiki/List_of_HTTP_status_codes.
// Start the server:

var https = require("https");
var fs = require("fs");
var sql = require("sqlite3");
var db = new sql.Database("data.db");
//conn.connet();
var OK = 200, NotFound = 404, BadType = 415, Error = 500;
var types, banned;
start();

// Start the http service. Accept only requests from localhost, for security.
function start() {
    if (! checkSite()) return;
    types = defineTypes();
    banned = [];
    banUpperCase("./public/", "");
    var options = {
        key: fs.readFileSync("server.key"),
        cert: fs.readFileSync("server.crt"),
        passphrase: "2018WebTech",
        requestCert: false,
        rejectUnauthorized: false
    };
    var service = https.createServer(options, handle);
    service.listen(port, "localhost");
    var address = "https://localhost";
    if (port != 80) address = address + ":" + port;
    console.log("Server running at", address);
}

// chong 5/15, 23:00
// Serve a request by delivering a file.
function handle(request, response) {

    //chenxi write user's history to cookie
    writeHistory(request, response);

    var url = request.url;
    if (url.endsWith("/")) {
        url = url + "index.html";
    }
    if (url.startsWith("/video.html")) return getVideo(url, response);

    //chenxi 5/21 Parsing data by cookie
    //url is checked
    if (url.startsWith("/prefer_videos")) {
        var cookies = request.headers.cookie.split(";");
        for (let i = 0; i < cookies.length; i++) {
            if (cookies[i].includes("preIndex=")) {
                var preIndexFromCookie = cookies[i].split("Index=")[1];
                if(isNumber(parseInt(preIndexFromCookie)) && isValid(parseInt(preIndexFromCookie))) {
                    return getMain(response, preIndexFromCookie);
                }
                return fail(response, NotFound, "URL has been banned");
            }
        }
    }

    //chenxi 5/22 history page
    if (url.startsWith("/history_videos")) {
        return getHistoryVideos(request, response);
    }

    if (url == "/data") return getList(response);
    if (isBanned(url)) return fail(response, NotFound, "URL has been banned");
    var type = findType(url);
    if (type == null) return fail(response, BadType, "File type unsupported");
    var file = "./public" + url;
    fs.readFile(file, ready);
    function ready(err, content) { deliver(response, type, err, content); }
}

//chenxi 5/21 cookies
// videoId[1] === "http://localhost:8080/video.html?" is used to make sure no insidious input
function writeHistory(request, response){
    var numOfHis = 21;
    var ref = request.headers.referer;

    if(ref) {
        var refArray= request.headers.referer.split("id=");
        if(refArray.length === 2 && refArray[0].startsWith("https")) {
            var videoId = refArray[1];
            //get current history
            var videoIdArray = readHistoryFromCookie(request);
            if (!isHistoryContains(videoIdArray, videoId)) {
                if (videoIdArray.length === numOfHis) {
                    videoIdArray.shift();
                }
                videoIdArray.push(videoId);
                writeArrayToCookie(response, videoIdArray);
            }
        }
    }
}

//chenxi 5/22
function writeArrayToCookie(response, videoIdArray) {
    var historyString = videoIdArray[0];
    for (let i = 1; i < videoIdArray.length; i++) {
        historyString += "/" + videoIdArray[i];
    }
    response.setHeader("Set-Cookie",
        ["history=" + historyString+ ";expires=" + new Date(new Date().getTime()+86409000).toUTCString()]);

}

//Chenxi 5/22
function readHistoryFromCookie(request) {
    var cookies = request.headers.cookie.split(";");
    var historyString;
    var videoIdArray = [];
    for (let i = 0; i < cookies.length; i++) {
        if (cookies[i].includes("history")) {
            historyString = cookies[i].split("=")[1];
            videoIdArray = historyString.split("/");
            break;
        }
    }
    return videoIdArray;
}

//chenxi 5/22
function isHistoryContains(videoIdArray, videoId) {
    for (var i = 0; i < videoIdArray.length; i++) {
        if (videoIdArray[i] === videoId) {
            return true;
        }
    }
    return false;
}

//************************************
//Chenxi 5/22

//get history videos and send it to front end
function getHistoryVideos(request, response) {
    var historyVideoArray = readHistoryFromCookie(request);
    if (historyVideoArray.length === 0) {
        return fail(response, NotFound, "You have no records!");
    }
    fs.readFile("./history.html", ready);
    function ready(err, content) {
        getHistoryData(content, response, historyVideoArray);
    }
}

//async should be used to solve the problem
async function getHistoryData(text, response, historyVideoArray) {
    var page = '';
    text = text+'';
    var parts = text.split("$");
    var end = parts[23];
    page = page + parts[0];

    for (let i = 0; i < historyVideoArray.length; i++) {
        var preps = await db.prepare("select * from Videos where id=?");
        await new Promise(resolve => setTimeout(resolve, 100));
        preps.get(historyVideoArray[i],  (err, row) => {
            if (err || !row) {
                return fail(response, NotFound, "File not found");
            }
            page =  prepareMain(row, page, parts, response, i, end, historyVideoArray.length - 1);
        });
    }
}

//end
//************************************

// chong 5/15, 23:00
// Main page with videos
function getMain(response, preIndex) {
    fs.readFile("./videomain.html", ready);
    function ready(err, content) {
        if (err) {
            return fail(response, NotFound, "File not found");
        }
        getMainData(content, response, preIndex);
    }
}

// Call videos from the database, calculate similarity to a user preference,
// and insert those videos to the HTML template
function getMainData(text, response, preIndex) {
    var page='';
    text = text+'';
    var parts = text.split("$");
    var end = parts[23];
    var ps = db.prepare("select * from Videos");
    page = page + parts[0];
    //18 containers
    var videoLib = new Array(18);
    for (var i = 0; i < 18 ; i++) {
        videoLib[i] = new Array(0);
    }
    var counter = 0;
    ps.each(function ready(err, obj) {
        var similarIndex = similarity(preIndex, parseInt(obj.labels));
        if (similarIndex != 0) {
            videoLib[similarIndex].push(obj);
        }
        counter++;
        //2056 is database size
        if (counter === 2056) {
            var dbpos = 0;
            for (var q = 17; q > 0; q-- ) {
                for (var r = 0; r < videoLib[q].length; r++) {
                    //chenxi 5/22
                    page = prepareMain(videoLib[q][r], page, parts, response, dbpos, end, 101);
                    dbpos++;
                }
            }
        }
    });
}

// Chenxi 5/22
// Chong 5/16, 22:30
// Return page with inserting data from the database
function prepareMain(data, page, parts, response, dbpos, end, videoNum) {
    var pos = (dbpos % 3 == 1) ? 8 : (dbpos % 3 == 2) ? 15 : 1;
    page = page + parts[pos] + data.id + parts[pos+1] + data.id + parts[pos+2] +
        data.id + parts[pos+3] + data.title.replace("& ", "&amp; ") + parts[pos+4] +
        data.owner.replace("& ", "&amp; ") + parts[pos+5] + data.view + parts[pos+6] + data.created;
    if (dbpos %3 === 2) {
        page = page + parts[22];
    }
    if(dbpos === videoNum) {
        if (dbpos % 3 === 0 || dbpos % 3 === 1) {
            page = page + parts[22];
        }
        page = page + end;
        deliver(response, "application/xhtml+xml", null, page);
    }
    return page;
}

// chong 5/16, 17:30
// To make second page showing individual video players + playlists
function getVideo(url, response) {
    fs.readFile("./video.html", ready);
    function ready(err, content) {
        getData(content, url, response);
    }
}

// chong 5/16, 23:00
// Display other all videos (needs to be modified later) as the playlist
// chong 5/21, 00:00 (korea)
// Changed playlists according to similarity between videos
function getData(text, url, response) {
    var mainlabel;
    url = url+'';
    var parts = url.split("=");
    var id = parts[1];
    text = text+'';
    var textparts= text.split("$");
    var page = textparts[0] + id + textparts[1];
    var preps = db.prepare("select * from Videos where id=?");
    preps.get(id, ready);

    function ready(err, row) {
        if (err || !row) {
            return fail(response, NotFound, "File not found");
        }
        page = readyVideo(row, page, textparts);
        mainlabel = row.labels;
    }

    var dbsize = 0;
    var ps = db.prepare("select * from Videos");
    ps.each(function ready(err, obj) {
        if (! (String(obj.id).valueOf() == id.valueOf())) {
            var similarIndex = similarity(mainlabel, parseInt(obj.labels));

            if (similarIndex != 0) {
                page = prepare(obj, textparts, page, response, dbsize);
                dbsize ++;
            }
        }
    });
}

function readyVideo(data, page, textparts) {
    page = page + data.title + textparts[2] + data.view + textparts[3] +
        data.owner + textparts[4];
    return page;
}

// chong 5/16, 17:30
function prepare(data, textparts, page, response, dbsize) {
    var pos = 5;
    page = page + textparts[pos] + data.id + textparts[pos+1] + data.id +
        textparts[pos+2] + data.id + textparts[pos+3] + data.title + textparts[pos+4] + data.owner +
        textparts[pos+5] + data.view + textparts[pos+6] + data.created + textparts[pos+7];

    if (dbsize == 10) {
        page = page + textparts[13];
        deliver(response, "application/xhtml+xml", null, page);
    }
    return page;
}

// Check that the site folder and index page exist.
function checkSite() {
    var path = "./public";
    var ok = fs.existsSync(path);
    if (ok) path = "./public/index.html";
    if (ok) ok = fs.existsSync(path);
    if (! ok) console.log("Can't find", path);
    return ok;
}

// Forbid any resources which shouldn't be delivered to the browser.
function isBanned(url) {
    for (var i=0; i<banned.length; i++) {
        var b = banned[i];
        if (url.startsWith(b)) return true;
    }
    return false;
}

// Find the content type to respond with, or undefined.
function findType(url) {
    var dot = url.lastIndexOf(".");
    var extension = url.substring(dot + 1);
    return types[extension];
}

// Deliver the file that has been read in to the browser.
function deliver(response, type, err, content) {
    if (err) return fail(response, NotFound, "File not found");
    var typeHeader = { "Content-Type": type };
    response.writeHead(OK, typeHeader);
    response.write(content);
    response.end();
}

// Give a minimal failure response to the browser
function fail(response, code, text) {
    var textTypeHeader = { "Content-Type": "text/plain" };
    response.writeHead(code, textTypeHeader);
    response.write(text, "utf8");
    response.end();
}

// Check a folder for files/subfolders with non-lowercase names.  Add them to
// the banned list so they don't get delivered, making the site case sensitive,
// so that it can be moved from Windows to Linux, for example. Synchronous I/O
// is used because this function is only called during startup.  This avoids
// expensive file system operations during normal execution.  A file with a
// non-lowercase name added while the server is running will get delivered, but
// it will be detected and banned when the server is next restarted.
function banUpperCase(root, folder) {
    var folderBit = 1 << 14;
    var names = fs.readdirSync(root + folder);
    for (var i=0; i<names.length; i++) {
        var name = names[i];
        var file = folder + "/" + name;
        if (name != name.toLowerCase()) {
            if (verbose) console.log("Banned:", file);
            banned.push(file.toLowerCase());
        }
        var mode = fs.statSync(root + file).mode;
        if ((mode & folderBit) == 0) continue;
        banUpperCase(root, file);
    }
}

// The most common standard file extensions are supported, and html is
// delivered as "application/xhtml+xml".  Some common non-standard file
// extensions are explicitly excluded.  This table is defined using a function
// rather than just a global variable, because otherwise the table would have
// to appear before calling start().  NOTE: add entries as needed or, for a more
// complete list, install the mime module and adapt the list it provides.
function defineTypes() {
    var types = {
        html : "application/xhtml+xml",
        css  : "text/css",
        js   : "application/javascript",
        mjs  : "application/javascript", // for ES6 modules
        png  : "image/png",
        gif  : "image/gif",    // for images copied unchanged
        jpeg : "image/jpeg",   // for images copied unchanged
        jpg  : "image/jpeg",   // for images copied unchanged
        svg  : "image/svg+xml",
        json : "application/json",
        pdf  : "application/pdf",
        txt  : "text/plain",
        ttf  : "application/x-font-ttf",
        woff : "application/font-woff",
        aac  : "audio/aac",
        mp3  : "audio/mpeg",
        mp4  : "video/mp4",
        webm : "video/webm",
        ico  : "image/x-icon", // just for favicon.ico
        xhtml: undefined,      // non-standard, use .html
        htm  : undefined,      // non-standard, use .html
        rar  : undefined,      // non-standard, platform dependent, use .zip
        doc  : undefined,      // non-standard, platform dependent, use .pdf
        docx : undefined,      // non-standard, platform dependent, use .pdf
    }
    return types;
}

//just changed spell! similarity
/*****************************************************/

//Calculate the similarity of two movie, Chenxi
function similarity(preIndex, videoIndex) {
    //console.log(preIndex);
    //console.log(videoIndex);
    var numOfSame = 0;
    var simliarIndex = preIndex & videoIndex;
    while (simliarIndex !== 0) {
        numOfSame++;
        simliarIndex = simliarIndex & (simliarIndex - 1);
    }
    return numOfSame;
}

//rank videos, chenxi
function rankVideo(preIndex, videoIndex, videoAddress) {
    var videoLib = new Array(18);
    for (var i = 0; i < 18 ; i++) {
        videoLib[i] = new Array();
    }
    for (var i = 0; i < videoIndex.length; i++) {
        var similarIndex = similarity(preIndex, videoIndex);
        videoLib[similarIndex].push(videoAddress[i]);
    }
    return videoLib;
}

//check is number, chenxi
function isNumber(val){
    return typeof val === 'number' && isFinite(val);
}

//13071 is the largest number, if user choose all.
//31 is the smallest number, if user choose 1,2,4,8,16
function isValid(val) {
    if (val > 131071 || val < 31) {
        return false;
    }
    else {
        return true;
    }

}
