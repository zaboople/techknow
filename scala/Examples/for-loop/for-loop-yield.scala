//Yield allows one to create a list from a "for" statement. 
//Note that it also includes variables inside the for statement, to show how you can yield a for loop variable. 
val list=
  for {
      x <- 1 to 100 
      y=x*3*5*10
    }
    yield x+"->"+y
    
for (z<-list)
  print(" "+z);
