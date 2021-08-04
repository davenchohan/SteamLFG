var steamid = document.getElementById("steamidhidden").innerHTML;
steamid = steamid.substring(36);
document.getElementById("steamidhidden").style.display = "none";
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

const apiData = {
    url: 'http://api.steampowered.com/ISteamUser/',
    version: 'v0001',
    type: 'GetPlayerSummaries',
    id: steamid,
}

const {url, type, version,id} = apiData
const apiUrl = `https://blooming-headland-71532.herokuapp.com/${url}/${type}/${version}/?key=89024256E255B94B2EB67AC30A60D4A6&steamids=${id}`
fetch(apiUrl)
    .then( (data) => data.json())
    .then( (user) => generateHtml(user) )

const generateHtml = (data) => {
    console.log(data)
    const html = `
        <div class="name" style="color:white" >Steam Name: ${data.response.players.player[0].personaname}</div>
        <img src=${data.response.players.player[0].avatarmedium}>
        <a class="link" style="color:white" href=${data.response.players.player[0].profileurl} >Take me to Steam </a>
    `
    const steamDiv = document.querySelector('.steam')
    steamDiv.innerHTML = html
}







 


