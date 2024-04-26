#!/usr/bin/env python3

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

def compare(prev, next):
    zipped = zip(prev, next)
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

def withFormatting():
    # Initial precision would be about 26
    myprecision = 200
    mymax = 100
    decimal.getcontext().prec = myprecision # number of digits of decimal precision

    fmtIncrWidth = len(f"{mymax}")
    fmtPrecWidth = myprecision
    fmtMainWidth = 2 + fmtPrecWidth
    fmtLenWidth = len(f"{myprecision}")

    fmtResultPrev = ""
    for n in range(2, mymax + 1):
        nextResult = chudnovsky(n)
        fmtIncr = "{0:>{width}d}".format(n, width=fmtIncrWidth)
        fmtResult = "{0:>{width}.{prec}f}".format(
            nextResult, width=fmtMainWidth, prec=fmtPrecWidth
        )
        fmtLen = "{0:>{width}d}".format(len(fmtResult - 2), width=fmtLenWidth)
        toPrint = compare(fmtResultPrev, fmtResult)
        print(f"{fmtIncr} length: {fmtLen} -> {toPrint}")
        fmtResultPrev = fmtResult

withFormatting()
