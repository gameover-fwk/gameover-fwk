package gameover.fwk.ai

import com.badlogic.gdx.math.{GridPoint2, MathUtils, Rectangle, Vector2}
import com.badlogic.gdx.utils.Array
import gameover.fwk.libgdx.collection.GdxArray
import gameover.fwk.libgdx.utils.LibGDXHelper
import gameover.fwk.pool.{GridPoint2Pool, Vector2Pool}

import scala.math.Ordering

class AStar(hComputeStrategy: HComputeStrategy) extends LibGDXHelper {
  private val opened: GdxArray[Point] = new GdxArray()
  private val closed: GdxArray[Point] = new GdxArray()
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
   * @param findClosestPoint return the nearest point if target is not reachable
   * @param collisionDetector the collision detector to use
   * @return an array of tiles indices to reach path. Returns <code>null</code> if node path is found.
   */
  def findPath(x: Float, y: Float, tx: Float, ty: Float, findClosestPoint: Boolean, collisionDetector: CollisionDetector): GdxArray[GridPoint2] = {
    val ret: GdxArray[GridPoint2] = new GdxArray[GridPoint2]
    val ix: Int = MathUtils.floor(x)
    val iy: Int = MathUtils.floor(y)
    val itx: Int = MathUtils.floor(tx)
    val ity: Int = MathUtils.floor(ty)
    if (ix == itx && iy == ity) {
      return ret
    }
    var p: Option[Point] = addToOpenedIfElligible(ix, iy, ix, iy, itx, ity, collisionDetector, None, hComputeStrategy)
    if (p.isDefined) {
      p = processPath(itx, ity, collisionDetector, p.get, hComputeStrategy)
      if (p.isDefined) {
        return computeFinalPath(ix, iy, p.get)
      }
    }
    if (findClosestPoint && nearest != null) {
      computeFinalPath(ix, iy, nearest)
    } else {
      null
    }
  }

  def findSmoothPath(area: Rectangle, tx: Float, ty: Float, findClosestPoint: Boolean, collisionDetector: CollisionDetector): GdxArray[Vector2] = {
    val center = area.getCenter(Vector2Pool.obtain())
    val path: GdxArray[GridPoint2] = findPath(center.x, center.y, tx, ty, findClosestPoint, collisionDetector)
    if (path != null) {
      val smoothPath: GdxArray[Vector2] = new GdxArray[Vector2]
      computePointForSmoothPathAuxRecursively(area, path, 0, MathUtils.floor(center.x), MathUtils.floor(center.y), smoothPath)
      if (path.nonEmpty) {
        val last: GridPoint2 = path.peekLast()
        if (MathUtils.floor(tx) == last.x && MathUtils.floor(ty) == last.y) smoothPath.add(Vector2Pool.obtain(tx, ty))
      }
      Vector2Pool.free(center)
      smoothPath
    } else null
  }

  private def computePointForSmoothPathAuxRecursively(
                                                       area: Rectangle,
                                                       path: GdxArray[GridPoint2],
                                                       i: Int,
                                                       previousTileX: Int,
                                                       previousTileY: Int,
                                                       smoothPath: GdxArray[Vector2]) {
    if (i < path.lastIndex) {
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

  private def createBottomLeftPoint(area: Rectangle, tile: GridPoint2): Vector2 = Vector2Pool.obtain(tile.x.toFloat + area.width/2, tile.y.toFloat + area.height/2)

  private def createBottomRightPoint(area: Rectangle, tile: GridPoint2): Vector2 = Vector2Pool.obtain(tile.x.toFloat + 1f - area.width/2, tile.y.toFloat + area.height/2)

  private def createTopLeftPoint(area: Rectangle, tile: GridPoint2): Vector2 = Vector2Pool.obtain(tile.x.toFloat + area.width/2, tile.y.toFloat + 1f - area.height/2)

  private def createTopRightPoint(area: Rectangle, tile: GridPoint2): Vector2 = Vector2Pool.obtain(tile.x.toFloat + 1f - area.width/2, tile.y.toFloat + 1f - area.height/2)

  private def computeFinalPath(ix: Int, iy: Int, p: Point): GdxArray[GridPoint2] = {
    var point: Point = p
    val ret: GdxArray[GridPoint2] = new Array[GridPoint2]
    for (i <- 0 to p.index-1) {
      ret.insert(0, GridPoint2Pool.obtain(point.x, point.y))
      point = point.ancestor.orNull
    }
    ret.insert(0, GridPoint2Pool.obtain(ix, iy))
    for (i <- ret.size-2 to 1 by -1) {
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
      opened.drop(_ == ancestor)
      closed ::= ancestor
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
    if (!closed.exists(_.is(x, y))) {
      try {
        if (!collisionDetector.checkCollision(x, y, onlyBlocking = false)) {
          val found: Option[Point] = opened.find(_.is(x, y))
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
            opened ::= p
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
