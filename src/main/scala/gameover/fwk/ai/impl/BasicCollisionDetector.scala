package gameover.fwk.ai.impl

import com.badlogic.gdx.math.{Rectangle, Vector2}
import gameover.fwk.ai.CollisionDetector
import gameover.fwk.libgdx.gfx.GeometryUtils
import gameover.fwk.libgdx.utils.LibGDXHelper
import gameover.fwk.math.MathUtils
import gameover.fwk.pool.Vector2Pool


abstract class BasicCollisionDetector extends CollisionDetector with LibGDXHelper {

  override def hasDirectView(visionArea: Rectangle, targetArea: Rectangle): Boolean = {
    val centerVisionArea = visionArea.getCenter(Vector2Pool.obtain())
    val x = centerVisionArea.x
    val y = centerVisionArea.y
    Vector2Pool.free(centerVisionArea)
    val centerTargetArea = targetArea.getCenter(Vector2Pool.obtain())
    val tx = centerTargetArea.x
    val ty = centerTargetArea.y
    Vector2Pool.free(centerVisionArea)
    val dirs = MathUtils.directions(x, y, tx, ty)

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
        if (hasDirectView(xy._1, xy._2, txy._1, txy._2))
          return true
      }
    }
    false
  }

  override def hasDirectView(visionArea: Rectangle, tx: Float, ty: Float) : Boolean = {
    val centerVisionArea = visionArea.getCenter(Vector2Pool.obtain())
    val x = centerVisionArea.x
    val y = centerVisionArea.y
    Vector2Pool.free(centerVisionArea)
    val xys = new GdxArray[(Float, Float)]
    val dirs = MathUtils.directions(x, y, tx, ty)
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
      if (hasDirectView(xy._1, xy._2, tx, ty))
        return true
    }
    false
  }

  override def hasDirectView(x: Float, y: Float, tx: Float, ty: Float) : Boolean = {
    !hasCollisions(x, y, tx, ty, onlyBlocking = true)
  }

  def hasDirectPath(x: Float, y: Float, tx: Float, ty: Float) : Boolean = {
    !hasCollisions(x, y, tx, ty, onlyBlocking = false)
  }

  def hasCollisions(x: Float, y: Float, tx: Float, ty: Float, onlyBlocking: Boolean) : Boolean = {
    val intersectionPoints = GeometryUtils.computeTiledIntersectionPoints(x, y, tx, ty)
    try {
      checkCollisions(intersectionPoints, onlyBlocking)
    } finally {
      Vector2Pool.free(intersectionPoints)
    }
  }

  override def canMoveStraightToPoint(area: Rectangle, tx: Float, ty: Float): Boolean = {
    if (area.contains(tx, ty))
      true
    else {
      val centerArea = area.getCenter(Vector2Pool.obtain())
      val cx = centerArea.x
      val cy = centerArea.y
      Vector2Pool.free(centerArea)
      val dirs = MathUtils.directions(cx, cy, tx, ty)
      val x = area.x
      val y = area.y
      val w = area.width
      val hw = area.width / 2
      val h = area.height
      val hh = area.height / 2
      if (dirs.top) {
        if (dirs.right) {
          hasDirectPath(x, y + h, tx - w, ty) &&
            hasDirectPath(centerArea.x + w, centerArea.y, tx, ty - w)
        } else if (dirs.left) {
          hasDirectPath(centerArea.x, centerArea.y, tx, ty - h) &&
            hasDirectPath(centerArea.x + w, centerArea.y + h, tx + w, ty)
        } else {
          hasDirectPath(x, y + h, tx - hw, ty - h) &&
            hasDirectPath(x + w, y + h, tx + hw, ty - h)
        }
      } else if (dirs.bottom) {
        if (dirs.right) {
          hasDirectPath(x, y, tx - w, ty) &&
            hasDirectPath(x + w, y + h, tx, ty + h)
        } else if (dirs.left) {
          hasDirectPath(x, y + h, tx, ty + h) &&
            hasDirectPath(x + w, y, tx + w, ty)
        } else {
          hasDirectPath(x, y, tx - hw , ty + h) &&
            hasDirectPath(x + w, y, tx + hw, ty + h)
        }
      } else {
        if (dirs.right) {
          hasDirectPath(x + w, y, tx - w , ty - hh) &&
            hasDirectPath(x + w, y + h, tx - w, ty + hh)
        } else if (dirs.left) {
          hasDirectPath(x , y, tx + w, ty - hh) &&
            hasDirectPath(x, y + h, tx + w, ty + hh)
        } else {
          true
        }
      }
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
