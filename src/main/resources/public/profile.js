
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


document.getElementById("slider1").addEventListener("input", function(event){
    let value = event.target.value;
    document.getElementById("current-value").innerText  = value;
    document.getElementById("current-value").classList.add("active");
    document.getElementById("current-value").style.left = `${value/0.2425}%`
   });

   document.getElementById("slider2").addEventListener("input", function(event){
    let value = event.target.value;
    document.getElementById("current-value1").innerText  = value;
    document.getElementById("current-value1").classList.add("active");
    document.getElementById("current-value1").style.left = `${value/0.2425}%`
   });



 


