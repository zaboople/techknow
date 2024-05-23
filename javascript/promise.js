        // This prints "dook"
        // after waiting:
        const result = await new Promise(
            resolver => setTimeout(()=>resolver("dook"), 2000)
        );
        console.log("Result of promise: "+result);

        // This doesn't wait for the promise to finish.
        // That means you to await it, or do a then():
        const promise = new Promise(
            resolver => setTimeout(()=>resolver("dook"), 2000)
        );
        promise.then(v=>console.log("Done: "+v));
