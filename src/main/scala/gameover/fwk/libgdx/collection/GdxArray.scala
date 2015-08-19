package gameover.fwk.libgdx.collection

import java.util.Comparator

import com.badlogic.gdx.utils.{Array, Predicate}

import scala.collection.immutable

/**
 * GdxArray is a scala wrapper to GdxArray from libgdx. Notice that the collection is mutable for performance reason.
 *
 * @author pixelduck.game@gmail.com
 */
object GdxArray {

  def apply[T](): GdxArray[T] = new GdxArray[T]()

  def apply[T](elts: T*): GdxArray[T] = {
    val array = new Array[T](elts.size)
    elts.foreach(array.add)
    new GdxArray[T](array)
  }

  def fill[T](nb:Int)(elem: => T) : GdxArray[T] = {
    val array = new GdxArray[T]()
    for (i <- 0 to nb-1)
      array.add(elem)
    array
  }
}

case class GdxArray[T](internalArray : Array[T] = new Array[T])
  extends Serializable
  with java.lang.Cloneable {

  def asLibGDXArray() : Array[T] = internalArray

  /* methods and public members from Array */
  def items = internalArray.items
  def ordered = internalArray.ordered
  def add(value: T) = internalArray.addAll(value)
  def addAll(values: T*) { values.foreach(add) }
  def addAll[U <: T](array: GdxArray[U]) { internalArray.addAll(array.asLibGDXArray()) }
  def addAll[U <: T](array: GdxArray[U], start: Int, count: Int) { internalArray.addAll(array.asLibGDXArray(), start, count) }
  def clear() { internalArray.clear() }
  def contains (value: T, identity: Boolean) : Boolean = internalArray.contains(value, identity)
  def ensureCapacity(additionalCapacity: Int) { internalArray.ensureCapacity(additionalCapacity) }
  override def equals(a: Any) : Boolean = a match {
    case array: GdxArray[T] => internalArray.equals(array.asLibGDXArray())
    case _ => false
  }
  def first() : T = internalArray.first()
  def get(idx: Int) : T = internalArray.get(idx)
  def indexOf(value: T, identity: Boolean) : Int = internalArray.indexOf(value, identity)
  def insert(index: Int, value: T) { internalArray.insert(index, value) }
  def iterator() : java.util.Iterator[T] = internalArray.iterator()
  def lastIndexOf(value: T, identity: Boolean) : Int = internalArray.lastIndexOf(value, identity)
  def peek() : T = if (size > 0) internalArray.peek() else throw new NoSuchElementException("Array is empty")
  def peekLast() : T = peek()
  def pop() : T = if (size > 0) internalArray.pop() else throw new NoSuchElementException("Array is empty")
  def popLast() : T = pop()
  def peekFirst(): T = head
  def peekFirstOption(): Option[T] = headOption
  def popFirst() : T = if (size > 0) internalArray.removeIndex(0) else throw new NoSuchElementException("Array is empty")
  def popFirstOption(): Option[T] = if (size > 0) Some(popFirst()) else None
  def random() : T = internalArray.random()
  def removeAll(array: GdxArray[_ <: T], identity: Boolean) : Boolean = if (array != null) internalArray.removeAll(array.asLibGDXArray(), identity) else false
  def removeIndex(index: Int) : T = internalArray.removeIndex(index)
  def removeRange(start:Int, end: Int) { internalArray.removeRange(start, end) }
  def removeValue(value: T, identity: Boolean) : Boolean = internalArray.removeValue(value, identity)
  def reverse() { internalArray.reverse() }
  def select(predicate: Predicate[T]) : java.lang.Iterable[T] = internalArray.select(predicate)
  def selectRanked(comparator: Comparator[T], kthLowest: Int) = internalArray.selectRanked(comparator, kthLowest)
  def selectRankedIndex(comparator: Comparator[T], kthLowest: Int) = internalArray.selectRankedIndex(comparator, kthLowest)
  def set(index: Int, value: T) { internalArray.set(index, value) }
  def shrink() = internalArray.shrink()
  def shuffle() { internalArray.shuffle() }
  def sort() { internalArray.sort() }
  def sort(comparator: Comparator[_ >: T]) { internalArray.sort(comparator) }
  def swap(first: Int, second: Int) { internalArray.swap(first, second) }
  def toArray = internalArray.toArray
  def toArray(t: Class[_]) = internalArray.toArray(t)
  def toString(separator: String) = internalArray.toString(separator)
  def truncate(newSize: Int) = internalArray.truncate(newSize)
  def indices: Iterable[Int] = Range.apply(0, length)
  def head: T = if (size > 0) internalArray.get(0) else throw new NoSuchElementException("Array is empty")
  def headOption: Option[T] = if (size > 0) Some(head) else None
  def last: T = if (size > 0) internalArray.get(lastIndex) else throw new NoSuchElementException("Array is empty")
  def lastOption: Option[T] = if (size > 0) Some(last) else None
  def tail: GdxArray[T] = drop(1)
  def drop(nb: Int): GdxArray[T] = {
    if (nb <= size) {
      internalArray.removeRange(0, nb - 1)
      this
    } else throw new NoSuchElementException(s"Array do not contains $nb elements")
  }
  def lastIndex = size - 1

  def map[R]( f : T => R ) : GdxArray[R] = {
    val newArray = new GdxArray[R]()
    val array: Array[R] = newArray.internalArray
    for (i <- indices) {
      val newValue: R = f(get(i))
      array.add(newValue)
    }
    newArray
  }

  def flatMap[R](f: T => GdxArray[R]): GdxArray[R] = {
    val newArray = new GdxArray[R]()
    for (i <- indices) {
      val newValue: GdxArray[R] = f (get (i))
      newArray.addAll(newValue)
    }
    newArray
  }

  def drop(f: T => Boolean) {
    for (i <- size-1 to 0 by -1) {
      val value: T = get(i)
      if (f(value)) {
        internalArray.removeIndex(i)
      }
    }
  }

  def filter(f: T => Boolean): GdxArray[T] = {
    val newArray = new GdxArray[T]()
    val array: Array[T] = newArray.internalArray
    for (i <- indices) {
      val value: T = get(i)
      if (f(value)) {
        array.add(value)
      }
    }
    newArray
  }

  def foreach[U] (f: T => U) {
    for (i <- indices) {
      val value: T = get (i)
      f (value)
    }
  }

  def forall(p: (T) ⇒ Boolean): Boolean = {
    for (i <- indices) {
      val value: T = get (i)
      if (!p(value))
        return false
    }
    true
  }

  def update(idx: Int, elem: T) {
    set(idx, elem)
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
        val elt: T = get(i)
        if (p(elt))
          return Some(elt)
      }
      None
    }
  }

  def exists(p: (T) => Boolean): Boolean = find(p).isDefined

  def length: Int = internalArray.size
  def size: Int = internalArray.size
  def isEmpty: Boolean = internalArray.size == 0
  def nonEmpty: Boolean =  internalArray.size != 0

  def +(value: T) : GdxArray[T] = {
    this += value
    this
  }

  def +=(value: T) {
    add(value)
  }

  def ++(value: GdxArray[T]) : GdxArray[T] = {
    this ++= value
    this
  }

  def ++=(value: GdxArray[T]) {
    addAll(value)
  }

  def ::(value: T) : GdxArray[T] = {
    this ::= value
    this
  }

  def ::=(value: T) {
    insert(0, value)
  }

  def -(value: T) : GdxArray[T] = {
    this -= value
    this
  }

  def -=(value: T) {
    removeValue(value, identity = true)
  }

  def copy : GdxArray[T] = GdxArray[T](internalArray)

  def reduceLeft(f: (T, T) => T) : T = {
    if (size == 0)
      throw new NoSuchElementException("Array is empty")
    else if (size == 1)
      peekFirst()
    else {
      var res = peekFirst()
      for (i <- 1 to size - 1) {
        res = f(res, get(i))
      }
      res
    }
  }

  def reduceRight(f: (T, T) => T) : T = {
    if (size == 0)
      throw new NoSuchElementException("Array is empty")
    else if (size == 1)
      peekLast()
    else {
      var res = peekLast()
      for (i <- size - 2 to 0 by -1) {
        res = f(res, get(i))
      }
      res
    }
  }

  def toMap[A, B](f: T => (A, B)): immutable.Map[A, B] = {
    val b = immutable.Map.newBuilder[A, B]
    for (i <- indices)
      b += f(get(i))
    b.result()
  }
}
