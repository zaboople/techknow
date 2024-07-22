export function log(...x: unknown[]) {
    if (x.length==1)
        console.log(x[0]);
    else
        console.log(x.join(""));
}
export function json(x: unknown): string {
    return JSON.stringify(x);
}