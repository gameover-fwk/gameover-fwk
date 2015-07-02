package gameover.fwk.ai

import com.badlogic.gdx.math.{Rectangle, Vector2}
import gameover.fwk.libgdx.utils.LibGDXHelper
import gameover.fwk.pool.Vector2Pool

/**
 * This class is an attempt to make a smart path finding. The algorithm first check
 * if the target can be reached directly from a direct path taking care of the area.
 * If it is not possible then an A* algorithm is apply with a smooth path finding
 */
class SmartPathFinding(hComputeStrategy: HComputeStrategy) extends LibGDXHelper {
  val aStar = new AStar(hComputeStrategy)

  def findSmoothPath(area: Rectangle, x: Float, y: Float, targetX: Float, targetY: Float, findNearestPoint: Boolean, collisionDetector: CollisionDetector): GdxArray[Vector2] = {
    if (collisionDetector.isDirect(area, x, y, targetX, targetY, onlyBlocking = false)) {
      val ret: GdxArray[Vector2] = Vector2Pool.obtainAsGdxArray(1)
      ret.get(0).set(targetX, targetY)
      return ret
    }
    aStar.findSmoothPath(area, x, y, targetX, targetY, findNearestPoint, collisionDetector)
  }
}
