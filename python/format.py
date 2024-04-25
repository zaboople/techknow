#!/usr/bin/env python3
def comment(ss=""):
    print("\n-----------------")
    for s in ss.split("\n"):
        if s!="": print(s.strip())

############################

comment("""
    https://docs.python.org/3/library/string.html#formatstrings
""")

comment("Ignores extra args:")
print("{0} -- {1} -- {2}".format("hello", "you", "nerd", "what"))

############################

comment("""
    Right-justifying decimals so "." lines up, with commas:
""")

import decimal
def myRightJustify(number, mywidth, myprec):
    formatStr = "{0:>{mywidth},.{myprec}f} {mywidth} wide, {myprec} digits after point"
    return formatStr.format(number, mywidth=23, myprec=5)

for x in [
        -1 * 3234.33 / 35.7,
        -1 * decimal.Decimal(1233337.233) / decimal.Decimal(35.7),
        345.67 / 2334.555,
        998883345.67 / 2334.555,
    ]:
    print(
        myRightJustify(x, mywidth=25, myprec=6)
    )

