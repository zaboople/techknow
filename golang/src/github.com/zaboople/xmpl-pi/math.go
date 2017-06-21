package main

import (
	"math/big"
	"fmt"
)

/**
 Everything here is convenience types/functions to make go's
 math functions a little less hairy.
*/


type ChanBig chan *big.Rat
func makeChanBig(size int) ChanBig {
	return make(ChanBig, size)
}

func bigPower(x *big.Rat, exp int64) *big.Rat {
	var n int64
	var res=big.NewRat(1, 1)
	for n=0; n<exp; n++ {
		res.Mul(res, x)
	}
	return res
}
func makeBig(i int64) *big.Rat {
	return big.NewRat(i, 1)
}
func bigAdd(x *big.Rat, y *big.Rat) *big.Rat {
	return new(big.Rat).Add(x, y)
}
func bigSubtract(x *big.Rat, y *big.Rat) *big.Rat {
	return new(big.Rat).Sub(x, y)
}
func bigMultiply(x *big.Rat, y *big.Rat) *big.Rat {
	return new(big.Rat).Mul(x, y)
}
func bigDivide(x *big.Rat, y *big.Rat) *big.Rat {
	res:=new(big.Rat).Inv(y)
	return res.Mul(x, res)
}

////////////
// TESTS: //
////////////
func testMath() {
	testDivide()
	testPower()
	testSubtract()
	testMultiply()
}
func testDivide() {
	nuh:=big.NewRat(12,1)
	duh:=big.NewRat(1, 3)
	nuhduh:=bigDivide(nuh, duh)
	fmt.Println(nuh, " / ", duh, " = ", nuhduh)
}
func testPower() {
	var power int64=3
	thrFr:=big.NewRat(3, 4)
	thrFrThr:=bigPower(thrFr, power)
	fmt.Println(thrFr, " ** ", power, " = ", thrFrThr)
}
func testSubtract() {
	first:=big.NewRat(1,2)
	second:=big.NewRat(3,4)
	res:=bigSubtract(first, second)
	fmt.Println(first, " - ", second, " = ", res)
}
func testMultiply() {
	first:=big.NewRat(5,2)
	second:=big.NewRat(3,4)
	res:=bigMultiply(first, second)
	fmt.Println(first, " * ", second, " = ", res)
}
func testPrint() {
	num := big.NewRat(12, 1)
	fmt.Println(num.FloatString(20))
}
