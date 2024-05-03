#!/usr/bin/env python3

from myutils import comment;

def myformat(s):
    print("{0:>75}".format(s))

comment("""
    Shows how names preceded by __ are "mangled" by python
    to prevent collisions; they're sorta effectively private.
    Subclasses can't see them either.
""")

class BigParent:
    #__mangled = "2"
    name: int

    def __init__(self):
        self.__mangled = "120"
        self.unmangled = "140"
        myformat(f"init: {self.speak()=}")
    def speak(self):
        return f"{self.__mangled=} {self.unmangled=}"


class SmallChild(BigParent):
    def talkSmall(self):
        self.__mangled = "332"
        self.unmangled = "334"
        return f"{self.__mangled=} {self.unmangled=}"


x = BigParent()
myformat(f"{x.speak()=}")

y = SmallChild()
myformat(f"{y.talkSmall()=}")
myformat(f"{y.speak()=}")

print()
print(f"{x.unmangled=} {y.unmangled=}")
try:
    print(f"{x.__mangled=} {y.__mangled=}")
except:
    print("Could not get to the mangled variables, got exception")