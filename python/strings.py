#!/usr/bin/env python3

def lblr(stri):
  print("----")
  print(f"*** {stri}: ***")


lblr("Line breaks in strings")
print("""
  Fall off!
    Todo
""");
print("Doop,\r\nDoop!");
print("Done.");

lblr("String interpolation with f")
zzz = 1234
print(f"Number interpolated: {zzz}")
print(f"Number interpolated: {zzz=}")


lblr("Slicing a string")
bigstr = "0123456789";
print(f"Start with: {bigstr=}");
print(f"{bigstr[-4:] =}");
print(f"{bigstr[4:6] =}");
print(f"{bigstr[4:-1] =}");

lblr("Doing multiple prints to one line");
print("Hello", end=" ")
print("there", end=" ")
print("done.")

lblr("Slicing string with line breaks")
print("Slice 2: ");
print("""
  ends with line break
"""[-20:], "");

lblr("Use str() and len() functions:")
print("Length of word: "+str(len("Done")))


print("----");