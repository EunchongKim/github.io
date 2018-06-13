"use strict"
//This is used for video.html and videomain.html
//scrollIntoView can't work well in safari(smooth is not allowed)!
function mySlowMove() {
    var div = document.getElementById("start");
    var distance = div.offsetTop;
    asyncLoop(distance);
}

async function asyncLoop(distance) {

    for(var i = 1; i < 101; i++) {
        window.scrollTo(0, i/100*distance);
        await new Promise(resolve => setTimeout(resolve, 5));
    }

}

function showHistory() {
    if (isHistory()) {
        window.location.href="history_videos";
    }
    else {
        alert("There is no records")
    }
}

function isHistory() {
    var cookies = document.cookie.split(";");
    if (document.cookie.length>0)
    {
        for (let k = 0; k < cookies.length; k++) {
            if(cookies[k].includes("history=")) {
                if (cookies[k].split("=")[1].length > 0) {
                    return true;
                }
            }
        }
    }
    return false;
}






//These are our previously test.

function pickerstart1() {
    var go = document.getElementById("move");
    go.onclick = move;
}
function move(event) {
    var ele = document.getElementById("anchor");
    ele.scrollIntoView({ block: 'start',  behavior: 'smooth' });

}

function slowMove(event) {
    window.moveBy(500,500);
    //setTimeout( function() { window.moveTo(500,-500) }, 500);

}



function loopMove(distance) {

    for(var i = 1; i < 101; i++) {
        setTimeout(function () {window.scrollTo(0, i/100*distance)}, i*20)
    }

}

/*
function delay (URL) {
    setTimeout( function() { window.location = location }, 5000 );

    for(var i = 0; i < 10; i++) {
        setTimeout(function () {window.scrollTo(0, perMove)}, 1000)
        perMove += distance/10;
    }
}*/
