function foo(s: "A" | "B" | "C") {
    console.log(`foo(): ${s}`);
}
function foo2(input: {s: "A" | "B" | "C"}) {
    console.log(`foo(): ${input.s}`);
}
function foo3(input: {readonly s: "A" | "B" | "C"}) {
    console.log(`foo(): ${input.s}`);
}

export function test() {
    // This only works because we use "const" instead of "let"; in the
    // latter case typescript tends to lose track and be unusure
    const x = "A";
    foo(x);

    // This requires a special "as const" tacked on or compiler will
    // be unhappy, similar to prior example; typescript knows y might
    // be const, but the s variable inside our object is not.
    const y = {s:"A"} as const;
    foo2(y);

    // No, tacking on a "readonly" in foo3() didn't help, so "as const"
    // it is:
    const z = {s:"A"} as const;
    foo3(z);
}