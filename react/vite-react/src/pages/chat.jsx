import React, {useState, useEffect} from "react";
import "./styles.css";
import "./button.css";

async function getRawData() {
    const whereAmI = window.location.href;
    const url = whereAmI.replace(
            new RegExp("(^http.+?//.+?/).*"), "$1"
        ) + "chatlog.json";
    console.log("Chat.Discussion() Calling... "+url);
    await new Promise(
        resolver => setTimeout(()=>resolver("dook"), 3000)
    );
    const response = await fetch(url);
    return await response.json();
}
function randomRange(min, max) {
	return min + Math.round(Math.random() * (max - min));
}
async function sleepMax(minSecs, maxSecs) {
	const sleep = minSecs + (
		(maxSecs - minSecs) * Math.random()
	);
    return await new Promise(
        r => setTimeout(()=>r(sleep), sleep * 1000)
    );
}

function makeMsg(key, name, msg) {
	return {key: key, name: name, msg: msg};
}
function pickOne(user, list, key) {
    const ix = randomRange(0, list.length -1);
    const msg = makeMsg(key, user.name, list[ix]);
    list.splice(ix, 1);
    return msg;
}

function checkMsgs(users, userMap, oldMsgs) {
    if (users.length==0) {
	    console.log("I don't know why I am getting called, no users");
	    return oldMsgs;
    }
    const msgKey = oldMsgs.length + 1;
    const userPick = randomRange(0, users.length-1);
    const user = users[userPick];
    const [answers, comments] = [user.comments, user.answers];
    console.log("Message from: "+user.name+" comm/ans left: "+
	    comments.length+"/"+answers.length);
    if (comments.length > 0) {
	    const msg = pickOne(user, comments, msgKey);
	    return [...oldMsgs, msg];
    }
    if (answers.length > 0) {
	    const msg = pickOne(user, answers, msgKey);
	    return [...oldMsgs, msg];
    }
    userMap[user.name] = null;
    users.splice(userPick, 1);
    const msg = makeMsg(
	    msgKey, user.name,
	    <i>... has left the chat</i>
    );
    return [...oldMsgs, msg];
}

function Discussion(userName) {
	const [rawData, setRawData] = useState([]);
	const [messages, setMessages] = useState([]);
	useEffect(()=>{
        let ok = true;
        if (messages.length > 0)
	        return;
        (async ()=>{
	        try {
	            const users = await getRawData();
	            const userMap = new Map();
	            users.forEach(user=>
		            userMap[user.name] = user
	            );
	            console.log("Chat.Discussion(): Got data");
	            sleepMax(3, 4);
	            var msgs = [];
	            while (ok) {
		            const desiredLen = msgs.length+1;
		            await setMessages(()=>{
			            if (desiredLen==msgs.length)
				            return msgs;
			            msgs = checkMsgs(users, userMap, msgs)
			            if (ok && users.length == 0) {
				            ok = false;
				            msgs = [...msgs,
					            makeMsg(msgs.length+1, null, <i>Everyone else is... gone</i>)
				            ];
			            }
			            return msgs;
		            });
		            const slept = await sleepMax(1, 2);
	            }
	            console.log("Exiting.");
	        } catch (err) {
	            console.log("Chat.Discussion(): Fetch not working: "+err);
	            setMessages(m=> [...m,
		            makeMsg(m.length+1, null, ""+err)
	            ]);
	        }
        })();
        return ()=>{
            console.log("Chat.Discussion(): Shutdown async.");
            ok = false;
        }
	}, []);
	const realMsgs = messages.length == 0
		?[makeMsg(1, null, <i>Loading...</i>)]
		:messages;
	useEffect(()=>{
		const bm = document.getElementById("bottomMost");
		if (bm)
			bm.scrollIntoView();
		else
			console.log("Can't scroll into view, sigh...");
	});
	return <>
	    <div className="chatmessages">
		    {realMsgs.map(msg =>
			    <div key={msg.key}>
					{msg.name!=null
						?<span className={"chatname"}>{msg.name}: </span>
						:null
					}
					{msg.msg}
				</div>
			)}
			<div id={"bottomMost"}>---</div>
	    </div>
	    <textarea rows={"5"} cols={70}/>
	</>;
}

export default function Chat() {
	const [userName, setUserName] = useState(null);
	const showSignup = userName == null
		?<div className="chatsignup">
			<input type="text" placeholder="Your user name"/>
			<button onClick={setUserName}>Sign in</button>
		</div>
		:null;
	return <div className="subbody flexVert">
		<h2>Hot Chat</h2>
		<p>Chat Yo' Self Up W/ Hot People Who Can Chat Hot!</p>
	    <div className="chat flexVert">
		    <div className="chatmain">
			    <Discussion username={userName}/>
			</div>
			{showSignup}
	    </div>
    </div>;
};


