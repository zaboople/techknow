console.log("Loading mymodule2")
import {myFunc1} from "./mymodule.js";

myFunc1();

let i = 10
export default function myclick() {
    Array.from(document.getElementsByTagName("div")).forEach(
        (e)=>{
            e.innerHTML += "<p>Iteration "+(i+=10)+" from mymodule2 ";
        }
    );
    myFunc1()
    return false;
}

document.getElementById("mybutton").onclick = myclick;

