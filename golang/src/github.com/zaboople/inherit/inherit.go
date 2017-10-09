package main


import (
	"fmt"
)


// We have interface Pet, struct Dog, and struct Person:
type Pet interface {
	Speak() string
}

type Dog struct {
	say string
}
func (d Dog) Speak() string {
	return d.say
}

type Person struct {
	name string
}

// Inheriting both interface & struct:
type PetOwner struct {
	Pet
	Person
}
func (pair PetOwner) introduce() {
	//Note how both variable name & method Speak() are inherited
	fmt.Println(pair.name, "introducing", pair.Speak())
}


//Note how this prints pet fields and other fields that
//might be a child of Pet:
func examinePet(pet Pet) {
	fmt.Println(pet)
}



func main() {
	examinePet(Dog{"ruff"})
	pair := PetOwner{
		Dog{"Woofer"},
		Person{"Mr McJajajones"}}
	examinePet(pair);
	pair.introduce()
}


