
Iterator(1, 3, 4, 5, 6, 7).collect(
  _ match {
    case a if (a==(a/2)*2) => a
  }
) foreach println _ 
