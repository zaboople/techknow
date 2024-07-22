export function log(...x: unknown[]) {
    console.log(x.join(""));
}
export function json(x: unknown): string {
    return JSON.stringify(x);
}