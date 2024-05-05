#!/usr/bin/env python3
from myutils import comment

comment("""
    Yield can be used to create a "generator". This is much easier than
    using __iter__() and __next__()
""")

def iterate():
    yield 12
    yield 10
    yield 8
    yield 1
    return "bonkers" # This is meaningless and does nothing

for x in iterate():
    print(x)
