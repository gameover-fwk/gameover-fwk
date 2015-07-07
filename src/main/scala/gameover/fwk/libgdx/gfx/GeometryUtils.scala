package gameover.fwk.libgdx.gfx

import java.util.Comparator

import com.badlogic.gdx.math.{Rectangle, Vector2}
import gameover.fwk.libgdx.GdxArray
import gameover.fwk.pool.Vector2Pool

object GeometryUtils {

  def computeTiledIntersectionPoints(x1: Float, y1: Float, x2: Float, y2: Float): GdxArray[Vector2] = {
    val ret = new GdxArray[Vector2]
    val diffX: Float = x2 - x1
    val fX1: Float = com.badlogic.gdx.math.MathUtils.floor(x1)
    val diffY: Float = y2 - y1
    val fY1: Float = com.badlogic.gdx.math.MathUtils.floor(y1)
    val alpha: Double = Math.atan2(diffY, diffX)
    val angle: Double = Math.abs(alpha % (Math.PI / 2d))
    val inverseAxis: Boolean = alpha > Math.PI / 2d || alpha < -Math.PI / 2d
    val alphaTan: Float = Math.tan(angle).toFloat
    val xSign: Float = Math.signum(diffX)
    val ySign: Float = Math.signum(diffY)
    val xs = new GdxArray[Float]
    val ys = new GdxArray[Float]
    var finished: Boolean = false
    while (!finished) {
      val x: Float = if (xSign < 0f) fX1 - (if (fX1 == x1) 1 else 0) - xs.size else fX1 + 1 + xs.size
      if ((xSign > 0 && x >= x2) || (xSign < 0 && x <= x2) || xSign == 0)
        finished = true
      else
        xs.add(x)
    }
    finished = false
    while (!finished) {
      val y: Float = if (ySign < 0f) fY1 - (if (fY1 == y1) 1 else 0) - ys.size else fY1 + 1 + ys.size
      if ((ySign > 0 && y >= y2) || (ySign < 0 && y <= y2) || ySign == 0)
        finished = true
      else
        ys.add(y)
    }
    for (x <- xs) {
      if (!inverseAxis)
        ret.add(Vector2Pool.obtain(x, computeYAux(x1, y1, x, ySign, alphaTan)))
      else
        ret.add(Vector2Pool.obtain(x, computeXAux(y1, x1, x, ySign, alphaTan)))
    }
    for (y <- ys) {
      if (!inverseAxis)
        ret.add(Vector2Pool.obtain(computeXAux(x1, y1, y, xSign, alphaTan), y))
      else
        ret.add(Vector2Pool.obtain(computeYAux(y1, x1, y, xSign, alphaTan), y))
    }
    ret.sort(new Function2DPointComparator(xSign, ySign))
    var i = ret.size - 2
    while ((i >= 0) && (i + 1 < ret.size)) {
      if (ret.get(i).equals(ret.get(i + 1)))
        ret.removeIndex(i)
      else
        i = i - 1
    }
    ret
  }

  def computeYAux(x1: Float, y1: Float, x2: Float, ySign: Float, alphaTan: Float) = y1 + ySign * (alphaTan * Math.abs(x2 - x1))

  def computeXAux(x1: Float, y1: Float, y2: Float, xSign: Float, alphaTan: Float) = x1 + (if (alphaTan != 0) xSign * (Math.abs(y2 - y1) / alphaTan) else 0f)

  def centerAndEdges(area: Rectangle): List[Vector2] =
    area.getCenter(Vector2Pool.obtain()) ::
      area.getPosition(Vector2Pool.obtain()) ::
      area.getPosition(Vector2Pool.obtain()).add(area.width, 0) ::
      area.getPosition(Vector2Pool.obtain()).add(0, area.height) ::
      area.getPosition(Vector2Pool.obtain()).add(area.width, area.height) :: Nil
}


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
