package gameover.fwk.utils;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;

/**
 * Utilities for Lib GDX that should be push to official libs
 */
public final class LibGdxUtils {

    private LibGdxUtils(){

    }

    public static String toString(GridPoint2 gridPoint2){
        return "["+gridPoint2.x+","+gridPoint2.y+"]";
    }

    public static String toString(Array<GridPoint2> gridPoint2s){
        StringBuilder builder = new StringBuilder("[");
        for(int i=0, max=gridPoint2s.size; i<max; i++){
            if(i>0) builder.append(", ");
            builder.append(toString(gridPoint2s.get(i)));
        }
        builder.append("]");
        return builder.toString();
    }

}
