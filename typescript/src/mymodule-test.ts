import {Buglar, doober, mania} from "./mymodule.js";
export function test() {

    mania();
    doober();
    console.log();

    const bug: Buglar = new Buglar("Goop");
    console.log("Bug: "+bug.toJson());
    console.log(bug);
    bug.name = "Stupid";
    console.log(bug);

    console.log("\nAnother...");
    const bug1: Buglar = new Buglar("Another");
    bug1.level = 'hardcore';
    bug1.id = 12;
    console.log("jsonify(): "+bug1.toJson());
    console.log(
        "Object.entries(): "+Object.entries(bug1).map(([x,y])=> `${x}: ${y}` ).join(", ")
    );

    console.log("Explodo: "+bug1.explodo(2)+" "+bug1.explodo(1.32));
}
