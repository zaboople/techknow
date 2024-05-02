#!/usr/bin/env python3

import collections
import myutils

myutils.comment("""
    Queue:
""")
myqueue = collections.deque([1,2,3,4])
myqueue.extend([12, 13, 14, 15])
while len(myqueue) > 0:
    print(myqueue.pop(), end=" ")
print()

