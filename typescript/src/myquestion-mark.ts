import {json, log} from "./util.js";

function testQuestionMark() {
    log("\ntestQuestionMark():");

    // We can use our "?" operator to allow something to be "undefined"
    // but not *null*.
    const y: {pos?:number, nut?:number} = {nut:4};
    log(y.pos?.toString() +" "+y.nut?.toString());
    const q = y.pos??0 + (y.nut??0);
    log("q:", q);
}

function testNesting() {
    log("\ntestNesting():");
    type thing = {
        a?: {b?: {c?: {d?: number}}}
    };
    function boop(f: thing, g: thing){
        const fb = f?.a?.b;
        const fd = fb?.c?.d;
        const gd = g?.a?.b?.c?.d;
        log("boop(): json ", json(fd), " ", json(gd));
        log("boop(): numbers ", (fd ?? 0) + (gd ?? 0));
        log("boop(): add ", (f?.a?.b?.c?.d ?? 0) + (g?.a?.b?.c?.d ?? 0));
    }
    boop({a: {}}, {a: {}});
    boop({a: {b: {c: {d: 1}}}}, {a: {b: {c:{}}}});
    boop(
        {a: {b: {c: {d: 12}}}},
        {a: {b: {c: {d: 13}}}},
    );
}

export function test() {
    testQuestionMark();
    testNesting();
}
