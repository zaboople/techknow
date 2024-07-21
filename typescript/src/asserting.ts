
/* This allows us to force typescript to use our assertion to reason about any kind of "narrowing" */
function assertIsString(val: any): asserts val is string {
    if (typeof val !== "string")
        throw new Error("Not a string!");
}
export function test() {
    try {
        assertIsString(1);
    } catch (t) {
        console.log("Caught: "+t.toString());
    }
    assertIsString("Hello");
}
