$('.nav ul li').click(function() 
{
    $(this).addClass("active").siblings().removeClass('active');
}
)

const tabBtn= document.querySelectorAll('.nav ul li');
const tab = document.querySelectorAll('.tab');

function tabs(panel)
{
    tab.foreach(function(node) {
        node.style.display='none';
    });
    tab[panel].style.display='block';
}

tabs(0);