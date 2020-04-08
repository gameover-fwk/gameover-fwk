package gameover.fwk.ai

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import gameover.fwk.math.MathUtils.edges
import gameover.fwk.pool.GridPoint2Pool
import gameover.fwk.pool.Vector2Pool

class AStar(val hComputeStrategy: HComputeStrategy) {

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
    fun findPath(x: Float, y: Float, tx: Float, ty: Float, findClosestPoint: Boolean, collisionDetector: CollisionDetector): List<GridPoint2>? {
        val opened = ArrayList<Point>()
        val closed = ArrayList<Point>()
        val ret = arrayListOf<GridPoint2>()
        val ix: Int = MathUtils.floor(x)
        val iy: Int = MathUtils.floor(y)
        val itx: Int = MathUtils.floor(tx)
        val ity: Int = MathUtils.floor(ty)
        if (ix == itx && iy == ity) {
            return ret
        }

        val nearest = Nearest()
        var point: Point? = addToOpenedIfElligible(ix, iy, itx, ity, collisionDetector, null, opened, closed, nearest)
        if (point != null) {
            point = processPath(itx, ity, collisionDetector, point, hComputeStrategy, opened, closed, nearest)
            if (point != null) {
                return computeFinalPath(ix, iy, point)
            }
        }
        if (findClosestPoint && nearest.p != null) {
            return computeFinalPath(ix, iy, nearest.p!!)
        } else {
            return null
        }
    }

    /** Compute smooth path to a target point from an area.
     * If the center of the starting area is in the void, we look at all edges and search for each of
     * these edges  to find the shortest smart path.
     */
    fun findSmoothPath(area: Rectangle, tx: Float, ty: Float, findClosestPoint: Boolean, collisionDetector: CollisionDetector): ArrayList<Vector2>? {
        val center = area.getCenter(Vector2Pool.obtain())
        val points = if (collisionDetector.checkPosition(center.x, center.y) == CollisionState.Void)
            edges(area).filter { (x, y) -> collisionDetector.checkPosition(x, y) == CollisionState.Empty }
        else
            arrayListOf(Pair(center.x, center.y))
        val (x, y, path) = points.map { p -> Pair(p, findPath(p.first, p.second, tx, ty, findClosestPoint, collisionDetector)) }.sortedBy {
            (_, pathPoints) -> if (pathPoints != null && pathPoints.last().x.toFloat() == tx && pathPoints.last().y.toFloat() == ty) 0 else gameover.fwk.math.MathUtils.distanceSum(pathPoints).toInt()
        }.map { (p, path) ->
            Triple(p.first, p.second, path)
        }.firstOrNull() ?: Triple(0f, 0f, null)
        if (path != null) {
            val smoothPath = arrayListOf<Vector2>()
            computePointForSmoothPathAuxRecursively(area, path, 0, MathUtils.floor(x), MathUtils.floor(y), smoothPath)
            if (path.isNotEmpty()) {
                val last: GridPoint2 = path.last()
                if (MathUtils.floor(tx) == last.x && MathUtils.floor(ty) == last.y) smoothPath.add(Vector2Pool.obtain(tx, ty))
            }
            Vector2Pool.free(center)
            return smoothPath
        } else
            return null
    }

    private fun computePointForSmoothPathAuxRecursively(
            area: Rectangle,
            path: List<GridPoint2>,
            i: Int,
            previousTileX: Int,
            previousTileY: Int,
            smoothPath: ArrayList<Vector2>) {
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

    private fun createBottomLeftPoint(area: Rectangle, tile: GridPoint2): Vector2 = Vector2Pool.obtain(tile.x.toFloat() + area.width/2, tile.y.toFloat() + area.height/2)

    private fun createBottomRightPoint(area: Rectangle, tile: GridPoint2): Vector2 = Vector2Pool.obtain(tile.x.toFloat() + 1f - area.width/2, tile.y.toFloat() + area.height/2)

    private fun createTopLeftPoint(area: Rectangle, tile: GridPoint2): Vector2 = Vector2Pool.obtain(tile.x.toFloat() + area.width/2, tile.y.toFloat() + 1f - area.height/2)

    private fun createTopRightPoint(area: Rectangle, tile: GridPoint2): Vector2 = Vector2Pool.obtain(tile.x.toFloat() + 1f - area.width/2, tile.y.toFloat() + 1f - area.height/2)

    private fun computeFinalPath(ix: Int, iy: Int, target: Point): List<GridPoint2>{
        var point: Point = target
        val ret = arrayListOf<GridPoint2>()
        for (i in 0 until point.index()) {
            ret.add(0, GridPoint2Pool.obtain(point.x, point.y))
            point = point.ancestor!!
        }
        ret.add(0, GridPoint2Pool.obtain(ix, iy))
        for (i in ret.size-2 downTo 1) {
            val p = ret.get(i)
            val prevP = ret.get(i - 1)
            val nextP = ret.get(i + 1)
            if ((p.x == prevP.x && p.x == nextP.x) ||
                    (p.y == prevP.y && p.y == nextP.y) ||
                    (p.x == prevP.x + 1 && p.x + 1 == nextP.x && p.y == prevP.y + 1 && p.y + 1 == nextP.y) ||
                    (p.x == prevP.x + 1 && p.x + 1 == nextP.x && p.y == prevP.y - 1 && p.y - 1 == nextP.y) ||
                    (p.x == prevP.x - 1 && p.x - 1 == nextP.x && p.y == prevP.y + 1 && p.y + 1 == nextP.y) ||
                    (p.x == prevP.x - 1 && p.x - 1 == nextP.x && p.y == prevP.y - 1 && p.y - 1 == nextP.y))
                ret.removeAt(i)
        }
        ret.removeAt(0)
        return ret
    }

    class Nearest(var p : Point? = null)

    private fun processPath(tx: Int, ty: Int, collisionDetector: CollisionDetector, ancestor: Point,
            hComputeStrategy: HComputeStrategy,
            opened: ArrayList<Point>,
            closed: ArrayList<Point>,
            nearest: Nearest): Point? {
        val x: Int = ancestor.x
        val y: Int = ancestor.y
        val a: Point? = ancestor
        addToOpenedIfElligible(x - 1, y, tx, ty, collisionDetector, a, opened, closed, nearest)
        addToOpenedIfElligible(x, y - 1, tx, ty, collisionDetector, a, opened, closed, nearest)
        addToOpenedIfElligible(x, y + 1, tx, ty, collisionDetector, a, opened, closed, nearest)
        addToOpenedIfElligible(x + 1, y, tx, ty, collisionDetector, a, opened, closed, nearest)
        if (ancestor.eq(tx, ty)) {
            return a
        } else {
            opened.remove(ancestor)
            closed.add(ancestor)
            if (opened.size > 0) {
                val lowest = opened.minBy { it.f }
                return processPath(tx, ty, collisionDetector, lowest!!, hComputeStrategy, opened, closed, nearest)
            } else {
                return null
            }
        }
    }

    private fun addToOpenedIfElligible(x: Int, y: Int,
        targetX: Int, targetY: Int,
        collisionDetector: CollisionDetector,
        ancestor: Point?,
        opened: ArrayList<Point>,
        closed: ArrayList<Point>,
        nearest: Nearest): Point? {

        if (closed.none { it.eq(x, y) }) {
            if (!collisionDetector.checkCollision(x.toFloat(), y.toFloat(), false)) {
                val found: Point? = opened.find { it.eq(x, y) }
                val g = if (ancestor != null) {
                    when {
                        ancestor.x == x -> 10
                        ancestor.y == y -> 10
                        else -> 14
                    }
                } else {
                    0
                }
                val h = hComputeStrategy.h(x, y, targetX, targetY, collisionDetector)
                val point = if (found != null) {
                    if (g + h < found.f) {
                        found.g = g
                        found.h = h
                        found.ancestor = ancestor
                    }
                    found
                } else {
                    val p = Point(x, y, g, h, ancestor)
                    opened.add(p)
                    p
                }
                if (nearest.p == null || h < nearest.p!!.h || (h == nearest.p!!.h && g + h < nearest.p!!.f)) {
                    nearest.p = point
                }
                return point
            }
        }
        return null
    }
}

/**
 * Class to hold a Point during A* computation.
 */
class Point(val x: Int, val y: Int, var g: Int, var h: Int, var ancestor: Point?) {

    fun index() : Int = if (ancestor != null) ancestor!!.index() + 1  else 0

    fun canEqual(a: Any) : Boolean = a is Point

    override fun equals(other: Any?): Boolean {
        if (other is Point)
            return other.canEqual(this) && this.x == other.x && this.y == other.y
        else return false
    }


    override fun hashCode(): Int = 41 * x + y

    override fun toString(): String = if (ancestor != null) "$x,$y<=$ancestor" else "$x,$y"

    val f : Int
        get() = g + h

    fun eq(tx: Int, ty: Int) = tx == x && ty == y
}


interface HComputeStrategy {
    fun h(x: Int, y: Int, px: Int, py: Int, collisionDetector: CollisionDetector): Int
}