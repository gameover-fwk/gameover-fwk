package gameover.fwk.ai

import com.badlogic.gdx.math.{Rectangle, Vector2}
import gameover.fwk.libgdx.collection.GdxArray
import gameover.fwk.pool.Vector2Pool

/**
 * This class is an attempt to make a smart path finding. The algorithm first check
 * if the target can be reached directly from a direct path taking care of the area.
 * If it is not possible then an A* algorithm is apply with a smooth path finding
 */
class SmartPathFinding(hComputeStrategy: HComputeStrategy) {
  val aStar = new AStar(hComputeStrategy)

  def findSmoothPath(area: Rectangle, targetX: Float, targetY: Float, findNearestPoint: Boolean, collisionDetector: CollisionDetector): GdxArray[Vector2] = {
    if (collisionDetector.isDirect(area, targetX, targetY, onlyBlocking = false)) {
      val ret: GdxArray[Vector2] = Vector2Pool.obtainAsGdxArray(1)
      ret.get(0).set(targetX, targetY)
      return ret
    }
    aStar.findSmoothPath(area, targetX, targetY, findNearestPoint, collisionDetector)
  }
}
