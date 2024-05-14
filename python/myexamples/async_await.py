#!/usr/bin/env python3

import asyncio;

async def doof(x):
    result = f"Hoopto {x}"
    print(result)
    return result

async def doowop():
    listy = [doof(x) for x in range(1, 1000)]
    print(f"coroutines: {listy}")
    # This gathers the results into an output list
    results = await asyncio.gather(*listy)
    print(f"Results: {results}")
asyncio.run(doowop())
