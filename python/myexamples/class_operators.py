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

    def __lt__(self, other):
        # Supposedly if I just do this and __eq__, all
        # the other comparisons fall into place.
        if isinstance(other, self.__class__):
            return (self.x < other.x) or (self.x == other.x and self.y < other.y)
        else:
            return False

    def __hash__(self):
        # Necessary for sets & maps
        return hash((self.x, self.y))

    def __str__(self):
        # For toString()ing
        return f"Pair({self.x}, {self.y})"

    def __repr__(self):
        # For the repr() global function
        return f"Pair({repr(self.x)}, {repr(self.y)})"


p1 = Pair(1, 2)
p2 = Pair(1, 2)
p3 = Pair(1, 3)
p4 = Pair(1, 4)
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

comment("""
    Make sure a set or dictionary works right
""")
myset = {p1,p2,p3,p1}
print(f"{myset=}")
myhash = {p1: p2, p2: p3, p3: p1}
print(f"{myhash=}")

comment("""
    Can it sort?
""")
mylist = [p4, Pair(2, 1), Pair(2, 5), p3, Pair(-1, 1000), p2, p1,]
for item in sorted([p4, Pair(2, 1), Pair(2, 5), p3, Pair(-1, 1000), p2, p1,]):
    print(f"  {item}")

