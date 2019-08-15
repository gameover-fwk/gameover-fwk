package gameover.fwk.math

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

import gameover.fwk.pool.Vector2Pool

object MathUtils {

    /**
     * Compute the distance for a list of points.
     * For example, if we give three points a, b and c, it
     * will compute two distances: from a to b and from b to c
     * @param pathPoints the path point
     * @return a list of distances
     */
    fun distance(pathPoints: List<GridPoint2>?) : List<Double>?
            = if (pathPoints != null) pathPoints.mapIndexed { i, p -> if (i == 0) 0.0 else distance(p, pathPoints[i - 1]) } else null

    fun floorAsInt(x: Float): Int {
        return MathUtils.floor(x)
    }

    fun floor(x: Float): Float {
        return floorAsInt(x).toFloat()
    }

    /**
     * Compute the sum of all distances for a list of points.
     * For example, if we give three points a, b and c, it
     * will compute two distances: from a to b and from b to c, then it will sum this two distances
     * @param pathPoints the path point
     * @return a distance
     */
    fun distanceSum(pathPoints: List<GridPoint2>?) : Double = distance(pathPoints)?.reduceRight { v, acc -> v + acc } ?: 0.0

    fun distance(a: GridPoint2, b: GridPoint2) : Double = Math.sqrt(Math.pow((b.x - a.x).toDouble(), 2.0) + Math.pow((b.y - a.y).toDouble(), 2.0))

    fun computeAngle(x1: Float, y1: Float, x2: Float, y2: Float): Float = computeAngle(x2 - x1, y2 - y1)

    fun computeAngle(dx: Float, dy: Float): Float {
        val dv: Vector2 = Vector2Pool.obtain(dx, dy)
        try {
            return dv.angle()
        } finally {
            Vector2Pool.free(dv)
        }
    }


    /**
     * Find directions to reach a point from a point.
     */
    fun directions(x: Float, y: Float, tx: Float, ty: Float) : Directions {
        val diffX = Math.signum(tx - x)
        val diffY = Math.signum(ty - y)
        return Directions(diffY > 0f, diffY < 0f, diffX < 0f, diffX > 0f)
    }

    /**
     * Return all edges
     */
    fun edges(area: Rectangle) : List<Pair<Float, Float>> =
        listOf(Pair(area.x, area.y), Pair(area.x + area.width, area.y), Pair(area.x, area.y + area.height), Pair(area.x + area.width, area.y + area.height))

    /**
     * Return all edges sorted from the closest to farest
     */
    fun nearestEdges(x: Float, y: Float, area: Rectangle) : List<Pair<Float, Float>> {
        return edges(area).sortedBy {
            (tx, ty) -> Math.pow((tx - x).toDouble(), 2.0) + Math.pow((ty - y).toDouble(), 2.0)
        }
    }

}

data class Directions(val top: Boolean, val bottom: Boolean, val left: Boolean, val right: Boolean)
