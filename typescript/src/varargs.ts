import {json, log} from "./util.js";

/**
    So the normal sig for varargs is
        `...x: something[]` or
        `...x: Array<something>`
    In the special case of any, you can just do:
        `...x: any`
    - but why do it different?
**/

function fn(...input: any) {
    for (const z of input)
        log("fn(): ", json(z));
}

class Junk {
    x: string
    constructor(x:string) {this.x=x;}
}
function fnjunk(...input: Junk[]) {
    for (const z of input)
        log("fnjunk(): ", z);
}

function fnn(...input: Array<number>) {
    for (const z of input)
        log("fnn(): ", z);
}

function un(...input: unknown[]) {
    for (const z of input)
        log("un(): ", json(z));
}

export function test() {
    fn(1, 2, "hi");
    un({a: 3, b:4}, "yerp", 12);
    fnn(12, 23.2, 0.3);
}