package gameover.fwk.libgdx.gfx

import com.badlogic.gdx.graphics.{TextureData, Texture, Pixmap}
import com.badlogic.gdx.graphics.g2d.{NinePatch, Animation}
import com.badlogic.gdx.math.Rectangle
import gameover.fwk.gdx.GdxTest
import junit.framework.TestSuite
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, BeforeAndAfter}

class GraphicsLoaderTest extends FlatSpec {

  GdxTest.initializeLibGdx()
  val graphicsLoader = new GraphicsLoader()

  "GraphicsLoader" should "scan files and load images" in {
    assert(graphicsLoader.size > 0)
  }

  it should "load properly an image as a pixmap and a texture" in {
    graphicsLoader.pixmap("pad_area") match {
      case Some(pixmap) =>
      case _ => fail("Pixmap is not found")
    }

    graphicsLoader.texture("pad_area") match {
      case Some(texture) =>
      case _ => fail("Texture is not found")
    }
  }

  it should "load a nine patch getting information from filename" in {
    graphicsLoader.ninePatch("bar_window") match {
      case Some(ninePatch) =>
      case _ => fail("Nine patch is not found")
    }

    graphicsLoader.texture("pad_area") match {
      case Some(texture) =>
      case _ => fail("Texture is not found")
    }
  }


  it should "load properly an animation with the accurate number of frames and duration" in {
    graphicsLoader.animation("professor_move_right") match {
      case Some(anim) =>
        assert(anim.anim.getFrameDuration == 0.08f)
        assert(anim.anim.getKeyFrames.length == 4)
        assert(anim.area == new Rectangle(5f, 5f, 3f, 3f))
      case _ => fail("Animation is not found")
    }
  }

  it should "create a 'stand' animation from a 'move' animation containing the first frame only" in {
    val moveAnimation = graphicsLoader.animation("professor_move_right")
    assume(moveAnimation.isDefined)
    graphicsLoader.animation("professor_stand_right") match {
      case Some(standAnim) =>
        assert(standAnim.anim.getKeyFrames.length == 1)
        val td1: TextureData = standAnim.anim.getKeyFrames()(0).getTexture.getTextureData
        td1.prepare()
        val td2: TextureData = moveAnimation.get.anim.getKeyFrames()(0).getTexture.getTextureData
        td2.prepare()
        assert(td1.consumePixmap().getPixels == td2.consumePixmap().getPixels)
        td1.disposePixmap()
        td2.disposePixmap()
      case _ => fail("Stand animation is not found")
    }
  }
}
