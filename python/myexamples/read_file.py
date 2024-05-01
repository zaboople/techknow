#!/usr/bin/env python3

import sys

def doData():
    """Reads file data into byte array, makes that into a string"""
    argv = list(reversed(sys.argv))
    argv.pop()
    if len(argv) == 0:
        print("I need a file name")
        return

    file = argv.pop()
    print(f"Opening: \"{file}\"")

    instream = open(file, mode="rb") # rb==read binary
    while data:= instream.read(128):
        print(data.decode("utf-8"), end="")

doData()
print("{0:100.10}".format(13.444))