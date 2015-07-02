package gameover.fwk.pool

import com.badlogic.gdx.utils.Pool
import gameover.fwk.libgdx.utils.LibGDXHelper

import scala.reflect.ClassTag

trait ObjectPool[T] extends LibGDXHelper {

  private val pool: Pool[T] = new Pool[T]() {
    protected def newObject: T = instantiateObject
  }

  def instantiateObject: T

  def clear() {
    pool.clear()
  }

  def free(a: Array[T]) {
    a.foreach(free)
  }

  def free(a: GdxArray[T]) {
    for (i <- 0 to a.size -1)
      free(a.get(i))
  }

  def free(l: List[T]) {
    l.foreach(free)
  }

  def free[U: ClassTag](t: T) {
    pool.free(t)
  }

  def obtain(): T = pool.obtain()

  def obtainAsArray[T:ClassTag](nb: Int): Array[T] = Array.fill[T](nb)(obtain().asInstanceOf[T])

  def obtainAsGdxArray(nb: Int): GdxArray[T] = {
    val array = new GdxArray[T](nb)
    for (i <- 0 to nb -1)
      array.set(i, obtain())
    array
  }

  def obtainAsList(nb: Int): List[T] = List.fill(nb)(obtain())
}
