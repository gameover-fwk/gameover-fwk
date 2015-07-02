package gameover.fwk.pool

import com.badlogic.gdx.math.GridPoint2

object GridPoint2Pool extends ObjectPool[GridPoint2] {

  def instantiateObject: GridPoint2 = new GridPoint2()

  def obtain(o: GridPoint2): GridPoint2 = obtain().set(o)

  def obtain(x: Int, y: Int): GridPoint2 = obtain().set(x, y)
}
