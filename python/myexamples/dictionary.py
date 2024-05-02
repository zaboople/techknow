#!/usr/bin/env python3

def comment(ss=""):
    print("-----------------")
    for s in ss.split("\n"):
        if s!= "": print(s.strip())


comment("""
    You can make a dictionary out of a sequence of two-member
    tuples, which is what I'm doing here, convoluted as it looks:
""")
import functools
myDict = dict(map(
    lambda name: (
        name,
        functools.reduce(
            # My best effort at hashing a string:
            lambda total, c:
                total + ord(c) ^ 11111111,
            name,
            0
        )
    ),
    ["onesy", "two", "three", "four", "rouf"]
))

print(f"{myDict=}")
myDict[123]="Huh well"
print(f"{myDict=}")

comment("""
    Dictionary loop k-v pairs:
""")
for key, value in myDict.items():
    print(f"  {key=} {value=}")

comment("""
    Dictionary loop keys:
""")
for key in myDict:
    print(f"  {key=} {myDict[key]=}")

comment("""
    Check for keys:
""")
if 121231231244 not in myDict:
    print("And that's how you check for keys")
if not myDict.get("hi"):
    print("Another way");