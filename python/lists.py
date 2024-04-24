#!/usr/bin/env python3

def comment(ss=""):
    print("-----------------")
    for s in ss.split("\n"): print(s.strip())

comment("""
    Quick lists using range():
""")
pseudo = range(1, 10)
print(f"{pseudo}")
lst = list(pseudo)
lst[3] = 99
print(f"{lst}")

comment("""
    Remove from list using del... operator... function?
    Uses slice notation:
""")
lst = list(range(1, 22))
while (len(lst) > 0):
    del lst[:4]
    print(lst)

comment("""
    Crazy not-in syntax
    I'm using a set as target of not-in, i.e. {} instead of []
    even though latter would work
""")
for x in [1,2,3,4,5]:
    if x not in {1,2,3}:
        print(x)