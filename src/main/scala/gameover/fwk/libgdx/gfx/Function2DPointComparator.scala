package gameover.fwk.libgdx.gfx

import java.util.Comparator

import com.badlogic.gdx.math.Vector2

class Function2DPointComparator(xSign: Float, ySign: Float) extends Comparator[Vector2] {

  override def compare(o1: Vector2, o2: Vector2): Int = {
    val signum: Float = Math.signum(o1.x - o2.x)
    if (signum == 0) {
      (Math.signum(o1.x - o2.x) * ySign).toInt
    }
    else {
      (signum * xSign).toInt
    }
  }
}
