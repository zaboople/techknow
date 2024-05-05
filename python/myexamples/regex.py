#!/usr/bin/env python3

import re
from myutils import comment

comment("""
    Regex
    https://docs.python.org/release/3.12.2/library/re.html
""")


comment("""
    Search a string with findall(). There is also a search(),
    but findall() is so much easier to work with, especially if
    you don't need indices of string locations, just the data
    that you're looking for:
""")
for astr in re.findall(
        "[a-z]* to [a-z]*",
        "The alphabet goes from a to z but my tastes run from soup to nuts",
        flags=re.IGNORECASE
    ):
    print(f"{astr}")

comment("""
    Compiled regex, and...
    finditer(), which returns Match objects, not strings
    Nasty nested groups are not so bad
""")
searchstr = "name=goober location=nuts place=blah"
keylist = [
    f"({key})=(.*?)( |$)"
    for key in ["name", "place", "location"]
]
strgex = "|".join(keylist)
print(f"  Matching with this thing: {strgex}")
regex = re.compile(strgex)
for mymatch in regex.finditer(searchstr):
    print(f"  Main match: {mymatch}")
    grouplist = [
        g for g in mymatch.groups()
        if g and g!= " "
    ]
    print(f"    Sub: {grouplist}")

comment("""
    sub() aka substitution
""")
regex = re.compile("a.*z")
print(regex.sub("my house", "alcatraz is your house"))

