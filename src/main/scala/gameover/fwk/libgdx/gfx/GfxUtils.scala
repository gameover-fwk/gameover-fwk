package gameover.fwk.libgdx.gfx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.utils.ScreenUtils
import gameover.fwk.libgdx.utils.LibGDXHelper

object GfxUtils extends LibGDXHelper {

  def takeScreenShot() : Pixmap = {
    val width = Gdx.graphics.getWidth
    val height = Gdx.graphics.getHeight
    val pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, width, height)
    val pixels = pixmap.getPixels
    val amountOfBytes = width * height * 4
    val lines = new BadlogicGdxArray[Byte](amountOfBytes)
    val amountOfBytesPerLine: Int = width * 4
    for (i <- 0 to height - 1) {
      pixels.position((height - i - 1) * amountOfBytesPerLine)
      pixels.get(lines, i * amountOfBytesPerLine, amountOfBytesPerLine)
    }
    pixels.clear()
    pixels.put(lines)
    pixmap
  }
}
