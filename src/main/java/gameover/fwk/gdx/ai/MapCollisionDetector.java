package gameover.fwk.gdx.ai;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import gameover.fwk.utils.RectanglePool;
import gameover.fwk.utils.Vector2Pool;

/**
 * A class to detect collision.
 */
public class MapCollisionDetector extends BasicCollisionDetector implements CollisionDetector{

    private Array<CollisionState> collisionTiles = new Array<CollisionState>();

    private enum State {BLOCKING, VOID};

    private int width, height;

    private class CollisionState{
        CollisionState(Rectangle r, State s){
            this.state = s;
            this.r = r;
        }
        State state;
        Rectangle r;
    }

    public void clear() {
        for(CollisionState cs : collisionTiles)
            RectanglePool.free(cs.r);
        collisionTiles.clear();
    }


    public void loadCollisionFromMap(TiledMap map) {
        MapLayers layers = map.getLayers();
        width = ((TiledMapTileLayer)layers.get(0)).getWidth();
        height = ((TiledMapTileLayer)layers.get(0)).getHeight();
        for(int l=0; l<layers.getCount(); l++){
            MapLayer layer = layers.get(l);
            if("yes".equals(layer.getProperties().get("collision"))){
                analyseLayer(layer, State.BLOCKING);
            }
            if("yes".equals(layer.getProperties().get("fall"))){
                analyseLayer(layer, State.VOID);
            }
        }
    }

    private void analyseLayer(MapLayer layer, State state) {
        if(layer instanceof TiledMapTileLayer){
            TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer)layer;
            for (int y = 0; y < tiledMapTileLayer.getHeight(); y++) {
                for (int x = 0; x < tiledMapTileLayer.getWidth(); x++) {
                    TiledMapTileLayer.Cell cell = tiledMapTileLayer.getCell(x, y);
                    if (cell != null) {
                        Rectangle rect = RectanglePool.obtain();
                        rect.set(x, y, 1, 1);
                        collisionTiles.add(new CollisionState(rect, state));
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Can't analyse class "+layer.getClass()+". This class is not managed currently by the code");
        }
    }



    public boolean checkCollision(Rectangle r, boolean onlyBlocking) {
        if(r.x<0 || r.x>width || r.y<0 || r.y>height){
            return true;
        }
        for (int t=0; t< collisionTiles.size; t++) {
            CollisionState cs = collisionTiles.get(t);
            if (r.overlaps(cs.r)
                    && (!onlyBlocking || cs.state==State.BLOCKING)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkCollision(int tileX, int tileY, boolean onlyBlocking) {
        if(tileX<0 || tileX>width || tileY<0 || tileY>height){
            return true;
        }
        for (int t=0; t< collisionTiles.size; t++) {
            CollisionState cs = collisionTiles.get(t);
            if ((int)cs.r.x == tileX && (int)cs.r.y == tileY
                    && (!onlyBlocking || cs.state==State.BLOCKING)) {
                return true;
            }
        }
        return false;
    }

    public Array<Rectangle> checkCollision(Rectangle collisionArea, Vector2 movingSpriteVelocity, boolean onlyBlocking) {
        Array<Rectangle> ret = new Array<Rectangle>();
        Vector2 c1 = Vector2Pool.obtain(collisionArea.x, collisionArea.y);
        Vector2 c2 = Vector2Pool.obtain(collisionArea.x + collisionArea.width, collisionArea.y);
        Vector2 c3 = Vector2Pool.obtain(collisionArea.x, collisionArea.y + collisionArea.height);
        Vector2 c4 = Vector2Pool.obtain(collisionArea.x + collisionArea.width, collisionArea.y + collisionArea.height);

        Vector2 v1 = Vector2Pool.obtain();
        Vector2 v2 = Vector2Pool.obtain();
        Vector2 v3 = Vector2Pool.obtain();
        Vector2 v4 = Vector2Pool.obtain();

        if (movingSpriteVelocity.x != 0 || movingSpriteVelocity.y != 0) {
            for (int r=0; r< collisionTiles.size; r++) {
                CollisionState cs = collisionTiles.get(r);
                Rectangle tile = cs.r;
                if (collisionArea.overlaps(tile)
                        && (!onlyBlocking || cs.state==State.BLOCKING)) {
                    //sprite.velocity.x = 0f;
                    //sprite.velocity.y = 0f;
                    //ret.add(tile);
                    v1.set(tile.x, tile.y);
                    v2.set(tile.x + tile.width, tile.y);
                    v3.set(tile.x, tile.y + tile.height);
                    v4.set(tile.x + tile.width, tile.y + tile.height);
                    //Vector2 intersection = Vector2Pool.obtain();
                    if (movingSpriteVelocity.y > 0f &&
                            (Intersector.intersectSegments(v1, v2, c1, c3, null)
                                    || Intersector.intersectSegments(v1, v2, c2, c4, null))) {
                        movingSpriteVelocity.y = 0f;
                        ret.add(RectanglePool.obtain(v1, v2));
                    }
                    if (movingSpriteVelocity.y < 0f &&
                            (Intersector.intersectSegments(v3, v4, c1, c3, null)
                                    || Intersector.intersectSegments(v3, v4, c2, c4, null))) {
                        movingSpriteVelocity.y = 0f;
                        ret.add(RectanglePool.obtain(v3, v4));
                    }
                    if (movingSpriteVelocity.x > 0f &&
                            (Intersector.intersectSegments(v1, v3, c1, c2, null)
                                    || Intersector.intersectSegments(v1, v3, c3, c4, null))) {
                        movingSpriteVelocity.x = 0f;
                        ret.add(RectanglePool.obtain(v1, v3));
                    }
                    if (movingSpriteVelocity.x < 0f &&
                            (Intersector.intersectSegments(v2, v4, c1, c2, null)
                                    || Intersector.intersectSegments(v2, v4, c3, c4, null))) {
                        movingSpriteVelocity.x = 0f;
                        ret.add(RectanglePool.obtain(v2, v4));
                    }
                }
            }
        }
        Vector2Pool.free(v1);
        Vector2Pool.free(v2);
        Vector2Pool.free(v3);
        Vector2Pool.free(v4);
        return ret;
    }



}

