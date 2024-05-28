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
	return {key: key, name: name, msg: msg, isQuestion:false, to:null};
}
function pickOne(user, list, key, replyTo, forceReply) {
    const ix = randomRange(0, list.length -1);
    const txt = list[ix];
    list.splice(ix, 1);
    let finalTxt = txt;
    if (replyTo!=null) {
	    let keyIndex = 0;
	    function makeReplyTo(extra) {
		    return <span key={keyIndex++}
			    className={"mention"}>#{replyTo}{extra}</span>;
	    }
	    if (txt.indexOf("{name}") > -1){
		    const newReply = [];
		    const splt = txt.split("\{name\}");
			for (let i=0; i<splt.length; i++) {
				const chunk = splt[i];
				newReply.push(chunk);
				if (i!=splt.length-1)
					newReply.push(makeReplyTo());
			}
			finalTxt = newReply;
	    } else if (forceReply) {
		    finalTxt = [makeReplyTo(" "), txt];
	    }
    }
    const newMsg = makeMsg(key, user.name, finalTxt);
    newMsg.to = replyTo;
    newMsg.isQuestion = txt.indexOf("?") > -1;
    return newMsg;
}

function createProcessor(rawData) {
    const users = rawData;
    const userMap = new Map(), hasAnswers = new Map(),
	    hasReplies = new Map(), hasComments = new Map();
    users.forEach(user=> {
	    userMap.set(user.name, user);
	    hasComments.set(user.name, user.comments && user.comments.length > 0)
	    hasAnswers.set(user.name, user.answers && user.answers.length > 0)
	    hasReplies.set(user.name, user.replies && user.replies.length > 0)
    });
    console.log("Users: map/list: "+userMap.size+" "+users.length);
    function findMsg(msgs, limit, condition) {
	    limit = Math.min(limit, msgs.length);
	    for (let i=1; i<=limit; i++) {
		    const index = msgs.length - i;
		    const msg = msgs[index];
		    if (condition(msg))
			    return msg;
	    }
	    return null;
    }
    function pickUser(condition) {
	    const list = users.filter(condition);
	    if (list.length==0) {
		    //console.log("Warning: No user for condition");
		    return null;
	    }
	    return list[randomRange(0, list.length-1)];
    }
    function checkHas(user, list, map) {
	    if (list && list.length > 0)
		    return true;
	    map.delete(user.name);
	    return false;
    }

	return (msgs)=> {
	    let msgKey = msgs.length + 1;
	    if (users.length < 3 || msgs.length % 3 ==0)
			for (let i=users.length-1; i>=0; i--) {
				const u = users[i];
				const c = checkHas(u, u.comments, hasComments);
				const a = checkHas(u, u.answers, hasAnswers);
				const r = checkHas(u, u.replies, hasReplies);
				//console.log(u.name+" "+c+" "+a+" "+r);
				if (!(c || a || r)) {
				    console.log("Delete user "+u.name);
				    users.splice(i, 1);
					userMap.delete(u.name);
				    const msg = makeMsg(
					    msgKey++, u.name, <i>... has left the chat</i>
				    );
					msgs = [...msgs, msg];
				}
			}
	    if (users.length==0) {
		    console.log("No users left");
		    return msgs;
	    }

		const mustAnswer = hasComments.size == 0 && hasReplies.size == 0;
	    const question = (mustAnswer || randomRange(0,3) < 3)
		    ?findMsg(
			    msgs, mustAnswer ?msgs.length :4, msg=>msg.isQuestion
		    )
		    :null;
	    if (question) {
		    const user = pickUser(user=>
			    user.answers && user.answers.length > 0 &&
				    user.name != question.name
		    );
		    if (user!=null) {
			    const msg = pickOne(
				    user, user.answers, msgKey, question.name, true
			    );
			    return [...msgs, msg];
		    }
	    }

		if (randomRange(0, 2) < 2) {
		    const needsDirectReply = findMsg(msgs, 4, msg=>{
			    if (!msg.to)
				    return false;
			    const user = userMap.get(msg.to);
			    return user && user.name!=msg.to && user.replies && user.replies.length > 0;
		    });
		    if (needsDirectReply) {
			    const user = userMap.get(needsDirectReply.to);
			    const msg = pickOne(
				    user, user.replies, msgKey, needsDirectReply.to, true
			    );
			    return [...msgs, msg];
		    }
	    }

	    const mustReply = hasComments.size == 0;
	    if (mustReply || randomRange(0, 1)==1) {
		    const replyTo = findMsg(msgs, mustReply ?msgs.length :2, msg=>{
			    if (msg.isQuestion)
				    return false;
			    if (userMap.get(msg.name)==null && !mustReply)
				    return false;
			    if (users.length==1)
				    return msg.name != users[0].name;
			    return true;
		    });
		    if (replyTo) {
			    const user = pickUser(u=>u.replies && u.replies.length > 0
				    && (u.name!=replyTo.name|| (mustReply && users.length==1))
			    );
			    if (user!=null) {
				    const msg = pickOne(
					    user, user.replies, msgKey, replyTo.name, false
				    );
				    return [...msgs, msg];
			    }
			    console.log("Couldn't find replier");
		    }
	    }

		const commUser = pickUser(u => u.comments && u.comments.length > 0);
		if (commUser) {
		    const msg = pickOne(
			    commUser, commUser.comments, msgKey, null, false
		    );
		    return [...msgs, msg];
		}

		console.log("I... couldn't find anything to do!");
		console.log("CAR: "+hasComments.size+" "+hasAnswers.size+" "+hasReplies.size+
			" All: "+users.length+" "+userMap.size);
		console.log(users.map(i=>i.name).join(" "));
		return msgs;
	};
}

function Discussion(userName) {
	const [rawData, setRawData] = useState([]);
	const [messages, setMessages] = useState([]);
	useEffect(()=>{
        let ok = true;
        function fail(err) {
	        console.log("FAIL "+err);
	        console.log(err);
	        ok = false;
            setMessages(m=> [...m,
	            makeMsg(m.length+1, null, ""+err)
            ]);
        }
        (async ()=>{
	        try {
	            const users = await getRawData();
		        const checkMsgs = createProcessor(users);
	            let msgs = [];
	            console.log("Chat.Discussion(): Got data");
	            sleepMax(2, 3);
	            while (ok) {
		            const desiredLen = msgs.length+1;
		            setMessages(()=>{
			            try {
				            if (desiredLen<=msgs.length)
					            return msgs;
				            msgs = checkMsgs(msgs)
				            if (ok && users.length == 0) {
					            ok = false;
					            msgs = [...msgs,
						            makeMsg(msgs.length+1, null,
							            <i><br/>Everyone else is... gone.</i>)
					            ];
				            }
				            return msgs;
			            } catch (e) {
				            fail(e);
			            }
		            });
		            await sleepMax(2, 10);
	            }
	            console.log("Exiting.");
	        } catch (e) {
	            fail(e);
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

