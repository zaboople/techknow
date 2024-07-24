import {json, log} from "./util.js";

function testRegularConst() {
    function foo(s:"A" | "B" | "C") {
        log("testRegularConst():", s);
    }
    // This only works because we use "const" instead of "let"; in the
    // latter case typescript tends to lose track and be unusure
    const x = "A";
    foo(x);
}

function testConstObject() {
    function foo2(input: {s:"A" | "B" | "C"}) {
        log("testConstObject() foo2():", json(input));
    }
    function foo3(input: {readonly s:"A" | "B" | "C"}) {
        log("testConstObject() foo3():", json(input));
    }

    // This requires a special "as const" tacked on or compiler will
    // be unhappy, similar to prior example; typescript knows y might
    // be const, but the s variable inside our object is not.
    const y = {s:"A"} as const;
    foo2(y);

    // No, tacking on a "readonly" in foo3() didn't help, so "as const"
    // it is:
    const z = {s:"A"} as const;
    foo3(z);
}

function testArrayFun() {
    // Clever ... forces varargs to be used:
    function fn(a: number, b:string, c:number, d:string): void {
        log("testArrayFun():", a, b, c, d);
    }
    const q = [1, "hi", 2, "hey"] as const;
    fn(...q);
}


export function test() {
    testRegularConst();
    testConstObject();
    testArrayFun();
}