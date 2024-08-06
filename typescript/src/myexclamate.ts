import {json, log} from "./util.js";

/*
    The real point of "!" is only to bypass compiler errors, not to prevent javascript
    from blowing up (use ? && ?? for that). With "!" you're saying, "Yes this might
    blow up on me but I don't care, so stop complaining."
*/
function testIgnoreMissingValue() {
    log("\ntestIgnoreMissingValue():");

    type KlutzNum = number | null | undefined;
    function flex(input: {pos:KlutzNum, nut:KlutzNum}) {
        log("flex():", (input.pos! + input.nut!));
    }
    // This will work out because null ~~ 0 (sorta)
    flex({pos:null, nut:null});

    // The exclamation mark makes no difference here:
    const x: {pos:number, nut:number | null} = {pos:1, nut:2};
    log(x.pos!.toString());
    log(x.nut!.toString());
}

function testQuestionMark() {
    log("\ntestQuestionMark():");

    function stiff(input: {pos:number, nut:number}) {
        // The ? operator prevents us from blowing up, of course. It only can be used
        // before a "." and results in "undefined" when confronted with null *or* undefined.
        // This is an existing javascript operator, BTW, not a new typescript thing:
        log("stiff() inputs:", json(input));
        log("stiff() inputs toString()'d:",
            input.pos?.toString() + " " + input.nut?.toString());

        // Things don't actually blow up, because javascript just treats
        // undefined as NaN and null as 0.
        log("stiff() add:", (input.pos! + input.nut!));
    }
    // By declaring z as "any" I can steer around uncooperative type signatures.
    let z:any = {pos: 3, nut:null};
    stiff(z);
    z.pos=undefined;
    stiff(z);
    z.pos=null;
    stiff(z);
}

export function test() {
    testIgnoreMissingValue();
    testQuestionMark();
}
