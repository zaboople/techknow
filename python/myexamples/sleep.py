#!/usr/bin/env python3
import time
import sys

# This slowly prints characters, backspacing and overwriting some of them.
# This is only going to work at the console, for the most part.

def prt(c):
    # The flush prevents a system hang caused by I/O buffering
    # combined with time.sleep()
    print(c, end="")
    sys.stdout.flush()
    time.sleep(0.3)

first = True
for i in range(ord('A'), ord('z')+1):
    if (not first) and i % 5 == 0:
        prt("\b")
        prt("_")
    else:
        first=False
    prt(chr(i))
