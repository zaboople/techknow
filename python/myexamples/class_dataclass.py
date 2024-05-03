#!/usr/bin/env python3
from dataclasses import dataclass
from myutils import comment

@dataclass(frozen=True)
class MyData:
    myint: int
    mystr: str

comment("""
    Note automated constructor, str(), repr() and __eq__()
    Also note that I am abusing my type signatures... I guess
    those are just "advisory"
""")
mydata = MyData("hello", 1)
print(f"{mydata}")
print(f"{mydata=}")
mydata2 = MyData("hello", 1)
print(f"{mydata==mydata2 =}")

comment("""
    Hashing only works on frozen dataclasses, apparently:
""")
myset = {mydata, mydata2}
print(f"{myset =}")