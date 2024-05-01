#!/usr/bin/env python3

def say(ss=""):
    print("-----------------")
    for s in ss.split("\n"):
        if s!="": print(s.strip())
