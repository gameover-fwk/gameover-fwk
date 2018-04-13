package gameover.fwk.ai

import com.badlogic.gdx.math.{Rectangle, Vector2}
import gameover.fwk.pool.Vector2Pool
import com.badlogic.gdx.utils.{Array => GdxArray}
import gameover.fwk.libgdx.utils.LibGDXHelper
import gameover.fwk.math.MathUtils

/**
 * This class is an attempt to make a smart path finding. The algorithm first check
 * if the target can be reached directly from a direct path taking care of the area.
 * If it is not possible then an A* algorithm is apply with a smooth path finding
 */
class SmartPathFinding(hComputeStrategy: HComputeStrategy) extends AnyRef with LibGDXHelper {
  val aStar = new AStar(hComputeStrategy)

  /**
    * Find a smooth path for an area to reach a point. It is possible to get the path to the nearest point if no path is found.
    */
  def findSmoothPath(area: Rectangle, targetX: Float, targetY: Float, findNearestPoint: Boolean, collisionDetector: CollisionDetector): GdxArray[Vector2] = {
    if (collisionDetector.canMoveStraightToPoint(area, targetX, targetY)) {
      val ret: GdxArray[Vector2] = Vector2Pool.obtainAsGdxArray(1)
      ret.get(0).set(targetX, targetY)
      return ret
    }
    aStar.findSmoothPath(area, targetX, targetY, findNearestPoint, collisionDetector)
  }

  /**
    * Find a smooth path for an area to reach another area. It is possible to get the path to the nearest point if no path is found.
    */
  def findSmoothPath(area: Rectangle, targetArea: Rectangle, findNearestPoint: Boolean, collisionDetector: CollisionDetector) : GdxArray[Vector2] = {
    val center = targetArea.getCenter(Vector2Pool.obtain())
    try {
      val pathToCenter = findSmoothPath(area, center.x, center.y, findNearestPoint = false, collisionDetector)
      if (pathToCenter != null && pathToCenter.nonEmpty) {
        return pathToCenter
      }
    } finally {
      Vector2Pool.free(center)
    }
    val position = area.getCenter(Vector2Pool.obtain())
    try {
      val edges = MathUtils.nearestEdges(position.x, position.y, targetArea)
      for ((ex, ey) <- edges) {
        val pathToEdge = findSmoothPath(area, ex, ey, findNearestPoint = false, collisionDetector)
        if (pathToEdge != null && pathToEdge.nonEmpty)
          return pathToEdge
      }
    } finally {
      Vector2Pool.free(position)
    }
    if (findNearestPoint)
      findSmoothPath(area, center.x, center.y, findNearestPoint, collisionDetector)
    else
      new GdxArray[Vector2]()
  }
}
