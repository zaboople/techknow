export function myFunc1() {
    console.log("mymodule myFunc1")
    var i = 0;
    Array.from(document.getElementsByTagName("div")).forEach(
        (e)=>{
            e.innerHTML += "<p> Iterate to div "+(++i)+" from func1 ";
        }
    );
}

export function myFunc2() {
    console.log("myFunc2()");
}

