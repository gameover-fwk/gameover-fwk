package gameover.fwk.libgdx.utils

import com.badlogic.gdx.math.GridPoint2

import scala.reflect.ClassTag

trait LibGDXHelper {

  def toString(gridPoint2: GridPoint2): String = s"[${gridPoint2.x},${gridPoint2.y}]"

  def toString(gridPoint2s: Array[GridPoint2]): String = "[" + gridPoint2s.map(toString).mkString(", ") + "]"

  type GdxArray[T] = com.badlogic.gdx.utils.Array[T]

  implicit def arrayWrapper[T:ClassTag](gdxArray: GdxArray[T]): Array[T] = {
    val builder = Array.newBuilder[T]
    for (i <- 0 to gdxArray.size - 1)
      builder += gdxArray.get(i)
    builder.result()
  }

  implicit def listWrapper[T:ClassTag](gdxArray: GdxArray[T]): List[T] = {
    val builder = List.newBuilder[T]
    for (i <- 0 to gdxArray.size - 1)
      builder += gdxArray.get(i)
    builder.result()
  }
}
