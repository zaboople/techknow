#!/usr/bin/env python3

import traceback
from myutils import comment

def debugEx(ex):
    """Custom-prints a stack trace"""
    print(f"\nDebugging exception: {ex}")
    for line in traceback.format_exc().split("\n"):
        print(f"     >>> {line}")
    print()

def badfunc():
    for x in x:
        pass

def blowup(toSay):
    raise Exception(f"You wanted me to say: '{toSay}'")

def nestedBlowup(toSay):
    try:
        blowup(f"Calling blowup with: \"{toSay}\"")
    except Exception as ex:
        raise Exception(f"Blew up in nestedBlowup(); nested: {ex}") from ex

class MyEx(BaseException): pass

def doMyException(param):
    if (param==2):
        raise Exception(f"Bleagh {param}")
    if (param==1):
        raise MyEx(f"Jackpot! {param}")
    print(f"Acceptable: {param}")

######################

comment("Nested exceptions...")
try:
    nestedBlowup("Hi there")
except Exception as ex:
    debugEx(ex)

comment("Defunct function call...")
try:
    badfunc()
except Exception as ex:
    debugEx(ex)

comment("Classic integer parsing mess...")
try:
    int("hello")
except ValueError as ex:
    print(f"\nAs expected we couldn't parse int: {ex}\n")

comment("Using custom exception...")
for xxx in [4,3,2,1]:
    try:
        doMyException(xxx)
    except MyEx as ex:
        print(f"  Got *my* exception: \"{ex}\"")
    except BaseException as ex:
        print(f"  Got regular exception: \"{ex}\"")

comment("Multiple exceptions caught at once:")
try:
    doMyException(1)
except (RuntimeError, TypeError, NameError, MyEx) as ex:
    print(f"  Got one of several exceptions: \"{ex.__class__.__name__}\"")

comment("try - except - else - finally:")
try:
    doMyException(88)
except (RuntimeError, TypeError, NameError, MyEx) as ex:
    print(f"  Got one of several exceptions: \"{ex.__class__.__name__}\"")
else:
    print("Successfully made it thru to else")
finally:
    print("Yes, finally exists")

comment("Re-raise, also adding notes to an exception:")
try:
    try:
        ii = int("efwef")
    except (RuntimeError, TypeError, NameError, ValueError) as ex:
        print(f"Got an exception but re-raising: {ex=}")
        ex.add_note("Here is a note")
        ex.add_note("Here is another note")
        raise ex
except BaseException as ex:
    print("Got the sucker")
    debugEx(ex)

comment("""
    You're kidding! Returning a value from finally disables exception handling.
    So does break and continue
""")
def whatTheHeck():
    try:
        int("nope not a number")
        print("This should not execute!")
        return 2
    except BaseException as ex:
        print("Well I caught it, re-raising")
        raise ex;
    finally:
        return 1

print(f"Did it not blow up? {whatTheHeck()=}")