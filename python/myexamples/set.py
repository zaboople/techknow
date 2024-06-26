#!/usr/bin/env python3

import collections
import myutils

myutils.comment("""
    Crazy not-in syntax
    I'm using a set as target of not-in, i.e. {} instead of []
    even though latter would work
""")
for x in [1,2,3,4,5]:
    if x not in {1,2,3}:
        print(x)

a = set('abracadabra')
b = set('alacazam')
myutils.comment(f"""
    Set logic:
    {sorted(a)      = }
    {sorted(b)      = }
    diff:      {a-b = }
    union:     {a|b = }
    intersect: {a&b = }
    xor:       {a^b = }
""")

