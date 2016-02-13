package gameover.fwk.gdx

import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration}
import com.badlogic.gdx.graphics.{GL20, GL30}
import com.badlogic.gdx._
import org.scalamock.scalatest.MockFactory
import org.scalatest._


/**
 * Class to use in test depending on LibGdx. In the test class, initiate LibGdx and pass this
 * application.
 */
object GdxTest{
  def initializeLibGdx(): LwjglApplication = {
    val config = new LwjglApplicationConfiguration()
    config.width = 0
    config.height = 0
    val application: LwjglApplication = new LwjglApplication(new GdxTest, config)
    Gdx.gl = new FakeGL20
    application
  }
}
class GdxTest extends ApplicationAdapter {
}