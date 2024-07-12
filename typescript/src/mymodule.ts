export function doober() {
    console.log("doober()");
}
export function mania() {
    console.log("mania()");
}
class Std {
    public toJson(): string {
        return JSON.stringify(this);
    }
}
type TLevel = "amateur" | "pro" | "hardcore";
export class Buglar extends Std{
    id: number;
    name: string;
    level: TLevel;

    constructor(s: string) {
        super();
        this.name=s;
    }
    explodo(i: number): number {
        return this.id = this.id * i;
    }
}
