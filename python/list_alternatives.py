#!/usr/bin/env python3

def comment(ss=""):
    print("-----------------")
    for s in ss.split("\n"):
        print(s.strip())

import collections

comment("""
    Queue:
""")
myqueue = collections.deque([1,2,3,4])
myqueue.extend([12, 13, 14, 15])
while len(myqueue) > 0:
    print(myqueue.pop(), end=" ")
print()


comment("""
    Crazy not-in syntax
    I'm using a set as target of not-in, i.e. {} instead of []
    even though latter would work
""")
for x in [1,2,3,4,5]:
    if x not in {1,2,3}:
        print(x)