import {json, log} from "./util.js";

/* Suppose we want something that just has a .junk on it, but we don't care what else.
   Typescript will tend to blow up if we define an object type that only has .junk
   and something tries to pass us something with *other* fields. We can throw up our
   hands and drop an "any" to force our way, but extends can help here: */
function testExtraFields() {
    log("\ntestExtraFields(): ");
    function printClod(x: any) {
        log("printClod():", x.clod);
    }
    printClod(2);
    printClod({clod: "jupe", klang:23});

    /** An interface like ClodHaver gets us a better solution. Note that
        I have to do goofy T extends crap because the more straightforward
        way doesn't work.*/
    interface ClodHaver {
        clod: number | null;
    }
    function printClod1<T extends ClodHaver>(x: T) {
        log("printClod1():", json(x), x.clod ?? "<null>");
    }
    printClod1({foo: 1, clod: 2});
    printClod1({foo: 1, clod: null});

    /** Oh boy, this is weird, but it works the same.
        This brackety thing is utterly absurd, but it's a way of saying
        that you can have any additional properties that you want. */
    interface ClodHaver3 {
        clod: number | null;
        [crudBomb: string]: any;
    }
    function printClod3(...xx: ClodHaver3[]) {
        for (const x of xx)
        log("printClod3():", json(x), x.clod ?? "<null>");
    }
    printClod3(
        {clod: null, foo: "bar", key: "nud"}, {clod:12}
    );


    /** Oh look! We don't even need an interface for this. There are some
        extraordinary quirks but in the end it's hard to recommend interfaces...
        https://www.typescriptlang.org/docs/handbook/2/everyday-types.html
    */
    type ClodHaver2 = {clod: number | null};
    function printClod2<T extends ClodHaver2>(x: T) {
        log("printClod2(): ", json(x), x.clod ?? "<null>");
    }
    printClod2({foo: 1, clod: 2});
    printClod2({foo: 1, clod: null});

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
