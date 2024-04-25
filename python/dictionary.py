#!/usr/bin/env python3

def comment(ss=""):
    print("-----------------")
    for s in ss.split("\n"):
        if s!= "": print(s.strip())


def makeHash(name):
    return functools.reduce(
        lambda total, c: total + ord(c) ^ 11111,
        name, 0
    )

import functools
myDict = dict(map(
    lambda name: (
        name,
        functools.reduce(
            lambda total, c: total + ord(c) ^ 11111,
            name, 0
        )
    ),
    ["onesy", "two", "three", "four", "rouf"]
))

print(f"{myDict=}")
