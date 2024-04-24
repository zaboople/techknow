#!/usr/bin/env python3

lst = list(range(1, 100, 10))
print(f"Starting List {lst=} {len(lst)=}")
print("""
  Looping, using range as substitute for
    for (i=0; i<10; i++)
      etc etc
""")
for x in range(0, len(lst)):
  print(f"Next index {x=} {lst[x]=}")


print("""
  Range of 10 to 100 by 10:
""")
lst = range(0, 101, 10)[1:]
for x in range(0, len(lst)):
  print(f"Next index {x=} {lst[x]=}")

lst = range(10, 0, -1)
print(list(lst))