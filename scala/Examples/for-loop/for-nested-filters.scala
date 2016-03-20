//A list of lists;
val lists=
  List(
    List(1, 2, 3, 4),
    List(3, 5, 7, 9, 11, 13, 15),
    List(10, 20, 30)
  )
println(lists.getClass());

//Magical nesting!
for (list<-lists; i<-list)
  print(" "+i);

//With filters! 
println 
for (list<-lists if (list.size>3);
     i   <-list  if (i<10))
  print(" "+i);  
  
//With stupid variables! 
println 
for (list<-lists     if (list.size>3);
     i   <-list      if (i % 10!=0);
     x=i*10;y=i*100; if (x<300 && y>1000)
     )
  print(" "+i+"->"+x+":"+y);    
  
//Without semi-colons! Use curlies. Note that I had to use an extra line break
//between the assignment expressions:  
println
for {list<-lists     if (list.size>3)
     i   <-list      if (i % 10!=0)
     x=i*10 
     y=i*100         if (x<300 && y>1000)
     }
  print(" "+i+"->"+x+":"+y);      