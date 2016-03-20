println
args match {
  case Array()=>
    println("DUDE, NOTHING IS GOING TO HAPPEN IF YOU DON'T GIVE ME ARGUMENTS")
  case _=>go
}
def go{
  def describe(s:String)={println("\n\n "+s+":")}
  def doprint(s:String)={print(" "+s)}
  def doprint()={print(" <no argument>")}
  
  describe("While loop");
  var x=0
  while (x < args.length) {
    doprint(args(x))
    x+=1
  }
  
  describe("for... to");  
  for (i <- 0 to args.length-1)
    doprint(args(i))  
  
  //"Until" is clever:
  describe("for ... until ");  
  for (i <- 0 until args.length)
    doprint(args(i));
  
  describe("for (<-)");
  for (arg <- args)
    doprint(arg)
    
  describe("foreach (=>)");
  args.foreach(arg => doprint(arg))
  
  //Guess what, you don't have to specify the argument:
  describe("foreach without parameter");
  args.foreach(doprint)
  
  //This invokes the no-arguments doprint twice, then 
  //the one-arguments version once for each argument.
  //Altogether surprising but I suppose there's a reason:
  describe("foreach super-strange");
  args.foreach({doprint;doprint;doprint})
  
}