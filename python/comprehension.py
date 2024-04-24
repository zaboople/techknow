#!/usr/bin/env python3

print("""
    List comprehensions...
""")

print("""
    Obtain all two-char combinations of A & Z where two chars are different:
""")
rangeBig = range(ord('A'), ord('Z')+1)
rangeLil = range(ord('A'), ord('Z')+1)
mylist = [
    chr(x)+chr(y)
    for x in rangeBig for y in rangeLil
    if x != y
]
bigStr = "".join(mylist)
print(bigStr)
print(f"{len(bigStr)=}")

print("""
    Now split it into lines and reassemble
""");

lineLen = 65
chunkList = [
    bigStr[index: index + lineLen]
    for index in range(0, len(bigStr), lineLen)
];
print("\n".join(chunkList))