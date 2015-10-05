var doc = document;

(function controlStyle() {

    var prev = doc.getElementById("prev");
    var next = doc.getElementById("next");

    prev.onclick = function(){
        alert("prev")
    }

    next.onclick = function(){
        alert("next")
    }
})()