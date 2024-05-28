import React, {useState, useEffect, useRef} from "react";
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
	const minRange = min - 0.49,
		maxRange = max + 0.49;
	const result = Math.round(
		minRange + (
			Math.random() * (maxRange - minRange)
		)
	);
	return result;
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
	return {key: key, name: name, msg: msg,
		isQuestion:false, to:null, hasDirectReplies: 0};
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
    const goneUsers = [];
    const userMap = new Map(), hasAnswers = new Map(),
	    hasReplies = new Map(), hasComments = new Map();
    users.forEach(user=> {
	    userMap.set(user.name, user);
	    if (!user.comments) user.comments=[];
	    if (!user.answers) user.answers=[];
	    if (!user.replies) user.replies=[];
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
	    if (list.length > 0)
		    return true;
	    map.delete(user.name);
	    return false;
    }

	return (msgs)=> {
	    let msgKey = msgs.length + 1;
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
				goneUsers.push(u);
			}
		}
		if (msgs.length % 8 == 0 || users.length ==0) {
			while (goneUsers.length > 0) {
				const u = goneUsers.pop();
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

		const mustAnswer =
			hasComments.size == 0 && hasReplies.size == 0;
	    const question = (mustAnswer || randomRange(0,2) < 2)
		    ?findMsg(
			    msgs, mustAnswer ?msgs.length :3,
			    msg=> {
				    if (mustAnswer) {
					    return hasAnswers.size > 1 ||
						    ! hasAnswers.get(msg.name);
		    		}
		    		else
			    		return msg.isQuestion &&
				    		msg.hasDirectReplies < 2;
	    		}
		    )
		    :null;
	    if (question) {
		    const user = pickUser(user=>
			    user.answers && user.answers.length > 0 &&
				    user.name != question.name
		    );
		    if (user!=null) {
			    const msg = pickOne(
				    user, user.answers, msgKey, question.name, !mustAnswer
			    );
			    question.hasDirectReplies++;
			    return [...msgs, msg];
		    } else if (mustAnswer) {
			    console.log("Could not get answerer! Question: "+question.name+
				    " Has answers: "+hasAnswers.size);
		    }
	    }

		if (randomRange(0, 2) < 2) {
		    const needsDirectReply = findMsg(msgs, 4, msg=>{
			    if (msg.hasDirectReplies > 1 || !msg.to)
				    return false;
			    const user = userMap.get(msg.to);
			    return user && user.replies && user.replies.length > 0;
		    });
		    if (needsDirectReply) {
			    const user = userMap.get(needsDirectReply.to);
			    const msg = pickOne(
				    user, user.replies, msgKey, needsDirectReply.name, false
			    );
			    msg.hasDirectReplies++;
			    return [...msgs, msg];
		    }
	    }

	    const mustReply = hasComments.size == 0;
	    if (mustReply || randomRange(0, 1)==1) {
		    const replyTo = findMsg(msgs, mustReply ?msgs.length :2, msg=>{
			    if (msg.isQuestion)
				    return false;
			    if (!userMap.get(msg.name) && !mustReply)
				    return false;
			    if (hasReplies.size==1)
				    return ! hasReplies.get(msg.name);
			    return true;
		    });
		    if (replyTo) {
			    const user = pickUser(u=>
			    	u.replies.length > 0 && u.name!=replyTo.name
			    );
			    if (user!=null) {
				    const msg = pickOne(
					    user, user.replies, msgKey, replyTo.name, false
				    );
				    return [...msgs, msg];
			    }
		    }
	    }
		const commUser = pickUser(u => u.comments && u.comments.length > 0);
		if (commUser!=null) {
		    const msg = pickOne(
			    commUser, commUser.comments, msgKey, null, false
		    );
		    return [...msgs, msg];
		}

		console.log("I... couldn't find anything to do!");
		console.log("CAR: "+hasComments.size+" "+hasAnswers.size+" "+hasReplies.size+
			" All: "+users.length+" "+userMap.size);
		users.forEach(u=>
			console.log(u.name+" CAR: "+u.comments.length+" "+
				u.answers.length+" "+u.replies.length)
		);
		console.log("Has Comments "+hasComments.keys().toArray());
		console.log("Has Answers "+hasAnswers.keys().toArray());
		console.log("Has Replies "+hasReplies.keys().toArray());
		return msgs;
	};
}

function Discussion({userName}) {
	const [rawData, setRawData] = useState([]);
	const [messages, setMessages] = useState([]);
	const [doScroll, setScroll] = useState([true]);
	const userMsgRef = useRef();
	function handleSend(){
		const newMsg = userMsgRef.current.value;
		console.log("User message: "+newMsg);
		setMessages(msgs => [...msgs,
			makeMsg(
				userName+" "+(msgs.length+1), userName, newMsg
			)
		]);
	}
	function handleScrolled(ev) {
		const t = ev.target
		const pos = t.scrollTop,
			bottom = t.scrollHeight - t.offsetHeight;
		const diff = bottom - pos;
		if (diff > 40 && doScroll)
			setScroll(false);
		else
		if (diff < 40 && !doScroll)
			setScroll(true);
	}
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
	            console.log("Chat.Discussion(): Got data");
	            sleepMax(2, 3);
	            let desiredLen = 1;
	            while (ok) {
		            setMessages((msgs)=>{
			            try {
				            if (desiredLen<=msgs.length) {
					            desiredLen = msgs.length + 1;
					            return msgs;
				            }
				            msgs = checkMsgs(msgs)
				            if (ok && users.length == 0) {
					            ok = false;
					            msgs = [...msgs,
						            makeMsg(msgs.length+1, null,
							            <i><br/>Everyone else is... gone.</i>)
					            ];
				            }
				            desiredLen = msgs.length + 1;
				            return msgs;
			            } catch (e) {
				            fail(e);
			            }
		            });
		            await sleepMax(2, 7);
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

	useEffect(()=>{
		const bm = document.getElementById("bottomMost");
		if (bm && doScroll)
			bm.scrollIntoView();
		else
			console.log("Scroll into view is off");
	});
	const userInput = userName == null
		?[]
		:(<>
		<textarea rows={"5"} cols={70} ref={userMsgRef} placeholder="Send a message..."/>
		<button onClick={handleSend}>Send</button>
		</>);
	const realMsgs = messages.length == 0
		?[makeMsg(1, null, <i>Loading...</i>)]
		:messages;
	return <>
	    <div className="chatmessages" onScroll={handleScrolled}>
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
	    {userInput}

	</>;
}

export default function Chat() {
	const [userName, setUserName] = useState(null);
	const textInputRef = useRef();
	const showSignup = userName == null
		?<div className="chatsignup">
			<input type="text" ref={textInputRef} placeholder="Your user name"/>
			<button onClick={
					()=>setUserName(textInputRef.current.value)
				}>Sign in</button>
		</div>
		:null;
	return <div className="subbody flexVert">
		<h2>Hot Chat</h2>
		<p>Chat Yo' Self Up W/ Hot People Who Can Chat Hot!</p>
	    <div className="chat flexVert">
		    <div className="chatmain">
			    <Discussion userName={userName}/>
				{showSignup}
			</div>
	    </div>
    </div>;
};

