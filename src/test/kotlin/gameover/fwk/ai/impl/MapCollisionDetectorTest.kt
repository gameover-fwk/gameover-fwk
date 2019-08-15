package gameover.fwk.ai.impl

import com.badlogic.gdx.math.Rectangle
import gameover.fwk.ai.CollisionState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MapCollisionDetectorTest {

    data class IsDirectTestData(val area: Rectangle, val pointX: Float, val pointY: Float, val resultView: Boolean, val resultMove: Boolean)

    val collisionDetector = MapForTest.collisionDetector
    
    val isDirectTestData = listOf(
            // simple cases horizontal
            IsDirectTestData(Rectangle(1.5f, 1.5f, 0.2f, 0.2f), 3.5f, 1.5f, true, true),
            // simple cases vertical
            IsDirectTestData(Rectangle(0.5f, 0.5f, 0.2f, 0.2f), 0.5f, 2.5f, true, true),
            // simple cases diag 1
            IsDirectTestData(Rectangle(0.5f, 0.5f, 0.2f, 0.2f), 3.5f, 1.5f, true, true),
            // simple cases diag 2
            IsDirectTestData(Rectangle(0.5f, 1.5f, 0.2f, 0.2f), 3.5f, 0.5f, true, true),
            // through void horizontal
            IsDirectTestData(Rectangle(2.5f, 5.5f, 0.2f, 0.2f), 4.5f, 5.5f, true, false),
            // through void vertical
            IsDirectTestData(Rectangle(0.5f, 5.5f, 0.2f, 0.2f), 0.5f, 2.5f, true, false),
            // through void diag 1
            IsDirectTestData(Rectangle(2.5f, 5.5f, 0.2f, 0.2f), 5.5f, 3.5f, true, false),
            // through void diag 2
            IsDirectTestData(Rectangle(5.5f, 5.5f, 0.2f, 0.2f), 2.5f, 4.5f, true, false),
            // double void
            IsDirectTestData(Rectangle(0.5f, 5.5f, 0.2f, 0.2f), 5.5f, 4.5f, true, false),
            IsDirectTestData(Rectangle(5.5f, 5.5f, 0.2f, 0.2f), 0.5f, 4.5f, true, false),
            // block by wall horizontal
            IsDirectTestData(Rectangle(1.5f, 3.5f, 0.2f, 0.2f), 4.5f, 3.5f, false, false),
            // block by wall vertical
            IsDirectTestData(Rectangle(1.5f, 2.5f, 0.2f, 0.2f), 4.5f, 2.5f, false, false),
            // block by wall diag 1
            IsDirectTestData(Rectangle(2.5f, 3.5f, 0.2f, 0.2f), 4.5f, 1.5f, false, false),
            // block by wall diag 2
            IsDirectTestData(Rectangle(4.5f, 3.5f, 0.2f, 0.2f), 1.5f, 1.5f, false, false)
    )

    @Test
    fun `For the map above, a collision detector can be created`() {
        println(collisionDetector)
        assertTrue(true)
    }

    @Test
    fun `it should return the correct result when checking direct view for an area to a point`(){
        isDirectTestData.forEach { data ->
            val isDirect = collisionDetector.hasDirectView(data.area, data.pointX, data.pointY)
            assertThat(isDirect)
                    .describedAs("when checking direct view for ${data.area} to point(${data.pointX}, ${data.pointY}, collision detector did${if (isDirect) "" else " not"} find a direct view but should${if (isDirect) " not" else ""}")
                    .isEqualTo(data.resultView)
            val secondArea = Rectangle(data.pointX, data.pointY, data.area.width, data.area.height)
            val isDirect2 = collisionDetector.hasDirectView(secondArea, data.area.x, data.area.y)
            assertThat(isDirect2)
                    .describedAs("when checking direct view for $secondArea to point(${data.area.x}}, ${data.area.y}, , collision detector did${if (isDirect) "" else " not"} find a direct view but should${if (isDirect) " not" else ""}")
                    .isEqualTo(data.resultView)
        }
    }

    @Test
    fun `it should return the correct result when checking direct move for an area to a point`(){
        isDirectTestData.forEach { data ->
            val isDirect = collisionDetector.canMoveStraightToPoint(data.area, data.pointX, data.pointY)
            assertThat(isDirect)
                    .describedAs("when checking direct move for ${data.area} to point(${data.pointX}, ${data.pointY}, collision detector did${if (isDirect) "" else " not"} find a direct path but should${if (isDirect) " not" else ""}")
                    .isEqualTo(data.resultMove)
            val secondArea = Rectangle(data.pointX, data.pointY, data.area.width, data.area.height)
            val isDirect2 = collisionDetector.canMoveStraightToPoint(secondArea, data.area.x, data.area.y)
            assertThat(isDirect2)
                    .describedAs("when checking direct move for $secondArea to point(${data.area.x}, ${data.area.y}, collision detector did${if (isDirect) "" else " not"} find a direct path but should${if (isDirect) " not" else ""}")
                    .isEqualTo(data.resultMove)
        }
    }

    data class CheckPositionTestData(val pointX: Float, val pointY: Float, val result: CollisionState)

    @Test
    fun `it should return the correct state when checking position`() {
        val checkPositionTestData = listOf(
            //-- simple cases
            CheckPositionTestData(0.5f, 0.5f, CollisionState.Empty),
            CheckPositionTestData(4.5f, 4.5f, CollisionState.Empty),
            CheckPositionTestData(2.5f, 2.5f, CollisionState.Blocking),
            CheckPositionTestData(3.5f, 2.5f, CollisionState.Blocking),
            CheckPositionTestData(3.5f, 3.5f, CollisionState.Blocking),
            CheckPositionTestData(3.5f, 4.5f, CollisionState.Void),
            CheckPositionTestData(3.5f, 5.5f, CollisionState.Void))
        checkPositionTestData.forEach { data ->
            val state = collisionDetector.checkPosition(data.pointX, data.pointY)
            assertThat(state)
                    .describedAs("expecting the state ${data.result} for position (${data.pointX}, ${data.pointY})")
                    .isEqualTo(data.result)
        }
    }
}
