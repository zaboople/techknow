#!/usr/bin/env python3

import datetime as dt
import time
import sys
from myutils import comment

comment("""
    https://docs.python.org/3/library/datetime.html
    Date/Time is ugly as usual
""")

today = dt.datetime(year=2017, month=1, day=27)
print(f"Date: {today}")

comment("""
    Note: datetime.utcnow() is deprecated
""")
today = dt.datetime.now(dt.UTC)
print("sleeping...")
sys.stdout.flush()
time.sleep(0.1);
today2 = dt.datetime.now(dt.UTC)
print("{0} seconds".format(
    (today2 - today).total_seconds()
))

comment("""
    Format a date:
""")
print(today2.strftime("%d/%m/%y %H:%M:%S.%f %p %z"))
print(dt.time(12, 10, 3, 245000).strftime("%d/%m/%y %H:%M:%S.%f %p %z"))

comment("""
    Create a date, but with a specific time zone:
""")
today = dt.datetime.now(dt.timezone(dt.timedelta(hours=-5)))
print(today.strftime("%Y-%m-%d %H:%M:%S.%f %p %z"))