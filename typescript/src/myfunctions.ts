import {json, log} from "./util.js";

/**
    This is just crap. You have to declare all the overloads, then
    implement one function that tries to be all of them. What's the
    point? Anyhow, the initial signatures define what you can actually
    call. The function itself is sort of... masked? Masked, yes.
*/
function testOverloads() {
    log("\ntestOverloads():");
    function fn(): string;
    function fn(s: string): string;
    function fn(s: string, i: number): string;
    function fn(s?: string, i?:number): string {
        if (i!=undefined) return ""+(i ** 2);
        if (s!=undefined) return s+s;
        return "" as never;
    }

    // These two make sense:
    log("fn():", fn());
    log("fn():", fn("test"));
    log("fn():", fn("ferg", 1));

    // There is no way to support this! If we try
    // to define the interface it just gets in fights
    //log("fn():", fn(1));
}
function testArrowAsType() {
    log("\ntestArrowAsType():");
    type StringToNum = (s: string) => number;
    function runStringToNum(ff: StringToNum) {
        ["a", "ab", "abc", "abcd"].forEach(s=>
            log(`runStringToNum(): ${s} => `+ff(s))
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
    log("\ntestInlineDiamond():");
    function smush<T>(a: T, b: T): T {
        return a > b ?a :b;
    }
    // This is interesting in that I have to call with the diamond:
    const result = smush<string | number>(1, "two");
    log("smush():", result);

    // Even defining these two things as the same type, I had to <> diamond
    // it again:
    type smushable = string | number;
    const a: smushable = "hello", b: smushable = 22293939.33;
    log("smush():", smush<smushable>(a, b));

    // Tighter syntax for same function as pure arrow function:
    const smash: <T>(a: T, b: T)=>T =
        (a,b) => a > b ?a :b;
    log("smash():", smash<smushable>(a, b));
    log("smash():", smash<smushable>(1345, "Yagarbgh"));
}

function testOptionalParams() {
    log("\ntestOptionalParams():");
    function fn(a?:string, b?: string, c?:string, d?:string):void {
        log("fn:",
            arguments.length, " -> ",
            (a ?? "*") + (b ?? "-") + (c ?? "_") + (d ?? "|")
        );
    }
    fn();
    fn("a");
    fn("a", "b");
    fn("a", "b", "c");
    fn("a", "b", "c", "d");
}
function testDefaultParams() {
    log("\ntestDefaultParams():");
    function fn(x: string, y:number = 1, z:number = 2, a:string="3") {
        log("fn(): ", x, y, z, a);
    }
    fn("hello");
    fn("hello", 33);
    fn("hello", 33, 44);
    fn("hello", 33, 44, "eek");
}

function testAssignment() {
    log("\ntestAssignment():");

    // Start with a regular function & arrow function
    // that basically mean the same thing:
    function regular<T>(arg: T): T {return arg;}
    const arrow = <T>(arg: T) => arg;

    // Assign them interchangeably to constants typed
    // as arrow, regular function (note the weird curly
    // braces), or interface, even:
    type tArrow = <T>(arg: T) => T;
    type tRegular = {<T>(arg: T): T};
    interface GenericIdentityFn {<T>(arg: T): T;};
    type tInterface = GenericIdentityFn;
    const all: [
        tArrow, tRegular, tInterface, tArrow, tRegular, tInterface,
        ] = [
        regular, regular, regular, arrow, arrow, arrow,
        ];

    // They just come out as "Function" when logged:
    for (const x of all)
        log(x);
    log(all.map(x => x(11)).join(" "));
}

export function test() {
    testOverloads();
    testArrowAsType();
    testInlineDiamond();
    testOptionalParams();
    testDefaultParams();
    testAssignment();
}
