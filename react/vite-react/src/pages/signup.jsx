import {useState, useEffect} from "react";
import "./styles.css";
import DotDot from "../components/DotDot.jsx";

function DoSignUp() {
    const [result, setResult] = useState({data: null, error: null, done: false});
    async function initFunc() {
        try {
            const whereAmI = window.location.href;
            const url = whereAmI.replace(
                    new RegExp("(^http.+?//.+?/).*"), "$1"
                ) + "samplejson.json";
            console.log("Signup() Calling... "+url);
            await new Promise(
                resolver => setTimeout(()=>resolver("dook"), 3000)
            );
            const response = await fetch(url);
            const myjson = await response.json();
            setResult({data: myjson, error: null, done: false});
            console.log("Signup(): Got data");
        } catch (err) {
            console.log("Not working: "+err);
            setResult({data: null, error: err, done: false});
        }
    }
    useEffect(()=>{initFunc()}, []);

    if (result.error!=null)
        return <p>Error: {result.error.message}</p>;
    else if (result.data!=null)
        return <ul> {
            result.data.map(item =>
                <li key={item.id}>{item.name}</li>
            )
        } </ul>;
    else
        return <p><em>Loading</em><DotDot/></p>;
}

export default function SignUp() {
    return  <div className="subbody">
        <h2> Who... signed up? </h2>
        These people signed up, for something, but we don&apos;t know what.
        Either way: <em>You</em> can&apos;t:
        <DoSignUp/>
    </div>;
}
