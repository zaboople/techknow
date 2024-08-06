import {json, log} from "./util.js";

// Look I'm defining an interface for... a constructor!
// Certainly weird inasmuch as how do I call it?
type ObjConstr = {
  new (s: string): Object;
};
function runConstr(fn: ObjConstr) {
    return new fn(" ->from runConstr()<- ");
}

// This is better, because it's generically typed:
interface BetterConstr<T> {
  new (s: string): T;
};
function betterConstr<T>(fn: BetterConstr<T>): T {
    return new fn(" ->from betterConstr()<- ");
}


class Boog {
    readonly b:string;
    constructor(q:string) {this.b=q;}
}
export function test() {
    log(new Boog("test!"));
    log(runConstr(Boog));
    const boo: Boog = betterConstr(Boog);
    log(boo);
}
