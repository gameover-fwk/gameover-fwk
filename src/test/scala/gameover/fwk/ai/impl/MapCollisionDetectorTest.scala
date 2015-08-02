package gameover.fwk.ai.impl

import com.badlogic.gdx.math.Rectangle
import gameover.fwk.ai.CollisionState
import org.scalatest.FlatSpec

class MapCollisionDetectorTest extends FlatSpec{


  lazy val collisionDetector = {
    val collisionDetector = new MapCollisionDetector()
    collisionDetector.addCollisionTile(new CollisionSquare(new Rectangle(0f, 0f, 1f, 1f), CollisionState.Empty))
    collisionDetector.addCollisionTile(new CollisionSquare(new Rectangle(5f, 5f, 1f, 1f), CollisionState.Empty))
    collisionDetector.addCollisionTile(new CollisionSquare(new Rectangle(2f, 2f, 1f, 1f), CollisionState.Blocking))
    collisionDetector.addCollisionTile(new CollisionSquare(new Rectangle(3f, 2f, 1f, 1f), CollisionState.Blocking))
    collisionDetector.addCollisionTile(new CollisionSquare(new Rectangle(3f, 3f, 1f, 1f), CollisionState.Blocking))
    collisionDetector.addCollisionTile(new CollisionSquare(new Rectangle(3f, 4f, 1f, 1f), CollisionState.Void))
    collisionDetector.addCollisionTile(new CollisionSquare(new Rectangle(3f, 5f, 1f, 1f), CollisionState.Void))
    println(collisionDetector.toString)
    collisionDetector
  }

  case class TestData(area: Rectangle, pointX: Float, pointY: Float, result: Boolean)
  val testData = List[TestData](
    //-- simple cases
    TestData(new Rectangle(0.5f, 0.5f, 0.5f, 0.5f), 4.5f, 0.5f, result = true),
    TestData(new Rectangle(4.5f, 0.5f, 0.5f, 0.5f), 0.5f, 0.5f, result = true),
    TestData(new Rectangle(0.5f, 0.5f, 0.5f, 0.5f), 0.5f, 4.5f, result = true),
    TestData(new Rectangle(0.5f, 4.5f, 0.5f, 0.5f), 0.5f, 0.5f, result = true),
    TestData(new Rectangle(0.5f, 0.5f, 0.5f, 0.5f), 1.5f, 1.5f, result = true),
    TestData(new Rectangle(1.5f, 1.5f, 0.5f, 0.5f), 0.5f, 0.5f, result = true),
    TestData(new Rectangle(0.5f, 1.5f, 0.5f, 0.5f), 1.5f, 0.5f, result = true),
    TestData(new Rectangle(1.5f, 0.5f, 0.5f, 0.5f), 0.5f, 1.5f, result = true),
    //-- through void
    TestData(new Rectangle(0.5f, 4.5f, 0.5f, 0.5f), 4.5f, 4.5f, result = true),
    TestData(new Rectangle(4.5f, 4.5f, 0.5f, 0.5f), 0.5f, 4.5f, result = true),
    //-- block by wall
    TestData(new Rectangle(0.5f, 2.5f, 0.5f, 0.5f), 4.5f, 2.5f, result = false),
    TestData(new Rectangle(4.5f, 2.5f, 0.5f, 0.5f), 0.5f, 2.5f, result = false),
    TestData(new Rectangle(0.5f, 0.5f, 0.5f, 0.5f), 4.5f, 4.5f, result = false),
    TestData(new Rectangle(2.5f, 2.5f, 0.5f, 0.5f), 3.5f, 3.5f, result = false)
  )

  "For the map above, a collision detector" can "be created" in {
      assert(collisionDetector != null)
  }

  testData foreach { data =>

    it should s"return true when checking direct access for ${data.area} to a point (${data.pointX}, ${data.pointY})" in {
      val isDirect: Boolean = collisionDetector.isDirect(data.area, data.pointX, data.pointY, onlyBlocking = true)
      assert(isDirect === data.result)
    }

  }
}
