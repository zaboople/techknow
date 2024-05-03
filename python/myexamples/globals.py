#!/usr/bin/env python3

def printGlob():
    print(f"Global: {myglob}")


def setup():
    print("""
        Necessary "global" keyword for setting globals in functions.
    """)
    global myglob
    myglob = 10
    printGlob()

setup();

# This is taken straight from python tutorial:
def scope_test():
    def do_local():
        # No effect outside of here:
        spam = "local spam"

    def do_nonlocal():
        # Makes it inside the enclosing function:
        nonlocal spam
        spam = "nonlocal spam"

    def do_global():
        # Global to the module:
        global spam
        spam = "global spam"

    spam = "test spam"
    do_local()
    print("After local assignment:", spam)
    do_nonlocal()
    print("After nonlocal assignment:", spam)
    do_global()
    print("After global assignment:", spam)

scope_test()
print("In global scope:", spam)