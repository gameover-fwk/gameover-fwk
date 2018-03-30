package gameover.fwk.ai.impl

import com.badlogic.gdx.math.{Rectangle, Vector2}
import gameover.fwk.ai.CollisionDetector
import gameover.fwk.libgdx.gfx.GeometryUtils
import gameover.fwk.libgdx.utils.LibGDXHelper
import gameover.fwk.pool.Vector2Pool


abstract class BasicCollisionDetector extends CollisionDetector with LibGDXHelper {

  override def isDirect(visionArea: Rectangle, targetArea: Rectangle, onlyBlocking: Boolean): Boolean = {
    val centerVisionArea = visionArea.getCenter(Vector2Pool.obtain())
    val x = centerVisionArea.x
    val y = centerVisionArea.y
    Vector2Pool.free(centerVisionArea)
    val centerTargetArea = targetArea.getCenter(Vector2Pool.obtain())
    val tx = centerTargetArea.x
    val ty = centerTargetArea.y
    Vector2Pool.free(centerVisionArea)
    val dirs = directions(x, y, tx, ty)

    val xys = new GdxArray[(Float, Float)]
    val txys = new GdxArray[(Float, Float)]
    if (dirs.top) {
      if (dirs.right) {
        xys.add((visionArea.x + visionArea.width, visionArea.y))
        xys.add((visionArea.x, visionArea.y + visionArea.height))
        txys.add((targetArea.x + targetArea.width, targetArea.y))
        txys.add((targetArea.x, targetArea.y + targetArea.height))
      } else if (dirs.left) {
        xys.add((visionArea.x, visionArea.y))
        xys.add((visionArea.x + visionArea.width, visionArea.y + visionArea.height))
        txys.add((targetArea.x, targetArea.y))
        txys.add((targetArea.x + targetArea.width, targetArea.y + targetArea.height))
      } else {
        xys.add((visionArea.x, visionArea.y + visionArea.height))
        xys.add((visionArea.x + visionArea.width, visionArea.y + visionArea.height))
        txys.add((targetArea.x, targetArea.y))
        txys.add((targetArea.x + targetArea.width, targetArea.y))
      }
    } else if (dirs.bottom) {
      if (dirs.right) {
        xys.add((visionArea.x, visionArea.y))
        xys.add((visionArea.x + visionArea.width, visionArea.y + visionArea.height))
        txys.add((targetArea.x, targetArea.y))
        txys.add((targetArea.x + targetArea.width, targetArea.y + targetArea.height))
      } else if (dirs.left) {
        xys.add((visionArea.x, visionArea.y + visionArea.height))
        xys.add((visionArea.x + visionArea.width, visionArea.y))
        txys.add((targetArea.x, targetArea.y + targetArea.height))
        txys.add((targetArea.x + targetArea.width, targetArea.y))
      } else {
        xys.add((visionArea.x, visionArea.y))
        xys.add((visionArea.x + visionArea.width, visionArea.y))
        txys.add((targetArea.x, targetArea.y + targetArea.height))
        txys.add((targetArea.x + targetArea.width, targetArea.y + targetArea.height))
      }
    } else {
      if (dirs.right) {
        xys.add((visionArea.x + visionArea.width, visionArea.y))
        xys.add((visionArea.x + visionArea.width, visionArea.y + visionArea.height))
        txys.add((targetArea.x, targetArea.y))
        txys.add((targetArea.x, targetArea.y + targetArea.height))
      } else if (dirs.left) {
        xys.add((visionArea.x, visionArea.y))
        xys.add((visionArea.x, visionArea.y + visionArea.height))
        txys.add((targetArea.x + targetArea.width, targetArea.y))
        txys.add((targetArea.x + targetArea.width, targetArea.y + targetArea.height))
      }
    }
    for (xy <- xys.items) {
      for (txy <- txys.items) {
        if (isDirect(xy._1, xy._2, txy._1, txy._2, onlyBlocking))
          return true
      }
    }
    false
  }

  def directions(x: Float, y: Float, tx: Float, ty: Float) : Directions = {
    val diffX = Math.signum(tx - x)
    val diffY = Math.signum(ty - y)
    Directions(diffY > 0f, diffY < 0f, diffX < 0f, diffX > 0f)
  }

  case class Directions(top: Boolean, bottom: Boolean, left: Boolean, right: Boolean)

  override def isDirect(visionArea: Rectangle, tx: Float, ty: Float, onlyBlocking: Boolean) : Boolean = {
    val centerVisionArea = visionArea.getCenter(Vector2Pool.obtain())
    val x = centerVisionArea.x
    val y = centerVisionArea.y
    Vector2Pool.free(centerVisionArea)
    val xys = new GdxArray[(Float, Float)]
    val dirs = directions(x, y, tx, ty)
    if (dirs.top) {
      if (dirs.right) {
        xys.add((visionArea.x + visionArea.width, visionArea.y))
        xys.add((visionArea.x, visionArea.y + visionArea.height))
      } else if (dirs.left) {
        xys.add((visionArea.x, visionArea.y))
        xys.add((visionArea.x + visionArea.width, visionArea.y + visionArea.height))
      } else {
        xys.add((visionArea.x, visionArea.y + visionArea.height))
        xys.add((visionArea.x + visionArea.width, visionArea.y + visionArea.height))
      }
    } else if (dirs.bottom) {
      if (dirs.right) {
        xys.add((visionArea.x, visionArea.y))
        xys.add((visionArea.x + visionArea.width, visionArea.y + visionArea.height))
      } else if (dirs.left) {
        xys.add((visionArea.x, visionArea.y + visionArea.height))
        xys.add((visionArea.x + visionArea.width, visionArea.y))
      } else {
        xys.add((visionArea.x, visionArea.y))
        xys.add((visionArea.x + visionArea.width, visionArea.y))
      }
    } else {
      if (dirs.right) {
        xys.add((visionArea.x + visionArea.width, visionArea.y))
        xys.add((visionArea.x + visionArea.width, visionArea.y + visionArea.height))
      } else if (dirs.left) {
        xys.add((visionArea.x, visionArea.y))
        xys.add((visionArea.x, visionArea.y + visionArea.height))
      }
    }
    for (xy <- xys) {
      if (isDirect(xy._1, xy._2, tx, ty, onlyBlocking))
        return true
    }
    false
  }

  override def isDirect(x: Float, y: Float, tx: Float, ty: Float, onlyBlocking: Boolean) : Boolean = {
    val intersectionPoints = GeometryUtils.computeTiledIntersectionPoints(x, y, tx, ty)
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
