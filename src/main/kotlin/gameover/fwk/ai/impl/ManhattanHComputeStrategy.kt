package gameover.fwk.ai.impl

import gameover.fwk.ai.CollisionDetector
import gameover.fwk.ai.HComputeStrategy

class ManhattanHComputeStrategy : HComputeStrategy {
    override fun h(x: Int, y: Int, px: Int, py: Int, collisionDetector: CollisionDetector) : Int = Math.abs(px - x) * 10 + Math.abs(py - y) * 10
}