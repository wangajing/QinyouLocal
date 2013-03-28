package stage2;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class Point {

    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        x = 0;
        y = 0;
    }

    Point(Point p) {
        x = p.x;
        y = p.y;
    }

    Point addX(int i) {
        return new Point(x + i, y);
    }

    Point addY(int y) {
        return new Point(x, y + this.y);
    }

    public Point move(int dx, int dy) {
        return new Point(x + dx, y + dy);
    }
}
