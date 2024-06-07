import {useState, useEffect} from "react";
export default function DotDot() {
    const maxDots = 3
    const [dotCount, setDots] = useState(maxDots);
    useEffect(()=>{
        let ok = true;
        async function doDots() {
             while (ok) {
                await new Promise(
                    resolver => setTimeout(()=>resolver(), 200)
                );
                if (ok)
                    setDots(x => (x==maxDots * 2) ?0 :(x+1));
             }
        }
        doDots();
        return ()=>{
            ok=false;
            console.log("DotDot.jsx: Stopped dots.");
        }
    }, []);

    const limit = Math.abs(dotCount - maxDots);
    let s="";
    for (let i=0; i<limit; i++)
        s+=". "
    return s;
}
