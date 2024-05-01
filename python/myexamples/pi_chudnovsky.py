#!/usr/bin/env python3
# This implements PI calculation using the chudnovsky algorithm. I seem
# to have overcome all the usual overflow/out-of-memory/etc crud.
# For a better performing example that I wrote in java (egads, java!):
#   https://github.com/zaboople/pi-chudnov
# I tried using both ProcessPoolExecutor and ThreadPoolExecutor here
# and got mild improvement.

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

def dcml(x):
    return decimal.Decimal(x)

def upper(precision, Q1n):
    setup(precision)
    res = dcml(426880 * Q1n) * dcml(10005).sqrt()
    print("Upper done")
    return res

def lower(precision, Q1n, R1n):
    setup(precision)
    res = dcml((13591409 * Q1n) + R1n)
    print("Lower done")
    return res

def chudnov(n, precision, cores):
    """Chudnovsky algorithm. """

    print("Do splits...")
    ____, Q1n, R1n = binary_split(1, n, cores)

    print("Final monster... now we bog down completely...")
    if cores > 1:
        print("Forking two more...")
        with futures.ProcessPoolExecutor(max_workers=cores) as executor:
            futup = executor.submit(upper, precision, Q1n)
            futlo = executor.submit(lower, precision, Q1n, R1n)
            print("Waiting...")
            sys.stdout.flush()
            up = futup.result()
            lo = futlo.result()
    else:
        lo = lower(precision, Q1n, R1n)
        up = upper(precision, Q1n)
    print("And finally, painfully...")
    sys.stdout.flush()
    return up / lo

def setup(precision):
    """Be careful that precision/emin/emax is set up correctly even
    when forking process. We want insanely huge negative exponents"""
    con = decimal.getcontext()
    con.prec = precision + 3
    con.Emax = precision * precision * 100
    con.Emin = -decimal.getcontext().Emax


##################
# TESTING LOGIC: #
##################


def runOnce():
    def help():
        print("""
            Usage:
                -p precision
                    Number of digits of PI you want
                -d depth
                    You need about 8,000 depth for 100,000 precision
                -c cores
                    Defaults to 4 - use 1 to avoid stupid concurrency
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
            print("\nInvalid argument: "+arg)
            return help()

    started = datetime.datetime.now()
    setup(precision)
    print("Calculating...")
    nextResult = chudnov(depth, precision, cores);
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
