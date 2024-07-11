// Plain old assign object to separate variables trick. We can do it the sloppy
// way as in assign1, or the clean way as in assign2
function assign1() {
    console.log("\nassign1()");
    const x = {foo:"string", bar:"cant"};
    const {foo, bar} = x;
    console.log(x);
    console.log(`${foo} ${bar}`);
}
function assign2() {
    console.log("\nassign2()");
    const x = {foo:"string2", bar:"cant2"};
    const {foo, bar}:{foo:string, bar:string} = x;
    console.log(x);
    console.log(`${foo} ${bar}`);
}


// The variables-in-one-big-object parameter is a common practice in react
// with its custom elements/attributes stuff. This is the dumb way:
function paramsAsObj1({pos, nut}: any) {
    console.log("\nparamsAsObj1()");
    console.log(`${pos} ${nut}`);
}

// Now we can make last example properly typed in typescript like so:
function paramsAsObj2({pos, nut}: {pos:number, nut:number}) {
    console.log("\nparamsAsObj2()");
    console.log(`${pos} ${nut}`);
}

// Doing it with "optional" params, which will become "undefined" rather
// than "null" when missing; the specified params are still the *only*
// ones allowed, though:
function paramsAsObj3({pos, nut}: {pos?:number, nut?:number}) {
    console.log("\nparamsAsObj3()");
    console.log(`${pos} ${nut}`);
}

// A different angle: We only care about one thing on the object; we don't
// care about the rest. Yes we have to use the "extends" and a generic for this:
function paramsAsObj4<T extends {foo:number}>({foo}: T){
    console.log("\nparamsAsObj4()");
    console.log(foo);
}

/* Let's just do type aliases: */
type Aggregate = {
    foo: number;
    bar: string;
};
function checkAggregate(a: Aggregate) {
    console.log("\ncheckAggregate(): "+JSON.stringify(a));
}
type Agg2 = {
    foo?: number;
    bar?: string;
}
function checkAgg(a: Agg2) {
    console.log("\ncheckAgg()");
    console.log(a);
    console.log(a.foo +" "+a.bar);
}

export function test() {
    assign1();
    assign2();
    paramsAsObj1({pos: 12});
    paramsAsObj2({pos: 42, nut:523});
    paramsAsObj3({});
    paramsAsObj3({nut:12});
    paramsAsObj3({pos:4, nut:12});
    paramsAsObj4({rando: "slop", junk: 222, glop: 13, foo:222.22});
    checkAggregate({foo: 1, bar:"hi"});
    checkAgg({foo: 1, bar:"hi"});

    // I have to use "any" here so that I can add things:
    let foo: any={i:"hi"};
    foo.bar = "cheep";
    foo = {...foo, j:1, k:3};
    // Notice how I an cheat on type signatures because of that "any"
    checkAggregate(foo);
    checkAgg(foo);
}
