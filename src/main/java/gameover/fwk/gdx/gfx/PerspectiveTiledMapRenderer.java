package gameover.fwk.gdx.gfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;

/**
 * This tiled map renderer can be used to produce a perspective effect. For this we another method called renderPerspective
 * should be used after {@link #render()} and avatar renderer to draw perspective tiles.
 */
public class PerspectiveTiledMapRenderer extends OrthogonalTiledMapRenderer{

    private final MapLayer perspectiveLayer;

    public PerspectiveTiledMapRenderer(String perspectiveLayerName, TiledMap map, float unitScale, SpriteBatch spriteBatch) {
        super(map, unitScale, spriteBatch);
        this.perspectiveLayer = map.getLayers().get(perspectiveLayerName);
    }

    @Override
    public void render() {
        AnimatedTiledMapTile.updateAnimationBaseTime();
        batch.begin();
        for (MapLayer layer : map.getLayers()) {
            if (layer!=perspectiveLayer && layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {
                    renderTileLayer((TiledMapTileLayer) layer);
                } else {
                    for (MapObject object : layer.getObjects()) {
                        renderObject(object);
                    }
                }
            }
        }
        batch.end();
    }

    public void renderPerspective() {
        batch.begin();
        if (perspectiveLayer instanceof TiledMapTileLayer) {
            renderTileLayer((TiledMapTileLayer) perspectiveLayer);
        } else {
            for (MapObject object : perspectiveLayer.getObjects()) {
                renderObject(object);
            }
        }
        batch.end();
    }
}
