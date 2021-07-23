
$('.nav ul li').click(function() 
{
    $(this).addClass("active").siblings().removeClass('active');
}
)

const tabBtn= document.querySelectorAll('.nav ul li');
const tab = document.querySelectorAll('.tab');

function tabs(panel)
{
    tab.forEach(function(node) {
        node.style.display='none';
    });
    tab[panel].style.display='block';
}

tabs(0);

window.onload = popUp();
function popUp() {
    document.getElementById("checkpopup").style.display = "none";
    var popUp = document.getElementById("newRequests");
    checkRequests = document.getElementById("checkpopup").innerHTML;
    if (checkRequests == "true"){
        popUp.classList.toggle("show");
    }
}

function closePopUp() {
    var popUp = document.getElementById("newRequests");
    popUp.classList.toggle("show");
}
