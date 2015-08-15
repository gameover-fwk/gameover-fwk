package gameover.fwk.ai.impl

import com.badlogic.gdx.math.Rectangle
import gameover.fwk.ai.CollisionState
import org.scalatest.FlatSpec

class MapCollisionDetectorTest extends FlatSpec with MapForTest{

  case class IsDirectTestData(area: Rectangle, pointX: Float, pointY: Float, result: Boolean)
  val isDirectTestData = List[IsDirectTestData](
    //-- simple cases
    IsDirectTestData(new Rectangle(0.5f, 0.5f, 0.5f, 0.5f), 4.5f, 0.5f, result = true),
    IsDirectTestData(new Rectangle(4.5f, 0.5f, 0.5f, 0.5f), 0.5f, 0.5f, result = true),
    IsDirectTestData(new Rectangle(0.5f, 0.5f, 0.5f, 0.5f), 0.5f, 4.5f, result = true),
    IsDirectTestData(new Rectangle(0.5f, 4.5f, 0.5f, 0.5f), 0.5f, 0.5f, result = true),
    IsDirectTestData(new Rectangle(0.5f, 0.5f, 0.5f, 0.5f), 1.5f, 1.5f, result = true),
    IsDirectTestData(new Rectangle(1.5f, 1.5f, 0.5f, 0.5f), 0.5f, 0.5f, result = true),
    IsDirectTestData(new Rectangle(0.5f, 1.5f, 0.5f, 0.5f), 1.5f, 0.5f, result = true),
    IsDirectTestData(new Rectangle(1.5f, 0.5f, 0.5f, 0.5f), 0.5f, 1.5f, result = true),
    //-- through void
    IsDirectTestData(new Rectangle(0.5f, 4.5f, 0.5f, 0.5f), 4.5f, 4.5f, result = true),
    IsDirectTestData(new Rectangle(4.5f, 4.5f, 0.5f, 0.5f), 0.5f, 4.5f, result = true),
    //-- block by wall
    IsDirectTestData(new Rectangle(0.5f, 2.5f, 0.5f, 0.5f), 4.5f, 2.5f, result = false),
    IsDirectTestData(new Rectangle(4.5f, 2.5f, 0.5f, 0.5f), 0.5f, 2.5f, result = false),
    IsDirectTestData(new Rectangle(0.5f, 0.5f, 0.5f, 0.5f), 4.5f, 4.5f, result = false),
    IsDirectTestData(new Rectangle(2.5f, 2.5f, 0.5f, 0.5f), 3.5f, 3.5f, result = false)
  )

  "For the map above, a collision detector" can "be created" in {
    println(collisionDetector)
    assert(collisionDetector != null)
  }

  isDirectTestData foreach { data =>

    it should s"return true when checking direct access for ${data.area} to a point (${data.pointX}, ${data.pointY})" in {
      val isDirect: Boolean = collisionDetector.isDirect(data.area, data.pointX, data.pointY, onlyBlocking = true)
      assert(isDirect === data.result)
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
