
type StringToNum = (s: string) => number;
function runStringToNum(ff: StringToNum) {
    ["a", "ab", "abc", "abcd"].forEach(s=>
        console.log(`runStringToNum(): ${s} => `+ff(s))
    );
}

type myfunc = (x: String) => void;
const arbitrary: myfunc = q => console.log("arbitrary(): "+q);

export function test() {
    runStringToNum(s => s.length);
    runStringToNum((s:String)=>
        s.split("")
            .map(i=>i.charCodeAt(0))
            .reduce((acc, init)=>acc+init, 0)
    );
    arbitrary("Hello");
}
