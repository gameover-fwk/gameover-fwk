package gameover.fwk.ai.impl

import com.badlogic.gdx.math.{Rectangle, Vector2}
import gameover.fwk.ai.CollisionDetector
import gameover.fwk.libgdx.collection.GdxArray
import gameover.fwk.libgdx.gfx.GeometryUtils
import gameover.fwk.pool.Vector2Pool

abstract class BasicCollisionDetector extends CollisionDetector{

  override def isDirect(area: Rectangle, targetX: Float, targetY: Float, onlyBlocking: Boolean): Boolean = {
    val center: Vector2 = area.getCenter(Vector2Pool.obtain())
    val x = center.x
    val y = center.y
    Vector2Pool.free(center)
    val isGoingTop: Boolean = Math.signum(targetY - y) >= 0f
    val isGoingRight: Boolean = Math.signum(targetX - x) >= 0f
    val halfWidth = area.width / 2
    val halfHeight = area.height / 2
    val diffX1: Float = if (isGoingRight) -1  else 1
    val diffY1: Float = if (isGoingTop) 1 else -1
    val intersectionPoints: GdxArray[Vector2] = GeometryUtils.computeTiledIntersectionPoints(x + diffX1 * halfWidth, y + diffY1 * halfHeight, targetX + diffX1 * halfWidth, targetY + diffY1 * halfHeight)
    val diffX2: Float = if (isGoingRight) 1 else 1
    val diffY2: Float = if (isGoingTop) -1 else 1
    intersectionPoints.addAll(GeometryUtils.computeTiledIntersectionPoints(x + diffX2 * halfWidth, y + diffY2 * halfHeight, targetX + diffX2 * halfWidth, targetY + diffY2 * halfHeight))
    try {
      !checkCollisions(intersectionPoints, onlyBlocking)
    } finally {
      Vector2Pool.free(intersectionPoints)
    }
  }


  private def checkCollisions(intersections: GdxArray[Vector2], onlyBlocking: Boolean): Boolean = {
    for (intersection <- intersections) {
      if (Math.floor(intersection.x) == intersection.x) {
        if (checkCollision(intersection.x, Math.floor(intersection.y).toFloat, onlyBlocking) ||
          checkCollision(intersection.x - 1, Math.floor(intersection.y).toFloat, onlyBlocking)) {
          return true
        }
      }
      if (Math.floor(intersection.y) == intersection.y) {
        if (checkCollision(Math.floor(intersection.x).toFloat, intersection.y, onlyBlocking) ||
          checkCollision(Math.floor(intersection.x).toFloat, intersection.y - 1, onlyBlocking)) {
          return true
        }
      }
    }
    false
  }


}
