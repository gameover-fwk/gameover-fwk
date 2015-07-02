package gameover.fwk.collection

object Arrays {

  def copyArray[T](src: Array[Array[T]], srcPos: Int, dest: Array[Array[T]], destPos: Int, length: Int) {
    for (i <- 0 to length) {
      Array.copy(src(i), srcPos, dest(i), destPos, src(0).length)
    }
  }
}
