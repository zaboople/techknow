import {json, log, logIntro} from "./util.js";

function testPlain() {
    logIntro("testPlain()");
    enum Moof {
        q, r, s, a, b, c
    };
    const fn = (input: Moof) =>
        log("fn():", input, "=>", Moof[input]);
    log("Here is all of Moof...");
    log(Moof);
    log("Moof.a is a number:", Moof.a);
    log("Backreferencing Moof.a gets its letter:", Moof[Moof.a]);
    log("Backreferencing Moof[3] gets same:", Moof[3]);
    fn(Moof.c);
    fn(3);
}

function testDoubled() {
    logIntro("testDoubled()");
    enum E2 {
        A = 1.4,
        B = 2,
        C = 13,
    };
    const fn = (input: E2) => log("fn():", input, "=>", E2[input]);
    log(E2);
    log("Values of E2:", E2.A, E2.B, E2.C);
    fn(E2.A);
    fn(E2.B);
    fn(E2.C);
}

// NOTE: We can't do E2[key] here because we're using strings
// and for whatever reasons typescript doesn't agree to use the
// same keying strategy:
function testDoubled2() {
    logIntro("testDoubled2()");
    enum E2 {
        A = "Hi there",
        B = "Yo",
        C = "Bye",
    };
    //log(E2[E2.A]); illegal because string?
    log(E2);
    log("Values of E2:", E2.A, E2.B, E2.C);
}
function testBad() {
    logIntro("testBad()");
    enum E2 {
        A = 1,
        B = 1,
        C = 1,
    };
    const fn = (input: E2) =>
        log("fn():", input, "=>", E2[input]);
    log(E2);
    log("Values of E2:", E2.A, E2.B, E2.C);
    log("Trying to look up each goes wrong:");
    fn(E2.A);
    fn(E2.B);
    fn(E2.C);
}
export function test() {
    testPlain();
    testDoubled();
    testDoubled2();
    testBad();
}
