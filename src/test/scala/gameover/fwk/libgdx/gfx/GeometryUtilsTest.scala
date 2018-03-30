package gameover.fwk.libgdx.gfx

import org.scalatest.FlatSpec

class GeometryUtilsTest extends FlatSpec {

  "GeometryUtilsTest" should "show return no intersections for two points in the same tile" in {
    assert(GeometryUtils.computeTiledIntersectionPoints(1.3f, 1.2f, 1.5f, 1.7f).size == 0)
    assert(GeometryUtils.computeTiledIntersectionPoints(4.1169744f, 2.539641f, 4.119716f, 2.1467845f).size == 0)
  }

  it should "show return one intersection for two points in two tiles adjacents" in {
    assert(GeometryUtils.computeTiledIntersectionPoints(1.3f, 1.2f, 1.5f, 2.7f).size == 1)
    assert(GeometryUtils.computeTiledIntersectionPoints(1.3f, 1.2f, 2.5f, 1.7f).size == 1)
  }

  it should "show returns no intersection for two points in the tile border " in {
    assert(GeometryUtils.computeTiledIntersectionPoints(1f, 1.2f, 1f, 1.7f).size == 0)
    assert(GeometryUtils.computeTiledIntersectionPoints(1.2f, 1f, 1.5f, 1f).size == 0)
  }

  it should "show return two intersections for two points in two tiles separated by one tile" in {
    assert(GeometryUtils.computeTiledIntersectionPoints(1.3f, 3.2f, 1.5f, 1.7f).size == 2)
    assert(GeometryUtils.computeTiledIntersectionPoints(3.3f, 1.2f, 1.5f, 1.7f).size == 2)
  }

  it should "show return three intersections for two points in two tiles separated by one tile" in {
    assert(GeometryUtils.computeTiledIntersectionPoints(1.3f, 3.2f, 1.5f, 1.7f).size == 2)
    assert(GeometryUtils.computeTiledIntersectionPoints(3.3f, 1.2f, 1.5f, 1.7f).size == 2)
  }

}
