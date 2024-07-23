import {json, log} from "./util.js";

/*
    The real point of "!" is only to bypass compiler errors, not to prevent javascript
    from blowing up (like ? && ?? can do). With "!" you're saying, "Yes this might
    blow up on me but I don't care, so stop complaining."
*/

function testIgnoreMissingValue() {
    log("\ntestIgnoreMissingValue(): ");

    type KlutzNum = number | null | undefined;
    function flex(input: {pos:KlutzNum, nut:KlutzNum}) {
        log("flex(): ", (input.pos! + input.nut!));
    }
    // This will work out because null ~~ 0 (sorta)
    flex({pos:null, nut:null});

    // The exclamation mark makes no difference here:
    const x: {pos:number, nut:number | null} = {pos:1, nut:2};
    log(x.pos!.toString());
    log(x.nut!.toString());
}

function testQuestionMark1() {
    log("\ntestQuestionMark1(): ");

    function stiff(input: {pos:number, nut:number}) {
        // The ? operator prevents us from blowing up, of course. It only can be used
        // before a "." and results in "undefined" when confronted with null *or* undefined.
        // This is an existing javascript operator, BTW, not a new typescript thing:
        log("stiff() inputs: ", json(input));
        log("stiff() inputs toString()'d: ",
            input.pos?.toString() + " " + input.nut?.toString());

        // Things don't actually blow up, because javascript just treats
        // undefined as NaN and null as 0.
        log("stiff() add: ", (input.pos! + input.nut!));
    }
    // By declaring z as "any" I can steer around uncooperative type signatures.
    let z:any = {pos: 3, nut:null};
    stiff(z);
    z.pos=undefined;
    stiff(z);
    z.pos=null;
    stiff(z);
}

function testQuestionMark2() {
    log("\ntestQuestionMark2(): ");

    // We can use our "?" operator to allow something to be "undefined"
    // but not *null*.
    const y: {pos?:number, nut?:number} = {nut:4};
    log(y.pos?.toString() +" "+y.nut?.toString());
    const q = y.pos??0 + (y.nut??0);
    log("q: ", q);
}

function testNesting() {
    log("\ntestNesting(): ");
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
    testIgnoreMissingValue();
    testQuestionMark1();
    testQuestionMark2();
    testNesting();
}
