package main
import (
	"math/big"
)


type Factors struct{
	bigD16 *big.Rat
	bigD4  *big.Rat
	bigD2  *big.Rat
	bigD1  *big.Rat
	bigD0  *big.Rat
}
func NewFactors() *Factors {
	return &Factors{
		makeBig(16), makeBig(4), makeBig(2), makeBig(1), makeBig(0),
	}
}
var x = NewFactors() //FIXME make this the Factors not the struct itself.