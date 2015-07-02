package gameover.fwk.pool

import com.badlogic.gdx.math.Vector2

object Vector2Pool extends ObjectPool[Vector2] {

  def instantiateObject: Vector2 = new Vector2

  def obtain(o: Vector2): Vector2 = obtain().set(o)

  def obtain(x: Float, y: Float): Vector2 = obtain().set(x, y)
}
