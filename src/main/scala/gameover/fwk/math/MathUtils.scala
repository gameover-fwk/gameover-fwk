package gameover.fwk.math

import com.badlogic.gdx.math.{Rectangle, Vector2}
import gameover.fwk.pool.Vector2Pool

object MathUtils {

  def computeAngle(x1: Float, y1: Float, x2: Float, y2: Float): Float = computeAngle(x2 - x1, y2 - y1)

  def computeAngle(dx: Float, dy: Float): Float = {
    val dv: Vector2 = Vector2Pool.obtain(dx, dy)
    try {
      dv.angle
    } finally {
      Vector2Pool.free(dv)
    }
  }


  /**
    * Find directions to reach a point from a point.
    */
  def directions(x: Float, y: Float, tx: Float, ty: Float) : Directions = {
    val diffX = Math.signum(tx - x)
    val diffY = Math.signum(ty - y)
    Directions(diffY > 0f, diffY < 0f, diffX < 0f, diffX > 0f)
  }

  case class Directions(top: Boolean, bottom: Boolean, left: Boolean, right: Boolean)

  /**
    * Return all edges sorted from the closest to farest
    */
  def nearestEdges(x: Float, y: Float, area: Rectangle) : Seq[(Float, Float)] = {
    val edges = (area.x, area.y) :: (area.x + area.width, area.y) :: (area.x, area.y + area.height) :: (area.x + area.width, area.y + area.height) :: Nil
    edges sortBy {
      case (tx, ty) => Math.pow(tx - x, 2) + Math.pow(ty - y, 2)
    }
  }
}
