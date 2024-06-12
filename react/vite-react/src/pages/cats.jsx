import {useEffect, useState, useRef} from 'react';

function initCats() {
    console.log("Initializing cats...");
    const catList = [];
    const base = "https://loremflickr.com/320/240/cat?lock=";
    for (let i = 0; i < 10; i++)
        catList.push({id: i, imageUrl: (base + i)});
    return catList;
}
export default function Cats() {
    const refPrev = useRef(null);
    const refNext = useRef(null);
    const mymap = useRef(new Map());
    const [firstLoad, setFirstLoad] = useState(true);
    const [catList, setCatList] = useState(initCats);
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
        console.log("Index "+ix);
        setIndex(ix);
        refPrev.current.disabled = ix == 0;
        refNext.current.disabled = ix == catList.length -1;
        if (refPrev.current.disabled)
            refNext.current.focus();
        else if (refNext.current.disabled)
            refPrev.current.focus();
        const pic = mymap.current.get(ix);
        if (pic)
            pic.scrollIntoView({behavior:"smooth"});
        else
            console.log("No pic!");
    }
    function toRef(xx, cat){
        if (xx)
            mymap.current.set(cat.id, xx)
        else
            mymap.current.delete(cat.id)
    }
    useEffect(()=>{
        if (firstLoad) {
            refNext.current.focus();
            refPrev.current.disabled=true;
            setFirstLoad(false);
        }
    }, [firstLoad]);
    return (
        <div className="subbody flexVert">
            <h2>Cats - What? Cats</h2>
            <nav className="catbox">
                <button ref={refPrev} onClick={clickPrev}>Previous</button>
                <button ref={refNext} onClick={clickNext}>Next</button>
            </nav>
            <div className="catbox">
                <ul>
                    {catList.map((cat, i) => (
                        <li key={cat.id}>
                            <img ref={xx=>toRef(xx, cat)}
                                className={index === i ?'active' :''}
                                src={cat.imageUrl}
                                alt={'Cat #' + cat.id}
                            />
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
}



