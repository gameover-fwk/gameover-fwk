package gameover.fwk.libgdx.gfx

import com.badlogic.gdx.graphics.{Texture, Pixmap}
import com.badlogic.gdx.graphics.g2d.{NinePatch, Animation}
import gameover.fwk.gdx.GdxTest
import junit.framework.TestSuite
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, BeforeAndAfter}

class GraphicsLoaderTest extends FlatSpec {

  "GraphicsLoader" should "scan files and load images" in {
    GdxTest.initializeLibGdx()
    val graphicsLoader = new GraphicsLoader()
    assert(graphicsLoader.size > 0)
  }
}
