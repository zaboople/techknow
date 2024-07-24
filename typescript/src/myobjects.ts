import {json, log} from "./util.js";

// Plain old assign object to separate variables trick. We can do it the sloppy
// way as in assign1, or the clean way as in assign2
function testAssign() {
    log("\ntestAssign():");
    function assign1() {
        const x = {foo:"string", bar:"cant"};
        const {foo, bar} = x;
        log("assign1():", json(x), foo, bar);
    }
    function assign2() {
        const x = {foo:"string2", bar:"cant2"};
        const {foo, bar}:{foo:string, bar:string} = x;
        log("assign2():", json(x), foo, bar);
    }
    assign1();
    assign2();
}

function testParamsAsObj() {
    log("\ntestParamsAsObj():");
    // The variables-in-one-big-object parameter is a common practice in react
    // with its custom elements/attributes stuff. This is the dumb way:
    function test1({pos, nut}: any) {
        log("test1()", pos, nut);
    }
    test1({pos: 12});

    // Now we can make last example properly typed in typescript like so:
    function test2({pos, nut}: {pos:number, nut:number}) {
        log("test2()", pos, nut);
    }
    test2({pos: 42, nut:523});

    // Doing it with "optional" params, which will become "undefined" rather
    // than "null" when missing; the specified params are still the *only*
    // ones allowed, though:
    function test3({pos, nut}: {pos?:number, nut?:number}) {
        log("test3():", pos, nut);
    }
    test3({});
    test3({nut:12});
    test3({pos:4, nut:12});

    // A different angle: We only care about one thing on the object; we don't
    // care about the rest. Yes we have to use the "extends" and a generic for this:
    function test4<T extends {foo:number}>({foo}: T){
        log("test4():", json(foo));
    }
    test4({rando:"slop", junk: 222, glop: 13, foo:222.22});
}

function testTypeAlias() {
    log("\ntestTypeAlias()");
    /* Let's just do type aliases: */
    type Agg1 = {foo: number; bar: string;};
    function check1(a: Agg1) {
        log("check1():", json(a), a.foo, a.bar);
    }
    type Agg2 = {foo?: number; bar?: string;}
    function check2(a: Agg2) {
        log("check2():", json(a), a.foo, a.bar);
    }
    check1({foo: 1, bar:"hi"});
    check2({foo: 1, bar:"hi"});

    // I have to use "any" here so that I can add things:
    let foo: any={i:"hi"};
    foo.bar = "cheep";
    foo = {...foo, j:1, k:3};
    // Notice how I an cheat on type signatures because of that "any"
    check1(foo);
    check2(foo);
    // I can even pass off some undefined values:
    foo={};
    check1(foo);
}

function testObjDefaults() {
    console.log("\ntestObjDefaults():");
    type Parms = {a?:number, b?:number, c?:number};
    const fn = ({a=1, b=2, c=3}: Parms) =>
        log("fn():", a, b, c);
    fn({});
    fn({a:33});
    fn({a:33, b:44});
}

export function test() {
    testAssign();
    testParamsAsObj();
    testTypeAlias();
    testObjDefaults();

    // I didn't know this part, even though it's probably just regular javascript:
    const x=1, y="hi";
    const o = {x,y};
    log("\nAssemble variables as object:", json(o));
}
