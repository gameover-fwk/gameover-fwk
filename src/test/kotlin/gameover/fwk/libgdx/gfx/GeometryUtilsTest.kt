package gameover.fwk.libgdx.gfx

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GeometryUtilsTest {

    @Test
    fun `returns no intersection for two points in the same tile`(){
        assert(GeometryUtils.computeTiledIntersectionPoints(1.3f, 1.2f, 1.5f, 1.7f).size == 0)
        assert(GeometryUtils.computeTiledIntersectionPoints(4.1169744f, 2.539641f, 4.119716f, 2.1467845f).size == 0)
    }

    @Test
    fun `returns one intersection for two points in two tiles adjacents`(){
        assert(GeometryUtils.computeTiledIntersectionPoints(1.3f, 1.2f, 1.5f, 2.7f).size == 1)
        assert(GeometryUtils.computeTiledIntersectionPoints(1.3f, 1.2f, 2.5f, 1.7f).size == 1)
    }

    @Test
    fun `returns no intersection for two points in the tile border`(){
        assert(GeometryUtils.computeTiledIntersectionPoints(1f, 1.2f, 1f, 1.7f).size == 0)
        assert(GeometryUtils.computeTiledIntersectionPoints(1.2f, 1f, 1.5f, 1f).size == 0)
    }

    @Test
    fun `returns two intersections for two points in two tiles separated by one tile`(){
        assert(GeometryUtils.computeTiledIntersectionPoints(1.3f, 3.2f, 1.5f, 1.7f).size == 2)
        assert(GeometryUtils.computeTiledIntersectionPoints(3.3f, 1.2f, 1.5f, 1.7f).size == 2)
    }

    @Test
    fun `show return three intersections for two points in two tiles separated by one tile`(){
        assert(GeometryUtils.computeTiledIntersectionPoints(1.3f, 3.2f, 1.5f, 1.7f).size == 2)
        assert(GeometryUtils.computeTiledIntersectionPoints(3.3f, 1.2f, 1.5f, 1.7f).size == 2)
    }
}