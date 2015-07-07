package gameover.fwk.ai

import com.badlogic.gdx.math.{GridPoint2, MathUtils, Rectangle, Vector2}
import com.badlogic.gdx.utils.Array
import gameover.fwk.libgdx.GdxArray
import gameover.fwk.libgdx.utils.LibGDXHelper
import gameover.fwk.pool.{GridPoint2Pool, Vector2Pool}

import scala.math.Ordering

class AStar(hComputeStrategy: HComputeStrategy) extends LibGDXHelper {
  private var opened: List[Point] = Nil
  private var closed: List[Point] = Nil
  private var nearest: Point = null

  /**
   * This is the classical method to call to compute an a* path.
   * It will return tiles by tiles the path to use to reach the target. The vector
   * Notice that the path returned is really a tileset path. If you need a smooth
   * path to reach target, prefer method #findSmoothPath which is compute a smooth path from
   * the path computed by this method.
   * @param x the initial position on x axis
   * @param y the initial position on y axis
   * @param tx the target position on x axis
   * @param ty the target position on y axis
   * @param findNearestPoint return the nearest point if target is not reachable
   * @param collisionDetector the collision detector to use
   * @return an array of tiles indices to reach path
   */
  def findPath(x: Float, y: Float, tx: Float, ty: Float, findNearestPoint: Boolean, collisionDetector: CollisionDetector): GdxArray[GridPoint2] = {
    val ret: GdxArray[GridPoint2] = new GdxArray[GridPoint2]
    val ix: Int = MathUtils.floor(x)
    val iy: Int = MathUtils.floor(y)
    val itx: Int = MathUtils.floor(tx)
    val ity: Int = MathUtils.floor(ty)
    if (ix == itx && iy == ity) {
      return ret
    }
    opened = Nil
    closed = Nil
    var p: Option[Point] = addToOpenedIfElligible(ix, iy, ix, iy, itx, ity, collisionDetector, None, hComputeStrategy)
    if (p.isDefined) {
      p = processPath(itx, ity, collisionDetector, p.get, hComputeStrategy)
      if (p.isDefined) {
        return computeFinalPath(ix, iy, p.get)
      }
    }
    if (findNearestPoint && nearest != null) {
      computeFinalPath(ix, iy, nearest)
    } else {
      null
    }
  }

  def findSmoothPath(area: Rectangle, tx: Float, ty: Float, findNearestPoint: Boolean, collisionDetector: CollisionDetector): GdxArray[Vector2] = {
    val center = area.getCenter(Vector2Pool.obtain())
    val path: GdxArray[GridPoint2] = findPath(center.x, center.y, tx, ty, findNearestPoint, collisionDetector)
    val smoothPath: GdxArray[Vector2] = new GdxArray[Vector2]
    computePointForSmoothPathAuxRecursively(area, path, 0, MathUtils.floor(center.x), MathUtils.floor(center.y), smoothPath)
    if (path.size > 0) {
      val last: GridPoint2 = path.get(path.size - 1)
      if (MathUtils.floor(tx) == last.x && MathUtils.floor(ty) == last.y) smoothPath.add(Vector2Pool.obtain(tx, ty))
    }
    Vector2Pool.free(center)
    smoothPath
  }

  private def computePointForSmoothPathAuxRecursively(
                                                       area: Rectangle,
                                                       path: GdxArray[GridPoint2],
                                                       i: Int,
                                                       previousTileX: Int,
                                                       previousTileY: Int,
                                                       smoothPath: GdxArray[Vector2]) {
    if (i < path.size - 1) {
      val tile: GridPoint2 = path.get(i)
      val nextTile: GridPoint2 = path.get(i + 1)
      val fromDiag: Boolean = tile.x != previousTileX && tile.y != previousTileY
      val fromLeft: Boolean = tile.x > previousTileX
      val toLeft: Boolean = tile.x > nextTile.x
      val fromBottom: Boolean = tile.y > previousTileY
      val toBottom: Boolean = tile.y > nextTile.y
      val fromRight: Boolean = tile.x < previousTileX
      val toRight: Boolean = tile.x < nextTile.x
      val fromEqualsX: Boolean = tile.x == previousTileX
      val toEqualsX: Boolean = tile.x == nextTile.x
      val fromTop: Boolean = tile.y < previousTileY
      val toTop: Boolean = tile.y < nextTile.y
      if (fromDiag) {
        if (toEqualsX) {
          if (toTop) {
            smoothPath.add(if (fromLeft) createBottomLeftPoint(area, tile) else createBottomRightPoint(area, tile))
          }
          else {
            smoothPath.add(if (fromLeft) createTopLeftPoint(area, tile) else createTopRightPoint(area, tile))
          }
        }
        else {
          if (toRight) {
            smoothPath.add(if (fromBottom) createBottomRightPoint(area, tile) else createTopRightPoint(area, tile))
          }
          else {
            smoothPath.add(if (fromBottom) createBottomLeftPoint(area, tile) else createTopLeftPoint(area, tile))
          }
        }
      }
      else {
        if (fromEqualsX) {
          if (fromTop) {
            smoothPath.add(if (toLeft) createTopLeftPoint(area, tile) else createTopRightPoint(area, tile))
          }
          else {
            smoothPath.add(if (toLeft) createBottomLeftPoint(area, tile) else createBottomRightPoint(area, tile))
          }
        }
        else {
          if (fromRight) {
            smoothPath.add(if (toBottom) createBottomRightPoint(area, tile) else createTopRightPoint(area, tile))
          }
          else {
            smoothPath.add(if (toBottom) createBottomLeftPoint(area, tile) else createTopLeftPoint(area, tile))
          }
        }
      }
      computePointForSmoothPathAuxRecursively(area, path, i + 1, tile.x, tile.y, smoothPath)
    }
  }

  private def createBottomLeftPoint(area: Rectangle, tile: GridPoint2): Vector2 = Vector2Pool.obtain(tile.x.toFloat - area.x, tile.y.toFloat - area.y)

  private def createBottomRightPoint(area: Rectangle, tile: GridPoint2): Vector2 = Vector2Pool.obtain(tile.x.toFloat + 1f - (area.width + area.x), tile.y.toFloat - area.y)

  private def createTopLeftPoint(area: Rectangle, tile: GridPoint2): Vector2 = Vector2Pool.obtain(tile.x.toFloat - area.x, tile.y.toFloat + 1f - (area.height + area.y))

  private def createTopRightPoint(area: Rectangle, tile: GridPoint2): Vector2 = Vector2Pool.obtain(tile.x.toFloat + 1f - (area.width + area.x), tile.y.toFloat + 1f - (area.height + area.y))

  private def computeFinalPath(ix: Int, iy: Int, p: Point): GdxArray[GridPoint2] = {
    var point: Point = p
    val ret: GdxArray[GridPoint2] = new Array[GridPoint2]
    for (i <- 0 to p.index-1) {
      ret.insert(0, GridPoint2Pool.obtain(point.x, point.y))
      point = point.ancestor.orNull
    }
    ret.insert(0, GridPoint2Pool.obtain(ix, iy))
    for (i <- ret.size-2 to 0 by -1) {
      val p: GridPoint2 = ret.get(i)
      val prevP: GridPoint2 = ret.get(i - 1)
      val nextP: GridPoint2 = ret.get(i + 1)
      if ((p.x == prevP.x && p.x == nextP.x) ||
          (p.y == prevP.y && p.y == nextP.y) ||
          (p.x == prevP.x + 1 && p.x + 1 == nextP.x && p.y == prevP.y + 1 && p.y + 1 == nextP.y) ||
          (p.x == prevP.x + 1 && p.x + 1 == nextP.x && p.y == prevP.y - 1 && p.y - 1 == nextP.y) ||
          (p.x == prevP.x - 1 && p.x - 1 == nextP.x && p.y == prevP.y + 1 && p.y + 1 == nextP.y) ||
          (p.x == prevP.x - 1 && p.x - 1 == nextP.x && p.y == prevP.y - 1 && p.y - 1 == nextP.y))
        ret.removeIndex(i)
    }
    ret.removeIndex(0)
    ret
  }

  def contains(x: Int, y: Int, points: List[Point]): Boolean = {
    for (p <- points) {
      if (p.is(x, y)) return true
    }
    false
  }

  def find(x: Int, y: Int, points: List[Point]): Option[Point] = {
    for (p <- points) {
      if (p.is(x, y)) return Some(p)
    }
    None
  }

  private def processPath(tx: Int, ty: Int, collisionDetector: CollisionDetector, ancestor: Point, hComputeStrategy: HComputeStrategy): Option[Point] = {
    val x: Int = ancestor.x
    val y: Int = ancestor.y
    val a = Some(ancestor)
    addToOpenedIfElligible(x - 1, y, x, y, tx, ty, collisionDetector, a, hComputeStrategy)
    addToOpenedIfElligible(x, y - 1, x, y, tx, ty, collisionDetector, a, hComputeStrategy)
    addToOpenedIfElligible(x, y + 1, x, y, tx, ty, collisionDetector, a, hComputeStrategy)
    addToOpenedIfElligible(x + 1, y, x, y, tx, ty, collisionDetector, a, hComputeStrategy)
    if (ancestor.is(tx, ty)) {
      a
    } else {
      opened = opened.filter(_ != ancestor)
      closed = ancestor :: closed
      if (opened.nonEmpty) {
        val lowest = opened.min(Ordering.by((p: Point)=>p.f))
        processPath(tx, ty, collisionDetector, lowest, hComputeStrategy)
      } else {
        None
      }
    }
  }

  private def addToOpenedIfElligible(x: Int, y: Int, previousX: Int, previousY: Int, targetX: Int, targetY: Int,
                                         collisionDetector: CollisionDetector, ancestor: Option[Point], hComputeStrategy: HComputeStrategy): Option[Point] = {
    var p: Point = null
    if (find(x, y, closed) == null) {
      try {
        if (!collisionDetector.checkCollision(x, y, onlyBlocking = false)) {
          val found: Option[Point] = find(x, y, opened)
          var g: Int = 0
          if (ancestor.isDefined) {
            g = if ((ancestor.get.x == x) || (ancestor.get.y == y)) 10 else 14
          }
          val h: Int = hComputeStrategy.h(x, y, targetX, targetY, collisionDetector)
          if (found.isDefined) {
            p = found.get
            if (g + h < p.f) {
              p.g = g
              p.h = h
              p.ancestor = ancestor
            }
          }
          else {
            p = new Point(x, y, g, h, ancestor)
            opened = p :: opened
          }
          if (nearest == null || h < nearest.h || (h == nearest.h && g + h < nearest.f)) {
            nearest = p
          }
        }
      } finally {
      }
    }
    Option.apply(p)
  }
}
