import {React, useState, useEffect} from "react";
import Navbar from "../components/Navbar";
import "./styles.css";

function DotDot() {
    const maxDots = 4
    const [dotCount, setDots] = useState(maxDots);
    useEffect(()=>{
        let ok = true;
        async function doDots() {
             while (ok) {
                await new Promise(
                    resolver => setTimeout(()=>resolver(), 200)
                );
                if (ok)
                    setDots(x => (x==maxDots * 2) ?0 :(x+1));
             }
        }
        doDots();
        return ()=>{
            ok=false;
            console.log("Stopped dots.");
        }
    }, []);
    let s="";
    for (let i=0; i<Math.abs(dotCount - maxDots); i++)
        s+=". "
    return s;
}
function DoSignUp() {
    console.log("SignUp()...");
    const whereAmI = window.location.href;
    const [result, setResult] = useState({data: null, error: null, done: false});
    async function initFunc() {
        try {
            const url = whereAmI.replace(
                    new RegExp("(^http.+?//.+?/).*"), "$1"
                ) + "samplejson.json";
            console.log("Calling... "+url);
            await new Promise(
                resolver => setTimeout(()=>resolver("dook"), 3000)
            );
            const response = await fetch(url);
            const myjson = await response.json();
            setResult({data: myjson, error: null, done: false});
            console.log("Got data");
        } catch (err) {
            console.log("Not working: "+err);
            setResult({data: null, error: err, done: false});
        }
    }
    useEffect(()=>{initFunc()}, []);

    let dataDisplay = <p><em>Loading</em><DotDot/></p>;
    if (result.error!=null)
        dataDisplay = <p>Error: {result.error.message}</p>;
    else if (result.data!=null)
        dataDisplay = <ul> {
            result.data.map(item =>
                <li key={item.id}>{item.name}</li>
            )
        } </ul>;

    return dataDisplay;
}
const SignUp = () => {
    return <>
        <Navbar/>
        <div className="subbody">
            <h2> Who... signed up? </h2>
            These people signed up. <em>You</em> can't:
            <DoSignUp/>
        </div>
    </>;
};

export default SignUp;