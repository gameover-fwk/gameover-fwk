package gameover.fwk.ai.impl

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import gameover.fwk.ai.CollisionState
import gameover.fwk.libgdx.gfx.GeometryUtils
import gameover.fwk.math.MathUtils
import gameover.fwk.pool.RectanglePool
import gameover.fwk.pool.Vector2Pool

/**
 * This class handle collision for a map.
 */
class MapCollisionDetector : BasicCollisionDetector() {

    val collisionTiles = arrayListOf<CollisionSquare>()
    val disableCollisionLayers = HashSet<String>()

    var width: Int = 0
    var height: Int = 0


    fun clear() {
        for (cs in collisionTiles)
            RectanglePool.free(cs.r)
        collisionTiles.clear()
    }

    val v:String = 0x2551.toChar().toString()
    val h:String = 0x2550.toChar().toString() + 0x2550.toChar().toString()
    val tl:String = 0x2554.toChar().toString()
    val tr:String = 0x2557.toChar().toString()
    val bl:String = 0x255A.toChar().toString()
    val br:String = 0x255D.toChar().toString()
    val w:String =  0x2588.toChar().toString() + 0x2588.toChar().toString()
    val g:String = 0x2591.toChar().toString() + 0x2591.toChar().toString()

    override fun toString() : String {
        val minX = collisionTiles.reduce { first, second -> if (first.x < second.x) first else second }.x
        val maxX = collisionTiles.reduce { first, second -> if (first.x > second.x) first else second }.x
        val minY = collisionTiles.reduce { first, second -> if (first.y < second.y) first else second }.y
        val maxY = collisionTiles.reduce { first, second -> if (first.y > second.y) first else second }.y
        val stateByCoord = collisionTiles.map { square -> Pair(square.xy, square.state) }.toMap()
        val builder = StringBuilder()
        var y = maxY.toInt()
        while (y >= minY.toInt()) {
            var x = minX.toInt()
            while (x <= maxX.toInt()) {
                val state = when (stateByCoord.getOrDefault(Pair(x.toFloat(), y.toFloat()), CollisionState.Empty)) {
                    CollisionState.Blocking -> w
                    CollisionState.Void -> "  "
                    else -> g
                }
                builder.append(state)
                x++
            }
            builder.append("\n")
            y--
        }
        return builder.substring(0, builder.length)
    }

    fun loadCollisionFromMap(map: TiledMap) {
        val layers = map.layers
        val l = layers[0]
        if (l is TiledMapTileLayer) {
            width = l.width
            height = l.height
        }
        for (i in 0 until layers.count) {
            val layer = layers.get(i)
            if ("yes" == layer.properties.get("collision"))
                analyseLayer(layer, CollisionState.Blocking)
            if ("yes" == layer.properties.get("fall"))
                analyseLayer(layer, CollisionState.Void)
        }
    }

    fun addCollisionTile(collisionSquare: CollisionSquare) {
        collisionTiles.add(collisionSquare)
    }

    fun disabledCollisionOnLayer(name: String) {
        disableCollisionLayers += name
    }

    private fun analyseLayer(layer: MapLayer, state: CollisionState) {
        if (layer is TiledMapTileLayer) {
            for (y in 0 until layer.height) {
                for (x in 0 until layer.width) {
                    val cell = layer.getCell(x, y)
                    if (cell != null) {
                        val rect = RectanglePool.obtain(x.toFloat(), y.toFloat(), 1f, 1f)
                        addCollisionTile(CollisionSquare(layer.name, rect, state))
                    }
                }
            }
        } else {
            throw IllegalArgumentException("Can't analyse class ${layer.javaClass}. This class is not managed currently by the code")
        }
    }

    override fun checkPosition(area: Rectangle) : CollisionState {
        val centerAndEdges = GeometryUtils.centerAndEdges(area)
        try {
            val collisionStates = centerAndEdges.map { v -> checkPosition(v.x, v.y) }
            return when {
                collisionStates.contains(CollisionState.Blocking) -> CollisionState.Blocking
                collisionStates.all { cs -> cs == CollisionState.Void } -> CollisionState.Void
                else -> CollisionState.Empty
            }
        } finally {
            Vector2Pool.free(centerAndEdges)
        }
    }

    override fun checkPosition(x: Float, y: Float) : CollisionState {
        val tileX = MathUtils.floor(x).toInt()
        val tileY = MathUtils.floor(y).toInt()
        return if (tileX >= 0 && tileY >= 0 && tileX < width && tileY < height) {
            for (cs in collisionTiles) {
                if (cs.r.x.toInt() == tileX && cs.r.y.toInt() == tileY && !disableCollisionLayers.contains(cs.name))
                    return cs.state
            }
            CollisionState.Empty
        } else CollisionState.Blocking
    }

    override fun checkCollision(area: Rectangle, movingSpriteVelocity: Vector2, onlyBlocking: Boolean): List<Rectangle> {
        val ret = arrayListOf<Rectangle>()
        val c1 = Vector2Pool.obtain(area.x, area.y)
        val c2 = Vector2Pool.obtain(area.x + area.width, area.y)
        val c3 = Vector2Pool.obtain(area.x, area.y + area.height)
        val c4 = Vector2Pool.obtain(area.x + area.width, area.y + area.height)
        val v1 = Vector2Pool.obtain()
        val v2 = Vector2Pool.obtain()
        val v3 = Vector2Pool.obtain()
        val v4 = Vector2Pool.obtain()
        if (movingSpriteVelocity.x != 0f || movingSpriteVelocity.y != 0f) {
            for (cs in collisionTiles) {
                val tile = cs.r
                if (area.overlaps(tile) && !disableCollisionLayers.contains(cs.name) && (!onlyBlocking || (cs.state == CollisionState.Blocking))) {
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
        return ret
    }
}

data class CollisionSquare (val name: String, val r: Rectangle, val state: CollisionState) {
    val xy: Pair<Float, Float>
        get() = Pair(r.x, r.y)
    val x: Float
        get() = r.x
    val y: Float
        get() = r.y
    val width: Float
        get() = r.width
    val height: Float
        get() = r.height
}