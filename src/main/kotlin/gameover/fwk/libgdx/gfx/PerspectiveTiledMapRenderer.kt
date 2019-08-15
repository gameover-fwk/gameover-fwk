package gameover.fwk.libgdx.gfx

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapLayers
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile

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
open class PerspectiveTiledMapRenderer(m: TiledMap, unitScale: Float, spriteBatch: SpriteBatch, wallsLayerLevelName: String = "walls", topLayerLevelName: String = "top")
    : OrthogonalTiledMapRenderer(m, unitScale, spriteBatch) {

    private val wallsLayers = filterLayers(m.layers, wallsLayerLevelName)
    private val topLayers = filterLayers(m.layers, topLayerLevelName)

    open fun filterLayers(layers: MapLayers, levelName: String) : List<MapLayer> {
        val ret = ArrayList<MapLayer>()
        val it = layers.iterator()
        while (it.hasNext()) {
            val layer = it.next()
            val lvl = layer.properties.get("level", String::class.java)
            if (lvl != null && lvl == levelName)
                ret += layer
        }
        return ret
    }


    override fun render() {
        renderFloorLayers()
        renderWallsLayers()
        renderTopLayers()
    }

    open fun renderFloorLayers() {
        AnimatedTiledMapTile.updateAnimationBaseTime()
        val layers = map.layers
        batch.begin()
        for (i in 0 until layers.count) {
            val layer = layers.get(i)
            if ((!wallsLayers.contains(layer)) && (!topLayers.contains(layer)) && layer.isVisible) {
                if (layer is TiledMapTileLayer) {
                    renderTileLayer(layer)
                } else {
                    layer.objects.forEach { renderObject(it) }
                }
            }
        }
        batch.end()
    }

    open fun renderWallsLayers() {
        wallsLayers.forEach { renderSuppLayer(it) }
    }

    open fun renderTopLayers() {
        topLayers.forEach { renderSuppLayer(it) }
    }

    private fun renderSuppLayer(layer: MapLayer) {
        if (layer.isVisible) {
            batch.begin()
            if (layer is TiledMapTileLayer)
                renderTileLayer(layer)
            else
                layer.objects.forEach { renderObject(it) }
            batch.end()
        }
    }
}