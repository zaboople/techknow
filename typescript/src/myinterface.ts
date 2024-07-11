
/* Suppose we want something that just has a .junk on it, but we don't care what else.
   Typescript will tend to blow up if we define an object type that only has .junk
   and something tries to pass us something with *other* fields. We can throw up our hands
   and drop an "any" to force our way... meh: */
function onAny(x: any) {
    console.log("onAny(): "+x.clod);
}

/** This allows us to make a function that accepts an input as long as it has
    the field we want; it can have more fields but it doesn't have to. Note that
    I have to do goofy T extends crap because just the vanilla declaration doesn't work.*/
interface ClodHaver {
    clod: number | null;
}
function printClod<T extends ClodHaver>(x: T) {
    console.log("\nprintClod()");
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



export function test() {
    onAny(2);
    onAny({clod: "jupe", klang:23});
    printClod({foo: 1, clod: 2});
    printClod2({foo: 1, clod: 2});
    printClod({foo: 1, clod: null});
    printClod2({foo: 1, clod: null});
}


