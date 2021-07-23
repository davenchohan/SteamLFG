window.onload = popUp();
function popUp() {
    document.getElementById("checkpopup1").style.display = "none";
    var popUp = document.getElementById("newRequests");
    checkRequests = document.getElementById("checkpopup1").innerHTML;
    if (checkRequests == "true"){
        popUp.classList.toggle("show");
    }
}

function closePopUp() {
    var popUp = document.getElementById("newRequests");
    popUp.classList.toggle("show");
}