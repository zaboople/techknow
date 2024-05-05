#!/usr/bin/env python3

from myutils import comment
from myutils import printOnPurposeExc

comment("""
    Also refer to class.py for "static" variables and such weirdness
""");

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
    try:
        f2b()
    except BaseException as be:
        printOnPurposeExc()
    print(f"f1 {x=}")

    comment("""
        Okay now we can force our way to outer scope
        by tossing a "nonlocal" on there
    """)
    def f3b():
        nonlocal x
        x += 1
        print(f"f2b {x=}")
    f3b()
    print(f"f1 {x=}")

f1()

