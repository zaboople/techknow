import {json, log, logIntro} from "./util.js";

// This is the official way, and it's dumb. It's stupid.
// It's impossible to specify a return type so you
// have no idea what you're getting. Middle finger
function testDumb() {
    type Constructor = new (...args: any[]) => {};

    function Scale<TBase extends Constructor>(Base: TBase){
        class Scaling extends Base  {
            // Mixins may not declare private/protected properties
            // however, you can use ES2020 private fields
            _scale = 1;
            setScale(scale: number):void {
                this._scale = scale;
            }
            getScale(): number {
                return this._scale;
            }
        };
        return Scaling
    }

    class Dingus {
        constructor(public name: string){}
    }
    const ScaleDingus = Scale(Dingus);
    const burp = new ScaleDingus("Hi there");
    burp.setScale(12);
    log(burp.getScale(), burp.name);
}

/** This seems just as good */
function testSmart() {
    interface Scalable {
        setScale(n: number): void;
        getScale(): number | undefined;
    }
    function addScale<T>(t: T): T & Scalable {
        let scale: number | undefined;
        const u: Scalable = {
            setScale: (n: number) => scale = n,
            getScale: () => scale,
        };
        return {...t, ...u};
    }
    class Box {
        constructor(public height:number, public width:number){}
    }
    const box = addScale(new Box(11, 22));
    box.setScale(1.2);
    log(box);
    log(box.getScale());
}
export function test() {
    testDumb();
    testSmart();
}