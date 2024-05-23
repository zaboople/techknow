import {React, useState, useEffect} from "react";
import Navbar from "../components/Navbar";
import "./styles.css";

const SignUp = () => {
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
                resolver => setTimeout(()=>resolver("dook"), 2000)
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

    var dataDisplay = <p>Loading...</p>;
    if (result.error!=null)
        dataDisplay = <p>Error: {result.error.message}</p>;
    else if (result.data!=null)
        dataDisplay = (<ul> {
            result.data.map(item =>
                <li key={item.id}>{item.name}</li>
            )
        } </ul>);

    return (<>
        <Navbar/>
        <div className="subbody">
            <h2> Signed up </h2>
            {dataDisplay}
        </div>
    </>);
};

export default SignUp;