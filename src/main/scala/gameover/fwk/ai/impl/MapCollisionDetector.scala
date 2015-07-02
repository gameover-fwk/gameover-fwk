package gameover.fwk.ai.impl

import com.badlogic.gdx.maps.{MapLayer, MapLayers}
import com.badlogic.gdx.maps.tiled.{TiledMap, TiledMapTileLayer}
import com.badlogic.gdx.math.{Intersector, Vector2, Rectangle}
import com.badlogic.gdx.utils.Array
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
        analyseLayer(layer, State.BLOCKING)
      if ("yes" == layer.getProperties.get("fall"))
        analyseLayer(layer, State.VOID)
    }
  }

  def analyseLayer(mapLayer: MapLayer, state: State.Value) {
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

  def checkCollision(r: Rectangle, onlyBlocking: Boolean): Boolean = {
    if (r.x < 0 || r.x > width || r.y < 0 || r.y > height) {
      return true
    } else {
      for (i <- 0 to collisionTiles.size-1) {
        val cs = collisionTiles.get(i)
        if (r.overlaps(cs.r) && (!onlyBlocking || (cs.state eq State.BLOCKING)))
          return true
      }
      return false
    }
  }

  override def checkCollision(tileX: Int, tileY: Int, onlyBlocking: Boolean): Boolean = {
    if (tileX < 0 || tileX > width || tileY < 0 || tileY > height) {
      return true
    } else {
      for (i <- 0 to collisionTiles.size-1) {
        val cs = collisionTiles.get(i)
        if (cs.r.x.toInt == tileX && cs.r.y.toInt == tileY && (!onlyBlocking || (cs.state eq State.BLOCKING)))
          return true
      }
      return false
    }
  }

  private def checkCollision(collisionArea: Rectangle, movingSpriteVelocity: Vector2, onlyBlocking: Boolean): GdxArray[Rectangle] = {
    val ret: Array[Rectangle] = new GdxArray[Rectangle]
    val c1: Vector2 = Vector2Pool.obtain(collisionArea.x, collisionArea.y)
    val c2: Vector2 = Vector2Pool.obtain(collisionArea.x + collisionArea.width, collisionArea.y)
    val c3: Vector2 = Vector2Pool.obtain(collisionArea.x, collisionArea.y + collisionArea.height)
    val c4: Vector2 = Vector2Pool.obtain(collisionArea.x + collisionArea.width, collisionArea.y + collisionArea.height)
    val v1: Vector2 = Vector2Pool.obtain
    val v2: Vector2 = Vector2Pool.obtain
    val v3: Vector2 = Vector2Pool.obtain
    val v4: Vector2 = Vector2Pool.obtain
    if (movingSpriteVelocity.x != 0 || movingSpriteVelocity.y != 0) {
      {
        for (i <- 0 to collisionTiles.size-1) {
          val cs = collisionTiles.get(i)
          val tile: Rectangle = cs.r
          if (collisionArea.overlaps(tile) && (!onlyBlocking || (cs.state eq State.BLOCKING))) {
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
    }
    Vector2Pool.free(v1)
    Vector2Pool.free(v2)
    Vector2Pool.free(v3)
    Vector2Pool.free(v4)
    ret
  }
}

private class CollisionState (val r: Rectangle, val state: State.Value)

private object State extends Enumeration {
  type State = Value
  val BLOCKING, VOID = Value
}

