package gameover.fwk.libgdx.gfx

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.{MapLayer, MapLayers}
import com.badlogic.gdx.maps.tiled.{TiledMap, TiledMapTileLayer}
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile

import scala.collection.mutable.ListBuffer

/**
  * This tiled map renderer can be used to produce a perspective effect.
  * The rendering is separated in 3 methods:
  * `#renderFloorLayers()`
  * `#renderWallsLayers()`.
  * `#renderTopLayers()`
  * In order to differentiate at which level the layer is drawn, the renderer is looking to
  * property `level` which can be set to `walls` or to `top`.
  * By default, a layer is rendered at the floor layer.
  * The old method render will call the 3 methods in a row to have the old behaviour.
 */
class PerspectiveTiledMapRenderer(m: TiledMap, unitScale: Float, spriteBatch: SpriteBatch, wallsLayerLevelName: String = "walls", topLayerLevelName: String = "top")
  extends OrthogonalTiledMapRenderer(m, unitScale, spriteBatch) {

  def filterLayers(layers: MapLayers, levelName: String) : List[MapLayer] = {
    val ret = new ListBuffer[MapLayer]()
    val it = layers.iterator()
    while (it.hasNext) {
      val layer = it.next()
      Option(layer.getProperties.get("level", classOf[String])) match {
        case Some(v) if v == levelName => ret += layer
        case _ =>
      }
    }
    ret.toList
  }

  private val wallsLayers = filterLayers(m.getLayers, wallsLayerLevelName)
  private val topLayers = filterLayers(m.getLayers, topLayerLevelName)

  override def render(): Unit = {
    renderFloorLayers()
    renderWallsLayers()
    renderTopLayers()
  }

  def renderFloorLayers(): Unit = {
    AnimatedTiledMapTile.updateAnimationBaseTime()
    val layers = map.getLayers
    batch.begin()
    for (i <- 0 until layers.getCount) {
      val layer = layers.get(i)
      if ((!wallsLayers.contains(layer)) && (!topLayers.contains(layer)) && layer.isVisible) {
        layer match {
          case layer: TiledMapTileLayer => renderTileLayer(layer.asInstanceOf[TiledMapTileLayer])
          case _ =>
            for (i <- 0 until layer.getObjects.getCount) {
              val o = layer.getObjects.get(i)
              renderObject(o)
            }
        }
      }
    }
    batch.end()
  }

  def renderWallsLayers(): Unit = {
    wallsLayers.foreach(renderSuppLayerTop(_))
  }

  def renderTopLayers(): Unit = {
    topLayers.foreach(renderSuppLayerTop(_))
  }

  private def renderSuppLayerTop(layer: MapLayer): Unit = {
    batch.begin()
    layer match {
      case l: TiledMapTileLayer =>  renderTileLayer(l)
      case _                        =>
        for (i <- 0 until layer.getObjects.getCount) {
          val o = layer.getObjects.get(i)
          renderObject(o)
        }
    }
    batch.end()
  }
}
