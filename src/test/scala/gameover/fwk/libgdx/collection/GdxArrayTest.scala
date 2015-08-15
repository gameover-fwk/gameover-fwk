package gameover.fwk.libgdx.collection

import org.scalatest.FlatSpec

class GdxArrayTest extends FlatSpec {
  "An empty GDXArray" should "have size 0" in {
    val array = GdxArray[Int]()
    assert(array.size == 0)
    assert(array.isEmpty)
  }

  it should "produce NoSuchElementException when head is invoked" in {
    intercept[NoSuchElementException] {
      val array = GdxArray[Int]()
      array.head
    }
  }

  it should "produce NoSuchElementException when tail is invoked" in {
    intercept[NoSuchElementException] {
      val array = GdxArray[Int]()
      array.tail
    }
  }

  it should "has a size of 1 when we an element is added" in {
    val array = GdxArray[Int]()
    array.add(123)
    assert(array.size == 1)
  }

  it should "be useable with a foreach" in {
    val array = GdxArray[Int]()
    var list: List[Int] = Nil
    array.foreach((x: Int) => list = x :: list)
    assert(array.size == list.size)
  }

  it should "be useable with map" in {
    val array = GdxArray[Int]()
    val size = array.size
    array.map((x: Int) => x * 2)
    assert(array.size == size)
  }

  it should "be useable with filter" in {
    val array = GdxArray[Int]()
    array.filter((x: Int) => x % 2 == 0)
    assert(array.isEmpty)
  }

  "A GDX array containing one element" should "return an empty array when tail is invoked" in {
    val array = GdxArray[Int](123)
    assert(array.size == 1)
    assert(array.tail.isEmpty)
  }

  it should "returns the element is head is invoked" in {
    val array = GdxArray[Int](123)
    array.head
  }

  val array = GdxArray[Int](1,2,3,4,5)

  "A GDXArray containing 5 elements" should "be useable with a foreach" in {
    var list: List[Int] = Nil
    array.foreach((x: Int) => list = x :: list)
    assert(array.size == list.size)
  }

  it should "be clonable" in {
    val array2 = array.copy
    assert(array == array2)
    assert(array ne array2)
  }

  it should "be useable with map" in {
    val size = array.size
    val array2 = array.copy.map((x: Int) => x * 2)
    assert(array2.size == size)
    assert(array2.get(0) == 2)
    assert(array2.get(4) == 10)
  }

  it should "be useable with filter" in {
    val size = array.size
    val array2 = array.copy.filter((x: Int) => x % 2 == 0)
    assert(array2.size == 2)
  }

  it should "return the sum of all elements when reducing left or right with an add operation" in {
    val res = array.reduceLeft(_ + _)
    assert(res == 15)
    val res2 = array.reduceRight(_ + _)
    assert(res == res2)
  }
}
