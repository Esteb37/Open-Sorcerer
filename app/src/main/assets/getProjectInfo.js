(function(){

    var repo;
    try{
       repo = document.querySelector("td").querySelector("a").href;
    }
    catch(e){
       repo = e.name;
    }

    var logo;
    try{
       logo = document.getElementsByClassName("logo")[0].querySelector("img").src;
    }
    catch(e){
       logo = "https://opensource.google/assets/static/images/misc/og1.jpg";
    }
    return [repo,logo];
})()