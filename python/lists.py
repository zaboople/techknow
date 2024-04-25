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
    Sorting a list with lambdas, also see
    https://docs.python.org/3/howto/sorting.html#sortinghowto
""")
print(
    sorted(
        [("aa", 33), ("bb", 11), ("cc", 22), ("winner", 0)],
        key = lambda x: x[1]
    )
)