#!/usr/bin/env python3

from myutils import comment;

comment("""
    Let's show off annotations on classes, especially
    the subtly different static/class method annotations:
""")


class Bleek(object):

    # This is not an instance variable! It is a class
    # variable. You can get to it using self.staticky, but that
    # is really just using it read-only; if you assign a value,
    # self.staticky becomes an actual instance variable and other
    # instances don't notice your change:
    staticky = "Variable In Bleek"

    def __init__(self, num, stri):
        print(f"Bleek.init(): {self.staticky=}")
        self.mynum = num
        self.mystr = stri

    # Class method always takes the class as its first parameter; for child classes
    # this class will be the first param.
    @classmethod
    def rando(cls):
        import random
        cls.staticky = "rando() was called"
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

comment("Here is a directly initialized Bleek:")
qq = Bleek(1, "Hi")
print(f"{qq=}")

comment("Here is a Bleek.rando():")
qq = Bleek.rando()
print(f"""
    Plain string: {qq}
    Repr  string: {qq=}
    {qq.mynum=} {qq.mystr=}
    {Bleek.randoStatic()=}
""")

comment("""
    Here is another directly initialized Bleek:
    Notice how the "staticky" class variable still has stuff left over
    from Bleek.rando()
""")
qq = Bleek(2, "Second time")
print(f"{qq=}")

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

rr.staticky="I changed staticky from outside class, against an instance"
comment(f"""
    Messing with staticky....
        str: {rr}
        rr.repr {rr=}
        {Bleek.staticky=}
        {Bleeko.staticky=}
""")

comment(f"""
    Seeing if last call affects staticky....
""")
rr = Bleeko(13, "How goes staticky now?")
print(f"{rr=}")
