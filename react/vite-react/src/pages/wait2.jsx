import {useState, useEffect} from "react";
import PropTypes from 'prop-types';

import "./styles.css";

function makePromise(msg, index) {
    //console.log("Make: "+msg+" "+index);
    const msreal = (400 * (index+1) * Math.random()).toFixed(0);
    return new Promise(
        resolve => setTimeout(
            () => resolve({
                msg: msg, slept: msreal,
                woke: Date.now(), key: msg
            }),
            msreal
        )
    );
}
function makePromises2(name, itemCount) {
    const aray = new Array(itemCount);
    for (let i=0; i<itemCount; i++){
        const p = makePromise(name+(i+1), i) ;
        aray[i]=p.then(x=>{
            let r = (i>0) ?makePromises2(name+(i+1)+" > ", i) :[];
            r.unshift(x);
            return r;
        });
    }
    return aray;
}



DrawIt.propTypes = {
    toRender: PropTypes.array
}
function DrawIt({toRender}) {
    return <div className="subbody">
        <h2>Testing Await</h2>
        <p>
        This also tests useEffect() and even defies
        useEffect()&apos;s habit of running twice in dev.
        </p>
        <p>
        Oh, actually, I ended up using <code>Promise.then()</code>
        &nbsp; instead of <code>await</code> anyhow.
        </p>
        <div>
            <table className="AwaitTable">
                <thead><tr>
                    <th>Message</th>
                    <th className="slept">Slept</th>
                    <th>Woke</th>
                </tr></thead>
                <tbody>
                {toRender.map(t=>
                    <tr key={t.key}>
                        <td>{t.msg==null ?"..." :t.msg}</td>
                        <td className="slept">{t.slept}</td>
                        <td>{t.woke}</td>
                    </tr>
                )}
            </tbody></table>
        </div>
    </div>;
}

export default function Wait2() {
    //console.log("Wait()...");
    const itemCount = 8;
    const [waiting, setWaiting] = useState([]);

    // Our cleanup function shuts us down in dev via "ok" because dev
    // double-executes useEffect(). Otherwise our "waiting" list jumps
    // back and forth as competing threads each try to update it:
    useEffect(() => {
        try {
            let finished = [];
            let ok=true;

            const logItem = i1 => {
                // Array, custom result object, or Promise:
                if (i1.length)
                    logItems(i1);
                else if (i1.woke)
                    ok && setWaiting(finished=
                        [...finished, i1].toSorted((x,y)=>x.woke - y.woke)
                    );
                else
                    i1.then(i2=>logItem(i2));
            }
            const logItems = aray => aray.forEach(i1 => ok && logItem(i1));

            logItem(makePromises2("X: ", itemCount))
            console.log("Wait2: Promises accounted for.");
            return () => {
                ok=false;
                console.log("Wait2: Unloaded useEffect()");
            }
        } catch (err) {
            console.log("Async FAIL: "+err);
        }
    }, []);
    const realCount = Math.pow(2, itemCount) -1;
    const toRender = [...waiting];
    const missing = realCount - toRender.length;
    for (let i=0; i<missing; i++)
        toRender.push({msg: null, slept: null, woke:null, key:i});
    return <DrawIt toRender={toRender}/>;
}
