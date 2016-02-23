package gameover.fwk.gdx.Font

import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration}
import com.badlogic.gdx.graphics.g2d.{BitmapFont, GlyphLayout, SpriteBatch}
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera}
import com.badlogic.gdx.{ApplicationListener, Files, Gdx}

/**
  * A viewer for BM Font. See `http://www.angelcode.com/products/bmfont/doc/file_format.html`
  */
object BMFontViewer extends App {
  val WIDTH: Int = 1400
  val HEIGHT: Int = 600
  val RATIO: Float = 0.5f

  val config: LwjglApplicationConfiguration = new LwjglApplicationConfiguration
  config.title = "BM Font Viewer"
  config.addIcon("icon.png", Files.FileType.Internal)
  config.width = WIDTH
  config.height = HEIGHT
  new LwjglApplication(new BMFontViewerApp, config)
}

class GfxContext {
  var font: BitmapFont = new BitmapFont(Gdx.files.internal("simplest_font.fnt"))
  var spriteBatch: SpriteBatch = new SpriteBatch()
  var glyphLayout: GlyphLayout = new GlyphLayout()
  val cam = new OrthographicCamera(BMFontViewer.WIDTH * BMFontViewer.RATIO, BMFontViewer.HEIGHT * BMFontViewer.RATIO)
  cam.position.x = 0
  cam.position.y = 0
  cam.update()
  spriteBatch.setProjectionMatrix(cam.combined)
}

class BMFontViewerApp extends ApplicationListener {

  var optionalContext: Option[GfxContext] = None

  override def create(): Unit = {
    this.optionalContext = Some(new GfxContext)
  }

  override def resize(width: Int, height: Int) {}

  def draw(context: GfxContext): Unit = {
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
                     |""".stripMargin('|')
    context.glyphLayout.setText(context.font, text)
    context.spriteBatch.begin()
    val w: Int = (context.glyphLayout.width * BMFontViewer.RATIO).toInt
    val h: Int = (context.glyphLayout.height * BMFontViewer.RATIO).toInt
    context.font.draw(
      context.spriteBatch,
      text,
      - context.glyphLayout.width / 2,
      context.glyphLayout.height / 2)
    context.spriteBatch.end()
  }

  override def render() {

    Gdx.gl.glClearColor(0, 255, 0, 0)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    optionalContext match {
      case Some(context) =>
        draw(context)
    }
  }

  override def pause() {
  }

  override def resume(){
  }

  override def dispose(): Unit ={
    optionalContext match {
      case Some(context) =>
        context.font.dispose()
      case _ =>
    }
  }
}
