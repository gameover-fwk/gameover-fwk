package gameover.fwk.ai

trait HComputeStrategy {
  def h(x: Int, y: Int, px: Int, py: Int, collisionDetector: CollisionDetector): Int
}
