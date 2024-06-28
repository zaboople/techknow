import {useState, useEffect} from 'react';
import PropTypes from 'prop-types';

export default function DotChaser() {
    return (
        <div id="mycanvas" className="subbody mycanvas">
            <DotChaser2/>
        </div>
    );
}

function DotChaser2() {
    const listLen = 1000;
    const fullDelay = 2 * (listLen+1);
    const [dragOn, setDragOn] = useState(true);
    const [posList, setPosList] = useState([]);
    const [posList2, setPosList2] = useState([]);
    const [last, setLast] = useState({x: -40, y: -40, opacity:1});
    useEffect(
        () => {
            const handler = event => {
                const next = {
                    x:event.clientX, y:event.clientY,
                    expire: Date.now() + fullDelay
                };
                setLast(next);
                setPosList(list=>[...list, next]);
                setTimeout(
                    () => setPosList(list=> {
                        const nowTime = Date.now();
                        return list.filter(x=>x.expire-1 > nowTime);
                    }),
                    fullDelay
                );
            }
            if (dragOn)
                return addHandler("pointermove", handler);
        },
        [fullDelay, dragOn]
    );
    useEffect(
        () => addHandler("click", ()=>setDragOn(x => !x)),
        []
    );
    useEffect(
        () => addHandler(
            "mouseout", ()=>setLast({x: -40, y: -40, opacity:0})
        ),
        []
    );

    // Tricky freeze-in-place with dragOn var & switch lists:
    if (!dragOn && posList2.length==0 && posList.length!=0)
        setPosList2(posList);
    else
    if (dragOn && posList2.length!=0)
        setPosList2([]);
    let newList = dragOn ?posList :posList2;

    // Add opacity spread according to age,
    // or maybe just list position, yeah:
    const n1 = Date.now();
    const actualLen = newList.length;
    newList = newList.map((x, ix)=>{
        let left = x.expire - n1;
        if (left <= 0) left = 0;
        const op = 1.0 * (ix / actualLen)
        return {x:x.x, y:x.y, opacity:op};
    });

    // This filter will allow us to make the balls appear to
    // move rather than simply fade out:
    //newList = newList.filter((x,ix)=> ix%4 == 0);

    // Ensures that we *always* have a big dot where the cursor is:
    newList.push(last);

    return <>
        {newList.map((value, index)=>
            <Dot key={index} position={value}/>
        )}
    </>;
}

function addHandler(eventName, handleMove) {
    console.log("Add: "+eventName);
    const div = document.getElementById("mycanvas");
    div.addEventListener(eventName, handleMove);
    return () => {
        console.log("Remove: "+eventName);
        div.removeEventListener(eventName, handleMove);
    }
}


Dot.propTypes = {position: PropTypes.object}
function Dot({position}) {
    const op = position.opacity;
    if (position.x < 30 || position.y < 80)
        return <></>;
    return (
        <div style={{
            position: 'absolute',
            backgroundColor: 'black',
            borderRadius: '50%',
            opacity: op,
            transform: `translate(${position.x}px, ${position.y}px)`,
            pointerEvents: 'none',
            left: -20, top: -20, width: 20, height: 20,
        }} />
    );
}

