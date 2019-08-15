package gameover.fwk.libgdx.gfx

import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import gameover.fwk.libgdx.utils.GdxArray
import gameover.fwk.pool.Vector2Pool

object Polygons {

    fun createArcAsPolygon(x: Float, y: Float, radius: Float, start: Float, angle: Float, segments: Int): Polygon {
        if (segments < 1)
            throw IllegalArgumentException("arc need at least 1 segment")
        val theta: Float = (2 * 3.1415926f * (angle / 360.0f)) / segments
        val cos: Float = com.badlogic.gdx.math.MathUtils.cos(theta)
        val sin: Float = com.badlogic.gdx.math.MathUtils.sin(theta)
        var cx: Float = radius * com.badlogic.gdx.math.MathUtils.cos(start * com.badlogic.gdx.math.MathUtils.degreesToRadians)
        var cy: Float = radius * com.badlogic.gdx.math.MathUtils.sin(start * com.badlogic.gdx.math.MathUtils.degreesToRadians)
        val vertices = FloatArray(segments * 2 + 2)
        vertices[vertices.size - 2] = x
        vertices[vertices.size - 1] = y
        for (i in 0 until segments) {
            val temp: Float = cx
            cx = cos * cx - sin * cy
            cy = sin * temp + cos * cy
            vertices[i * 2] = x + cx
            vertices[i * 2 + 1] = y + cy
        }
        return Polygon(vertices)
    }

    fun isPointInPolygon(polygon: GdxArray<Vector2>, pointX: Float, pointY: Float): Boolean {
        val lastVertice: Vector2 = polygon.get(polygon.size - 1)
        var lastVerticeX = lastVertice.x
        var lastVerticeY = lastVertice.y
        var oddNodes = false
        for (i in 0 until polygon.size) {
            val v = polygon.get(i)
            val x = v.x
            val y = v.y
            if (y < pointY && lastVerticeY >= pointY || lastVerticeY < pointY && y >= pointY) {
                if (x + (pointY - y) / (lastVerticeY - y) * (lastVerticeX - x) < pointX) {
                    oddNodes = !oddNodes
                }
            }
            lastVerticeX = x
            lastVerticeY = y
        }
        return oddNodes
    }

    fun createArcAsListOfVertices(x: Float, y: Float, radius: Float, start: Float, angle: Float, segments: Int, fromPool: Boolean): GdxArray<Vector2> {
        if (segments < 1)
            throw IllegalArgumentException("arc need at least 1 segment")
        val theta = (2 * 3.1415926f * (angle / 360.0f)) / segments
        val cos = com.badlogic.gdx.math.MathUtils.cos(theta)
        val sin = com.badlogic.gdx.math.MathUtils.sin(theta)
        var cx = radius * com.badlogic.gdx.math.MathUtils.cos(start * com.badlogic.gdx.math.MathUtils.degreesToRadians)
        var cy = radius * com.badlogic.gdx.math.MathUtils.sin(start * com.badlogic.gdx.math.MathUtils.degreesToRadians)
        val ret = if (fromPool) {
            Vector2Pool.obtainAsGdxArray(segments + 1)
        } else {
            val arr = GdxArray<Vector2>(segments + 1)
            for (i in 0..segments) {
                arr[i] = Vector2()
            }
            arr
        }
        for (i in 0..segments) {
            val temp: Float = cx
            cx = cos * cx - sin * cy
            cy = sin * temp + cos * cy
            ret.get(i).set(x + cx, y + cy)
        }
        ret.get(segments).set(x, y)
        return ret
    }
}