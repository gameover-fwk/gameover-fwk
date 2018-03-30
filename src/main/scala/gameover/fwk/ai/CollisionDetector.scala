package gameover.fwk.ai

import com.badlogic.gdx.math.{Rectangle, Vector2}
import com.badlogic.gdx.utils.{Array => GdxArray}

trait CollisionDetector {

  /**
   * Check an area status
   */
  def checkPosition(area: Rectangle): CollisionState.Value

  /**
   * Check a position status
   */
  def checkPosition(x: Float, y: Float) : CollisionState.Value

  /**
   * This method check if area is intersecting something it shouldn't and return an array of
   * Rectangle objects where the collision append. The velocity of the sprite is updated accordingly.
   */
  def checkCollision(area: Rectangle, movingSpriteVelocity: Vector2, onlyBlocking: Boolean): GdxArray[Rectangle]

  /**
    * Check if a point has a direct view to a target point.
    */
  def isDirect(x: Float, y: Float, targetX: Float, targetY: Float, onlyBlocking: Boolean): Boolean

  /**
   * Check if a sprite defined by its area has a direct view to a target point.
   */
  def isDirect(visionArea: Rectangle, targetX: Float, targetY: Float, onlyBlocking: Boolean): Boolean

  /**
    * Check if a sprite defined by its area has a direct view to a target area.
    */
  def isDirect(visionArea: Rectangle, targetArea: Rectangle, onlyBlocking: Boolean): Boolean

  def checkCollision(area: Rectangle, onlyBlocking: Boolean) : Boolean = {
    val state: CollisionState.Value = checkPosition(area)
    (onlyBlocking && state == CollisionState.Blocking) || (!onlyBlocking && state != CollisionState.Empty)
  }

  def checkCollision(x: Float, y: Float, onlyBlocking: Boolean) : Boolean = {
    val state: CollisionState.Value = checkPosition(x, y)
    (onlyBlocking && state == CollisionState.Blocking) || (!onlyBlocking && state != CollisionState.Empty)
  }
}

object CollisionState extends Enumeration {
  val Blocking, Void, Empty = Value
}


