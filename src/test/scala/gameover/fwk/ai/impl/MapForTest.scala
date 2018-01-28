package gameover.fwk.ai.impl

import com.badlogic.gdx.math.Rectangle
import gameover.fwk.ai.{CollisionDetector, CollisionState}

/**
 *
 * @author olmartin@expedia.com
 * @since 2015-08-09
 */
trait MapForTest {

  val width = 6
  val height = 6
  val blockingCells = List((2, 2), (3, 2), (3, 3))
  val voidCells = List((3, 4), (3, 5), (0, 4), (1, 4), (1, 5))

  val collisionDetector: CollisionDetector = {
    val ret = new MapCollisionDetector()
    val name = "test"
    for (i <- -1 to 6)
      ret.addCollisionTile(CollisionSquare(name, new Rectangle(i, -1, 1f, 1f), CollisionState.Blocking))
    for (i <- -1 to 6)
      ret.addCollisionTile(CollisionSquare(name, new Rectangle(i, 6, 1f, 1f), CollisionState.Blocking))
    for (i <- 0 to 5)
      ret.addCollisionTile(CollisionSquare(name, new Rectangle(-1, i, 1f, 1f), CollisionState.Blocking))
    for (i <- 0 to 5)
      ret.addCollisionTile(CollisionSquare(name, new Rectangle(6, i, 1f, 1f), CollisionState.Blocking))
    for (blockingCell <- blockingCells)
      ret.addCollisionTile(CollisionSquare(name, new Rectangle(blockingCell._1, blockingCell._2, 1f, 1f), CollisionState.Blocking))
    for (voidCell <- voidCells)
      ret.addCollisionTile(CollisionSquare(name, new Rectangle(voidCell._1, voidCell._2, 1f, 1f), CollisionState.Void))
    ret.width = 6
    ret.height = 6
    ret
  }
}
