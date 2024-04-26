#!/usr/bin/env python3
def comment(ss=""):
    print("\n-----------------")
    for s in ss.split("\n"):
        if s!="": print(s.strip())

comment("""
    modulo & floor-division:
""")

def doOps(x, y):
    modulo = x % y
    dfloor  = x // y
    str1 = f"{x} % {y} = {modulo}"
    str2 = f"{x} // {y} = {dfloor}"
    print("{0:<14}  {0:<14}".format(str1, str2))

for x in range(1,20):
    for y in range(1,x):
        doOps(x, y)

comment("""
    ternary:
""")
for x in range(1,10):
    for y in range(1,10):
        print(
            "_" if (x%3 == y%4) else ".", # ternary!
            end=""
        )
