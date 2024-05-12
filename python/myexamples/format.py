#!/usr/bin/env python3
import myutils
import datetime as dt
import time as tm

############################

myutils.comment("""
    Fomo info:
    https://docs.python.org/3/library/string.html#formatstrings
""")

myutils.comment("Ignores extra args:")
print("{0} -- {1} -- {2}".format("hello", "you", "nerd", "what"))

############################

myutils.comment("Simple decimal format:")
print("{0:.5f}".format(345.123456789))

myutils.comment("""
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

myutils.comment("""
    Justifying text:
""")
print("Left:   ->{0:<16}<-".format("hello"))
print("Right:  ->{0:>16}<-".format("hello"))
print("Center: ->{0:^16}<-".format("hello"))
print("None:   ->{0:}<-".format("hello"))


myutils.comment("""
    Old-timey formatting:
    https://docs.python.org/release/3.12.2/library/stdtypes.html#old-string-formatting
""")
print('Here is a number %5.3f.' % 2939393.4456787)

# offset is in seconds
today = dt.datetime.now(dt.timezone(dt.timedelta(hours=-5)))
print("{0:%Y-%m-%d %H:%M:%S.%f %p %z}".format(today))
today.strftime("{0:%Y-%m-%d %H:%M:%S.%f %p %z}")