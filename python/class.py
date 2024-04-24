#!/usr/bin/env python3

def comment(ss=""):
    print("-----------------")
    for s in ss.split("\n"):
        print(s.strip())

comment("""
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
            int(random.random()*1000), "hello world"
        )

    @staticmethod
    def randoo():
        import random
        return int(random.random()*100)

    def talk(self):
        return f"Num is {self.mynum} Str is {self.mystr}"

qq = Bleek(1, "Hi")
print(f"{qq.mynum=} {qq.mystr=}")
qq = Bleek.rando()
print(f"{qq.mynum=} {qq.mystr=}")
print(f"{Bleek.randoo()=}")

comment("""
    Now let's see about shadowing statics
""")

class Bleeko(Bleek):

    def __init__(self, num, str):
        super().__init__(num, str)

    @staticmethod
    def randoo():
        import random
        return int(random.random()*100000)

rr = Bleeko(1, "Hi")
print(f"{rr.mynum=} {rr.mystr=}")
print(rr.talk())
rr = Bleeko.rando()
print(f"{rr.mynum=} {rr.mystr=}")
print(f"{Bleeko.randoo()=}")