package gameover.fwk.pool

import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import gameover.fwk.libgdx.utils.GdxArray

import java.util.*

abstract class ObjectPool<T> {

    abstract fun instantiateObject(): T

    private val pool = object: Pool<T>() {
        override fun newObject(): T { return instantiateObject() }
    }

    /**
     * Clear all objects in pool.
     */
    fun clear() {
        pool.clear()
    }

    fun free(a: Array<T>) {
        for (e in 0..(a.size - 1))
            free(a.get(e))
    }

    fun free(a: GdxArray<T>) {
        for (e in 0..(a.size - 1))
            free(a.get(e))
    }

    fun free(l: List<T>) {
        for (e in l)
            free(e)
    }

    fun free(t: T) {
        pool.free(t)
    }

    fun obtain(): T = pool.obtain()

    abstract fun obtainAsArray(nb: Int) : Array<T>

    fun obtainAsGdxArray(nb: Int): GdxArray<T> {
        val a = GdxArray<T>(nb)
        for (i in 1..nb)
            a.add(obtain())
        return a
    }

    fun obtainAsList(nb: Int): ArrayList<T> {
        val a = ArrayList<T>(nb)
        for (i in 1..nb)
            a.add(obtain())
        return a
    }
}

object GridPoint2Pool : ObjectPool<GridPoint2>() {

    override fun instantiateObject() : GridPoint2 = GridPoint2()

    fun obtain(o: GridPoint2): GridPoint2 = obtain().set(o)

    fun obtain(x: Int, y: Int): GridPoint2 = obtain().set(x, y)

    override fun obtainAsArray(nb: Int) : Array<GridPoint2> = Array(nb) {
        obtain()
    }
}

object RectanglePool : ObjectPool<Rectangle>() {

    override fun instantiateObject(): Rectangle = Rectangle()

    fun obtain(rectangle: Rectangle): Rectangle = obtain().set(rectangle)

    fun obtain(v1: Vector2, v2: Vector2): Rectangle = obtain().set(v1.x, v1.y, v2.x, v2.y)

    fun obtain(x1: Float, y1: Float, w: Float, h: Float): Rectangle = obtain().set(x1, y1, w, h)

    override fun obtainAsArray(nb: Int) : Array<Rectangle> = Array(nb) {
        obtain()
    }
}

object Vector2Pool : ObjectPool<Vector2>() {

    override fun instantiateObject(): Vector2 = Vector2()

    fun obtain(o: Vector2): Vector2 = obtain().set(o)

    fun obtain(x: Float, y: Float): Vector2 = obtain().set(x, y)

    override fun obtainAsArray(nb: Int) : Array<Vector2> = Array(nb) {
        obtain()
    }
}