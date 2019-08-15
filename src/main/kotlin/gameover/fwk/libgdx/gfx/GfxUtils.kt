package gameover.fwk.libgdx.gfx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.utils.ScreenUtils

object GfxUtils {

    /**
     * Take a screenshot of the GDX viewport.
     */
    fun takeScreenShot() : Pixmap {
        val width = Gdx.graphics.width
        val height = Gdx.graphics.height
        val pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, width, height)
        val pixels = pixmap.pixels
        val amountOfBytes = width * height * 4
        val lines = ByteArray(amountOfBytes) { 0 }
        val amountOfBytesPerLine = width * 4
        for (i in 0 until height) {
            pixels.position((height - i - 1) * amountOfBytesPerLine)
            pixels.get(lines, i * amountOfBytesPerLine, amountOfBytesPerLine)
        }
        pixels.clear()
        pixels.put(lines)
        return pixmap
    }

}