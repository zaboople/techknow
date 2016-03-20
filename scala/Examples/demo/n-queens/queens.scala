def queens(n: Int): List[List[(Int, Int)]] = {
  def placeQueens(row: Int): List[List[(Int, Int)]] =
    if (row == 0)
      List(List())
    else
      for {
        queens <- placeQueens(row - 1)
        column <- 1 to n
        if isSafe(row, column, queens)
      } yield (row, column) :: queens
  placeQueens(n)
}

def isSafe(arow:Int, acol:Int, queens: List[(Int, Int)]) =
  queens forall (q => !inCheck(arow, acol, q._1, q._2))
  
def inCheck(arow: Int, acol: Int, brow:Int, bcol:Int) =
  arow == brow || // same row
  acol == bcol || // same column
  (arow - brow).abs == (acol - bcol).abs // on diagonal


def debug(colCount:Int, list: List[(Int,Int)])={
  for (x <- list){ 
    val mark=x._2;
    for (col<- 1 to mark -1)
      print("-");
    print("Q");
    for (col<- mark + 1 to colCount)
      print("-");
    println();
  }
  println();
}

val count=5;
for (list <- queens(count))
  debug(count, list);
