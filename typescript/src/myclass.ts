import {json, log, logIntro} from "./util.js";

/**
    You can turn on a feature to prevent uninitialized instance variables
    with --strictPropertyInitialization, then backpedal and turn it off
    with a "!" flag when you realize that it's difficult to make this
    a universal practice. Meh.
**/
function testLateInit() {
    logIntro("testLateInit()");
    class Foo {
        x!:string;
        constructor() {}
    };

    const foo = new Foo();
    log(foo);
}

// Getter/Setters allow you to make a field accessor that is
// actually a function call.
function testGetSet() {
    logIntro("testGetSet()");
    class Seget {
        private _q: number;
        get q(): number {
            return this._q;
        }
        set q(qq: number) {
            this._q=qq;
        }
    }
    const seg = new Seget();
    seg.q = 1;
    log(seg); // Reveals the hidden value... derp.
    log(seg.q);
}
export function test() {
    testLateInit();
    testGetSet();
}

