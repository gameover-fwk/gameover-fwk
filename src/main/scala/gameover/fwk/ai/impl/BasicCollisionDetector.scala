package gameover.fwk.ai.impl

import com.badlogic.gdx.math.{Rectangle, Vector2}
import gameover.fwk.ai.CollisionDetector
import gameover.fwk.libgdx.gfx.GeometryUtils
import gameover.fwk.libgdx.utils.LibGDXHelper
import gameover.fwk.pool.Vector2Pool

abstract class BasicCollisionDetector extends CollisionDetector with LibGDXHelper {

  override def isDirect(area: Rectangle, x: Float, y: Float, targetX: Float, targetY: Float, onlyWalls: Boolean): Boolean = {
    val isGoingTop: Boolean = Math.signum(targetY - y) >= 0f
    val isGoingRight: Boolean = Math.signum(targetX - x) >= 0f
    val diffX1: Float = if (isGoingRight) area.width + area.x else area.x
    val diffY1: Float = if (isGoingTop) area.y else area.height + area.y
    val intersectionPoints: GdxArray[Vector2] = GeometryUtils.computeTiledIntersectionPoints(x + diffX1, y + diffY1, targetX + diffX1, targetY + diffY1)
    val diffX2: Float = if (isGoingRight) area.x else area.width + area.x
    val diffY2: Float = if (isGoingTop) area.height + area.y else area.y
    intersectionPoints.addAll(GeometryUtils.computeTiledIntersectionPoints(x + diffX2, y + diffY2, targetX + diffX2, targetY + diffY2))
    try {
      !checkCollisions(intersectionPoints, onlyWalls)
    } finally {
      Vector2Pool.free(intersectionPoints)
    }
  }

  private def checkCollisions(intersections: Iterable[Vector2], onlyWalls: Boolean): Boolean = {
    for (intersection <- intersections) {
      if (Math.floor(intersection.x) == intersection.x) {
        if (checkCollision(intersection.x.toInt, Math.floor(intersection.y).toInt, onlyWalls) || checkCollision(intersection.x.toInt - 1, Math.floor(intersection.y).toInt, onlyWalls)) {
          return true
        }
      }
      if (Math.floor(intersection.y) == intersection.y) {
        if (checkCollision(Math.floor(intersection.x).toInt, intersection.y.toInt, onlyWalls) || checkCollision(Math.floor(intersection.x).toInt, intersection.y.toInt - 1, onlyWalls)) {
          return true
        }
      }
    }
    false
  }


}
