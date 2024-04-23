#!/usr/bin/env python3
import sys
def lblr(stri):
  print("----")
  print(f"*** {stri}: ***")

lblr("Shallow copy, emphasis on shallow!")
firstOne = [[1,2],[3,4]]
myCopy = firstOne[:]
print(f"Before messing: {firstOne=} {myCopy=}")
myCopy[0][0]=99
myCopy[1]=[33,44,55]
myCopy[1].append(66)
print(f"After:          {firstOne=} {myCopy=}")
print(f"Length of original list: {len(firstOne)=}");

lblr("Loop thru command line arguments and print them: ")
for agh in sys.argv:
  print("Argument: "+agh)
print(f"Skip file name with slice: {sys.argv[1:]=}")