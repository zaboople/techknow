#!/usr/bin/env python3

import datetime
import time
import sys

today = datetime.datetime(year=2017, month=1, day=27)
print(f"Date: {today}")

today = datetime.datetime.now()
print("sleeping...")
sys.stdout.flush()
time.sleep(0.1);
today2 = datetime.datetime.now()
print("{0} seconds".format(
    (today2 - today).total_seconds()
))