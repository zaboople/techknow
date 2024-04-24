#!/usr/bin/env python3

def comment(ss=""):
    print("-----------------")
    for s in ss.split("\n"):
        print(s.strip())

def myfunc(y):
    return y+1

comment("""
    https://docs.python.org/release/3.12.2/reference/expressions.html#lambda
    Lambdas must be expression-based; they are essentially one-liners. They can
    call functions, but they can't be treated as functions. They can have default
    parameters as per below
""")
mylam = lambda x, y=111: y + myfunc(x) * 10

print(f"mylam {mylam}")
print(f"{mylam(100)=}")

comment("""
    Here is use of inline lambda inside a lambda, w00t;
    also note that asterisk'd args works on lambdas:
""")
myconcat = lambda *argv: "--".join(
    map(
        lambda qq: str(qq), argv
    )
)
print(f"{myconcat(1,2,3,4)=}")
