#!/usr/bin/env python3

def comment(ss=""):
    print("\n-----------------")
    for s in ss.split("\n"):
        if s!="": print(s.strip())


comment("""
    String methods:
    https://docs.python.org/3/library/stdtypes.html#string-methods
""")

comment("Line breaks in strings:")
print("""
  Fall off!
    Todo
""");
print("Doop,\r\nDoop!");
print("Done.");

comment("String interpolation with f:")
zzz = 1234
print(f"Number interpolated: {zzz}")
print(f"Number interpolated: {zzz=}")


comment("Slicing a string:")
bigstr = "0123456789";
print(f"Start with: {bigstr=}");
print(f"{bigstr[-4:] =}");
print(f"{bigstr[4:6] =}");
print(f"{bigstr[4:-1] =}");

comment("Doing multiple prints to one line:");
print("Hello", end=" ")
print("there", end=" ")
print("done.")

comment("Slicing string with line breaks:")
print("Slice 2: ");
print("""
  ends with line break
"""[-20:], "");

comment("Use str() and len() functions:")
print("Length of word: "+str(len("Done")))


print("----");