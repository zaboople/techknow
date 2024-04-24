#!/usr/bin/env python3

def mymatch(xx):
  match xx:
    case (1, 1):
      print(f"Matched 1,1 exactly");
    case (1, y):
      print(f"Match 1 {y=}");
    case (x, 2):
      print(f"Match {x=} 2");
    case (int(x), int(y)):
      print(f"Two arbitrary ints: {x=} {y=}");
    case (str(x)):
      print(f"A string! {x=}")
    case broke:
      print(f"Invalid parameter: {broke!r}");

print("\n*** Valid matches:")
mymatch((1,1))
mymatch((1,4))
mymatch((21,2))
mymatch((21,332))
mymatch("Stunty")

print("\n*** Invalid elements:")
mymatch((222,333,444))
mymatch(("a", "b", "c"))
mymatch(("a", "b"))

print("\n*** Note how a non-tuple list works ok:")
mymatch([12, 13])


def myFunc(a, b, c):
  match a, b, c:
    case str(a), str(b), int(c):
      print(f"Legit {a=} {b=} {c=}")
    case nope:
      print(f"Not right: {nope=}")
      return
  while c > 0:
    print(f"  {a} {b} {c}")
    c-=1

print("\n*** Look at me validating function inputs...")
myFunc("Hello", "World", 2)
myFunc("Hello", "World", True)
myFunc("Hello", "World", False)
myFunc("Hello", "World", ".")

