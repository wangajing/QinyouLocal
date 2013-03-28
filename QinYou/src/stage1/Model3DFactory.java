package stage1;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * 程序说明：
 * 本程序作者为王阿晶，版权所有，请勿翻版。
 */
import java.io.IOException;
import javax.microedition.m3g.Group;
import javax.microedition.m3g.Loader;
import javax.microedition.m3g.Mesh;
import javax.microedition.m3g.Object3D;
import javax.microedition.m3g.PolygonMode;
import javax.microedition.m3g.World;

/**
 *
 * @author WangJing
 * @createtime 8:59:54
 * @modifiedTime 8:59:54
 *
 */
public class Model3DFactory {

    private Mesh plane;
    private Mesh groundPlane;
    private Group smallMap;
    private Group fullMap;
    private Group queryMask;
    private Group coin;
    private Mesh ball;
    private Group box;
    private Mesh centerPlane;

    public Mesh getCenterPlane() {
        return centerPlane;
    }

    public Group getBox() {
        return box;
    }

    public Mesh getBall() {
        return ball;
    }

    public Model3DFactory() {
        World world = null;
        try {
            Object3D[] objects = Loader.load("/res/migongchangjing.m3g");
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] instanceof World) {
                    world = (World) objects[i];
                }
            }
            groundPlane = (Mesh) world.find(1);
            plane = (Mesh) world.find(2);
            fullMap = (Group) world.find(3);
            smallMap = (Group) world.find(4);
            queryMask = (Group) world.find(5);
            coin = (Group) world.find(6);
            ball = (Mesh) world.find(7);
            box = (Group) world.find(8);
            centerPlane = MeshFactory.createPlane("/res/opoint.png", PolygonMode.CULL_FRONT );
            world = null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Group getFullMap() {
        return fullMap;
    }

    public Group getQueryMask() {
        return queryMask;
    }

    public Group getSmallMap() {
        return smallMap;
    }

    public Mesh getGroundPlane() {
        return groundPlane;
    }

    public Mesh getPlane() {
        return plane;
    }

    public Group getCoin() {
        return coin;
    }
}
