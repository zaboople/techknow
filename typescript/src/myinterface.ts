/* Suppose we want something that just has a .junk on it, but we don't care what else.
   Typescript will tend to blow up if we define an object type that only has .junk
   and something tries to pass us something with *other* fields. We can throw up our
   hands and drop an "any" to force our way... meh: */
function printClod(x: any) {
    console.log("printClod(): "+x.clod);
}

/** An interface like ClodHaver gets us a better solution. Note that
    I have to do goofy T extends crap because the more straightforward
    way doesn't work.*/
interface ClodHaver {
    clod: number | null;
}
function printClod1<T extends ClodHaver>(x: T) {
    console.log("\nprintClod1()");
    console.log(x.clod);
}

/** Oh look! We don't even need an interface for this. There are some
    extraordinary quirks but in the end it's hard to recommend interfaces...
    https://www.typescriptlang.org/docs/handbook/2/everyday-types.html
*/
type ClodHaver2 = {clod: number | null;};
function printClod2<T extends ClodHaver2>(x: T) {
    console.log("\nprintClod2()");
    console.log(x.clod);
}


/**
    Just demoing that both ways of declaring a function in an interface
    are equivalent and typescript doesn't care. Also note that readonly
    can be written to by the constructor and nowhere else, so it's like
    java's "final".
*/
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
        console.log(
            "\nmakeHay(): "+Array.from(new Array(this.pieces).keys())
                .map(()=>this.input).join("")
        );
    }
    makeHay2(): void {this.makeHay();}
}

export function test() {
    printClod(2);
    printClod({clod: "jupe", klang:23});
    printClod1({foo: 1, clod: 2});
    printClod1({foo: 1, clod: null});
    printClod2({foo: 1, clod: 2});
    printClod2({foo: 1, clod: null});
    new MyMaker("hey", 4).makeHay2();
}
