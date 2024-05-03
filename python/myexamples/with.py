#!/usr/bin/env python3

from myutils import comment


comment("""
    Creating a custom target for "with" statements turns out to
    be utterly trivial. Well how about that.
""")

class MyWither:
    def __init__(self, stuff):
        self.stuff=stuff;
    def __enter__(self):
        print("Heck yeah enter...")
        return self.stuff
    def __exit__(self, exc_type, exc_value, traceback):
        print("...Leaving, baby")


with MyWither([1,2,3]) as mystuff:
    print(f"What do we have: {mystuff}")

comment("""
    Double-withing, woo:
""")
with MyWither([1,2,3]) as aa, MyWither([4,5,6,7]) as bb:
    print(f"What do we have: {aa} {bb}")