const header =document.querySelector("header");

window.addEventListener("scroll",function(){
    header.classList.toggle("sticky",this.window.scrollY > 0);
})

// let menu=document.querySelector('#menu-icon');
// let navmenu=document.querySelector('.navmenu');

// menu.onclick=()=>{
//     menu.classList.toggle('fa-x');
//     navmenu.classList.toggle('open');

// }

let downMenu =document.querySelector(".dropdown-menu");
let downsquare=document.querySelector(".dropdown-toggle");
downsquare.onclick=()=>{
    downMenu.classList.toggle('dropMenu');
}

