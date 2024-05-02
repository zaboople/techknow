#!/usr/bin/env python3

import sys
import myutils
import os

def easyRead(ffile):
    with open(ffile, mode="r") as instream:
        for line in instream:
            print(line, end="")

def doData():
    """Reads file data into byte array, makes that into a string"""
    argv = list(reversed(sys.argv))
    argv.pop()
    if len(argv) == 0:
        print("I need a file name")
        return

    readFile, writeFile = None, None
    while len(argv) > 0:
        arg = argv.pop()
        if arg=="-r":
            readFile = argv.pop()
        elif arg=="-w":
            writeFile = argv.pop()
        else:
            readFile = arg

    if readFile:
        myutils.comment(f"""
            Reading file bytes from file:
                \"{readFile}\"
            (note "rb" for read-binary)
        """)
        with open(readFile, mode="rb") as instream:
            while data:= instream.read(128):
                print(data.decode("utf-8"), end="")
        print()

        myutils.comment("""
            Reading file lines:
        """)
        with open(readFile, mode="r") as instream:
            while line:= instream.readline():
                print(line, end="")
        print()

        myutils.comment("""
            Reading file lines, simplest way:
        """)
        easyRead(readFile)
        print()

    if writeFile:
        myutils.comment(f"""
            Writing to: {writeFile}
            (note use of "a" for append; "w" would just
            write to beginning)
        """)
        with open(writeFile, mode="a", encoding="utf-8") as outy:
            #outy.seek(0, whence=os.SEEK_END)
            outy.write("First line\n")
            outy.write("Second line\n")
            print(f"Byte position {outy.tell()}")
        print("Reading back out...")
        easyRead(writeFile)
        print()

doData()
