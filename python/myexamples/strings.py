#!/usr/bin/env python3

import myutils

myutils.comment("""
    String methods:
    https://docs.python.org/3/library/stdtypes.html#string-methods
""")

myutils.comment("Line breaks in strings:")
print("""
  Fall off!
    Todo
""");
print("Doop,\r\nDoop!");
print("Done.");

myutils.comment("String interpolation with f:")
zzz = 1234
print(f"Number interpolated: {zzz}")
print(f"Number interpolated: {zzz=}")


myutils.comment("Slicing a string:")
bigstr = "0123456789";
print(f"Start with: {bigstr=}");
print(f"{bigstr[-4:] =}");
print(f"{bigstr[4:6] =}");
print(f"{bigstr[4:-1] =}");

myutils.comment("Doing multiple prints to one line:");
print("Hello", end=" ")
print("there", end=" ")
print("done.")

myutils.comment("Slicing string with line breaks:")
print("Slice 2: ");
print("""
  ends with line break
"""[-20:], "");

myutils.comment("Use str() and len() functions:")
print("Length of word: "+str(len("Done")))

myutils.comment("startsWith:")
print("Geeble".startswith("Gee"))
print("Geeble".startswith("gee"))

print("----");

