package gameover.fwk.gdx.ai;

/**
 * Class to hold a Point during A* computation.
 */
public class Point {
    int x;
    int y;
    int g;
    int h;
    Point ancestor;
    int index;

    Point(int x, int y, int g, int h, Point ancestor) {
        this.x = x;
        this.y = y;
        this.g = g;
        this.h = h;
        this.ancestor = ancestor;
        this.index = ancestor!=null?ancestor.index+1:0;
    }

    @Override
    public boolean equals(Object obj) {
        Point p = (Point)obj;
        return is(p.x, p.y);
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString(){
        if(ancestor==null)
            return x+","+y;
        else
            return x+","+y+"<="+ancestor.toString();
    }

    int f(){
        return g + h;
    }

    public boolean is(int tx, int ty) {
        return tx==x && ty==y;
    }
}
