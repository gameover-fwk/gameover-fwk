package gameover.fwk.libgdx

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration

/**
 * Class to use in test depending on LibGdx. In the test class, initiate LibGdx and pass this
 * application.
 */
class GdxTest : ApplicationAdapter() {

    companion object {
        fun initializeLibGdx(): LwjglApplication {
            val config = LwjglApplicationConfiguration()
            config.width = 0
            config.height = 0
            val application = LwjglApplication(GdxTest(), config)
            Gdx.gl = FakeGL20()
            return application
        }
    }
}