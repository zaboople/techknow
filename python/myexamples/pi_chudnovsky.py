#!/usr/bin/env python3
# This implements PI calculation using the chudnovsky algorithm

import decimal
SUPER_FUDGE_CONSTANT = decimal.Decimal(10005).sqrt()

def binary_split(a, b):
    if b == a + 1:
        Pab = -(6*a - 5) * (2*a - 1) * (6*a - 1)
        Qab = 10939058860032000 * a**3
        Rab = Pab * (545140134*a + 13591409)
    else:
        m = (a + b) // 2  # Floor division
        Pam, Qam, Ram = binary_split(a, m)
        Pmb, Qmb, Rmb = binary_split(m, b)

        Pab = Pam * Pmb
        Qab = Qam * Qmb
        Rab = Qmb * Ram + Pam * Rmb
    return Pab, Qab, Rab

def chudnovsky(n):
    """Chudnovsky algorithm."""
    ____, Q1n, R1n = binary_split(1, n)
    return (426880 * SUPER_FUDGE_CONSTANT * Q1n) / (13591409 * Q1n + R1n)

def compareStrs(prev, next):
    """Finds the common ground between prev & next
        starting from position 0 and going forwards, returning
        the common part."""
    result = []
    for i in range(
            0, min(len(prev), len(next))
        ):
        c1, c2 = prev[i], next[i]
        if (c1 != c2):
            break;
        if (c1 != ""):
            result.append(c1)
    return "".join(result)

def withFormatting(myprecision, maxdepth):
    # This runs chudnovsky at increasing "depth" until we achieve our desired
    # digits of precision consistently. It compares results of previous and current
    # run to see how consistent we are, and reports back the consistent digits.
    # This is obviously kinda silly since we're computing the same crud over and over,
    # but it's fun:

    maxdepth = 10000
    decimal.getcontext().prec = myprecision + 3 # number of digits of decimal precision

    fmtIncrWidth = len(f"{maxdepth}")
    fmtPrecWidth = myprecision
    fmtMainWidth = 2 + fmtPrecWidth
    fmtLenWidth = len(f"{myprecision}")

    fmtResultPrev = ""
    convergeCount = 0;
    for n in range(2, maxdepth + 1):
        nextResult = chudnovsky(n)
        fmtIncr = "{0:>{width}d}".format(n, width=fmtIncrWidth)
        fmtResult = "{0:>{width}.{prec}f}".format(
            nextResult, width=fmtMainWidth, prec=fmtPrecWidth
        )
        toPrint = compareStrs(fmtResultPrev, fmtResult)
        digitCount = len(toPrint)
        digitCount = 0 if (digitCount <= 2) else (digitCount - 2)
        fmtLen = "{0:>{width}d}".format(digitCount, width=fmtLenWidth)
        print(f"{fmtIncr} length: {fmtLen} -> {toPrint}")
        if (digitCount == myprecision):
            if (convergeCount := convergeCount + 1) > 2:
                print(f"CONVERGENCE at depth: {n}")
                break
        else:
            convergeCount = 0

        fmtResultPrev = fmtResult

withFormatting(myprecision=400, maxdepth=1000)

