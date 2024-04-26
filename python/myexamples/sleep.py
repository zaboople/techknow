#!/usr/bin/env python3
import time
import threading
import sys

for i in range(ord('A'), ord('z')+1):
    if i % 5 == 0:
        print("\b", end="")
        print("_", end="")
    else:
        print(chr(i), end="")
    # The flush prevents a system hang caused by I/O buffering
    sys.stdout.flush()
    time.sleep(0.3)
