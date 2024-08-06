import {json, log} from "./util.js";

/**
    Ugh! You have to declare all the overloads, then implement one function
    that tries to be all of them. Only the declared overloads are visible
    to callers; the *implementation* function that does the actual work is
    NOT visible.

    This typically means that your implementation function has to defang
    absurd-looking union types, and where parameter names are nearly meaningless
    because they are actually stand-ins for completely different use-cases.
    You have to *really* want overloads to be bothered with this.
*/
function testOverloads() {
    log("\ntestOverloads():");

    // So our overloads allow no args, two strings, or two numbers:
    // (note our use of typeof does not force b to match a's
    //  real-time type; it only gives us a way not to repeat ourselves)
    function fn(): number;
    function fn(a: string, b: string): number;
    function fn(a: number, b: number): number;
    function fn(a?: string | number, b?: typeof a): number {
        // Type narrowing is also quietly saving the day as we go,
        // with the null checks, making sure parseInt() gets a string,
        // etc:
        if (a==null) return 0;
        if (b==null) return 0;
        const aa = (typeof(a)=="string") ?parseInt(a) :a;
        const bb = (typeof(b)=="string") ?parseInt(b) :b;
        return aa + bb;
    }
    log(fn("1", "2"));
    log(fn(3, 4));
    log(fn());
    // This won't compile, because we have no interface for it:
    //log(fn(3, "4"));
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
    interface iRegular {<T>(arg: T): T;};
    type tInterface = iRegular;
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
