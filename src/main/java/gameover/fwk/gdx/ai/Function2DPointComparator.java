package gameover.fwk.gdx.ai;

import com.badlogic.gdx.math.Vector2;

import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: Coin
 * Date: 23/07/14
 * Time: 16:31
 * To change this template use File | Settings | File Templates.
 */
public class Function2DPointComparator implements Comparator<Vector2> {
    private final float xSign;
    private final float ySign;

    public Function2DPointComparator(float xSign, float ySign) {
        this.xSign = xSign;
        this.ySign = ySign;
    }


    @Override
    public int compare(Vector2 o1, Vector2 o2) {
        float signum = Math.signum(o1.x - o2.x);
        if(signum==0){
            return (int) (Math.signum(o1.x - o2.x)*ySign);
        } else {
            return (int) (signum*xSign);
        }
    }
}
