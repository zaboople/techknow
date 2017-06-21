package main

import "fmt"


func main() {

	//You will notice here that we use fmt.Println instead of plain println;
	//this is because the latter spits out a hex pointer instead of a to-string:


	//Notice how maps are declared with map[type1]type2, which is nuts:
	var m1 map[string]int = make(map[string]int)	
	m1["Testing"] =12
	fmt.Println(m1)


	//Another map, but with values built in, and ZOMG they allow a comma 
	//after the last element:
	var m2 map[string]int = map[string]int {
		"Testing":12,
		"Other":123,
		"Yeah":1234, //COMMA!
	}
	m2["pukity"]=1234457
	fmt.Println(m2)

	
	//The following two map declarations are equivalent. The difference is that
	//type inference allowed us to drop the name "Vertex":
	var m3 map[string]Vertex;
	m3 = map[string]Vertex{
		"X1": {40.68433, -74.39967},
		"X2": {37.42202, -122.08408},
	}
	m3 = map[string]Vertex{
		"X1": Vertex{40.68433, -74.39967},
		"X2": Vertex{37.42202, -122.08408},
	}
	fmt.Println(m3)


	fmt.Println("\nLooking things up")
	//Note that when nothing is found, we still get a value, it's just initialized to zeroes:
	foundValue, foundOK := m3["Answer"]
	fmt.Println("The value:", foundValue, "Present?", foundOK)	
	
	fmt.Println("\nLooping through a map:")
	for key,val:=range m3 {
		fmt.Println("Key:", key, "Val:", val)
	}
	
}

type Vertex struct {
	Lat, Long float64
}
func (v Vertex) String() string {
	return fmt.Sprintf("(%.4f, %.4f)", v.Lat, v.Long)
}

