package gameover.fwk.ai.impl

import gameover.fwk.ai.{CollisionDetector, HComputeStrategy}

class ManhattanHComputeStrategy extends HComputeStrategy{
  override def h(x: Int, y: Int, px: Int, py: Int, collisionDetector: CollisionDetector) : Int = Math.abs(px - x) * 10 + Math.abs(py - y) * 10
}
