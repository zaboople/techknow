package main

import (
	"fmt"
	"io"
	"os"
	"strings"
)

type rot13Reader struct {
	other io.Reader
}
func (r13 *rot13Reader) Read(p []byte) (n int, err error){

	//Strange: If I declare these as const instead of var, I get overflow barking.
	//Well dammit fine:
	var rotateCap int=-1 + 'A' - 'Z'
	var rotateLow int=-1 + 'a' - 'z'

	//Read the input:
	n,err=r13.other.Read(p)
	
	//Transform with stupid rules:
	if err==nil {
		for i:=0; i<n; i++{
			if p[i] >= 'A' && p[i] <= 'Z'{
				p[i]+=13
				if p[i]>'Z' {
					p[i]+=byte(rotateCap)
				}		
			} else 
			if p[i] >= 'a' && p[i] <= 'z'{
				p[i]+=13
				if p[i]>'z' {
					p[i]+=byte(rotateLow)
				}
			}
		}
	}
	
	//Return bytes read & error per transform rules:
	return n,err
}

func main() {

	//We're going to give this thing unicode and it will screw it up.
	//That's a unicode snowman there, so it only works if we don't hit a byte boundary. 
	//So we put him at the very beginning.
	{
		r := strings.NewReader("☃ Hello, Readerly dibbitoo spasm!")
		b := make([]byte, 8)
	
		//Note how you get an error no matter what. OK.
		for {
			n, err := r.Read(b)
			//Print debugging information:
			fmt.Printf("count=%v error=%v bytes=%v\n", n, err, b)
			//Print the string read:
			fmt.Printf("b[:n]=%q\n", b[:n])
			if err == io.EOF {
				break
			}
		}
	}

	{
		fmt.Println("\nDoing rot13 tricks:")
		stringReader := strings.NewReader("Lbh penpxrq gur pbqr! Lbh Ner VAFNARYL NJRFBZR oyne")
		rot13 := rot13Reader{stringReader}
		io.Copy(os.Stdout, &rot13)
	}

	
}

