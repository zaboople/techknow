import {useEffect, useState, useRef} from 'react';

export default function RerenderTest() {
    const [countClicks, setCountClicks] = useState(0);
    const [countB, setCountB] = useState(0);
    const [countC, setCountC] = useState(0);
    const [countD, setCountD] = useState(0);
    const refRenderCount = useRef(0);
    const refPaintCount = useRef(0);
    refRenderCount.current = refRenderCount.current + 1;
    console.log("Rendering #"+refRenderCount.current+" Counts: "
        +countClicks+" "+countB+" "+countC+" "+countD);

    // This will force a re-render so that react
    // skip the repaint in favor of waiting for the second re-render.
    // It is normally preferable to avoid this but this time it's for
    // demonstration's sake:
    if (refRenderCount.current % 4==0) {
        console.log("Triggering re-render stupidly...");
        setCountB(x=>x+1);
        setCountC(x=>x+1);
        setCountD(x=>x+1);
    }

    function handleClick() {
        setCountClicks(x=>x+1);
    }
    useEffect(()=>{
        refPaintCount.current = refPaintCount.current + 1;
        document.getElementById("secretDiv").innerHTML = refPaintCount.current;
    });

    return (
        <div className="subbody flexVert">
            <div>
                <button onClick={handleClick}>Clicky le buttoon!</button>
            </div>
            <div style={{marginTop:20}}>
                Re-renders: {refRenderCount.current}
            </div>
            <div>
                Re-paints: {refPaintCount.current}
            </div>
            <div>
                Re-paints - naughty version: <span id="secretDiv"></span>
            </div>
            <div>
                Clicks: {countClicks}
            </div>
            <p style={{marginTop:20}}>
            So, &quot;Re-renders&quot; counts the number of times our rendering function executes; in dev, it&apos;s always doubled-up,
            but in production it should increment each time you clicky le buttoon. This re-rendering is actually
            triggered by our calls to useState()&apos;d setXXX()&apos;s, and we wanted to make sure we could call a bunch of those and
            still trigger only one (or in dev, two) re-render(s), as long as it&apos;s done in a GUI event. We also have a funky/weird
            thing inside our component rendering function that calls a bunch of setXXX()&apos;s on every 4th render, causing an
            additional bonus re-render just to mess with your head.
            </p>
            <p>But &quot;Re-paints&quot; is trying to count the number of times we actually redraw (e.g. we use useEffect()), in case
            react is doing the trick where it re-renders many times but throws them all out except the last one, which is
            exactly what happens with that silly 4th render trick (above). Re-paint also lags the truth by 1 because the value is
            updated during screen repaint, not during function exec - <i>however</i>, the &quot;naughty version&quot; is
            populated by a direct DOM modification (ooh! bad!), even if it is naughty to do that. To make matters
            even more complicated, we get a double-useEffect() on the first run through in dev.
            </p>

            <p>Clicks is just counting how often you clicky le buttoon, which triggers an event calling setClickCount() and thus
             the stateful machinery and side-effects and what have you kicks in.</p>
        </div>
    );
}
