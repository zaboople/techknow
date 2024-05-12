#!/usr/bin/env python3

import logging
import datetime as dt

# https://docs.python.org/3/library/logging.html

# So, the built-in formatter blows up on microseconds.
# I have a millisecond formatter and a microsecond formatter
class MyFormatterMilli(logging.Formatter):
    def formatTime(self, record, datefmt=None):
        tz = dt.timezone(dt.timedelta(hours=-5))
        ct = dt.datetime.fromtimestamp(record.created, tz)
        t = ct.strftime("%Y-%m-%d %H:%M:%S.")
        ms = "{0:03.0f}".format(record.msecs)
        ampm = ct.strftime(" %p %z")
        return t + ms + ampm
class MyFormatterMicro(logging.Formatter):
    def formatTime(self, record, datefmt=None):
        tz = dt.timezone(dt.timedelta(hours=-5))
        ct = dt.datetime.fromtimestamp(record.created, tz)
        return ct.strftime("[%Y-%m-%d %H:%M:%S.%f %p %z]")

# Create logger
logger = logging.getLogger('simple_example')
logger.setLevel(logging.DEBUG)

# Create console handler and set level to debug
ch = logging.StreamHandler()
ch.setLevel(logging.DEBUG)
ch.setFormatter(MyFormatterMicro(
    fmt='%(asctime)s - %(levelname)8s / %(threadName)s / %(message)s',
))
logger.addHandler(ch)

# Sample usage:
logger.debug('Just debugging!')
logger.info('Being highly informative!')
logger.warning('Oh dear.')
logger.error('Eagaghghgh')
logger.critical('ARGHGHGH BLURGHGH GURGLE')