package gameover.fwk.libgdx.collection

import java.util.Comparator

import com.badlogic.gdx.utils.{Array, Predicate}

import scala.collection.immutable

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
  def peek() : T = internalArray.peek()
  def pop() : T = internalArray.pop()
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
  def head = if (size > 0) internalArray.first() else throw new NoSuchElementException("Array is empty")
  def headOption = if (size > 0) Some(internalArray.first()) else None
  def last = internalArray.get(size - 1)
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

//  override def update(idx: Int, elem: T) {
//    set(idx, elem)
//  }

  def length: Int = internalArray.size
  def size: Int = internalArray.size
  def isEmpty: Boolean = internalArray.size == 0
  def nonEmpty: Boolean =  internalArray.size != 0

  def +(value: T) : GdxArray[T] = {
    val newArray = copy
    newArray += value
  }

  def +=(value: T) : GdxArray[T] = {
    add(value)
    this
  }

  def ++(value: GdxArray[T]) : GdxArray[T] = {
    val newArray = copy
    newArray.addAll(value)
    newArray
  }

  def ++=(value: GdxArray[T]) : GdxArray[T] = {
    addAll(value)
    this
  }

  def ::(value: T) : GdxArray[T] = {
    val newArray = copy
    newArray.insert(0, value)
    newArray
  }

  def ::=(value: T) : GdxArray[T] = {
    insert(0, value)
    this
  }

  def -(value: T) : GdxArray[T] = {
    val newArray = copy
    newArray.removeValue(value, identity = true)
    newArray
  }

  def -=(value: T) : GdxArray[T] = {
    removeValue(value, identity = true)
    this
  }

  def copy : GdxArray[T] = GdxArray[T](internalArray)

  def tail : GdxArray[T] = {
    if (size == 0)
      throw new NoSuchElementException("Array is empty")
    val c = copy
    c.removeIndex(0)
    c
  }

  def reduceLeft(f: (T, T) => T) : T = {
    if (size == 0)
      throw new NoSuchElementException("Array is empty")
    else if (size == 1)
      head
    else {
      var res = head
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
      head
    else {
      var res = last
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
