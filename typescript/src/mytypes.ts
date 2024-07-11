type StringToNum = (s: string) => number;
function runStringToNum(ff: StringToNum) {
    ["a", "ab", "abc", "abcd"].forEach(s=>
        console.log("runStringToNum(): "+ff(s))
    );
}

/* We can define a type as an "object" without using classes or interfaces */
type Aggregate = {
    foo: number;
    bar: string;
};
function checkAggregate(a: Aggregate) {
    console.log(`checkAggregate(): ${JSON.stringify(a)}`);
}

/* This requires an "any" in order to work. We are acknowledging that we're
  just grasping at straws: */
function onAny(x: any) {
    console.log(x.junk);
}

/** This allows us to make a function that accepts an input as long as it has
    the field we want; it can have more fields but it doesn't have to. */
interface ClodHaver {
    clod: any;
}
function printClod<T extends ClodHaver>(x: T) {
    console.log(x.clod);
}

type Agg2 = {
    foo?: number;
    bar?: string;
}
function checkAgg(a: Agg2) {
    console.log(a);
    console.log(a.foo +" "+a.bar);
}

export function test() {
    runStringToNum(s => s.length);
    runStringToNum(s => (s+s).length);
    checkAggregate({foo: 1, bar:"hi"});
    onAny(2);
    printClod({foo: 1, clod: 2});
}


