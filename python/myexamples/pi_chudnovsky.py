#!/usr/bin/env python3
# This implements PI calculation using the chudnovsky algorithm
# For a better performing example that I wrote in java (egads, java!):
#   https://github.com/zaboople/pi-chudnov
# I tried using both ProcessPoolExecutor and ThreadPoolExecutor here
# and nope, waste of time.

import decimal
import os
import sys
import datetime
import concurrent.futures as futures

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

def binary_split(a, b, cores=1):
    if b == a + 1:
        return computeLeaf(a, b)
    else:
        m = (a + b) // 2  # Floor division
        if cores > 1:
            print(f"Forking {a} {b}...{os.getpid()}")
            with futures.ProcessPoolExecutor(max_workers=2) as executor:
                f1 = executor.submit(binary_split, a, m, cores / 2)
                f2 = executor.submit(binary_split, m, b, cores / 2)
            r1, r2 = f1.result(), f2.result()
            print(f"{a} {b}...{os.getpid()} done")
            cores = 1
        else:
            r1, r2 = binary_split(a, m), binary_split(m, b)
        return computeNode(r1, r2)

def upper(precision, Q1n):
    setup(precision)
    sqrot = decimal.Decimal(10005).sqrt()
    return (426880 * sqrot * Q1n)

def lower(Q1n, R1n):
    return (13591409 * Q1n + R1n)

def chud(n, precision, cores):
    """Chudnovsky algorithm. """
    ____, Q1n, R1n = binary_split(1, n, cores)
    print("Final step...")
    if cores > 1:
        print("Forking two more...")
        with futures.ProcessPoolExecutor(max_workers=cores) as executor:
            futlo = executor.submit(lower, Q1n, R1n)
            futup = executor.submit(upper, precision, Q1n)
            up = futup.result()
            lo = futlo.result()
    else:
        lo = lower(Q1n, R1n)
        up = upper(precision, Q1n)
    print("And finally...")
    sys.stdout.flush()
    return up / lo

def setup(precision):
    decimal.getcontext().prec = precision + 3

##################
# TESTING LOGIC: #
##################


def runOnce():
    def help():
        print("""
            Usage:
                -p precision
                -d depth
                -c cores
        """)
    precision, depth, cores = 100, 1000, 4;
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
        elif arg.startswith("-c"):
            cores = int(argv.pop())
        else:
            print()
            print("Invalid argument: "+arg)
            return help()

    started = datetime.datetime.now()
    print("Setting up and finding square root...")
    setup(precision)
    print("Calculating...")
    nextResult = chud(depth, precision, cores);
    print("Formatting...")
    print("{0:>{width}.{prec}f}".format(
        nextResult, width=2+precision, prec=precision
    ))
    print(
        "In: {0:>.4f} seconds".format(
            (datetime.datetime.now()- started).total_seconds()
        )
    )

if __name__ == '__main__':
    runOnce()
