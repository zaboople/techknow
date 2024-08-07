import {json, log} from "./util.js";

class Circle {
  readonly kind = "circle";
  radius: number;
  constructor(r: number) {this.radius = r;}
}
class Square {
  readonly kind = "square";
  length: number;
  constructor(l: number) {this.length = l;}
}
class Dog {
  readonly kind = "dog";
}

type Shape = Circle | Square | Dog;

/*
    Here is our "never", and it sure is dumb. I thought it would
    save me on typechecking, but instead it just makes everything
    twice as bad. The "narrowing" in typescript doesn't even pick up
    my missing `case "dog":` except... it... should?
*/
function dub(val: Shape): number {
    log("\nIncoming:", typeof(val));
    log(val);
    switch (val.kind) {
        case "circle":
            return val.radius ** 2 * Math.PI;
        case "square":
            return val.length ** 2;
        default:
            // Notice how this allows me to NOT return a number.
            // What? Take the "as never" off and I'll get a proper
            // compiler error.
            return "ab" as never;
    }
}
export function test() {
    log("Circle:", dub(new Circle(12)));
    log("Square:", dub(new Square(12)));
    log("Dog:", dub(new Dog()));
    const x = dub(new Dog());
    log("One more time with dog:", x);
    log("Well, what was the type? It's:", typeof(x));
}
