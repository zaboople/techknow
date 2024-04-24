#!/usr/bin/env python3

def comment(ss=""):
    print("-----------------")
    for s in ss.split("\n"): print(s.strip())

comment("""
    Tuples act like lists but you can do match with them.
    Also note that parentheses around them are effectively optional:
""")
xx = 1, 2, 5, 7
print(f"{xx=}")
for i in xx:
  print(i)

comment("""
    Funny syntax for one-element tuple:
""")
xx = 111,
print(f"{xx}")
for i in xx:
    print(i)

comment("""
    Funny syntax for zero-element tuple:
""")
xx = ()
print(f"{xx=} {len(xx)=}")


comment("""
    Use tuples for multi-assign
""")
xx = 33, 44, 55
ii, jj, kk = xx
print(f"{xx=}:   {ii=} {jj=} {kk=}")

