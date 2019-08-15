package gameover.fwk.ai.impl

import com.badlogic.gdx.math.Rectangle
import gameover.fwk.ai.CollisionDetector
import gameover.fwk.ai.CollisionState.Blocking
import gameover.fwk.ai.CollisionState.Void

/**
 * Map
   ████████████████████████
5  ██░░  ░░  ░░░░░░░░░░░░██
4  ██    ░░  ░░░░░░░░░░░░██
3  ██░░░░░░██░░░░    ░░░░██
2  ██░░░░████░░░░    ░░░░██
1  ██░░░░░░░░░░░░░░░░░░░░██
0  ██░░░░░░░░░░░░░░░░░░░░██
   ████████████████████████
     0 1 2 3 4 5 6 7 8 9
*/
object MapForTest {

    val width = 10
    val height = 6
    val blockingCells = listOf(Pair(2, 2), Pair(3, 2), Pair(3, 3))
    val voidCells = listOf(Pair(3, 4), Pair(3, 5), Pair(0, 4), Pair(1, 4),
            Pair(1, 5), Pair(6, 2), Pair(7, 2), Pair(6, 3), Pair(7, 3))

    val collisionDetector: CollisionDetector
        get() {
            val ret = MapCollisionDetector()
            val name = "test"
            val tw = 1f
            val th = 1f
            for (i in -1..width)
                ret.addCollisionTile(CollisionSquare(name, Rectangle(i.toFloat(), -1f, tw, th), Blocking))
            for (i in -1..width)
                ret.addCollisionTile(CollisionSquare(name, Rectangle(i.toFloat(), height.toFloat(), tw, th), Blocking))
            for (i in -1..height)
                ret.addCollisionTile(CollisionSquare(name, Rectangle(-1f, i.toFloat(), tw, th), Blocking))
            for (i in -1..height)
                ret.addCollisionTile(CollisionSquare(name, Rectangle(width.toFloat(), i.toFloat(), tw, th), Blocking))
            for (bc in blockingCells)
                ret.addCollisionTile(CollisionSquare(name, Rectangle(bc.first.toFloat(), bc.second.toFloat(), tw, th), Blocking))
            for (vc in voidCells)
                ret.addCollisionTile(CollisionSquare(name, Rectangle(vc.first.toFloat(), vc.second.toFloat(), tw, th), Void))
            ret.width = width
            ret.height = height
            return ret
        }

}