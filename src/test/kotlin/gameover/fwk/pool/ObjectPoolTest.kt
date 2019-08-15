package gameover.fwk.pool

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import gameover.fwk.libgdx.utils.GdxArray
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ObjectPoolTest {

    @Nested
    inner class RectanglePool {
        private val pool = RectanglePool

        @Test
        fun `should obtain a reference to a rectangle`() {
            val r = pool.obtain()
            assertThat(r).isNotNull
        }

        @Test
        fun `should obtain a reference to a rectangle with values passed as float`() {
            val x = 1f
            val y = 2f
            val w = 5f
            val h = 6f
            val r = pool.obtain(x, y, w, h)
            assertThat(r).isNotNull
            assertThat(r).isEqualTo(Rectangle(x, y, w, h))
        }

        @Test
        fun `should obtain a reference to a rectangle with values passed as a rectangle`() {
            val original = Rectangle(1f, 2f, 5f, 6f)
            val r = pool.obtain(original)
            assertThat(r).isNotNull
            assertThat(r).isEqualTo(original)
            assertThat(r).isNotSameAs(original)
        }

        @Test
        fun `should free object in pool should return same object instead of creating a new one`() {
            pool.clear()
            val r = pool.obtain()
            pool.free(r)
            val r2 = pool.obtain(1f, 2f,3f,4f)
            assertThat(r2).isSameAs(r)
        }

        @Test
        fun `should get list of objects from pool`() {
            val rectangles: List<Rectangle> = pool.obtainAsList(2)
            assertThat(rectangles).hasSize(2)
            for (r in rectangles)
                assertThat(r).isNotNull
        }

        @Test
        fun `should get a gdx array of objects from pool`() {
            val rectangles: GdxArray<Rectangle> = pool.obtainAsGdxArray(2)
            assertThat(rectangles).hasSize(2)
            for (r in rectangles)
                assertThat(r).isNotNull
        }

        @Test
        fun `should get an array of objects from pool`() {
            val rectangles: Array<Rectangle> = pool.obtainAsArray(2)
            assertThat(rectangles).hasSize(2)
            for (r in rectangles)
                assertThat(r).isNotNull
        }

        @Test
        fun `should clear all instances in pool`() {
            pool.clear()
            val r = pool.obtain()
            pool.free(r)
            pool.clear()
            val r2 = pool.obtain()
            assertThat(r2).isNotSameAs(r)
        }

        @Test
        fun `should free list of objects in pool`() {
            pool.clear()
            val rectangles  = arrayListOf(pool.obtain(), pool.obtain())
            pool.free(rectangles)
        }

        @Test
        fun `should free gdx array of objects in pool`() {
            pool.clear()
            val rectangles  = GdxArray<Rectangle>()
            for (i in  0..2)
                rectangles.add(pool.obtain())
            pool.free(rectangles)
        }

        @Test
        fun `should free array of objects in pool`() {
            pool.clear()
            val rectangles  = Array(2) { pool.obtain() }
            pool.free(rectangles)
        }
    }

    @Nested
    inner class GridPoint2Pool {
        private val pool = GridPoint2Pool

        @Test
        fun `should obtain a reference to a gridpoint2d`() {
            val gp = pool.obtain()
            assertThat(gp).isNotNull
        }

        @Test
        fun `should obtain a reference to a GridPoint2 with values passed as int`() {
            val x = 1
            val y = 2
            val gp = pool.obtain(x, y)
            assertThat(gp).isNotNull
            assertThat(gp).isEqualTo(GridPoint2(x, y))
        }

        @Test
        fun `should obtain a reference to a GridPoint2 with values passed as another GridPoint2`() {
            val original = GridPoint2(1, 2)
            val gp = pool.obtain(original)
            assertThat(gp).isNotNull
            assertThat(gp).isEqualTo(original)
            assertThat(gp).isNotSameAs(original)
        }

        @Test
        fun `should free object in pool should return same object instead of creating a new one`() {
            pool.clear()
            val gp = pool.obtain()
            pool.free(gp)
            val gp2 = pool.obtain(1, 2)
            assertThat(gp2).isSameAs(gp)
        }

        @Test
        fun `should get list of objects from pool`() {
            val gridPoints2: List<GridPoint2> = pool.obtainAsList(2)
            assertThat(gridPoints2).hasSize(2)
            for (gp in gridPoints2)
                assertThat(gp).isNotNull
        }

        @Test
        fun `should get a gdx array of objects from pool`() {
            val gridPoints2: GdxArray<GridPoint2> = pool.obtainAsGdxArray(2)
            assertThat(gridPoints2).hasSize(2)
            for (gp in gridPoints2)
                assertThat(gp).isNotNull
        }

        @Test
        fun `should get an array of objects from pool`() {
            val gridPoints2: Array<GridPoint2> = pool.obtainAsArray(2)
            assertThat(gridPoints2).hasSize(2)
            for (gp in gridPoints2)
                assertThat(gp).isNotNull
        }

        @Test
        fun `should clear all instances in pool`() {
            pool.clear()
            val gp = pool.obtain()
            pool.free(gp)
            pool.clear()
            val gp2 = pool.obtain()
            assertThat(gp2).isNotSameAs(gp)
        }

        @Test
        fun `should free list of objects in pool`() {
            pool.clear()
            val gridPoints2  = arrayListOf(pool.obtain(), pool.obtain())
            pool.free(gridPoints2)
        }

        @Test
        fun `should free gdx array of objects in pool`() {
            pool.clear()
            val gridPoints2  = GdxArray<GridPoint2>()
            for (i in  0..2)
                gridPoints2.add(pool.obtain())
            pool.free(gridPoints2)
        }

        @Test
        fun `should free array of objects in pool`() {
            pool.clear()
            val gridPoints2  = Array(2) { pool.obtain() }
            pool.free(gridPoints2)
        }
    }

    @Nested
    inner class Vector2Pool {
        private val pool = Vector2Pool

        @Test
        fun `should obtain a reference to a Vector2`() {
            val v = pool.obtain()
            assertThat(v).isNotNull
        }

        @Test
        fun `should obtain a reference to a Vector2 with values passed as float`() {
            val x = 1f
            val y = 2f
            val v = pool.obtain(x, y)
            assertThat(v).isNotNull
            assertThat(v).isEqualTo(Vector2(x, y))
        }

        @Test
        fun `should obtain a reference to a Vector2 with values passed as a Vector2`() {
            val original = Vector2(1f, 2f)
            val v = pool.obtain(original)
            assertThat(v).isNotNull
            assertThat(v).isEqualTo(original)
            assertThat(v).isNotSameAs(original)
        }

        @Test
        fun `should free object in pool should return same object instead of creating a new one`() {
            pool.clear()
            val v = pool.obtain()
            pool.free(v)
            val v2 = pool.obtain(1f, 2f)
            assertThat(v2).isSameAs(v)
        }

        @Test
        fun `should get list of objects from pool`() {
            val vectors2: List<Vector2> = pool.obtainAsList(2)
            assertThat(vectors2).hasSize(2)
            for (v in vectors2)
                assertThat(v).isNotNull
        }

        @Test
        fun `should get a gdx array of objects from pool`() {
            val vectors2: GdxArray<Vector2> = pool.obtainAsGdxArray(2)
            assertThat(vectors2).hasSize(2)
            for (v in vectors2)
                assertThat(v).isNotNull
        }

        @Test
        fun `should get an array of objects from pool`() {
            val vectors2: Array<Vector2> = pool.obtainAsArray(2)
            assertThat(vectors2).hasSize(2)
            for (v in vectors2)
                assertThat(v).isNotNull
        }

        @Test
        fun `should clear all instances in pool`() {
            pool.clear()
            val v = pool.obtain()
            pool.free(v)
            pool.clear()
            val v2 = pool.obtain()
            assertThat(v2).isNotSameAs(v)
        }

        @Test
        fun `should free list of objects in pool`() {
            pool.clear()
            val vectors2  = arrayListOf(pool.obtain(), pool.obtain())
            pool.free(vectors2)
        }

        @Test
        fun `should free gdx array of objects in pool`() {
            pool.clear()
            val vectors2  = GdxArray<Vector2>()
            for (i in  0..2)
                vectors2.add(pool.obtain())
            pool.free(vectors2)
        }

        @Test
        fun `should free array of objects in pool`() {
            pool.clear()
            val vectors2  = Array(2) { pool.obtain() }
            pool.free(vectors2)
        }
    }
}