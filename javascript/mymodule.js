export function myFunc1(divs) {
    console.log("mymodule myFunc1")
    let i = 0;
    divs.forEach(
        (e)=>{
            e.innerHTML += "<p> Iterate to div "+(++i)+" from func1 ";
        }
    );
}

export function myFunc2() {
    console.log("myFunc2()");
}

