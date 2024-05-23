import {React, useState, useMemo, useEffect} from "react";
import Navbar from "../components/Navbar";
import "./styles.css";

function makePromise(msg, index) {
    const msreal = (400 * (index+1) * Math.random()).toFixed(0);
    return new Promise(
        resolve => setTimeout(
            () => resolve({
                msg: msg, slept: msreal,
                woke: Date.now(), key:index+10000
            }),
            msreal
        )
    );
}
function makePromises(itemCount) {
    var aray = new Array(itemCount);
    for (var i=0; i<aray.length; i++)
        aray[i]=makePromise("Instance: "+(i+1), i);
    return aray;
}

function DrawIt({toRender}) {
    return <div className="subbody">
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
    </div>;
}

function DoWork() {
    //console.log("Wait()...");
    const itemCount = 25;
    const [done, setDone] = useState(0);
    const [waiting, setWaiting] = useState([]);

    // Our cleanup function shuts us down in dev via "ok" because dev
    // double-executes useEffect(). Otherwise our "waiting" list jumps
    // back and forth as competing threads each try to update it:
    useEffect(() => {
        try {
            console.log("Start async...");
            var aray = makePromises(itemCount)
            var finished = [];
            var ok=true;
            aray.forEach(x => x.then(
                value =>
                    ok && setWaiting(finished=[...finished, value])
            ));
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
}

// We need to separate NavBar and DoWork here
// so that Navbar doesn't get re-rendered everytime
// state changes:
export default function Wait() {
    return <><Navbar/><DoWork/></>
};
