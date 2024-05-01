#!/usr/bin/env python3

import myutils;

myutils.comment("""
    I don't think I can come up with many good reasons to
    have the walrus operator but ok:
""")
for x in [1,2,3,4,5]:
    print(x:=x+1, end=" ")
    print(x)

myutils.comment("""
    Better usage usually happens in while:
""")
x=0
while (x := x + 1) < 10:
    print(x, end=" ")
print()


