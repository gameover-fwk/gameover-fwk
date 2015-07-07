package gameover.fwk.libgdx.utils

import com.badlogic.gdx.math.GridPoint2
import gameover.fwk.libgdx.collection.GdxArray

import scala.reflect.ClassTag

trait LibGDXHelper {

  def toString(gridPoint2: GridPoint2): String = s"[${gridPoint2.x},${gridPoint2.y}]"

  def toString(gridPoint2s: Array[GridPoint2]): String = "[" + gridPoint2s.map(toString).mkString(", ") + "]"

  type BadlogicGdxArray[T] = com.badlogic.gdx.utils.Array[T]

  implicit def gdxArrayWrapper[T:ClassTag](gdxArray: com.badlogic.gdx.utils.Array[T]): GdxArray[T] = {
    val scalaGdxArray = GdxArray[T]()
    for (i <- 0 to gdxArray.size - 1)
      scalaGdxArray += gdxArray.get(i)
    scalaGdxArray
  }

  implicit def arrayWrapper[T:ClassTag](gdxArray: com.badlogic.gdx.utils.Array[T]): Array[T] = {
    val builder = Array.newBuilder[T]
    for (i <- 0 to gdxArray.size - 1)
      builder += gdxArray.get(i)
    builder.result()
  }

  implicit def listWrapper[T:ClassTag](gdxArray: com.badlogic.gdx.utils.Array[T]): List[T] = {
    val builder = List.newBuilder[T]
    for (i <- 0 to gdxArray.size - 1)
      builder += gdxArray.get(i)
    builder.result()
  }
}
