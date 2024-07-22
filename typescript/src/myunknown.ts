import {log} from "./util.js";

class Junk {
    x: string
    constructor(x:string) {this.x=x;}
}
function unk(q: unknown) {
    // Have to use typeof() on non-objects because instanceof
    // will not accept types of literals like string & number
    if (q instanceof Junk)
        log("unk(): ", `Junk: ${(q as Junk).x}`);
    else if (typeof(q) == "string")
        log("unk(): ", `string: ${q}`);
    else
        log("unk(): ", `Still unknown: ${q}`);
}
function getLast(...a: unknown[]) {
    return a[a.length-1];
}

export function test() {
    unk(new Junk("hi2"));
    unk(12);
    log(getLast([1, 2, 3]));
    log(getLast([1, 2]));
    log(getLast([]));
}

