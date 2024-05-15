console.log("Loading mymodule2")
import {myFunc1, myFunc2} from "./mymodule.js";

myFunc1();

let i = 10
export default function myclick() {
    try {
        Array.from(document.getElementsByTagName("div")).forEach(
            (e)=>{
                e.innerHTML += "<p>Iteration "+(i+=10)+" from mymodule2 ";
            }
        );
        myFunc1();
        myFunc2();
    } catch (e) {
        console.log("I have to catch if I finally, or errors won't happen: "+e);
        console.log(e);
        // This is hopeless... I can't rethrow because finally.
        throw e;
    } finally {
        return false;
    }
}

document.getElementById("mybutton").onclick = myclick;

