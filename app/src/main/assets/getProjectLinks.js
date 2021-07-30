(function(){
    try{
        var projects = document.getElementsByTagName('project-card');
        var links = "";
        for(project of projects){
            links+=project.firstChild.href+",";
        }
        if(links==""){
           return "TypeError"
        }
        return links;
   }
   catch(e){
        return e.name;
   }
})()