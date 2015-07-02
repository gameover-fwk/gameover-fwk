package gameover.fwk.math

import com.badlogic.gdx.math.Vector2
import gameover.fwk.pool.Vector2Pool

object MathUtils {

  def computeAngle(x1: Float, y1: Float, x2: Float, y2: Float): Float = computeAngle(x2 - x1, y2 - y1)

  def computeAngle(dx: Float, dy: Float): Float = {
    val dv: Vector2 = Vector2Pool.obtain(dx, dy)
    try {
      dv.angle
    } finally {
      Vector2Pool.free(dv)
    }
  }
}
