package gameover.fwk.libgdx.utils

import com.badlogic.gdx.math.GridPoint2

import scala.collection.immutable
import scala.reflect.ClassTag

trait LibGDXHelper {

  def toString(gridPoint2: GridPoint2): String = s"[${gridPoint2.x},${gridPoint2.y}]"

  def toString(gridPoint2s: Array[GridPoint2]): String = "[" + gridPoint2s.map(toString).mkString(", ") + "]"

  type GdxArray[T] = com.badlogic.gdx.utils.Array[T]

  implicit class GdxArrayAsScalaCollection[T](internalArray: GdxArray[T]) {

    def peekFirst(): T = head
    def peekFirstOption(): Option[T] = headOption
    def popFirst(): T = if (size > 0) internalArray.removeIndex(0) else throw new NoSuchElementException("Array is empty")
    def popFirstOption(): Option[T] = if (size > 0) Some(popFirst()) else None
    def indices: Iterable[Int] = Range.apply(0, length)
    def head: T = if (size > 0) internalArray.get(0) else throw new NoSuchElementException("Array is empty")
    def headOption: Option[T] = if (size > 0) Some(head) else None
    def last: T = if (size > 0) internalArray.get(lastIndex) else throw new NoSuchElementException("Array is empty")
    def lastOption: Option[T] = if (size > 0) Some(last) else None
    def tail: GdxArray[T] = drop(1)
    def lastIndex = size - 1
    def exists(p: (T) => Boolean): Boolean = find(p).isDefined
    def length: Int = internalArray.size
    def size: Int = internalArray.size
    def isEmpty: Boolean = internalArray.size == 0
    def nonEmpty: Boolean = internalArray.size != 0
    def copy: GdxArray[T] = new GdxArray[T](internalArray)

    def drop(nb: Int): GdxArray[T] = {
      if (nb <= size) {
        internalArray.removeRange(0, nb - 1)
        internalArray
      } else throw new NoSuchElementException(s"Array do not contains $nb elements")
    }

    def map[R](f: T => R): GdxArray[R] = {
      val newArray = new GdxArray[R]()
      for (i <- indices) {
        val newValue: R = f(internalArray.get(i))
        newArray.add(newValue)
      }
      newArray
    }

    def flatMap[R](f: T => GdxArray[R]): GdxArray[R] = {
      val newArray = new GdxArray[R]()
      for (i <- indices) {
        val newValue: GdxArray[R] = f(internalArray.get(i))
        newArray.addAll(newValue)
      }
      newArray
    }

    def drop(f: T => Boolean) {
      for (i <- size - 1 to 0 by -1) {
        val value: T = internalArray.get(i)
        if (f(value)) {
          internalArray.removeIndex(i)
        }
      }
    }

    def filter(f: T => Boolean): GdxArray[T] = {
      val newArray = new GdxArray[T]()
      for (i <- indices) {
        val value: T = internalArray.get(i)
        if (f(value)) {
          newArray.add(value)
        }
      }
      newArray
    }

    def foreach[U](f: T => U) {
      for (i <- indices) {
        val value: T = internalArray.get(i)
        f(value)
      }
    }

    def forall(p: (T) ⇒ Boolean): Boolean = {
      for (i <- indices) {
        val value: T = internalArray.get(i)
        if (!p(value))
          return false
      }
      true
    }

    def update(idx: Int, elem: T) {
      internalArray.set(idx, elem)
    }

    def min[B >: T](implicit cmp: Ordering[B]): T = {
      if (isEmpty)
        throw new UnsupportedOperationException("empty.min")

      reduceLeft((x, y) => if (cmp.lteq(x, y)) x else y)
    }

    def max[B >: T](implicit cmp: Ordering[B]): T = {
      if (isEmpty)
        throw new UnsupportedOperationException("empty.max")

      reduceLeft((x, y) => if (cmp.gteq(x, y)) x else y)
    }

    def find(p: (T) => Boolean): Option[T] = {
      if (isEmpty)
        None
      else {
        for (i <- indices) {
          val elt: T = internalArray.get(i)
          if (p(elt))
            return Some(elt)
        }
        None
      }
    }

    def +(value: T): GdxArray[T] = {
      this += value
      internalArray
    }

    def +=(value: T) {
      internalArray.add(value)
    }

    def ++(value: GdxArray[T]): GdxArray[T] = {
      this ++= value
      internalArray
    }

    def ++=(value: GdxArray[T]) {
      internalArray.addAll(value)
    }

    def ::(value: T): GdxArray[T] = {
      this ::= value
      internalArray
    }

    def ::=(value: T) {
      internalArray.insert(0, value)
    }

    def -(value: T): GdxArray[T] = {
      this -= value
      internalArray
    }

    def -=(value: T) {
      internalArray.removeValue(value, true)
    }

    def reduceLeft(f: (T, T) => T): T = {
      if (size == 0)
        throw new NoSuchElementException("Array is empty")
      else if (size == 1)
        peekFirst()
      else {
        var res = peekFirst()
        for (i <- 1 to size - 1) {
          res = f(res, internalArray.get(i))
        }
        res
      }
    }

    def reduceRight(f: (T, T) => T): T = {
      if (size == 0)
        throw new NoSuchElementException("Array is empty")
      else if (size == 1)
        internalArray.peek()
      else {
        var res = internalArray.peek()
        for (i <- size - 2 to 0 by -1) {
          res = f(res, internalArray.get(i))
        }
        res
      }
    }

    def toMap[A, B](f: T => (A, B)): immutable.Map[A, B] = {
      val b = immutable.Map.newBuilder[A, B]
      for (i <- indices)
        b += f(internalArray.get(i))
      b.result()
    }

    def asArray[T:ClassTag](gdxArray: GdxArray[T]): Array[T] = {
      val builder = Array.newBuilder[T]
      for (i <- 0 to gdxArray.size - 1)
        builder += gdxArray.get(i)
      builder.result()
    }

    def asList[T:ClassTag](gdxArray: GdxArray[T]): List[T] = {
      val builder = List.newBuilder[T]
      for (i <- 0 to gdxArray.size - 1)
        builder += gdxArray.get(i)
      builder.result()
    }

    def fill(nb: Int)(f: () => T): GdxArray[T] = {
      for (i <- 1 to nb)
        internalArray.add(f())
      internalArray
    }
  }
}
