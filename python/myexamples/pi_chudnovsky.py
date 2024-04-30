#!/usr/bin/env python3
# This implements PI calculation using the chudnovsky algorithm
# For a better performing example that I wrote in java (egads, java!):
#   https://github.com/zaboople/pi-chudnov

import decimal
import sys
import datetime

def computeLeaf(a, b):
    Pab = -(6*a - 5) * (2*a - 1) * (6*a - 1)
    Qab = 10939058860032000 * a**3
    Rab = Pab * (545140134*a + 13591409)
    return Pab, Qab, Rab

def computeNode(tripleA, tripleB):
    Pa, Qa, Ra = tripleA
    Pb, Qb, Rb = tripleB
    Pab = Pa * Pb
    Qab = Qa * Qb
    Rab = Qb * Ra + Pa * Rb
    return Pab, Qab, Rab

def binary_split(a, b):
    if b == a + 1:
        return computeLeaf(a, b)
    else:
        m = (a + b) // 2  # Floor division
        return computeNode(
            binary_split(a, m),
            binary_split(m, b)
        )

def chud(n):
    """Chudnovsky algorithm."""
    ____, Q1n, R1n = binary_split(1, n)
    return (426880 * SQRT_10005 * Q1n) / (13591409 * Q1n + R1n)

def setup(precision):
    decimal.getcontext().prec = precision + 3
    global SQRT_10005
    SQRT_10005 = decimal.Decimal(10005).sqrt()

##################
# TESTING LOGIC: #
##################


def runOnce():
    def help():
        print("""
            Usage:
                -p precision
                -d depth
        """)
    precision, depth = 100, 1000;
    argv = list(reversed(sys.argv))
    argv.pop()
    while len(argv) > 0:
        arg = argv.pop()
        if arg.startswith("-h"):
            return help()
        elif arg.startswith("-p"):
            precision = int(argv.pop())
        elif arg.startswith("-d"):
            depth = int(argv.pop())
        else:
            print()
            print("Invalid argument: "+arg)
            return help()

    started = datetime.datetime.now()
    setup(precision)
    nextResult = chud(depth);
    print("{0:>{width}.{prec}f}".format(
        nextResult, width=2+precision, prec=precision
    ))
    print(
        "In: {0:>.4f} seconds".format(
            (datetime.datetime.now()- started).total_seconds()
        )
    )

runOnce()
