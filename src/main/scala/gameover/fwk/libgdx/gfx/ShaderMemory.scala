package gameover.fwk.libgdx.gfx

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram

import scala.collection.mutable

object ShaderMemory {

  private val shadersPerSpriteBatches = new mutable.HashMap[SpriteBatch, List[ShaderProgram]]

  private def findShaders(batch: SpriteBatch): List[ShaderProgram] = {
    val entries: Option[List[ShaderProgram]] = shadersPerSpriteBatches.get(batch)
    entries match {
      case Some(sp) => sp
      case None => Nil
    }
  }

  def switchShader(batch: SpriteBatch, newShader: ShaderProgram): ShaderProgram = {
    var shaders: List[ShaderProgram] = findShaders(batch)
    var old: ShaderProgram = null
    if (shaders.nonEmpty) {
      old = shaders.head
      shaders = shaders.tail
    }
    shaders = newShader :: shaders
    shadersPerSpriteBatches.put(batch, shaders)
    batch.setShader(newShader)
    old
  }

  def setShader(batch: SpriteBatch, newShader: ShaderProgram) {
    val shaders: List[ShaderProgram] = findShaders(batch)
    shadersPerSpriteBatches.put(batch, newShader :: shaders)
    batch.setShader(newShader)
  }

  def removeCurrentShader(batch: SpriteBatch) {
    var shaders: List[ShaderProgram] = findShaders(batch)
    if (shaders.nonEmpty) {
      shaders = shaders.tail
      shadersPerSpriteBatches.put(batch, shaders)
      if (shaders.nonEmpty) batch.setShader(shaders.head)
    }
  }

  def clearShader(batch: SpriteBatch) {
    shadersPerSpriteBatches.put(batch, Nil)
    batch.setShader(null)
  }
}
