#!/usr/bin/env python3

def comment(ss=""):
    print("-----------------")
    for s in ss.split("\n"):
        print(s.strip())

comment("""
  This behaves superweird. When a list has a default, it gets created once,
  and gets reused on every call. But NOT with an int - see accum variable.
""")
def weirdList(a, listy=[], accum=0):
    accum+=a;
    listy.append(accum)
    return listy

print("\nWatch accumulation of list...")
for i in range(1, 6):
    print(weirdList(i))

print("\nCall with named arguments:""");
print(weirdList(a=1, listy=[22,33]))

print("\nCall with named arguments using map & double-asterisk:")
map={"a":2, "listy":[12], "accum":100}
print(weirdList(**map))

print("\nCall with arguments using list & single-asterisk:")
_junk = [1, [1,2,3], 1000]
print(weirdList(*_junk))

comment("""
    One asterisk gives you an arbitrary dumpster of unnamed args
    Two asterisks gives you an arbitrary key-value map of named args
""")
def flexfun(*args, **dicti):
    for arg in args:
        print(f"Arg {arg}")
    for key in dicti:
        print(f"Key {key} {dicti[key]}")

flexfun(1, 2, "hello", boo=22, bash=23)

comment()
