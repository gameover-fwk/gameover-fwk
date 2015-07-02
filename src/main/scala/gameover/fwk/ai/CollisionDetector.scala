package gameover.fwk.ai

import com.badlogic.gdx.math.Rectangle

trait CollisionDetector {
  def checkCollision(r: Rectangle, onlyBlocking: Boolean): Boolean
  def checkCollision(tileX: Int, tileY: Int, onlyBlocking: Boolean): Boolean
  def isDirect(area: Rectangle, x: Float, y: Float, targetX: Float, targetY: Float, onlyBlocking: Boolean): Boolean
}
