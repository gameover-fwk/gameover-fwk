package gameover.fwk.pool

import com.badlogic.gdx.math.{Rectangle, Vector2}

object RectanglePool extends ObjectPool[Rectangle] {

  def instantiateObject: Rectangle = new Rectangle

  def obtain(rectangle: Rectangle): Rectangle = obtain().set(rectangle)

  def obtain(v1: Vector2, v2: Vector2): Rectangle = obtain().set(v1.x, v1.y, v2.x, v2.y)

  def obtain(x1: Float, y1: Float, x2: Float, y2: Float): Rectangle = obtain().set(x1, y1, x2, y2)
}
