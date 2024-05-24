import React, {useState} from "react";
import Navbar from "../components/Navbar";
import "./styles.css";


const MSG_LIST = Object.freeze([
    {limit: 5, msg: "That's definitely helping."},
    {limit: 12, msg: "Wow! This must be urgent"},
    {limit: 30, msg: "We're dropping everything! Right now!"},
    {limit: 60, msg: "WOOP WOOP WOOP (office sirens)"},
    {limit: 120, msg: "You can stop now. We get it already."},
    {limit: 200, msg: "Aaagh!"},
    {limit: 330, msg: "Cap'n! I'm givin' her all the cheeseburgers she can handle!"},
    {limit: 430, msg: "Turning on Special Messaging Protocol Q.zoot!"},
]);


function Buttoon() {
    const [count, setCount] = useState(0);
    const [incr, setIncr] = useState(1);
    const list = MSG_LIST.filter(m => count > m.limit)
	    .map(m=>
		    <p key={m.limit}>{m.msg}</p>
	    );
    const text = count == 0
	    ?"Get in touch"
	    :("Got in touch "+count+" times");

    if (count > 500 && incr!=-1)
	    setIncr(-1);
    else if (count < 1 && incr!=1)
	    setIncr(1);

    return <>
	    <p>
	    <button onClick={_ => setCount(count => count + incr)}>
	        {text}</button>
        </p>
        {list}
    </>;
}

const Contact = () => {
	return (<>
        <Navbar/>
        <div className="subbody">
			<p>
				Click the button to contact us. Click it extra for more contacting. Click it a bunch, why don't you. We will be sure to get back to you soon.
			</p>
            <Buttoon/>
        </div>
    </>);
};

export default Contact;
