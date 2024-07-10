import {useState, useEffect} from 'react';
import PropTypes from 'prop-types';

export default function DotChaser() {
    return <div id="mycanvas" className="subbody mycanvas">
        <DotChaser2/>
    </div>;
}

const OFF_MAP = {x: -40, y: -40, opacity:1};

function DotChaser2() {
    const fullDelay = 5000;
    const [dragOn, setDragOn] = useState(true);
    const [posList, setPosList] = useState([]);
    const [posList2, setPosList2] = useState([]);
    const [last, setLast] = useState(OFF_MAP);
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
        () => addHandler("mouseout", ()=>setLast(OFF_MAP)),
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
    const actualLen = newList.length;
    newList = newList.map((x, ix)=>{
        const op = 1.0 * (ix / actualLen)
        return {x:x.x, y:x.y, opacity:op};
    });

    // This filter will allow us to make the balls appear to
    // move rather than simply fade out:
    // newList = newList.filter((x,ix)=> ix%10 == 0);

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
    if (position.x < 20 || position.y < 70)
        return <></>;
    const width = 12;
    const offset = -width / 2;
    return (
        <div style={{
            position: 'absolute',
            backgroundColor: 'black',
            borderRadius: '50%',
            opacity: op,
            transform: `translate(${position.x}px, ${position.y}px)`,
            pointerEvents: 'none',
            left: offset, top: offset, width: width, height: width,
        }} />
    );
}

