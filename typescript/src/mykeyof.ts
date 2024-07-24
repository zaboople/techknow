import {json, log} from "./util.js";


export function test() {
    type Obj = {a: number, b:number, c:number};
    type ObjKey = keyof Obj;
    function fn(...keys: Array<ObjKey>) {
        log(
            "fn():", keys.map(key=>json(key))
        );
    }
    function fn2(...keys: Array<keyof Obj>) {
        fn(...keys);
    }
    // Only a, b and/or c can be passed in:
    fn("a", "b", "c");
    fn("a", "b");
    fn("a");
    const a = "a", b = "b", c = "c";
    fn2(a, b, c);
}
