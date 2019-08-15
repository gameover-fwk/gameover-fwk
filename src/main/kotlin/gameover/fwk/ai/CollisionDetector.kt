package gameover.fwk.ai

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

interface CollisionDetector {

    /**
     * Check an area status
     */
    fun checkPosition(area: Rectangle): CollisionState

    /**
     * Check a position status
     */
    fun checkPosition(x: Float, y: Float) : CollisionState

    /**
     * This method check if area is intersecting something it shouldn't and return an array of
     * Rectangle objects where the collision append. The velocity of the sprite is updated accordingly.
     */
    fun checkCollision(area: Rectangle, movingSpriteVelocity: Vector2, onlyBlocking: Boolean): List<Rectangle>

    /**
     * Check if a point has a direct view to a target point. Only blocking can block this view.
     */
    fun hasDirectView(x: Float, y: Float, targetX: Float, targetY: Float): Boolean

    /**
     * Check if a sprite defined by its area has a direct view to a target point. Only blocking can block this view.
     */
    fun hasDirectView(visionArea: Rectangle, targetX: Float, targetY: Float): Boolean

    /**
     * Check if a sprite defined by its area has a direct view to a target area. Only blocking can block this view.
     */
    fun hasDirectView(visionArea: Rectangle, targetArea: Rectangle): Boolean

    /**
     * Check if a sprite can move straight to a point.
     */
    fun canMoveStraightToPoint(area: Rectangle, targetX: Float, targetY: Float): Boolean

    /**
     * Check area vs the collision map to see if something collides.
     */
    fun checkCollision(area: Rectangle, onlyBlocking: Boolean) : Boolean {
        val state: CollisionState = checkPosition(area)
        return (onlyBlocking && state == CollisionState.Blocking) || (!onlyBlocking && state != CollisionState.Empty)
    }

    /**
     * Check a point vs the collision map to see if something collides.
     */
    fun checkCollision(x: Float, y: Float, onlyBlocking: Boolean) : Boolean {
        val state: CollisionState = checkPosition(x, y)
        return (onlyBlocking && state == CollisionState.Blocking) || (!onlyBlocking && state != CollisionState.Empty)
    }
}

enum class CollisionState { Blocking, Void, Empty }
