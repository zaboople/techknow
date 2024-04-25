#!/usr/bin/env python3

def fibi(a, b):
  fibs = []
  while a < 9999:
      print(a, end=' ')
      a, b = b, a+b # Multi-assign
  print(a)

print("Multi assign variables in one statement:")
fibi(0, 1)
fibi(1, 2)
fibi(2, 3)
fibi(3, 4)
fibi(4, 5)
