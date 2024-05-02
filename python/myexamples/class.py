#!/usr/bin/env python3

from myutils import comment;

comment("""
    Let's show off annotations on classes, especially
    the subtly different static/class method annotations:
""")


class Bleek(object):

    # A class variable that can be overridden, sort... of...
    # If you refer to it using "self" it's overridden.
    staticky = "Variable In Bleek"

    def __init__(self, num, stri):
        print(f"Bleek.init(): {self.staticky=}")
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
        return f"mynum={self.mynum} mystr={self.mystr} staticky={self.staticky}"

    def __repr__(self):
        # Implements the classic repr()
        return f"mynum={self.mynum} mystr={repr(self.mystr)} staticky={repr(self.staticky)}"

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

comment("""
    Now let's see about shadowing statics and class extension:
""")

class Bleeko(Bleek):

    # A class variable
    staticky = "overriding variable in bleeko"

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
comment(f"""
    Does rando() call override of randoStatic? toStringing():
        str: {rr}
        str: {str(rr)}
        repr: {rr=}
""")
rr.staticky="What I changed it"
comment(f"""
    Messing with staticky....
        str: {rr}
        rr.repr {rr=}
        {Bleek.staticky=}
        {Bleeko.staticky=}
""")