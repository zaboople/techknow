#!/usr/bin/env python3
import time
import sys
from myutils import comment

print("Starting...")
sys.stdout.flush()
startTime = time.monotonic_ns()
bigstr = ""
for i in range(1000, 3 * 1000000):
    if i % 101 == 0:
        bigstr=""
    else:
        bigstr += str(i) + " "
print(bigstr)
endTime = (time.monotonic_ns() - startTime) / (1000 * 1000 * 1000)
comment("""
    The real demo here is to show elapsed time:
""")
print(f"Elapsed {endTime} seconds")
