package gameover.fwk.ai.impl

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import gameover.fwk.ai.CollisionDetector
import gameover.fwk.libgdx.gfx.GeometryUtils
import gameover.fwk.math.MathUtils
import gameover.fwk.pool.Vector2Pool

abstract class BasicCollisionDetector : CollisionDetector {

    override fun hasDirectView(visionArea: Rectangle, targetArea: Rectangle): Boolean {
        val centerVisionArea = visionArea.getCenter(Vector2Pool.obtain())
        val x = centerVisionArea.x
        val y = centerVisionArea.y
        Vector2Pool.free(centerVisionArea)
        val centerTargetArea = targetArea.getCenter(Vector2Pool.obtain())
        val tx = centerTargetArea.x
        val ty = centerTargetArea.y
        Vector2Pool.free(centerVisionArea)
        val dirs = MathUtils.directions(x, y, tx, ty)

        val xys = ArrayList<Pair<Float, Float>>()
        val txys = ArrayList<Pair<Float, Float>>()
        if (dirs.top) {
            when {
                dirs.right -> {
                    xys.add(Pair(visionArea.x + visionArea.width, visionArea.y))
                    xys.add(Pair(visionArea.x, visionArea.y + visionArea.height))
                    txys.add(Pair(targetArea.x + targetArea.width, targetArea.y))
                    txys.add(Pair(targetArea.x, targetArea.y + targetArea.height))
                }
                dirs.left -> {
                    xys.add(Pair(visionArea.x, visionArea.y))
                    xys.add(Pair(visionArea.x + visionArea.width, visionArea.y + visionArea.height))
                    txys.add(Pair(targetArea.x, targetArea.y))
                    txys.add(Pair(targetArea.x + targetArea.width, targetArea.y + targetArea.height))
                }
                else -> {
                    xys.add(Pair(visionArea.x, visionArea.y + visionArea.height))
                    xys.add(Pair(visionArea.x + visionArea.width, visionArea.y + visionArea.height))
                    txys.add(Pair(targetArea.x, targetArea.y))
                    txys.add(Pair(targetArea.x + targetArea.width, targetArea.y))
                }
            }
        } else if (dirs.bottom) {
            when {
                dirs.right -> {
                    xys.add(Pair(visionArea.x, visionArea.y))
                    xys.add(Pair(visionArea.x + visionArea.width, visionArea.y + visionArea.height))
                    txys.add(Pair(targetArea.x, targetArea.y))
                    txys.add(Pair(targetArea.x + targetArea.width, targetArea.y + targetArea.height))
                }
                dirs.left -> {
                    xys.add(Pair(visionArea.x, visionArea.y + visionArea.height))
                    xys.add(Pair(visionArea.x + visionArea.width, visionArea.y))
                    txys.add(Pair(targetArea.x, targetArea.y + targetArea.height))
                    txys.add(Pair(targetArea.x + targetArea.width, targetArea.y))
                }
                else -> {
                    xys.add(Pair(visionArea.x, visionArea.y))
                    xys.add(Pair(visionArea.x + visionArea.width, visionArea.y))
                    txys.add(Pair(targetArea.x, targetArea.y + targetArea.height))
                    txys.add(Pair(targetArea.x + targetArea.width, targetArea.y + targetArea.height))
                }
            }
        } else {
            if (dirs.right) {
                xys.add(Pair(visionArea.x + visionArea.width, visionArea.y))
                xys.add(Pair(visionArea.x + visionArea.width, visionArea.y + visionArea.height))
                txys.add(Pair(targetArea.x, targetArea.y))
                txys.add(Pair(targetArea.x, targetArea.y + targetArea.height))
            } else if (dirs.left) {
                xys.add(Pair(visionArea.x, visionArea.y))
                xys.add(Pair(visionArea.x, visionArea.y + visionArea.height))
                txys.add(Pair(targetArea.x + targetArea.width, targetArea.y))
                txys.add(Pair(targetArea.x + targetArea.width, targetArea.y + targetArea.height))
            }
        }
        for (xy in xys) {
            for (txy in txys) {
                if (hasDirectView(xy.first, xy.second, txy.first, txy.second))
                    return true
            }
        }
        return false
    }

    override fun hasDirectView(visionArea: Rectangle, targetX: Float, targetY: Float) : Boolean {
        val centerVisionArea = visionArea.getCenter(Vector2Pool.obtain())
        val x = centerVisionArea.x
        val y = centerVisionArea.y
        Vector2Pool.free(centerVisionArea)
        val xys = ArrayList<Pair<Float, Float>>()
        val dirs = MathUtils.directions(x, y, targetX, targetY)
        if (dirs.top) {
            when {
                dirs.right -> {
                    xys.add(Pair(visionArea.x + visionArea.width, visionArea.y))
                    xys.add(Pair(visionArea.x, visionArea.y + visionArea.height))
                }
                dirs.left -> {
                    xys.add(Pair(visionArea.x, visionArea.y))
                    xys.add(Pair(visionArea.x + visionArea.width, visionArea.y + visionArea.height))
                }
                else -> {
                    xys.add(Pair(visionArea.x, visionArea.y + visionArea.height))
                    xys.add(Pair(visionArea.x + visionArea.width, visionArea.y + visionArea.height))
                }
            }
        } else if (dirs.bottom) {
            when {
                dirs.right -> {
                    xys.add(Pair(visionArea.x, visionArea.y))
                    xys.add(Pair(visionArea.x + visionArea.width, visionArea.y + visionArea.height))
                }
                dirs.left -> {
                    xys.add(Pair(visionArea.x, visionArea.y + visionArea.height))
                    xys.add(Pair(visionArea.x + visionArea.width, visionArea.y))
                }
                else -> {
                    xys.add(Pair(visionArea.x, visionArea.y))
                    xys.add(Pair(visionArea.x + visionArea.width, visionArea.y))
                }
            }
        } else {
            if (dirs.right) {
                xys.add(Pair(visionArea.x + visionArea.width, visionArea.y))
                xys.add(Pair(visionArea.x + visionArea.width, visionArea.y + visionArea.height))
            } else if (dirs.left) {
                xys.add(Pair(visionArea.x, visionArea.y))
                xys.add(Pair(visionArea.x, visionArea.y + visionArea.height))
            }
        }
        for (xy in xys) {
            if (hasDirectView(xy.first, xy.second, targetX, targetY))
                return true
        }
        return false
    }

    override fun hasDirectView(x: Float, y: Float, targetX: Float, targetY: Float) : Boolean = !hasCollisions(x, y, targetX, targetY, true)


    fun hasDirectPath(x: Float, y: Float, tx: Float, ty: Float) : Boolean = !hasCollisions(x, y, tx, ty, false)

    fun hasCollisions(x: Float, y: Float, tx: Float, ty: Float, onlyBlocking: Boolean) : Boolean {
        val intersectionPoints = GeometryUtils.computeTiledIntersectionPoints(x, y, tx, ty)
        try {
            return checkCollisions(intersectionPoints, onlyBlocking)
        } finally {
            Vector2Pool.free(intersectionPoints)
        }
    }

    override fun canMoveStraightToPoint(area: Rectangle, targetX: Float, targetY: Float): Boolean {
        val ret = if (area.contains(targetX, targetY))
            true
        else {
            val centerArea = area.getCenter(Vector2Pool.obtain())
            val cx = centerArea.x
            val cy = centerArea.y
            Vector2Pool.free(centerArea)
            val dirs = MathUtils.directions(cx, cy, targetX, targetY)
            val x = area.x
            val y = area.y
            val w = area.width
            val hw = area.width / 2
            val h = area.height
            val hh = area.height / 2
            when {
                dirs.top -> when {
                    dirs.right -> hasDirectPath(x, y + h, targetX - w, targetY) &&
                            hasDirectPath(centerArea.x + w, centerArea.y, targetX, targetY - w)
                    dirs.left -> hasDirectPath(centerArea.x, centerArea.y, targetX, targetY - h) &&
                            hasDirectPath(centerArea.x + w, centerArea.y + h, targetX + w, targetY)
                    else -> hasDirectPath(x, y + h, targetX - hw, targetY - h) &&
                            hasDirectPath(x + w, y + h, targetX + hw, targetY - h)
                }
                dirs.bottom -> when {
                    dirs.right -> hasDirectPath(x, y, targetX - w, targetY) &&
                            hasDirectPath(x + w, y + h, targetX, targetY + h)
                    dirs.left -> hasDirectPath(x, y + h, targetX, targetY + h) &&
                            hasDirectPath(x + w, y, targetX + w, targetY)
                    else -> hasDirectPath(x, y, targetX - hw , targetY + h) &&
                            hasDirectPath(x + w, y, targetX + hw, targetY + h)
                }
                else -> when {
                    dirs.right -> hasDirectPath(x + w, y, targetX - w , targetY - hh) &&
                            hasDirectPath(x + w, y + h, targetX - w, targetY + hh)
                    dirs.left -> hasDirectPath(x , y, targetX + w, targetY - hh) &&
                            hasDirectPath(x, y + h, targetX + w, targetY + hh)
                    else -> true
                }
            }
        }
        return ret
    }

    private fun checkCollisions(intersections: List<Vector2>, onlyBlocking: Boolean): Boolean {
        for (intersection in intersections) {
            if (MathUtils.floor(intersection.x) == intersection.x) {
                if (checkCollision(intersection.x, MathUtils.floor(intersection.y), onlyBlocking) ||
                        checkCollision(intersection.x - 1, MathUtils.floor(intersection.y), onlyBlocking)) {
                    return true
                }
            }
            if (MathUtils.floor(intersection.y) == intersection.y) {
                if (checkCollision(MathUtils.floor(intersection.x), intersection.y, onlyBlocking) ||
                        checkCollision(MathUtils.floor(intersection.x), intersection.y - 1, onlyBlocking)) {
                    return true
                }
            }
        }
        return false
    }


}
