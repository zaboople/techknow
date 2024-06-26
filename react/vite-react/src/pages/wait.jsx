import {useState, useEffect} from "react";
import PropTypes from 'prop-types';

import "./styles.css";
import DotDot from "../components/DotDot.jsx";


export default function Wait() {
    //console.log("Wait()...");
    const itemCount = 25;
    const [waiting, setWaiting] = useState([]);

    // Our cleanup function shuts us down in dev via "ok" because dev
    // double-executes useEffect(). Otherwise our "waiting" list jumps
    // back and forth as competing threads each try to update it:
    useEffect(() => {
        try {
            console.log("Start async...");
            const aray = makePromises(itemCount)
            let finished = [];
            let ok=true;
            aray.forEach(x => x.then(
                value => ok && setWaiting(finished=[...finished, value])
            ));
            return () => {
                ok=false;
                console.log("Unloaded.");
            }
        } catch (err) {
            console.log("Async FAIL: "+err);
        }
    }, []);

    const toRender = [...waiting];
    const missing = itemCount - toRender.length;
    for (let i=0; i<missing; i++)
        toRender.push({msg: null, slept: null, woke:null, key:i});

    return <DrawIt toRender={toRender}/>;
}

function makePromises(itemCount) {
    return new Array(itemCount).keys().map(
        (__, index) => makePromise("Instance: "+(index+1), index)
    );
}
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

DrawIt.propTypes = {toRender: PropTypes.array};
function DrawIt({toRender}) {
    return <div className="subbody">
        <h2>Testing Promises</h2>
        <p>
        Uses a lot of <code>Promise.then()</code> instead of <code>await</code> because
        that doesn&apos;t require async functions that give me headaches.
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
                    <td>{t.msg==null ?<DotDot/> :t.msg}</td>
                    <td className="slept">{t.slept}</td>
                    <td>{t.woke}</td>
                </tr>
            )}
        </tbody></table>
    </div>;
}
