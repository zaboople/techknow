
type KlutzNum = number | null | undefined;
function flex(input: {pos:KlutzNum, nut:KlutzNum}) {
    // This is the real point of "!": To avoid a compiler error by admitting you're doing
    // something that is likely to blow up on you, i.e. your input is allowing null/undefined
    // (Note: Null will just be treated as "0", FWIW)
    console.log(input.pos! + input.nut!);
}

function stiff(input: {pos:number, nut:number}) {
    console.log("\n");

    // The ? operator prevents us from blowing up, of course. It only can be used
    // before a "." and results in "undefined" when confronted with null *or* undefined.
    console.log(`stiff() inputs: ${input.pos} ${input.nut}`);
    console.log(`stiff() inputs toString()'d: ${input.pos?.toString()} ${input.nut?.toString()}`);

    // Humorously the ! makes no difference (again); even with a missing value it
    // just treats undefined as NaN and null as 0, with or without "!".
    console.log("stiff() add: "+(input.pos! + input.nut!));
}

export function test() {

    // Sorry, but exclamation mark is not an elvis operator (but don't forget "??")
    // I can change nut's vaue to null but exclamation mark won't stop it from blowing up.
    const x: {pos:number, nut:number | null} = {pos:1, nut:2};
    console.log(x.pos!.toString());
    console.log(x.nut!.toString());

    // However we also have a "?" operator that allows something to be "undefined"
    // but not *null*.
    const y: {pos?:number, nut?:number} = {nut:4};
    console.log(y.pos?.toString() +" "+y.nut?.toString());
    const q = y.pos??0 + (y.nut??0);
    console.log(`q ${q}`);

    // By declaring z as "any" I can steer around uncooperative type signatures.
    let z:any = {pos: 3, nut:null};
    stiff(z);
    z.pos=undefined;
    stiff(z);
    z.pos=null;
    stiff(z);

    // This will work out because null ~~ 0 (sorta)
    flex({pos:null, nut:null});
}

