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

// Here is the proper way to do it with "optional" params, which will
// become "undefined" rather than "null" when missing; the specified
// params are still the *only* ones allowed, though:
function paramsAsObj3({pos, nut}: {pos?:number, nut?:number}) {
    console.log("\nparamsAsObj3()");
    console.log(`${pos} ${nut}`);
}

export function test() {
    assign1();
    assign2();
    paramsAsObj1({pos: 12});
    paramsAsObj2({pos: 42, nut:523});
    paramsAsObj3({});
    paramsAsObj3({nut:12});
    paramsAsObj3({pos:4, nut:12});
}
