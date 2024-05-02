#!/usr/bin/env python3

from io import StringIO
from myutils import comment
import json
import os

####################################

comment("""
    Putting an arbitrary object into json
""")

# String IO acts like an output/input stream to a file:
strm = StringIO()
json.dump(
    {"hi":4, 1:['a', 'b'], "m":[1,2,3,4]},
    strm
)
# The seek is necessary to reset the stream to beginning
strm.seek(0, os.SEEK_SET)
dicto2 = json.load(strm)
print(f"{dicto2=}")

####################################

comment("""
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
        r = dict(self.__dict__)
        r["mytype"] = self.__class__.__name__
        return r
    def __str__(self):
        # Does the classic toString()
        vv = ",".join(
            [repr(value) for key, value in self.__dict__.items()]
        )
        return f"CC({vv})"
    def __repr__(self):
        # Implements the classic repr()
        vv = ",".join(
            [f"{key}={value}" for key, value in self.__dict__.items()]
        )
        return f"CC({vv})"

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

#################################

comment("""
    Custom JSON to/from using just a function:
""")

def genericToJson(xx):
    r = dict(xx.__dict__)
    r["mytype"] = xx.__class__.__name__
    return r

def genericFromJson(dicty):
    print(f"Inside genericFromJson(): {dicty}")
    if "mytype" in dicty:
        # Normally I could use import_module() and
        # getattr() to try to do a class.forName()
        if dicty["mytype"] == "CC":
            del dicty["mytype"]
            return CC(**dicty)
        raise Exception(f"Could not initialize from: {dicty}")
    return dicty

strm = StringIO()
json.dump(
    [1,2,CC(2,3,["Just lambda"]), {"rando":"a"}], strm,
    default = genericToJson
)
print(f"Using lambda, stream has: {strm.getvalue()=}")

strm.seek(0, os.SEEK_SET)
fromJson = json.load(strm, object_hook=genericFromJson)
print(f"Back into a dict, CC should be in here: {fromJson=}")

