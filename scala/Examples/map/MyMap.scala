
  //Create:
  var capital = Map("US" -> "Washington", "France" -> "Paris")
  //Add element:
  capital += ("Japan" ->"Tokyo")
  println(capital)
  
  //Get value:
  println(capital("France"))
  
  //Set a value twice:
  capital += ("1"-> "2")
  capital += ("1"-> "3")
  //Add a bunch of pairs:
  capital += ("10"-> "20", "100"-> "200", "1000"-> "20")
  //Remove a bunch: 
  capital -= ("10", "20", "1000")
  println(capital)
