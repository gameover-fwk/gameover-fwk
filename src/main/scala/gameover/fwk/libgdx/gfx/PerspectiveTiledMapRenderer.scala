package gameover.fwk.libgdx.gfx

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.{TiledMap, TiledMapTileLayer}
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile

/**
 * This tiled map renderer can be used to produce a perspective effect. For this we another method called renderPerspective
 * should be used after {@link #render()} and avatar renderer to draw perspective tiles.
 */
class PerspectiveTiledMapRenderer(perspectiveLayerName: String, map: TiledMap, unitScale: Float, spriteBatch: SpriteBatch)
  extends OrthogonalTiledMapRenderer(map, unitScale, spriteBatch) {

  private val perspectiveLayer: MapLayer = map.getLayers.get(perspectiveLayerName)

  override def render() {
    AnimatedTiledMapTile.updateAnimationBaseTime()
    val layers = map.getLayers
    batch.begin()
    for (i <- 0 to layers.getCount - 1) {
      val layer = layers.get(i)
      if ((layer ne perspectiveLayer) && layer.isVisible) {
        layer match {
          case layer: TiledMapTileLayer => renderTileLayer(layer.asInstanceOf[TiledMapTileLayer])
          case _ =>
            for (i <- 0 to layer.getObjects.getCount-1) {
              val o = layer.getObjects.get(i)
              renderObject(o)
            }
        }
      }
    }
    batch.end()
  }

  def renderPerspective() {
    batch.begin()
    perspectiveLayer match {
      case layer: TiledMapTileLayer =>  renderTileLayer(layer)
      case _                        =>
        for (i <- 0 to perspectiveLayer.getObjects.getCount-1) {
          val o = perspectiveLayer.getObjects.get(i)
          renderObject(o)
        }
    }
    batch.end()
  }
}
