import {React, useState} from "react";

import "./styles.css";
import "./button.css";

type TMsg = {limit: number, msg: string};
const MSG_LIST: array<TMsg> = Object.freeze([
    {limit: 5, msg: "That's definitely helping."},
    {limit: 12, msg: "Wow! This must be urgent."},
    {limit: 30, msg: "We're dropping everything! Right now!"},
    {limit: 60, msg: "WOOP WOOP WOOP (office sirens)"},
    {limit: 120, msg: "You can stop now. We got it already."},
    {limit: 200, msg: "Aaagh!"},
    {limit: 330, msg: "Cap'n! I'm givin' her all the cheeseburgers she can handle!"},
    {limit: 430, msg: "Turning on Special Messaging Protocol Q.z00t!"},
]);

const MSG_LIST_NEG: array<TMsg> = Object.freeze([
    {limit: -5, msg: "Sending your messages back to you..."},
    {limit: -20, msg: "This is what you get for letting your cat sleep on the keyboard"},
    {limit: -40, msg: "Actually the cat stole all your passwords..."},
    {limit: -100, msg: "Thus all the \"free\" squeeze toy, catnip and fish deliveries"},
    {limit: -160, msg: "Attentiveness: Not your strong suit."},
]);

function Buttoon({enabled}: {enabled:boolean}) {
    const [count, setCount] = useState(0);
    const [incr, setIncr] = useState(1);
    const list: array<NonNullable<unknown>> = MSG_LIST.filter(m => count > m.limit)
        .concat(
            MSG_LIST_NEG.filter(m => count < m.limit)
        )
        .map(m=>
            <p key={m.limit}>{m.msg}</p>
        );
    const text = count == 0
        ?"Get in touch"
        :("Got in touch "+count+" times");
    const upperLimit = MSG_LIST.findLast(()=>true).limit + 70;
    if (count > upperLimit && incr!=-1)
        setIncr(-1);

    return <>
        <p>
            <button
                disabled={!enabled}
                onClick={() => setCount(count => count + incr)}>
                {text}</button>
        </p>
        {list}
    </>;
}

export default function Contact() {
    const [btnEnabled, setEnabled]: [boolean, (boolean)=>void] = useState(true);
    function handleCheck(e:{target: {checked: boolean}}) {
        setEnabled(!e.target.checked);
    }
    return <div className="subbody">
        <h2>Contact Us</h2>
        <p>
            Click the button to contact us. We will be sure to get back to you soon.
        </p><p>
            Click it extra for more contacting. Click it a bunch, why don&apos;t you.
        </p>
        <Buttoon enabled={btnEnabled}/>
        <div>
            <input className={"VerticalCenter"} type="checkbox" onChange={handleCheck}/>
            <span style={{fontSize:"11px"}} className={"VerticalCenter"}>
                Cat protector
            </span>
        </div>
    </div>;
}
