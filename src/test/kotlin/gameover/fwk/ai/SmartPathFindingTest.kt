package gameover.fwk.ai

import com.badlogic.gdx.math.Rectangle
import gameover.fwk.ai.impl.ManhattanHComputeStrategy
import gameover.fwk.ai.impl.MapForTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SmartPathFindingTest {
    private val collisionDetector = MapForTest.collisionDetector
    private val pathFinding = SmartPathFinding(ManhattanHComputeStrategy())

    init {
        println(collisionDetector)
    }

    @Nested
    inner class PathToPoint {

        @Test
        fun `Calling A* with smart path finding must return an empty list of points if the target point is inside the collision area`() {
            val area = Rectangle(0.1f, 0.1f, 0.8f, 0.8f)
            val tx = 0.7f
            val ty = 0.7f
            val path = pathFinding.findSmoothPath(area, tx, ty, false, collisionDetector)

            Assertions.assertThat(path).isEmpty()
        }
    }


    @Nested
    inner class PathToArea {

        @Test
        fun `Calling A* with smart path finding must return an empty list of points if the target area intersect the collision area`() {
            val pathFinding = SmartPathFinding(ManhattanHComputeStrategy())

            val area = Rectangle(0.1f, 0.1f, 0.8f, 0.8f)
            val targetArea = Rectangle(0.7f, 0.7f, 0.2f, 0.2f)
            val path = pathFinding.findSmoothPath(area, targetArea, false, collisionDetector)

            Assertions.assertThat(path).isEmpty()
        }
    }
}