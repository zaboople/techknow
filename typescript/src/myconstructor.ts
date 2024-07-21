// Look I'm defining an interface for... a constructor!
// Certainly weird inasmuch as how do I call it?
type ObjConstr = {
  new (s: string): Object;
};
function runConstr(fn: ObjConstr) {
    return new fn(" ->from runConstr()<- ");
}

class Boog {
    readonly b:string;
    constructor(q:string) {this.b=q;}
}
export function test() {
    console.log(new Boog("test!"));
    console.log(runConstr(Boog));
}
