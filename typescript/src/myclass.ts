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

/**
 * The clever trick hiding out here is I'm using `this` as
 * a return type instead of the name of the class.
 * This means that Box.get() will return Boxx when called
 * against an instance of the latter. Nifty.
 */
function testInheritance() {
    logIntro("testInheritance()");
    class Box {
        get(): this {return this;}
    }
    interface Loo {}
    class Boxx extends Box implements Loo {
        gett(): Boxx {return new Boxx();}
    }
    const b: Boxx = new Boxx().get();
    log(b);
}

/** It turns out you can take a shortcut on classes
    if you do it just so; put an access modifier on parameters
    and they automatically become instance variables */
export function testConstructorAutoMembers() {
    logIntro("testConstructorAutoMembers()");
    class Foo {
        constructor(public foo:string, private bar:number){
            log("constr...():", "foo", foo, "bar", bar);
        }
    }
    const f = new Foo("hi", 1);
    log(f);
}

/** "this is" gives type narrowing */
function testThisIs() {
    logIntro("testThisIs()");
    class Box {
        constructor(public name:string) {}
        isBlack(): this is BlackBox {
            return this instanceof BlackBox;
        }
    }
    class BlackBox extends Box {
        constructor(public name:string, public depth:number){
            super(name);
        }
    }
    function onBox(box: Box) {
        log(box);
        if (box.isBlack()) {
            // Notice how type narrowing allows me to reference
            // the depth variable directly!
            log("... is blackbox, depth =", box.depth.toString());
        } else {
            log("... is not blackbox.");
        }
    }
    const b = new Box("not black");
    const bb = new BlackBox("very black", 12);
    log("Testing Box...");
    onBox(b);
    log("Testing BlackBox...");
    onBox(bb);
}

/** Yet again this allows us narrowing, this time
    to get past a nullable nasty: */
function testThisIs2() {
    logIntro("testThisIs2()");
    class Box {
        a?: string;
        b?: number;
        hasA(): this is {a: string}{
            return this.a != null;
        }
        hasB(): this is {b: number}{
            return this.b != null;
        }
    }
    function onBox(box: Box): void {
        log(box);
        if (box.hasA())
            log("a", box.a.toString());
        if (box.hasB())
            log("b", box.b.toString());

        // This is illegal without narrowing
        // because a or b might be null; the
        // this-is gets us the narrowing above.
        //log("a", box.a.toString());
        //log("b", box.b.toString());
    }
    const buh = new Box();
    buh.a = "ah";
    buh.b = 123;
    const beh = new Box();
    beh.b = 345;
    onBox(buh);
    onBox(beh);
}
export function test() {
    testLateInit();
    testGetSet();
    testInheritance();
    testConstructorAutoMembers();
    testThisIs();
    testThisIs2();
}

