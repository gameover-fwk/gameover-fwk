package gameover.fwk.ai

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import gameover.fwk.pool.Vector2Pool
import gameover.fwk.math.MathUtils

class SmartPathFinding(hComputeStrategy: HComputeStrategy) {
    private val aStar = AStar(hComputeStrategy)

    /**
     * Find a smooth path for an area to reach a point. It is possible to get the path to the nearest point if no path is found.
     */
    fun findSmoothPath(area: Rectangle, targetX: Float, targetY: Float, findNearestPoint: Boolean, collisionDetector: CollisionDetector): ArrayList<Vector2>? {
        when {
            area.contains(targetX, targetY) -> return arrayListOf()
            collisionDetector.canMoveStraightToPoint(area, targetX, targetY) -> {
                val ret = Vector2Pool.obtainAsList(1)
                ret[0].set(targetX, targetY)
                return ret
            }
            else -> return aStar.findSmoothPath(area, targetX, targetY, findNearestPoint, collisionDetector)
        }
    }

    /**
     * Find a smooth path for an area to reach another area. It is possible to get the path to the nearest point if no path is found.
     */
    fun findSmoothPath(area: Rectangle, targetArea: Rectangle, findNearestPoint: Boolean, collisionDetector: CollisionDetector) : ArrayList<Vector2>? {
        val center = targetArea.getCenter(Vector2Pool.obtain())
        try {
            val pathToCenter = findSmoothPath(area, center.x, center.y, false, collisionDetector)
            if (pathToCenter != null && pathToCenter.size > 0) {
                return pathToCenter
            }
        } finally {
            Vector2Pool.free(center)
        }
        val position = area.getCenter(Vector2Pool.obtain())
        try {
            val edges = MathUtils.nearestEdges(position.x, position.y, targetArea)
            for ((ex, ey) in edges) {
                val pathToEdge = findSmoothPath(area, ex, ey, false, collisionDetector)
                if (pathToEdge != null && pathToEdge.size > 0)
                    return pathToEdge
            }
        } finally {
            Vector2Pool.free(position)
        }
        return if (findNearestPoint)
            findSmoothPath(area, center.x, center.y, findNearestPoint, collisionDetector)
        else
            arrayListOf()
    }
}
