I created this largely to test out Go's concurrency and see if I can max out a multi-core CPU, which is not always as easy as one would think. Calculating PI is a pretty good way to attempt such.

This calculation of PI relies on the "Bailey–Borwein–Plouffe" formula (BBP), a "spigot" algorithm for computing digits of PI. It involves a summation over a series, running the same equation over & over for values from 1..N. Thus I can try things like running different iterations concurrently, and doing the summation concurrently to the iterations as well.

Note that BBP is normally used to calculate N hexadecimal digits of PI, but we can just use the decimal output for our purposes; so I'm actually getting much more than N decimal digits, but I only print N's worth as a guarantee of correctness. Maybe I will get around to doing this smarter at some point.

* Results *

The individual iterations of the BBP formula are extremely fast. The hard part is summing all the numbers, and in fact it gets exponentially harder as we increase N. Partially this is because Go's "big.Rat" library gives extreme accuracy but does so by expressing numbers as fractions with very large numerators & denominators. Again, maybe I will get around doing this smarter at some point, as there are derivations of the formula which allow us to minimize the need for summation, and "big.Float" might be better too.

Anyhow, although there are lots of opportunities for concurrency within the equation itself, the only real value comes from making the addition concurrent. So there is some "dead" logic included for doing the equation-internal concurrency.

Eventually I upgraded from Go 1.5 to Go 1.8 and performance increased _dramatically_, both in terms of speed and memory usage. I can now do N=20000 in less than 10 seconds on a better-than-average Intel i5.


