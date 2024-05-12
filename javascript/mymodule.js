export function myFunc1() {
    console.log("mymodule myFunc1")
    var qq = document.getElementsByTagName("div");
    let i = 0;
    Array.from(document.getElementsByTagName("div")).forEach(
        (e)=>{
            e.innerHTML += "<p> Iterate "+(++i)+" from func1 ";
        }
    );
}
export function myFunc2() {
    document.body.innerHTML = "First div: <div></div> Second div<div></div>";
}

