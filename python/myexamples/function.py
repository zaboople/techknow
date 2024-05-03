#!/usr/bin/env python3

from myutils import comment

comment("""
  This behaves superweird. When a list has a default, it gets created once,
  and gets reused on every call. But NOT with an int - see accum variable.
""")
def weirdList(a, listy=[], accum=0):
    accum+=a;
    listy.append(accum)
    return listy

print("\nWatch accumulation of list...")
for i in range(1, 6):
    print(weirdList(i))

print("\nCall with named arguments:""");
print(weirdList(a=1, listy=[22,33]))

print("\nCall with named arguments using map & double-asterisk:")
map={"a":2, "listy":[12], "accum":100}
print(weirdList(**map))

print("\nCall with arguments using list & single-asterisk:")
_junk = [1, [1,2,3], 1000]
print(weirdList(*_junk))

comment("""
    One asterisk gives you an arbitrary dumpster of unnamed args
    Two asterisks gives you an arbitrary key-value map of named args
""")
def flexfun(*args, **dicti):
    for arg in args:
        print(f"Arg {arg}", end=" -- ")
    for key, value in dicti.items():
        print(f"Key {key} {dicti[key]}", end=" -- ")
    print()
flexfun(1, 2, "hello", boo=22, bash=23)
flexfun(*[1, 2, "hello"], **{"boo": 22, "bash": 23})

comment("Function documentation auto-populates the __doc__ variable:")
def docfunc(x, y):
    """
    What is my function about? About things.
    Yes it always been.
    """
    return x + y

print(f"Docs... {docfunc.__doc__=!s}")

comment("*** INSANE SCOPE SHADOWING: ***");
def f1():
    comment("""
        First let's see how variables shadow the parent
        because python sees the x= and decides it's local-only
    """)
    x=1
    def f2():
        x=2
        print(f"f2 {x=}")
        def f3():
            x=3
            print(f"f3 {x=}")
        f3()
        print(f"f2 {x=}")
    print(f"f1 {x=}")
    f2()
    print(f"f1 {x=}")

    comment("""
        Now we see that because they aren't assigning values,
        the inner functions suddenly see the outer assignment of x
    """)
    def f2a():
        print(f"f2a {x=}")
        def f3a():
            print(f"f3a {x=}")
        f3a()
    print(f"f1 {x=}")
    f2a()
    print(f"f1 {x=}")

    comment("""
        Now it just gets stupid. If you comment out the
        x += 1, f2b works, but otherwise it doesn't actually
        blow up on that line! The print() before it blows up!
    """)
    x += 1
    x -= 1
    def f2b():
        print(f"f2b {x=}")
        x += 1
    print(f"f1 {x=}")
    import traceback
    try:
        f2b()
    except BaseException as be:
        for line in traceback.format_exc().split("\n"):
            print(f"     >>> {line}")
    print(f"f1 {x=}")

f1()