package gameover.fwk.ai.impl

import com.badlogic.gdx.maps.tiled.{TiledMap, TiledMapTileLayer}
import com.badlogic.gdx.maps.{MapLayer, MapLayers}
import com.badlogic.gdx.math.{Intersector, Rectangle, Vector2}
import gameover.fwk.libgdx.GdxArray
import gameover.fwk.libgdx.gfx.GeometryUtils
import gameover.fwk.pool.{RectanglePool, Vector2Pool}

class MapCollisionDetector extends BasicCollisionDetector {

  private val collisionTiles = new GdxArray[CollisionState]

  private var width: Int = 0
  private var height: Int = 0


  def clear() {
    for (cs <- collisionTiles)
      RectanglePool.free(cs.r)
    collisionTiles.clear()
  }

  def loadCollisionFromMap(map: TiledMap) {
    val layers: MapLayers = map.getLayers
    layers.get(0) match {
      case layer: TiledMapTileLayer =>
        width = layer.getWidth
        height = layer.getHeight
      case _ =>
    }
    for (i <- 0 to layers.getCount-1) {
      val layer: MapLayer = layers.get(i)
      if ("yes" == layer.getProperties.get("collision"))
        analyseLayer(layer, CollisionState.Blocking)
      if ("yes" == layer.getProperties.get("fall"))
        analyseLayer(layer, CollisionState.Void)
    }
  }

  def analyseLayer(mapLayer: MapLayer, state: CollisionState.Value) {
    mapLayer match {
      case layer: TiledMapTileLayer =>
        for (y <- 0 to layer.getHeight - 1) {
          for (x <- 0 to layer.getWidth - 1) {
            val cell: TiledMapTileLayer.Cell = layer.getCell(x, y)
            if (cell != null) {
              val rect: Rectangle = RectanglePool.obtain(x, y, 1, 1)
              collisionTiles.add(new CollisionState(rect, state))
            }
          }
        }
      case _ => throw new IllegalArgumentException("Can't analyse class " + mapLayer.getClass + ". This class is not managed currently by the code")
    }
  }

  override def checkPosition(area: Rectangle) : CollisionState.Value = {
    val centerAndEdges = GeometryUtils.centerAndEdges(area)
    try {
      val collisionStates: List[CollisionState.Value] = centerAndEdges.map((v: Vector2) => checkPosition(v.x, v.y))
      if (collisionStates.contains(CollisionState.Blocking)) {
        CollisionState.Blocking
      } else if (collisionStates.forall((cs: CollisionState.Value) => cs == CollisionState.Void)) {
        CollisionState.Void
      } else {
        CollisionState.Empty
      }
    } finally {
      Vector2Pool.free(centerAndEdges)
    }
  }

  override def checkPosition(x: Float, y: Float) : CollisionState.Value = {
    val tileX = x.ceil.toInt
    val tileY = y.ceil.toInt
    for (i <- collisionTiles.indices) {
      val cs = collisionTiles.get(i)
      if (cs.r.x.toInt == tileX && cs.r.y.toInt == tileY)
        return cs.state
    }
    CollisionState.Empty
  }

  override def checkCollision(area: Rectangle, movingSpriteVelocity: Vector2, onlyBlocking: Boolean): GdxArray[Rectangle] = {
    val ret = GdxArray[Rectangle]()
    val c1 = Vector2Pool.obtain(area.x, area.y)
    val c2 = Vector2Pool.obtain(area.x + area.width, area.y)
    val c3 = Vector2Pool.obtain(area.x, area.y + area.height)
    val c4 = Vector2Pool.obtain(area.x + area.width, area.y + area.height)
    val v1 = Vector2Pool.obtain()
    val v2 = Vector2Pool.obtain()
    val v3 = Vector2Pool.obtain()
    val v4 = Vector2Pool.obtain()
    if (movingSpriteVelocity.x != 0 || movingSpriteVelocity.y != 0) {
      for (i <- collisionTiles.indices) {
        val cs = collisionTiles.get(i)
        val tile: Rectangle = cs.r
        if (area.overlaps(tile) && (!onlyBlocking || (cs.state eq CollisionState.Blocking))) {
          v1.set(tile.x, tile.y)
          v2.set(tile.x + tile.width, tile.y)
          v3.set(tile.x, tile.y + tile.height)
          v4.set(tile.x + tile.width, tile.y + tile.height)
          if (movingSpriteVelocity.y > 0f && (Intersector.intersectSegments(v1, v2, c1, c3, null) || Intersector.intersectSegments(v1, v2, c2, c4, null))) {
            movingSpriteVelocity.y = 0f
            ret.add(RectanglePool.obtain(v1, v2))
          }
          if (movingSpriteVelocity.y < 0f && (Intersector.intersectSegments(v3, v4, c1, c3, null) || Intersector.intersectSegments(v3, v4, c2, c4, null))) {
            movingSpriteVelocity.y = 0f
            ret.add(RectanglePool.obtain(v3, v4))
          }
          if (movingSpriteVelocity.x > 0f && (Intersector.intersectSegments(v1, v3, c1, c2, null) || Intersector.intersectSegments(v1, v3, c3, c4, null))) {
            movingSpriteVelocity.x = 0f
            ret.add(RectanglePool.obtain(v1, v3))
          }
          if (movingSpriteVelocity.x < 0f && (Intersector.intersectSegments(v2, v4, c1, c2, null) || Intersector.intersectSegments(v2, v4, c3, c4, null))) {
            movingSpriteVelocity.x = 0f
            ret.add(RectanglePool.obtain(v2, v4))
          }
        }
      }
    }
    Vector2Pool.free(v1)
    Vector2Pool.free(v2)
    Vector2Pool.free(v3)
    Vector2Pool.free(v4)
    ret
  }
}

private class CollisionState (val r: Rectangle, val state: CollisionState.Value)

object CollisionState extends Enumeration {
  val Blocking, Void, Empty = Value
}

