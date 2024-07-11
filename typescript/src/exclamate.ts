function stiff(input: {pos:number, nut:number}) {
    console.log("\n");

    // The ? operator prevents us from blowing up, of course:
    console.log(`stiff() input: ${input.pos?.toString()} ${input.nut?.toString()}`);

    // Humorously the ! makes no difference; even with a missing value it just treats
    // undefined as NaN and null as 0, with or without "!".
    console.log(`stiff() add: ${input.pos! + input.nut!}`);
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
    // Interestingly the question mark operator in stiff() will convert our null
    // to undefined somehow, which is to say: No, not even the mighty Typescript
    // can straighten out the null/undefined ambiguity.
    let z:any = {pos: 3, nut:null};
    stiff(z);
    z.pos=undefined;
    stiff(z);
}

