package gameover.fwk.libgdx.gfx

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import gameover.fwk.math.MathUtils
import gameover.fwk.pool.Vector2Pool


object GeometryUtils {

    fun computeTiledIntersectionPoints(x1: Float, y1: Float, x2: Float, y2: Float): List<Vector2> {
        val ret = arrayListOf<Vector2>()
        val diffX = x2 - x1
        val fX1 = MathUtils.floor(x1)
        val diffY = y2 - y1
        val fY1 = MathUtils.floor(y1)
        val alpha = com.badlogic.gdx.math.MathUtils.atan2(diffY, diffX)
        val angle = Math.abs(alpha % (Math.PI / 2.0))
        val inverseAxis = alpha > Math.PI / 2.0 || alpha < -Math.PI / 2.0
        val alphaTan = Math.tan(angle).toFloat()
        val xSign: Float = Math.signum(diffX)
        val ySign: Float = Math.signum(diffY)
        val xs = arrayListOf<Float>()
        val ys = arrayListOf<Float>()
        var finished = false
        while (!finished) {
            val x: Float = if (xSign < 0f) fX1 - (if (fX1 == x1) 1 else 0) - xs.size else fX1 + 1 + xs.size
            if ((xSign > 0 && x >= x2) || (xSign < 0 && x <= x2) || xSign == 0f)
                finished = true
            else
                xs.add(x)
        }
        finished = false
        while (!finished) {
            val y: Float = if (ySign < 0f) fY1 - (if (fY1 == y1) 1 else 0) - ys.size else fY1 + 1 + ys.size
            if ((ySign > 0 && y >= y2) || (ySign < 0 && y <= y2) || ySign == 0f)
                finished = true
            else
                ys.add(y)
        }
        for (x in xs) {
            if (!inverseAxis)
                ret.add(Vector2Pool.obtain(x, computeYAux(x1, y1, x, ySign, alphaTan)))
            else
                ret.add(Vector2Pool.obtain(x, computeXAux(y1, x1, x, ySign, alphaTan)))
        }
        for (y in ys) {
            if (!inverseAxis)
                ret.add(Vector2Pool.obtain(computeXAux(x1, y1, y, xSign, alphaTan), y))
            else
                ret.add(Vector2Pool.obtain(computeYAux(y1, x1, y, xSign, alphaTan), y))
        }
        ret.sortWith(Function2DPointComparator(xSign, ySign))
        var i = ret.size - 2
        while ((i >= 0) && (i + 1 < ret.size)) {
            if (ret.get(i).equals(ret.get(i + 1)))
                ret.removeAt(i)
            else
                i -= 1
        }
        return ret
    }

    fun computeYAux(x1: Float, y1: Float, x2: Float, ySign: Float, alphaTan: Float) = y1 + ySign * (alphaTan * Math.abs(x2 - x1))

    fun computeXAux(x1: Float, y1: Float, y2: Float, xSign: Float, alphaTan: Float) = x1 + (if (alphaTan != 0f) xSign * (Math.abs(y2 - y1) / alphaTan) else 0f)

    fun centerAndEdges(area: Rectangle): List<Vector2> = arrayListOf(
        area.getCenter(Vector2Pool.obtain()),
        area.getPosition(Vector2Pool.obtain()),
        area.getPosition(Vector2Pool.obtain()).add(area.width, 0f),
        area.getPosition(Vector2Pool.obtain()).add(0f, area.height),
        area.getPosition(Vector2Pool.obtain()).add(area.width, area.height))
}


class Function2DPointComparator(val xSign: Float, val ySign: Float) : Comparator<Vector2> {

    override fun compare(o1: Vector2, o2: Vector2): Int {
        val signum: Float = Math.signum(o1.x - o2.x)
        return if (signum == 0f) {
            (Math.signum(o1.x - o2.x) * ySign).toInt()
        }
        else {
            (signum * xSign).toInt()
        }
    }
}
