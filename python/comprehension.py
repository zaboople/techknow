#!/usr/bin/env python3
def comment(ss=""):
    print("-----------------")
    for s in ss.split("\n"):
        print(s.strip())


comment("""
    List comprehensions...
    Obtain all two-char combinations of A & Z where two chars are different:
""")
rangeBig = range(ord('A'), ord('Z')+1)
rangeLil = range(ord('A'), ord('Z')+1)
mylist = [
    chr(x)+chr(y)
    for x in rangeBig
    for y in rangeLil
    if x != y
]
bigStr = "".join(mylist)
print(f"{len(bigStr)=}  {bigStr=}")

print("\nNow split it into lines and reassemble:\n")
lineLen = 65
chunkList = [
    bigStr[index: index + lineLen]
    for index in range(0, len(bigStr), lineLen)
];
print("\n".join(chunkList))

comment("""
    Flattening! With comprehensions
""")
matrices=[
    [
        [11,22,33],
        [41,51,61],
        [72,82,92],
    ],
    [
        [1,2,3],
        [4,5,6],
        [7,8,9],
    ],
]
print(f"{matrices=}")
flattened = [
    xcol * 10
    for matrix in matrices
    for xrow in matrix if len(xrow) > 2
    for xcol in xrow   if xcol > 3
]
print(f"{flattened=}")
