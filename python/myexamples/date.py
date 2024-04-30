#!/usr/bin/env python3

import datetime
import time
import sys

today = datetime.datetime(year=2017, month=1, day=27)
print(f"Date: {today}")
today = datetime.datetime.now()
sys.stdout.flush()
time.sleep(2);
today2 = datetime.datetime.now()
print((today2 - today).total_seconds())