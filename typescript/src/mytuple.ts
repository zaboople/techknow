import {json, log} from "./util.js";

/**
    Tuples are nice, but do we really need them in JS? Hm, maybe.
    Arguably it's so easy to make objects that one is inclined to
    wave tuples off. Nice alternative when people insist on using
    arrays that are really meant to be tuples, though, and everything
    that works on arrays works on tuples too.
**/
export function test() {

    type MyTuple = readonly [string, number, {a:number, b:number}];

    function klong([s, n, {a, b}]: MyTuple) {
       log("klong():", s,  n,  a,  b);
    }

    function klang(lines: MyTuple[]) {
        for (const line of lines) {
            for (const item of line)
                log("klang():", json(item));
            klong(line);
        }
    }

    klang([
        ["hi", 1, {a: 12, b: 34}],
        ["hey", 2, {a: 45, b: 56}],
    ]);
}
