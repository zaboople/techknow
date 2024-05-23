import {React, useState, useMemo, useEffect} from "react";
import Navbar from "../components/Navbar";
import "./styles.css";

function resolveAfter(msg, index) {
    const msreal = (400 * (index+1) * Math.random()).toFixed(0);
    return new Promise(
        resolve =>
            setTimeout(
                () => resolve({
                    msg: msg, slept: msreal,
                    woke: Date.now(), key:index+10000
                }),
                msreal
            )
    );
}
function makePromises(itemCount) {
    console.log("MAKE PROMISES");
    var aray = new Array(itemCount);
    for (var i=0; i<aray.length; i++)
        aray[i]=resolveAfter("Instance: "+(i+1), i);
    return aray;
}

function DrawIt({toRender}) {
    return <>
        <Navbar/>
        <div className="subbody">
        <h2>Testing Await</h2>
        <p>
        This also tests useEffect() and even defies
        useEffect()'s habit of running twice in dev.
        </p>
        <table className="AwaitTable">
            <thead><tr>
                <th>Message</th>
                <th className="slept">Slept</th>
                <th>Woke</th>
            </tr></thead>
            <tbody>
            {toRender.map(t=>
                <tr key={t.key}>
                    <td>{t.msg}</td>
                    <td className="slept">{t.slept}</td>
                    <td>{t.woke}</td>
                </tr>
            )}
        </tbody></table>
        </div>
    </>;
}

const Wait = () => {
    //console.log("Wait()...");
    const itemCount = 25;
    const [done, setDone] = useState(0);
    const [waiting, setWaiting] = useState([]);
    useEffect(() => {
        try {
            console.log("Start async...");
            var aray = makePromises(itemCount)
            var finished = [];
            var ok=true;
            aray.forEach(x =>
                x.then(value => {
                    if (ok) {
                        finished.push(value);
                        setWaiting([...finished]);
                    }
                })
            );
            // Our cleanup shuts us down in dev
            // because dev double-executes useEffect():
            return () => {
                ok=false;
                console.log("Unloaded.");
            }
        } catch (err) {
            console.log("Async FAIL: "+err);
        }
    }, []);

    var toRender = [...waiting];
    var missing = itemCount - toRender.length;
    for (var i=0; i<missing; i++)
        toRender.push({msg: "...", slept: null, woke:null, key:i});
    return <DrawIt toRender={toRender}/>;
};

export default Wait;