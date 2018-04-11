package gameover.fwk.ai.impl

import com.badlogic.gdx.math.Rectangle
import gameover.fwk.ai.CollisionState
import org.scalatest.FlatSpec

class MapCollisionDetectorTest extends FlatSpec with MapForTest{
  /*
          ████████████████
       5  ██░░  ░░  ░░░░██
       4  ██    ░░  ░░░░██
       3  ██░░░░░░██░░░░██
       2  ██░░░░████░░░░██
       1  ██░░░░░░░░░░░░██
       0  ██░░░░░░░░░░░░██
          ████████████████
             0 1 2 3 4 5
  */

  case class IsDirectTestData(area: Rectangle, pointX: Float, pointY: Float, resultView: Boolean, resultMove: Boolean)
  val isDirectTestData = List[IsDirectTestData](
    // simple cases horizontal
    IsDirectTestData(new Rectangle(1.5f, 1.5f, 0.2f, 0.2f), 3.5f, 1.5f, resultView = true, resultMove = true),
    // simple cases vertical
    IsDirectTestData(new Rectangle(0.5f, 0.5f, 0.2f, 0.2f), 0.5f, 2.5f, resultView = true, resultMove = true),
    // simple cases diag 1
    IsDirectTestData(new Rectangle(0.5f, 0.5f, 0.2f, 0.2f), 3.5f, 1.5f, resultView = true, resultMove = true),
    // simple cases diag 2
    IsDirectTestData(new Rectangle(0.5f, 1.5f, 0.2f, 0.2f), 3.5f, 0.5f, resultView = true, resultMove = true),
    // through void horizontal
    IsDirectTestData(new Rectangle(2.5f, 5.5f, 0.2f, 0.2f), 4.5f, 5.5f, resultView = true, resultMove = false),
    // through void vertical
    IsDirectTestData(new Rectangle(0.5f, 5.5f, 0.2f, 0.2f), 0.5f, 2.5f, resultView = true, resultMove = false),
    // through void diag 1
    IsDirectTestData(new Rectangle(2.5f, 5.5f, 0.2f, 0.2f), 5.5f, 3.5f, resultView = true, resultMove = false),
    // through void diag 2
    IsDirectTestData(new Rectangle(5.5f, 5.5f, 0.2f, 0.2f), 2.5f, 4.5f, resultView = true, resultMove = false),
    // double void
    IsDirectTestData(new Rectangle(0.5f, 5.5f, 0.2f, 0.2f), 5.5f, 4.5f, resultView = true, resultMove = false),
    IsDirectTestData(new Rectangle(5.5f, 5.5f, 0.2f, 0.2f), 0.5f, 4.5f, resultView = true, resultMove = false),
    // block by wall horizontal
    IsDirectTestData(new Rectangle(1.5f, 3.5f, 0.2f, 0.2f), 4.5f, 3.5f, resultView = false, resultMove = false),
    // block by wall vertical
    IsDirectTestData(new Rectangle(1.5f, 2.5f, 0.2f, 0.2f), 4.5f, 2.5f, resultView = false, resultMove = false),
    // block by wall diag 1
    IsDirectTestData(new Rectangle(2.5f, 3.5f, 0.2f, 0.2f), 4.5f, 1.5f, resultView = false, resultMove = false),
    // block by wall diag 2
    IsDirectTestData(new Rectangle(4.5f, 3.5f, 0.2f, 0.2f), 1.5f, 1.5f, resultView = false, resultMove = false)
  )

  "For the map above, a collision detector" can "be created" in {
    println(collisionDetector)
    assert(collisionDetector != null)
  }

  isDirectTestData foreach { data =>
    it should s"return ${data.resultView} when checking direct view for ${data.area} to a point (${data.pointX}, ${data.pointY})" in {
      val isDirect = collisionDetector.hasDirectView(data.area, data.pointX, data.pointY)
      assert(isDirect === data.resultView, s" - collision detector did${if (isDirect) "" else " not"} find a direct view but should ${if (isDirect) "not"}")
    }
    val secondArea = new Rectangle(data.pointX, data.pointY, data.area.width, data.area.height)
    it should s"return ${data.resultView} when checking direct view for ${secondArea} to a point (${data.area.x}, ${data.area.y})" in {
      val isDirect = collisionDetector.hasDirectView(secondArea, data.area.x, data.area.y)
      assert(isDirect === data.resultView, s" - collision detector did${if (isDirect) "" else " not"} find a direct view but should ${if (isDirect) "not"}")
    }
  }

  isDirectTestData foreach { data =>
    it should s"return ${data.resultMove} when checking direct move for ${data.area} to a point (${data.pointX}, ${data.pointY})" in {
      val isDirect = collisionDetector.canMoveStraightToPoint(data.area, data.pointX, data.pointY)
      assert(isDirect === data.resultMove, s" - collision detector did${if (isDirect) "" else " not"} find a direct path but should ${if (isDirect) "not"}")
    }
    val secondArea = new Rectangle(data.pointX, data.pointY, data.area.width, data.area.height)
    it should s"return ${data.resultMove} when checking direct move for ${secondArea} to a point (${data.area.x}, ${data.area.y})" in {
      val isDirect = collisionDetector.canMoveStraightToPoint(secondArea, data.area.x, data.area.y)
      assert(isDirect === data.resultMove, s" - collision detector did${if (isDirect) "" else " not"} find a direct path but should ${if (isDirect) "not"}")
    }
  }

  case class CheckPositionTestData(pointX: Float, pointY: Float, result: CollisionState.Value)
  val checkPositionTestData = List[CheckPositionTestData](
    //-- simple cases
    CheckPositionTestData(0.5f, 0.5f, CollisionState.Empty),
    CheckPositionTestData(4.5f, 4.5f, CollisionState.Empty),
    CheckPositionTestData(2.5f, 2.5f, CollisionState.Blocking),
    CheckPositionTestData(3.5f, 2.5f, CollisionState.Blocking),
    CheckPositionTestData(3.5f, 3.5f, CollisionState.Blocking),
    CheckPositionTestData(3.5f, 4.5f, CollisionState.Void),
    CheckPositionTestData(3.5f, 5.5f, CollisionState.Void)
  )

  checkPositionTestData foreach { data =>
    it should s"return the state ${data.result} when checking position (${data.pointX}, ${data.pointY})" in {
      val state = collisionDetector.checkPosition(data.pointX, data.pointY)
      assert(state === data.result)
    }
  }
}
