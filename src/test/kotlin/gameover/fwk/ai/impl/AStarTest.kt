package gameover.fwk.ai.impl

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import gameover.fwk.ai.AStar
import gameover.fwk.ai.CollisionState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AStarTest {

    private val collisionDetector = MapForTest.collisionDetector
    private val pathFinding = AStar(ManhattanHComputeStrategy())


    init {
        println(collisionDetector)
    }

    @Test
    fun `Calling A* with standard path finding must return an empty list of points if the target is the same`(){
        val x = 0.5f
        val y = 0.5f
        val path = pathFinding.findPath(x, y, x, y, false, collisionDetector)

        assertThat(path).isEmpty()
    }

    @Test
    fun `it must return null if path can't be found`(){
        val path = pathFinding.findPath(0.5f, 0.5f, 0.5f, 4.5f, false, collisionDetector)

        assertThat(path).isNull()
    }

    @Test
    fun `it must return a path to closest point if path can't be found but closest point flag was set`(){
        val path = pathFinding.findPath(0.5f, 0.5f, 0.5f, 4.5f, true, collisionDetector)

        assertThat(path).isNotNull()
        assertThat(path).hasSize(1)
        val nearestPoint = path?.lastOrNull()!!
        assertThat(nearestPoint).isEqualTo(GridPoint2(0, 3))
    }

    @Test
    fun `it must return a path to the point if point is reachable - 1st use case`(){
        val origin = Vector2(2.5f, 4.5f)
        val target = Vector2(5.5f, 4.5f)
        val path = pathFinding.findPath(origin.x, origin.y, target.x, target.y, false, collisionDetector)

        assertThat(path).isNotNull()
        assertThat(path).hasSizeGreaterThan(1)
        val lastPoint = path?.lastOrNull()!!
        assertThat(lastPoint).isEqualTo(GridPoint2(5, 4))
        for (p in path.iterator()) {
            assertThat(collisionDetector.checkPosition(p.x.toFloat(), p.y.toFloat()))
                    .describedAs("Check if (${p.x}, ${p.y}) is valid")
                    .isEqualTo(CollisionState.Empty)
        }
        print("path from $origin to $target: $path")
    }

    @Test
    fun `it must return a path to the point if point is reachable - 2nd use case`(){
        val origin = Vector2(5.5f, 4.5f)
        val target = Vector2(2.5f, 4.5f)
        val path = pathFinding.findPath(origin.x, origin.y, target.x, target.y, false, collisionDetector)

        assertThat(path).isNotNull()
        assertThat(path).hasSizeGreaterThan(1)
        val lastPoint = path?.lastOrNull()!!
        assertThat(lastPoint).isEqualTo(GridPoint2(2, 4))
        for (p in path.iterator()) {
            assertThat(collisionDetector.checkPosition(p.x.toFloat(), p.y.toFloat()))
                    .describedAs("Check if (${p.x}, ${p.y}) is valid")
                    .isEqualTo(CollisionState.Empty)
        }
        print("path from $origin to $target: $path")
    }

    @Test
    fun `it must return a path to the point if point is reachable - 3rd use case`(){
        val origin = Vector2(4.5f, 3.5f)
        val target = Vector2(9.5f, 3.5f)
        val path = pathFinding.findPath(origin.x, origin.y, target.x, target.y, false, collisionDetector)

        assertThat(path).isNotNull()
        assertThat(path).hasSizeGreaterThan(1)
        val lastPoint = path?.lastOrNull()!!
        assertThat(lastPoint).isEqualTo(GridPoint2(9, 3))
        for (p in path.iterator()) {
            assertThat(collisionDetector.checkPosition(p.x.toFloat(), p.y.toFloat()))
                    .describedAs("Check if (${p.x}, ${p.y}) is valid")
                    .isEqualTo(CollisionState.Empty)
        }
        print("path from $origin to $target: $path")
    }

    @Test
    fun `it must return a path to the point if point is reachable - 4th use case`(){
        val origin = Vector2(4.5f, 3.5f)
        val target = Vector2(9.5f, 1.5f)
        val path = pathFinding.findPath(origin.x, origin.y, target.x, target.y, false, collisionDetector)

        assertThat(path).isNotNull()
        assertThat(path).hasSizeGreaterThan(1)
        val lastPoint = path?.lastOrNull()!!
        assertThat(lastPoint).isEqualTo(GridPoint2(9, 1))
        for (p in path.iterator()) {
            assertThat(collisionDetector.checkPosition(p.x.toFloat(), p.y.toFloat()))
                    .describedAs("Check if (${p.x}, ${p.y}) is valid")
                    .isEqualTo(CollisionState.Empty)
        }
        print("path from $origin to $target: $path")
    }

    @Test
    fun `Calling A* with smooth path finding must return a smooth path to the point if point is reachable`(){
        val sprite = Rectangle(2.5f, 4.5f, 0.2f, 0.2f)

        val path = pathFinding.findSmoothPath(sprite, 5.5f, 4.5f, false, collisionDetector)

        assertThat(path).isNotNull()
        assertThat(path).hasSizeGreaterThan(1)
        val lastPoint = path?.lastOrNull()!!
        assertThat(lastPoint).isEqualTo(Vector2(5.5f, 4.5f))
        for (p in path.iterator()) {
            assertThat(collisionDetector.checkPosition(p.x, p.y))
                    .describedAs("Check if (${p.x}, ${p.y}) is valid")
                    .isEqualTo(CollisionState.Empty)
        }
    }
}