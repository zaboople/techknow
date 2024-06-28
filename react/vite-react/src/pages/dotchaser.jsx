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
    const [posList, setPosList] = useState([]);
    const [last, setLast] = useState({x: -40, y: -40, opacity:1});
    useEffect(() =>
        setupHandlers(event => {
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
        }),
        [fullDelay]
    );

    // Add opacity spread according to age,
    // or maybe just list position, yeah:
    const n1 = Date.now();
    const lipLen = posList.length;
    const newList = posList.map((x, ix)=>{
        let left = x.expire - n1;
        if (left <= 0) left = 0;
        const op = 1.0 * (ix / lipLen)
        return {x:x.x, y:x.y, opacity:op};
    });

    // This filter will allow us to make the balls appear to
    // move rather than simply fade out:
    //const finalList = newList.filter((x,ix)=> ix%4 == 0);
    const finalList = newList;

    // Ensures that we *always* have a big dot where the cursor is:
    finalList.push(last);

    return <>
        {finalList.map((value, index)=>
            <Dot key={index} position={value}/>
        )}
    </>;
}

function setupHandlers(handleMove) {
    console.log("Entering...");
    //const handleOut = () => console.log("OUT!");
    //const handleIn = () => console.log("IN!");
    const div = document.getElementById("mycanvas");
    div.addEventListener('pointermove', handleMove);
    //div.addEventListener('mouseout', handleOut);
    //div.addEventListener('mouseenter', handleIn);
    return () => {
        console.log("Unloading...");
        div.removeEventListener('pointermove', handleMove);
        //div.removeEventListener('mouseenter', handleIn);
        //div.removeEventListener('mouseout', handleOut);
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
            left: -20,
            top: -20,
            width: 25,
            height: 25,
        }} />
    );
}

