import {useEffect, useState, useRef} from 'react';

function initUrls() {
    console.log("Initializing cat urls...");
    const base = "https://loremflickr.com/320/240/cat?lock=";
    return new Array(10).keys().map(i => base + i).toArray();
}
export default function Cats() {
    const refPrev = useRef(null);
    const refNext = useRef(null);
    const mymap = useRef(new Map());
    const [catList] = useState(initUrls);
    const [index, setIndex] = useState(0);
    function clickPrev() {
        clickTo(
            index != 0
            ?(index - 1) :(catList.length - 1)
        );
    }
    function clickNext() {
        clickTo(
            index < catList.length - 1
            ?(index + 1) :0
        );
    }
    function clickTo(ix) {
        setIndex(ix);
        const [bprev, bnext] = [refPrev.current, refNext.current];
        bprev.disabled = ix == 0;
        if (bprev.disabled)
            bnext.focus();
        bnext.disabled = ix == catList.length -1;
        if (bnext.disabled)
            bprev.focus();
        const pic = mymap.current.get(ix);
        if (pic)
            pic.scrollIntoView({behavior:"smooth"});
        else
            console.log("No pic!");
    }
    function toRef(imgElem, id){
        if (imgElem)
            mymap.current.set(id, imgElem)
        else
            mymap.current.delete(id)
    }
    useEffect(()=>{
        refNext.current.focus();
        refPrev.current.disabled=true;
    }, []);
    return (
        <div className="subbody flexVert">
            <h2>What? Cats? Meow!</h2>
            <nav className="catbox">
                <button ref={refPrev} onClick={clickPrev}>Previous</button>
                <button ref={refNext} onClick={clickNext}>Next</button>
            </nav>
            <div className="catbox">
                <ul>
                    {catList.map((url, i) => (
                        <li key={i}>
                            <img ref={img => toRef(img, i)}
                                className={index === i ?'active' :''}
                                src={url}
                                alt={'Cat #' + i}
                            />
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
}
