document.getElementById("requestsent").style.display = "none";
document.getElementById("friendreceive").style.display = "none";
document.getElementById("acceptfriend").style.display = "none";
document.getElementById("declinefriend").style.display = "none";
document.getElementById("addedfriend").style.display = "none";
document.getElementById("deletefriend").style.display = "none";
window.onload = test();
console.log("testing");
function test(){
    document.getElementById("request").style.display = "none";
    request = document.getElementById("request").innerHTML;
    console.log(request);
    if (request == "sent"){
        document.getElementById("requestsent").style.display = "block";
        document.getElementById("addfriend").style.display = "none";
    }
    if (request == "received"){
        document.getElementById("friendreceive").style.display = "block";
        document.getElementById("addfriend").style.display = "none";
        document.getElementById("acceptfriend").style.display = "block";
        document.getElementById("declinefriend").style.display = "block";
    }
    if (request == "accepted"){
        document.getElementById("addedfriend").style.display = "block";
        document.getElementById("deletefriend").style.display = "block";
        document.getElementById("addfriend").style.display = "none";
    }
}
