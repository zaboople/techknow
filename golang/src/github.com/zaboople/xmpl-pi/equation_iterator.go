package main
import (
	"math/big"
)

// Acts as a synchronous generator of BPP equation values. Someone else has
// to add them up.
type EquationIterator struct{
	factors *Factors
	powerOf16 *big.Rat
	multipleOf8 int64
	first bool
}
func NewEquationIterator() *EquationIterator {
	return &EquationIterator{NewFactors(), makeBig(1), 0, true}
}
func (this *EquationIterator) next() *big.Rat {
	if (this.first) {
		this.first=false
	} else {
		this.powerOf16=bigMultiply(this.powerOf16, this.factors.bigD16)
		this.multipleOf8 += 8
	}
	f:=this.factors
	i8:=this.multipleOf8
	p16:=this.powerOf16

	a:=bigDivide(f.bigD1, p16)
  b:=bigDivide(f.bigD4, makeBig(i8+1))
  c:=bigDivide(f.bigD2, makeBig(i8+4))
  d:=bigDivide(f.bigD1, makeBig(i8+5))
  e:=bigDivide(f.bigD1, makeBig(i8+6))
	return bigMultiply(
		a,
		bigSubtract(
			bigSubtract(
				bigSubtract(b, c), d,
			),
			e,
		),
	)
}

