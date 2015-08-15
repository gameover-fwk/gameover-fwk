package gameover.fwk.ai.impl

import com.badlogic.gdx.math.{Vector2, GridPoint2}
import gameover.fwk.ai.AStar
import gameover.fwk.libgdx.collection.GdxArray
import org.scalatest._

/**
 * Test on A*.
 * @author olmartin@expedia.com
 * @since 2015-08-09
 */
class AStarTest extends FlatSpec with MapForTest with SpriteForTest with GivenWhenThen {

  println(collisionDetector)

  "Calling A* with standard pathfinding" must "return an empty list of points if the target is the same" in {
    Given("a collision detector on a map")
    val pathFinding: AStar = new AStar(new ManhattanHComputeStrategy)

    When("looking for a path from a point to the same point")
    val x = 0.5f
    val y = 0.5f
    val path: GdxArray[GridPoint2] = pathFinding.findPath(x, y, x, y, findClosestPoint = false, collisionDetector)

    Then("the path return should be empty")
    assert(path.isEmpty)
  }

  it must "return null if path can't be found" in {
    Given("a collision detector on a map")
    val pathFinding: AStar = new AStar(new ManhattanHComputeStrategy)

    When("looking for a path from a point to a point not reachable")
    val path: GdxArray[GridPoint2] = pathFinding.findPath(0.5f, 0.5f, 0.5f, 4.5f, findClosestPoint = false, collisionDetector)

    Then("the path return should be null")
    assert(path === null)
  }

  it must "return a path to closest point if path can't be found but closest point flag was set" in {
    Given("a collision detector on a map")
    val pathFinding: AStar = new AStar(new ManhattanHComputeStrategy)

    When("looking for a path from a point to a point not reachable or the closest point")
    val path: GdxArray[GridPoint2] = pathFinding.findPath(0.5f, 0.5f, 0.5f, 4.5f, findClosestPoint = true, collisionDetector)

    Then("a path is returned to the closest point")
    assert(path !== null)
    assert(path nonEmpty)
    assert(path.size === 1)
    val nearestPoint: GridPoint2 = path.last
    assert(nearestPoint === new GridPoint2(0, 3))
  }

  it must "return a path to the point if point is reachable" in {
    Given("a collision detector on a map")
    val pathFinding: AStar = new AStar(new ManhattanHComputeStrategy)

    When("looking for a path from a point to a point not reachable or the closest point")
    val path: GdxArray[GridPoint2] = pathFinding.findPath(2.5f, 4.5f, 5.5f, 4.5f, findClosestPoint = false, collisionDetector)

    Then("a path is returned to the closest point")
    assert(path !== null)
    assert(path nonEmpty)
    assert(path.size > 1)
    val lastPoint: GridPoint2 = path.last
    assert(lastPoint === new GridPoint2(5, 4))
  }

  "Calling A* with smooth pathfinding" must "return a smooth path to the point if point is reachable" in {
    Given("a collision detector on a map")
    val pathFinding: AStar = new AStar(new ManhattanHComputeStrategy)
    And("a sprite on the map")
    sprite.setPosition(2.5f, 4.5f)

    When("looking for a path from a point to a point not reachable or the closest point")
    val path: GdxArray[Vector2] = pathFinding.findSmoothPath(sprite, 5.5f, 4.5f, findClosestPoint = false, collisionDetector)

    Then("a path is returned to the closest point")
    assert(path !== null)
    assert(path nonEmpty)
    assert(path.size > 1)
    val lastPoint: Vector2 = path.last
    assert(lastPoint === new Vector2(5.5f, 4.5f))
  }
}
