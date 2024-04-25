#!/usr/bin/env python3

def comment(ss=""):
    print("-----------------")
    for s in ss.split("\n"):
        if s!="": print(s.strip())


comment("""
    Checking on equals for classes:
""")

class Pair:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __eq__(self, other):
        # This is a standard way to test equivalence
        # so that == works right
        if isinstance(other, self.__class__):
            return self.__dict__ == other.__dict__
        else:
            return False

p1 = Pair(1, 2)
p2 = Pair(1, 2)
p3 = Pair(1, 3)
print(f"{(p1 is p1) =}")
print(f"{(p1 is not p1) =}")
print()
print(f"{(p1 == p2) =}")
print(f"{(p1 != p2) =}")
print(f"{(p1 is p2) =}")
print()
print(f"{(p1 == p3) =}")
print(f"{(p1 != p3) =}")
print(f"{(p1 is not p3) =}")
