// Returns a row as a sequence
def makeRowSeq(row: Int, size: Int) =
  for (col <-1 to size) yield {
    val prod = (row * col).toString
    val on=(col % 5==1) 
    val pad1=if (on) "|" else " "
    val pad2=" " * (
      5 - prod.length
    )
    pad1+pad2+prod
  }


// Returns table as a string with one row per line
def multiTable(size: Int) = {
  val tableSeq = // a sequence of row strings
    for (rowNum <-1 to size)
      yield {
        val s=makeRowSeq(rowNum, size).mkString
        val j=
          if (rowNum==1 || rowNum%5==1)
            ("-" * s.length)+"\n"
          else
            ""
        j+s;
      }
  tableSeq.mkString("\n")
}

println(
  multiTable(
    Integer.parseInt(args(0))
  )
)