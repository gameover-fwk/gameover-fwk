package gameover.fwk.ai

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import gameover.fwk.ai.impl.ManhattanHComputeStrategy
import gameover.fwk.ai.impl.MapForTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SmartPathFindingTest {

    private val collisionDetector = MapForTest.collisionDetector
    private val pathFinding = SmartPathFinding(ManhattanHComputeStrategy())

    init {
        println(collisionDetector)
    }

    @Nested
    inner class PathToPointForAreaSmallerThanTileDimTest {

        @Test
        fun `Calling A* with smart path finding must return an empty list of points if the target point is inside the collision area`() {
            val area = Rectangle(0.1f, 0.1f, 0.8f, 0.8f)
            val tx = 0.7f
            val ty = 0.7f
            val path = pathFinding.findSmoothPath(area, tx, ty, false, collisionDetector)

            assertThat(path).isEmpty()
        }

        @Test
        fun `Calling A* with smart path finding return most direct points - straight path`() {
            val area = Rectangle(5.1f, 4.1f, 0.8f, 0.8f)
            val tx = 4.3f
            val ty = 2.4f
            val path = pathFinding.findSmoothPath(area, tx, ty, false, collisionDetector)
            assertThat(path).isNotNull
            path?.let {
                assertThat(path).hasSize(1)
                assertThat(path[0]).isEqualTo(Vector2(tx, ty))
            }
        }

        @Test
        fun `Calling A* with smart path finding return most direct points - straight path for small area`() {
            val area = Rectangle(4.7f, 2.1f, 0.2f, 0.2f)
            val tx = 3.5f
            val ty = 1.4f
            val path = pathFinding.findSmoothPath(area, tx, ty, false, collisionDetector)
            assertThat(path).isNotNull
            path?.let {
                assertThat(path).hasSize(1)
                assertThat(path[0]).isEqualTo(Vector2(tx, ty))
            }
        }

        @Test
        fun `Calling A* with smart path finding return most direct points - turn point for big area`() {
            val area = Rectangle(4.4f, 1.8f, 0.8f, 0.8f)
            val tx = 3.5f
            val ty = 1.4f
            val path = pathFinding.findSmoothPath(area, tx, ty, false, collisionDetector)
            assertThat(path).isNotNull
            path?.let {
                assertThat(path).hasSize(2)
                assertThat(path[0]).isEqualTo(Vector2(4.4f, 1.6f))
                assertThat(path[1]).isEqualTo(Vector2(tx, ty))
            }
        }
    }


    @Nested
    inner class PathToAreaTest {

        @Test
        fun `Calling A* with smart path finding must return an empty list of points if the target area intersect the collision area`() {

            val area = Rectangle(0.1f, 0.1f, 0.8f, 0.8f)
            val targetArea = Rectangle(0.7f, 0.7f, 0.2f, 0.2f)
            val path = pathFinding.findSmoothPath(area, targetArea, false, collisionDetector)

            assertThat(path).isEmpty()
        }
    }
}