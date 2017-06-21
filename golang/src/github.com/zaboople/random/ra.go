package main

/* 
	Making random numbers is ugly. You have to make them one byte at a time.
*/

import (
	"crypto/rand"
	"fmt"
	"math"
)

func main() {
	testMaxBitsBytes()
	testRandomPowers()
	testRandomDistribution()
	testRandomMaxInt()
}

/////////////////////
// IMPLEMENTATION: //
/////////////////////

/*
  This is non-deterministic; it can potentially require infinite time to calculate a random
	number that fits the specified limit. Fortunately it will take near-infinite time to collide with 
	that problem. But it *will* work faster if the limit is 2**x -1 though, i.e. 1023, 511, 255... 
*/
func randomInt(limit int64) int64 {
	/* 1. Compute: 
		- The number of bits needed to accomodate our result;
		- The number of bytes needed to hold those bits, because
			Go can only compute randoms in byte clumps;
		- The number of bits to shift off those assembled bytes so that
		  we have a number close to our maximum, but perhaps slightly too large,
			by one bit.
	*/
	bitCount :=maxBits(limit)
	byteCount:=maxBytesForBits(bitCount) 
	shift:=uint((byteCount * 8) - bitCount)

	/* 
		2. Repeatedly compute a random number that may be slightly larger
		   (again, by one bit) than what we want, until it fits. 
	*/
	bytes:=make([]byte, byteCount)
	//failCount:=0
	for {
		r:=randomGuess(byteCount, bytes, shift)
		if (r<=limit && r>-1) {
			return r
		} else {
			//failCount++
			//fmt.Println("Guess again", failCount, r)
		}
	}
}

func randomGuess(byteCount int, bytes []byte, shift uint) int64 {
	rand.Read(bytes)
	var res int64 = 0
	for i := 0; i < byteCount; i++ {
		res <<= 8
		res += int64(bytes[i])
	}
	res >>=shift
	return res	
}
func maxBytesForBits(bits int) int {
	return int(
		math.Ceil(
			float64(bits) / 8,
		),
	)
}
func maxBits(max int64) int {
	if (max==0) {
		return 0
	} else {
		return int(
			math.Floor(
				math.Log2(
					float64(max),
				),
			) + 1,
		)
	}
}

//////////////////
// TEST HARNESS //
//////////////////

func testRandomDistribution() {	
	fmt.Println("\nTESTING A DISTRIBUTION:")
	max:=1024
	counts:=make([]int, max+1)
	for i:=0; i<max*max; i++ {
		counts[randomInt(int64(max))]++
	}
	fmt.Println(counts)
	hi:=0; lo:=max
	for i:= range(counts) {
		if counts[i]>hi {hi=counts[i]}
		if counts[i]<lo {lo=counts[i]}
	}
	fmt.Printf(
		"\n%-12s%-12s\n%-12d%-12d\n", "Low hits", "High hits", lo, hi,
	)	
}qqq
func testRandomPowers() {
	fmt.Println("\nTEST RANDOM POWERS OF 10/2:")
	tester:=func(xx int64) {
		fmt.Printf("Input: %-8d %-12d\n", xx, randomInt(xx))
	}
	
	fmt.Println("\n10:")
	for i:=0; i<7; i++ {
		tester(power(10, i))
	}
	
	fmt.Println("\n2:")
	for i:=1; i<10; i++ {
		tester(power(2, i))
	}	
}

func testMaxBitsBytes() {
	fmt.Println("MAX BITS/BYTES TEST:")
	
	//1 Helpers:
	testOne:=func(i int64) {
		bits:=maxBits(i)
		fmt.Printf(
			"Input: %-8d bits: %-4d bytes: %-4d\n", 
			i, bits, maxBytesForBits(bits),
		)
	}
	testUpDown:=func(max int64) {
		for i:=max-1; i<=max+1; i++ {testOne(i)}
	}
	
	//2 Tests:
	println("\nLet's make sure we understand the log2 of 0: ", math.Log2(0)) 	

	fmt.Println("\nUsing maxint64:")
	testOne(math.MaxInt64)
	
	fmt.Println("\nUsing powers of 2:")
	for i:=0; i<14; i++ {
		fmt.Println("2 **", i)
		testUpDown(power(2, i))
	}

	fmt.Println("\nUsing powers of 10:")
	for i:=0; i<8; i++ {
		fmt.Println("2 to the", i)
		testOne(power(10, i))
	}	
}

func testRandomMaxInt() {
	fmt.Println("\nTESTING RANDOMIZING MaxInt64:")
	fmt.Printf("%d\n", int64(math.MaxInt64))
	fmt.Println("---------------")
	for i:=1; i<10; i++ {
		fmt.Println(randomInt(math.MaxInt64))
	}
}

func power(x int, y int) int64 {
	return int64(
		math.Pow(float64(x), float64(y)),
	)
}

