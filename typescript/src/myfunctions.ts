const log = (a: any) => console.log(a);

function testOverloads() {
    // This is just crap. You have to declare all the overloads,
    // then implement one function that tries to be all of them.
    // What's the point? Anyhow, the initial signatures define
    // what you can actually call. The function itself is sort of...
    // masked? Masked, yes.
    function shib(): string;
    function shib(s: string): string;
    function shib(s: string, i: number): string;
    function shib(s?: string, i?:number): string {
        if (i!=undefined) return ""+(i ** 2);
        if (s!=undefined) return s+s;
        return "" as never;
    }

    // These two make sense:
    log("testOverloads(): "+shib());
    log("testOverloads(): "+shib("test"));
    log("testOverloads(): "+shib("ferg", 1));

    // There is no way to support this! If we try
    // to define the interface it just gets in fights
    //log("testOverloads(): "+shib(1));
}
function testArrowAsType() {
    type StringToNum = (s: string) => number;
    function runStringToNum(ff: StringToNum) {
        ["a", "ab", "abc", "abcd"].forEach(s=>
            console.log(`runStringToNum(): ${s} => `+ff(s))
        );
    }
    runStringToNum(s => s.length);
    runStringToNum(s =>
        s.split("")
            .map(i=>i.charCodeAt(0))
            .reduce((acc, init)=>acc+init, 0)
    );
}
function testInlineDiamond() {
    function smush<T>(a: T, b: T): T {
        return a > b ?a :b;
    }
    // This is interesting in that I have to call with the diamond:
    const result = smush<string | number>(1, "two");
    console.log(`smush(): ${result}`);

    // Even defining these two things as the same type, I had to <> diamond
    // it again:
    type smushable = string | number;
    const a: smushable = "hello", b: smushable = 22293939.33;
    console.log("smush(): "+ smush<smushable>(a, b));

    // Tighter syntax for same function as pure arrow function:
    const smash: <T>(a: T, b: T)=>T =
        (a,b) => a > b ?a :b;
    console.log("smash(): "+ smash<smushable>(a, b));
    console.log("smash(): "+ smash<smushable>(1345, "Yagarbgh"));
}
function testOptionalParams() {
    function fn(a?:string, b?: string, c?:string, d?:string):void {
        console.log("testOptionalParams(): "+
            arguments.length+" -> "+
            (a ?? "*") + (b ?? "-") + (c ?? "_") + (d ?? "|")
        );
    }
    fn();
    fn("a");
    fn("a", "b");
    fn("a", "b", "c");
    fn("a", "b", "c", "d");
}

export function test() {
    testOverloads();
    testArrowAsType();
    testInlineDiamond();
    testOptionalParams();
}

