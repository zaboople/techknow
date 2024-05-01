#!/usr/bin/env python3

import myutils;

myutils.comment("""
    Let's show off annotations on classes, especially
    the subtly different static/class method annotations:
""")


class Bleek(object):

    def __init__(self, num, stri):
        self.mynum = num
        self.mystr = stri

    @classmethod
    def rando(cls):
        import random
        return cls(
            int(cls.randoStatic()), "hello world"
        )

    @staticmethod
    def randoStatic():
        import random
        return int(random.random()*100)

    def __str__(self):
        # Does the classic toString()
        return f"mynum={self.mynum} mystr={self.mystr}"

    def __repr__(self):
        # Implements the classic repr()
        return f"mynum={self.mynum} mystr={repr(self.mystr)}"

    def talk(self):
        return f"Talk: Num is {self.mynum} Str is {self.mystr}"

qq = Bleek(1, "Hi")
print(f"{qq.mynum=} {qq.mystr=}")

qq = Bleek.rando()
print(f"""
    Plain string: {qq}
    Repr  string: {qq=}
    {qq.mynum=} {qq.mystr=}
    {Bleek.randoStatic()=}
""")

myutils.comment("""
    Now let's see about shadowing statics and class extension:
""")

class Bleeko(Bleek):

    @staticmethod
    def randoStatic():
        # Effectively overrides the static method
        # even when that is called from parent class's rando()
        import random
        return int(random.random() * -10000000)

print(f"Does new randoStatic() work: {Bleeko.randoStatic()=}")
rr = Bleeko(1, "Hi")
print(f"Does init work without override: {rr.mynum=} {rr.mystr=}")
print(f"Does talk() inherit: {rr.talk()=}")
rr = Bleeko.rando()
print(f"""Does rando() call override of randoStatic? toStringing():
    {rr}
    {str(rr)}
    {rr=}
""")
