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

    m = (a + b) // 2  # Floor division
    if cores > 1:
        print(f"Forking {a} {b}")
        with futures.ProcessPoolExecutor(max_workers=2) as executor:
            f1 = executor.submit(binary_split, a, m, cores / 2)
            f2 = executor.submit(binary_split, m, b, cores / 2)
        r1, r2 = f1.result(), f2.result()
        print(f"{a} {b} done")
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
            up, lo = futup.result(), futlo.result()
    else:
        up = upper(precision, Q1n)
        lo = lower(precision, Q1n, R1n)
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

def help():
    print("""
        Usage:
            -p <precision>
                Number of digits of PI you want
            -d <depth>
                You need about 8,000 depth for 100,000 precision
            -c <cores>
                Defaults to 4 - use 1 to avoid concurrency
            -f <file>
                Optional: Supply a text file with known pi digits to
                test your results.
    """)

def compare(strResult, pifile):
    print("Comparing...")
    instream = open(pifile, "rb")
    ptr = 0
    strlng = len(strResult)
    while bites := instream.read(1024 * 256):
        endIndex = min(strlng, ptr + len(bites))
        compareFrom = bites.decode("utf-8")
        compareTo = strResult[ptr: endIndex]
        count = len(compareTo)
        for i in range(0, count):
            if compareFrom[i] != compareTo[i]:
                matched = ptr + i
                print(f"Matched: {matched} digits")
                return
        ptr += count
    print(f"Matched all: {ptr} digits")

def runOnce():

    # Setup:
    precision, depth, cores, pifile = 100, 1000, 4, None;
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
        elif arg.startswith("-f"):
            pifile = argv.pop()
        else:
            print("\n\nInvalid argument: "+arg)
            return help()

    # Exec:
    started = datetime.datetime.now()
    setup(precision)
    print("Calculating...")
    nextResult = chudnov(depth, precision, cores);
    timeTaken = (datetime.datetime.now()- started).total_seconds()

    # Wrap up:
    print("Formatting...")
    strResult = "{0:.{prec}f}".format(nextResult, prec=precision)
    print("PI=", end="")
    print(strResult)
    if pifile:
        compare(strResult, pifile)
    print("In: {0:>.4f} seconds".format(timeTaken))

# if-statement is necessary when forking on MS Windows
# and perhaps other os's:
if __name__ == '__main__':
    runOnce()
