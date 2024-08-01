import {json, log} from "./util.js";

/*
    Suppose we want something that just has a .junk on it, but we don't care what
    else. Typescript will tend to blow up if we define an object type that only
    has the field we need, and something tries to pass us something with *other* fields
    that we don't care about and aren't an actual problem. What to do?
*/
function testExtraFields() {
    log("\ntestExtraFields(): ");

    /* The sloppiest, worst way: */
    function printClod(x: any) {
        log("printClod():", x.clod);
    }
    printClod(2);
    printClod({clod: "jupe", klang:23});

    /** This is a little better, but the "extends" is obnoxious.*/
    interface ClodHaver {clod: number | null;}
    function printClodIfc<T extends ClodHaver>(x: T) {
        log("printClodIfc():", json(x), x.clod ?? "<null>");
    }
    printClodIfc({foo: 1, clod: 2});
    printClodIfc({foo: 1, clod: null});

    /** Improved...? This brackety thing looks absurd, but it's a way of saying that
        you can have any additional properties that you want: */
    interface ClodHaverIfcX {
        clod: number | null;
        [___: string]: any;
    }
    function printClodIfcX(...xx: ClodHaverIfcX[]) {
        for (const x of xx)
        log("printClodIfcX():", json(x), x.clod ?? "<null>");
    }
    printClodIfcX(
        {clod: null, foo: "bar", key: "nud"}, {clod:12}
    );

    /** Wait... We don't even need an interface for this. */
    type ClodHaverObj = {clod: number | null};
    function printClodObj<T extends ClodHaverObj>(x: T) {
        log("printClodObj(): ", json(x), x.clod ?? "<null>");
    }
    printClodObj({foo: 1, clod: 2});
    printClodObj({foo: 1, clod: null});

    /** One better! No interface, no extends. This is the best, I think: */
    type ClodHaverMax = {
        clod: number | null;
        [__: string]: any;
    };
    function printClodMax(...xx: ClodHaverMax[]) {
        for (const x of xx)
            log("printClodMax():", json(x), x.clod ?? "<null>");
    }
    printClodMax(
        {clod: null, foo: "bar", key: "nud"}, {clod:12},
        {clod: 12, meep: {a:1, b:2}},
    );


    /* Just for kicks */
    function printLen<T extends {length:number}>(input: T) {
        log("printLen():", input.length);
    }
    printLen("hi there");
    printLen([1,2,3]);
    printLen({length:4, foo:"hi", bar:[1,2,3,4]});
}

/**
    Just demoing that both ways of declaring a function in an interface
    are equivalent and typescript doesn't care. Also note that readonly
    can be written to by the constructor and nowhere else, so it's like
    java's "final".
*/
function testFunctionDeclare() {
    log("\ntestFunctionDeclare(): ");
    interface HayMaker {
        makeHay(): void;
        makeHay2: ()=>void;
    }
    class MyMaker implements HayMaker {
        readonly input:string;
        readonly pieces:number;
        constructor(input: string, pieces:number) {
            this.input=input; this.pieces=pieces;
        }
        makeHay(): void {
            // Typescript iterators are broken so I have to do absurd syntax
            // just to get back to a proper array. There's probably some weirdness
            // I could do with compiler targets but WTF:
            log(
                "makeHay(): ",
                Array.from(new Array(this.pieces).keys())
                    .map(()=>this.input).join("")
            );
        }
        makeHay2(): void {this.makeHay();}
    }
    new MyMaker("hey", 4).makeHay2();
}

function testIntersect() {
    log("\ntestIntersect(): ");
    interface A {a: number;}
    interface B {b: number;}
    interface C extends A, B {c: number;};

    // This accomplishes the same as `extends A, B` but
    // intersect operator cannot be used with interfaces,
    // only types - but notice how I can turn around and extend
    // yet another interface *from* that type:
    type CC = A & B & {c: number;};
    interface D extends CC {d: number;};

    function fn(c: C, cc: CC, d: D) {
        log(json(c));
        log(json(cc));
        log(json(d));
    }
    const x = {a: 1, b: 2, c:3};
    fn(x, x, {...x, d:4});
}

export function test() {
    testExtraFields();
    testFunctionDeclare();
    testIntersect();
}

