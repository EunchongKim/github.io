//This function is used to test or similiarity function
console.log(similiarity(1, 0));//should be 3
console.log(similiarity(181, 150));//should be 5

var videoIndex = [0b10001, 0b11011, 0b00000, 0b11111, 0b011010, 0b11000];
var videoAddress = ["a", "b", "c", "d", "e", "f"];

var testRank = rankVideo(0b11111, videoIndex, videoAddress);
console.log(testRank);

function similiarity(preIndex, movieIndex) {
    var numOfSame = 0;
    var simliarIndex = preIndex & movieIndex;
    while (simliarIndex != 0) {
        numOfSame++;
        simliarIndex = simliarIndex & (simliarIndex - 1);
    }
    return numOfSame;
}

//rank videos
function rankVideo(preIndex, videoIndex, videoAddress) {
    var videoLib = new Array(18);
    for (var i = 0; i < 18 ; i++) {
        videoLib[i] = new Array();
    }
    for (var i = 0; i < videoIndex.length; i++) {
        var similarIndex = similiarity(preIndex, videoIndex[i]);
        videoLib[similarIndex].push(videoAddress[i]);
    }
    return videoLib;
}



