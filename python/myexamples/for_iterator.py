#!/usr/bin/env python3
from myutils import comment

comment("""
    #    Silly example of making a class iterable, which requires
    #    two methods:  __iter__() and __next__()
    #
    #    I am putting the latter in a separate inner class (yes
    #    inner classes work in python!)
""")

class MyThing:
    class MyIterator:
        def __init__(self, mything):
            self.yet = 0
            self.mything = mything

        def __next__(self):
            """Called by iteration"""
            if self.yet > 1:
                raise StopIteration()
            self.yet += 1
            if self.yet == 1:
                return self.mything
            return "<no more>"

    def __init__(self, thing):
        self.thing = thing

    def __iter__(self):
        """Makes this class iterable"""
        return self.MyIterator(self)

    def __str__(self):
        # Does the classic toString()
        return f"{self.thing}"

    def __repr__(self):
        # Implements the classic repr()
        return f"MyThing({self.thing})"

#########

t = MyThing("Hello World")
for x in t:
    print(f"Next iteration: {x}")
