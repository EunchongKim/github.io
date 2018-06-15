"use strict";
//This function is used to collect data

var addressArray = new Array();
var indexArray = new Array();
parseData();

var sql = require("sqlite3");
var db = new sql.Database("data.db");
db.serialize(create);
db.serialize(insertVideo);

function create() {
    /*    db.run("drop table if exists animals");
        db.run("create table animals (id, breed)");
        db.run("insert into animals values (1,'dog')");
        db.run("insert into animals values (2,'fish')");
    */
    db.run("drop table if exists Labels");
    db.run("create table Labels (id, name)");
    db.run("insert into Labels values (1,'Weight Training')");
    db.run("insert into Labels values (2,'Gym')");
    db.run("insert into Labels values (3,'Squat')");
    db.run("insert into Labels values (4,'Stretching')");
    db.run("insert into Labels values (5,'Pilates')");
    db.run("insert into Labels values (6,'Dumbbell')");
    db.run("insert into Labels values (7,'Pull-up')");
    db.run("insert into Labels values (8,'Kettlebell')");
    db.run("insert into Labels values (9,'Bodyweight Exercise')");
    db.run("insert into Labels values (10,'Aerobic Gymnastics')");
    db.run("insert into Labels values (11,'Exercise Ball')");
    db.run("insert into Labels values (12,'Health Club')");
    db.run("insert into Labels values (13,'Treadmill')");
    db.run("insert into Labels values (14,'Handbell')");
    db.run("insert into Labels values (15,'Functional Training')");
    db.run("insert into Labels values (16,'Burpee')");
    db.run("insert into Labels values (17,'Gold Gym')");

    db.run("drop table if exists Videos");
    db.run("create table Videos (id, title, owner, view, created, labels)");
}

function insertVideo() {
 //   var videos = ["PSPBCuEHVo4", "vPKcShZzuPo", "IfIaCXoxShw", "00lp2gJazAI", "05ZV3Nrk7qs",
 //       "0kH5ESuB-18", "U7K6yFOjsJI", "F9PXg7NeVP0", "QAuGN3nd6AU", "XIeCMhNWFQQ"];
 //   var binary = [0b00101000001000100, 0b00100000001000100, 0b10100000000000010, 0b001010000000000000, 0b001010000000000000,
 //       0b001010000010000000, 0b001010000000000000, 0b011000000000000000, 0b011000000000000000, 0b001010000000000000];
    var query = "insert into Videos (id, title, owner, view, created, labels) values (?, ?, ?, ?, ?, ?)";
    var ps = db.prepare(query);

    (async function loop() {
        for (let i = 0; i < addressArray.length; i++) {
            await new Promise(resolve => setTimeout(resolve, 100));
            var fetchVideoInfo = require('youtube-info');
            fetchVideoInfo(addressArray[i]).then(function (videoInfo) {
                if (String(videoInfo.thumbnailUrl).includes("maxresdefault") && videoInfo.views > 50000) {
                    ps.run(addressArray[i], videoInfo.title.replace("'", "\'").replace("&", "&amp;"),
                        videoInfo.owner.replace("'", "\'").replace("&", "&amp;"), videoInfo.views,
                        videoInfo.datePublished, indexArray[i]);
                }
            });
            console.log(i);
        }
    })();
    /*
        var recursion = function (i) {
            if (i < videos.length) {
                fetchVideoInfo(videos[i], function (err, videoInfo) {
                    console.log(i);
                    console.log(videos[i]);
                    if (err) throw new Error(err);
                    ps.run(videos[i], videoInfo.title, videoInfo.owner, videoInfo.views,
                        videoInfo.datePublished, binary[i]);
                    recursion(i+1);
    //            ps.set(videos[i], videoInfo.title, videoInfo.owner, videoInfo.views, videoInfo.datePublished, binary[i]);
                });
            }
        }
        recursion(0); */
    /*
    for (i=0; i<videos.length; i++) {
        (function (i) {
            fetchVideoInfo(videos[i], function (err, videoInfo) {
                console.log(i);
                console.log(videos[i]);
                if (err) throw new Error(err);
                ps.run(videos[i], videoInfo.title, videoInfo.owner, videoInfo.views,
                    videoInfo.datePublished, binary[i]);
//            ps.set(videos[i], videoInfo.title, videoInfo.owner, videoInfo.views, videoInfo.datePublished, binary[i]);
            });
        })(i);
    }

    for (i=0; i<videos.length; i++) {
        (function (i) {
            fetchVideoInfo(videos[i], function (err, videoInfo) {
                console.log(i);
                console.log(videos[i]);
                if (err) throw new Error(err);
                ps.run(videos[i], videoInfo.title, videoInfo.owner, videoInfo.views,
                    videoInfo.datePublished, binary[i]);
//            ps.set(videos[i], videoInfo.title, videoInfo.owner, videoInfo.views, videoInfo.datePublished, binary[i]);
            });
        })(i);
    } */
    /*
        for (i=0; i<videos.length; i++) {
            fetchVideoInfo(videos[i], function (err, videoInfo) {
                console.log(i);
                console.log(videos[i]);
                if (err) throw new Error(err);
                ps.run(videos[i], videoInfo.title, videoInfo.owner, videoInfo.views,
                    videoInfo.datePublished, binary[i]);
    //            ps.set(videos[i], videoInfo.title, videoInfo.owner, videoInfo.views, videoInfo.datePublished, binary[i]);
            });
        } */
//    ps.finalize();
}

//Chenxi 5/16 For parsing data
function parseData() {
    var fs = require('fs');
    var data = fs.readFileSync("Metrics.txt").toString();

    var array = data.split("\n");

    for (var i = 0; i < array.length; i++) {

        var temp = array[i].replace("\r", "");
        console.log(temp);

        var line = temp.split(",");
        if (line[0].length !== 11) {
            continue;
        }
        addressArray.push(line[0]);
        var index = 0;
        for (var j = 1; j < line.length; j++) {
            if(line[j] === "1"){
                index += (1<<(17 - j));
            }
        }
        indexArray.push(index);
    }
}