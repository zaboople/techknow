package main

import (
	"fmt"
	"log"
	"net/http"
)

type Hello struct{
	ii int
}
func (h *Hello) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	h.ii=h.ii + 1
	fmt.Fprint(w, "Hello! ", h.ii)
}


func main() {
	//This registers three handlers:
	http.Handle("/", &Hello{})
	http.Handle("/foo", &Hello{})
	http.Handle("/bar", &Hello{})
	//This starts serving
	err := http.ListenAndServe("localhost:80", nil)
	if err != nil {
		log.Fatal(err)
	}
}