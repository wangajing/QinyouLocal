package stage1;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class PointInfo {
    public static final int NOWAY = 15;
    public byte x;
    public byte y;
    public byte direction;
    public byte initDirection;
    public static final byte UP = 1;
    public static final byte RIGHT = 2;
    public static final byte DOWN = 4;
    public static final byte LEFT = 8;

    public PointInfo(int x, int y,int init) {
        this.x = (byte)x;
        this.y = (byte)y;
        direction = UP;
        initDirection =(byte)init;
    }

    public PointInfo move(int m, int n,int init) {
        return new PointInfo(x+m,y+n,init);
    }
      
}
