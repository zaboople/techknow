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
