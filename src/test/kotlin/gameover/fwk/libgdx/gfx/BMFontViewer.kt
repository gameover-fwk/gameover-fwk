package gameover.fwk.libgdx.gfx

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx

object BMFontViewer {
    val RATIO = 0.5f
    val WIDTH = 1400
    val HEIGHT = 600

    @JvmStatic
    fun main(args: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.title = "BM Font Viewer"
        config.addIcon("icon.png", Files.FileType.Internal)
        config.width = WIDTH
        config.height = HEIGHT
        LwjglApplication(BMFontViewerApp(), config)
    }
}

class GfxContext {
    var font: BitmapFont = BitmapFont(Gdx.files.internal("simplest_font.fnt"))
    var spriteBatch = SpriteBatch()
    var glyphLayout = GlyphLayout()
    val cam = OrthographicCamera(BMFontViewer.WIDTH * BMFontViewer.RATIO, BMFontViewer.HEIGHT * BMFontViewer.RATIO)
    init {
        cam.position.x = 0f
        cam.position.y = 0f
        cam.update()
        spriteBatch.setProjectionMatrix(cam.combined)
    }
}

class BMFontViewerApp : ApplicationListener {

    var optionalContext: GfxContext? = null

    override fun create() {
        this.optionalContext = GfxContext()
    }

    override fun resize(width: Int, height: Int) {}

    fun draw(context: GfxContext) {
        val text = """
                     |Bocconcini fondue cheesy grin. Taleggio, 1 boursin, 3 manchego cheese
                     |slices danish fontina, 76 boursin taleggio 4 airedale. Taleggio red
                     |24 leicester cheesy 5 feet cheddar/caerphilly ricotta cheesecake
                     |camembert de normandie. Rubber cheese bavarian with 678 bergkase
                     |cheddar taleggio goat cream cheese: gouda taleggio? Cheesy grin!
                     |+3 -23 Awesome!!! Wait? What are u doing??
                     |Cheeseburger cheese on toast gouda. Camembert de normandie
                     |st. agur blue cheese say cheese st. agur blue cheese monterey
                     |jack fromage roquefort cut the cheese. Brie mascarpone rubber cheese
                     |cheesy feet cheesecake lancashire swiss cheese triangles.
                     |Brie monterey jack cheese on toast bavarian bergkase ricotta
                     |emmental feta squirty cheese. Mascarpone cheese slices everyone loves.
                     |""".trimMargin("|")
        context.glyphLayout.setText(context.font, text)
        context.spriteBatch.begin()
        //val w: Int = (context.glyphLayout.width * BMFontViewer.RATIO).toInt()
        //val h: Int = (context.glyphLayout.height * BMFontViewer.RATIO).toInt()
        context.font.draw(
                context.spriteBatch,
                text,
                - context.glyphLayout.width / 2,
                context.glyphLayout.height / 2)
        context.spriteBatch.end()
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 255f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        optionalContext?.let { draw(it) }
    }

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {
        if (optionalContext != null) {
            optionalContext?.font?.dispose()
        }
    }
}