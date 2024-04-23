#!/usr/bin/env python3

fibs = []
a, b = 0, 1
while a < 1099:
    print(a, end=' ')
    a, b = b, a+b
print()