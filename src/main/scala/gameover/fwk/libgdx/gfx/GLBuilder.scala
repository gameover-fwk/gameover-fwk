package gameover.fwk.libgdx.gfx

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.{Pixmap, Texture}
import com.badlogic.gdx.graphics.g2d.{Animation, TextureRegion, NinePatch}
import com.badlogic.gdx.utils.Array

/**
 * Created by olmartin on 2016-02-09.
 */
trait GLBuilder {
  def newNinePatch(texture: Texture, left: Int, right: Int, top: Int, bottom: Int): NinePatch
  def newPixmap(fileHandle: FileHandle): Pixmap
  def newAnimation(frameDuration: Float, animArray: Array[TextureRegion]): Animation
  def newTexture(animationFile: FileHandle): Texture
}
