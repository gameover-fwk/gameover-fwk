package gameover.fwk.ai

/**
 * Class to hold a Point during A* computation.
 */
class Point(val x: Int, val y: Int, var g: Int, var h: Int, var ancestor: Option[Point] = None) {

  val index: Int = ancestor match {
    case Some(p) => p.index + 1
    case None => 0
  }

  def canEqual(a: Any) = a.isInstanceOf[Point]

  override def equals(that: Any): Boolean =
    that match {
      case that: Point => that.canEqual(this) && this.x == that.x && this.y == that.y
      case _ => false
    }


  override def hashCode: Int = {
    41 * x + y
  }

  override def toString: String = {
    ancestor match {
      case Some(p) => s"$x,$y<=$p"
      case None => s"$x,$y"
    }
  }

  def f = g + h

  def is(tx: Int, ty: Int) = tx == x && ty == y
}
