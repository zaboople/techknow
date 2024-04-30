#!/usr/bin/env python3

import sys

for ii in sys.argv:
    print(ii)

argv = list(reversed(sys.argv))
while len(argv) > 0:
    arg = argv.pop()
    print(arg)
