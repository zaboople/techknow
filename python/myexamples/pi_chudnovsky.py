#!/usr/bin/env python3
# This implements PI calculation using the chudnovsky algorithm

import decimal
SUPER_FUDGE_CONSTANT = decimal.Decimal(10005).sqrt()

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
    return (426880 * SUPER_FUDGE_CONSTANT * Q1n) / (13591409 * Q1n + R1n)

##################
# TESTING LOGIC: #
##################

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


def withFormatting(precision, maxdepth, mindepth=0, printit=True):
    """
    precision: Number of digits of PI desired
    mindepth: Optional, should be less than maxdepth. By default is maxdepth - 4
        so we can test for convergence, but you can push further back to see if
        you get there quicker than maxdepth allows.
    maxdepth: How deep we're willing to go to get it

    This runs chudnovsky at increasing "depth" until we achieve our desired
    digits of precision consistently. It compares results of previous and current
    run to see how consistent we are, and reports back the consistent digits.
    This is obviously kinda silly since we're computing the same crud over and over,
    but it's fun:
    """
    decimal.getcontext().prec = precision + 3

    fmtIncrWidth = len(f"{maxdepth}")
    fmtPrecWidth = precision
    fmtMainWidth = 2 + fmtPrecWidth
    fmtLenWidth = len(f"{precision}")

    fmtResultPrev = ""
    convergeCount = 0;
    if mindepth == 0:
        mindepth = maxdepth - 4
    for n in range(mindepth, maxdepth + 1):
        nextResult = chud(n)
        fmtIncr = "{0:>{width}d}".format(n, width=fmtIncrWidth)
        fmtResult = "{0:>{width}.{prec}f}".format(
            nextResult, width=fmtMainWidth, prec=fmtPrecWidth
        )
        toPrint = compareStrs(fmtResultPrev, fmtResult)
        digitCount = len(toPrint)
        digitCount = 0 if (digitCount <= 2) else (digitCount - 2)
        fmtLen = "{0:>{width}d}".format(digitCount, width=fmtLenWidth)
        if not printit:
            toPrint="..."
        print(f"{fmtIncr} length: {fmtLen} -> {toPrint}")
        if (digitCount == precision):
            if (convergeCount := convergeCount + 1) > 2:
                print(f"CONVERGENCE at depth: {n}")
                break
        else:
            convergeCount = 0

        fmtResultPrev = fmtResult

withFormatting(precision=1000, mindepth=2, maxdepth=10000)
#withFormatting(precision=70000, mindepth=5000, maxdepth=10000, printit=False)