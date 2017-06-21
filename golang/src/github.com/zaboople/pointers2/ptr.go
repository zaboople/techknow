package main

import "fmt"

func addSquareOf(p *int) {*p +=*p * *p}
type Location struct {
	latitude float64; longitude float64;
}
func (loc *Location) move(distanceLat float64, distanceLon float64){
	loc.longitude+=distanceLon
	loc.latitude +=distanceLat
}
func (loc *Location) moveTo(other *Location){
	loc.longitude=other.longitude
	loc.latitude =other.latitude
}
func (loc Location) moveNew(other Location) Location {
	loc.move(other.latitude, other.longitude)
	return loc
}
func (loc Location) String() string {
	return fmt.Sprintf("Location: Lat:%0.2f Lon:%0.2f", loc.latitude, loc.longitude)
}
func main() {
	fmt.Println("\nExample 1, pointer to int:")
	i:=2
	addSquareOf(&i)
	fmt.Println(i)

	fmt.Println("\nExample 2, pointer to Location:")
	x:=&Location{12, 13}
	fmt.Println(x)
	x.move(-1, 100)
	fmt.Println(x)

	fmt.Println("\nExample 3, Location:")
	y:=Location{100, 200}
	fmt.Println(y)
	y.move(9, -100)
	fmt.Println(y)

	fmt.Println("Does nothing:")
	y.moveTo(&y)
	fmt.Println(y)
	(&y).moveTo(&y)
	fmt.Println(y)

	fmt.Println("\nExample 4, Location:")
	z:=y.moveNew(Location{2,3})
	fmt.Println(y, z)
}
