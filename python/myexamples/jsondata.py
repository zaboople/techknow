#!/usr/bin/env python3

from io import StringIO
import myutils
import json
import os

myutils.comment("""
    Putting an arbitrary object into json
""")

strm = StringIO()
json.dump(
    {"hi":4, 1:['a', 'b'], "m":[1,2,3,4]},
    strm
)

strm.seek(0, os.SEEK_SET)
dicto2 = json.load(strm)
print(f"{dicto2=}")

myutils.comment("""
    Custom JSON to/from using a JSONEncoder class. Note
    that our custom encoding doesn't create a class; it just needs
    to make a dictionary, list or whatever and the json module
    will do the stringification from there.
""")

class CC:
    def __init__(self, x, y, z):
        self.x=x
        self.y=y
        self.z=z
    def toJson(self):
        return {"mytype": "CC", "x": self.x, "y": self.y, "z": self.z}

class CC_encoder(json.JSONEncoder):
    def default(self, obj):
        # This gets called only if the main JSONEncoder can't figure
        # out what to do with an object.
        if isinstance(obj, CC):
            print("Encoding my stuff...")
            return obj.toJson()
        return json.JSONEncoder.default(self, obj)

print("My encoder: "+json.dumps(["hi", "there", CC(4,5,6)], cls=CC_encoder))

strm = StringIO()
json.dump(
    [1,2,CC(2,3,[3,4,4])], strm, cls=CC_encoder
)
print(f"Stream has: {strm.getvalue()=}")


myutils.comment("""
    Custom JSON to/from using just a function:
""")

strm = StringIO()
json.dump(
    [1,2,CC(2,3,["Just lambda"])], strm,
    default = lambda x: x.toJson()
)
print(f"Using lambda, stream has: {strm.getvalue()=}")
