#!/usr/bin/env python3
"""
This is a PI calculator based on Chudnovsky's algorithm, currently/apparently
the fastest-known. Other versions are recursion-based, which is good enough for
sizeable runs, but not here; of course that means we need an in-memory stack to
help us out, but theoretically less memory maybe.

This could probably be multi-threaded without too much more hassle (ha ha, no.)
"""
import decimal
SUPER_FUDGE_CONSTANT = decimal.Decimal(10005).sqrt()

class TreeNode:
    """
    This is really a node in a binary tree. Nodes point to parents
    instead of the other way around. Each parent has a left & right
    value that is computed by its children and dropped into the
    parent accordingly, so a child has to know if it's a lefty or
    righty.
    """
    def __init__(self, a, b, target, isLeft):
        self.lohi=(a, b)
        self.parentNode = target
        self.isLeft = isLeft
        self.leftTriple = None
        self.rightTriple = None

    def nullify(self):
        for x in self.__dict__:
            self.__dict__[x] = None

def computeLeaf(a, b):
    Pab = -(6*a - 5) * (2*a - 1) * (6*a - 1)
    Qab = 10939058860032000 * a**3
    Rab = Pab * (545140134*a + 13591409)
    return Pab, Qab, Rab

def computeNode(tripleA, tripleB):
    pa, qa, ra = tripleA
    pb, qb, rb = tripleB
    return (
        pa * pb,
        qa * qb,
        qb * ra + pa * rb
    )

def initStack(aa, bb):
    """"
    Build a stack of leaf nodes. Instead of parents having a pointer to
    each of its left & right leaves, every leaf has pointer to its
    parent and a flag indicating left / right leaf. Thus we don't put
    parents in the stack here; we'll add them when their children finish
    computing their own triples. We *could* add them now, I'm just
    making memory a little easier and anticipating multithreaded queuing.
    """
    import collections
    mystack = []
    # Prime the pump with root node:
    toSplit = [TreeNode(aa, bb, None, False)]
    while len(toSplit) > 0:
        node = toSplit.pop()
        lo, hi = node.lohi
        if lo + 1 != hi:
            m = (lo + hi) // 2
            toSplit.append(TreeNode(lo, m, node, True))
            toSplit.append(TreeNode(m, hi, node, False))
        else:
            mystack.append(node)
    return mystack

def new_binary_split(low, high):
    stack = initStack(low, high)
    while item := stack.pop():
        lo, hi = item.lohi
        if lo + 1 == hi:
            triple = computeLeaf(lo, hi)
        else:
            triple = computeNode(item.leftTriple, item.rightTriple)
        parent = item.parentNode
        isLeft = item.isLeft
        item.nullify()
        if (not parent):
            if len(stack) > 0:
                raise Exception("Non-empty stack!")
            return triple # EARLY RETURN
        elif (isLeft):
            parent.leftTriple = triple
        else:
            parent.rightTriple = triple
        # Schedule parent for computation if they are ready...
        if parent.leftTriple and parent.rightTriple:
            stack.append(parent)

def chud(n):
    """Chudnovsky algorithm."""
    ____, Q1n, R1n = new_binary_split(1, n)
    return (426880 * SUPER_FUDGE_CONSTANT * Q1n) / (13591409 * Q1n + R1n)

#################
## TEST LOGIC: ##
#################

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
