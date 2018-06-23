package gameover.fwk.math

import com.badlogic.gdx.math.{GridPoint2, Rectangle, Vector2}
import gameover.fwk.libgdx.utils.LibGDXHelper
import gameover.fwk.pool.Vector2Pool

import scala.collection.Iterable

object MathUtils extends AnyRef {

  /**
    * Compute the distance for a list of points.
    * For example, if we give three points a, b and c, it
    * will compute two distances: from a to b and from b to c
    * @param pathPoints the path point
    * @return an array of distance
    */
  def distance(pathPoints: Array[GridPoint2]) : Array[Double] = {
    pathPoints.zipWithIndex.map { case (p, i) => if (i == 0) 0d else distance(p, pathPoints(i - 1)) }.tail
  }

  /**
    * Compute the sul of all distances for a list of points.
    * For example, if we give three points a, b and c, it
    * will compute two distances: from a to b and from b to c, then it will sum this two distances
    * @param pathPoints the path point
    * @return a distance
    */
  def distanceSum(pathPoints: Array[GridPoint2]) : Double = {
    distance(pathPoints).reduceRight[Double] { case (d1, d2) => d1 + d2 }
  }

  def distance(a: GridPoint2, b: GridPoint2) : Double = Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2)).toInt

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
    * Return all edges
    */
  def edges(area: Rectangle) : Iterable[(Float, Float)] =
    (area.x, area.y) :: (area.x + area.width, area.y) :: (area.x, area.y + area.height) :: (area.x + area.width, area.y + area.height) :: Nil

    /**
    * Return all edges sorted from the closest to farest
    */
  def nearestEdges(x: Float, y: Float, area: Rectangle) : Seq[(Float, Float)] = {
    edges(area).toList sortBy {
      case (tx, ty) => Math.pow(tx - x, 2) + Math.pow(ty - y, 2)
    }
  }
}
