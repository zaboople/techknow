#!/usr/bin/env python3

from myutils import comment
from urllib.request import urlopen
from datetime import datetime as dt

with urlopen('http://worldtimeapi.org/api/timezone/etc/UTC.txt') as response:
    for line in response:
        line = line.decode().strip()             # Convert bytes to a str
        print(f"  Output: {line}")
        if line.startswith('datetime'):
            utcstr = line.split(" ")[1]
            mydate = dt.fromisoformat(utcstr)
            print(f"""
                Found date: {mydate}
            """)