#!/usr/bin/env python3

import traceback

def comment(ss=""):
    print("-----------------")
    for s in ss.split("\n"):
        if s!="": print(s.strip())

def badfunc():
    for x in x:
        pass

def blowup(toSay):
    raise Exception(toSay)

def goup(toSay):
    try:
        blowup(f"Calling blowup with: \"{toSay}\"")
    except Exception as ex:
        raise Exception(f"Blew up in goup(); nested: {ex}") from ex

def debugEx(ex):
    print(f"\nDebugging exception: {ex}")
    for line in traceback.format_exc().split("\n"):
        print(f"     >>> {line}")
    print()

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
    goup("Hi there")
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

comment("")