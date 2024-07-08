// Plain old assign object to separate variables trick:
function do1() {
    console.log("\ndo1()");
    const x = {foo:"string", bar:"cant"};
    const {foo, bar} = x;
    console.log(x);
    console.log(foo);
    console.log(bar);
}

// The variables-in-one-big-object parameter is a common practice in react
// with its custom elements/attributes stuff. This is the dumb way:
function do2({pos, nut}: any) {
    console.log("\ndo2()");
    console.log(pos);
    console.log(nut);
}

// Now we can make last example properly typed in typescript like so:
function do3({pos, nut}: {pos:number, nut:number}) {
    console.log("\ndo3()");
    console.log(pos);
    console.log(nut);
}

export function test() {
    do1();
    do2({pos: 12});
    do3({pos: 42, nut:523});
}
