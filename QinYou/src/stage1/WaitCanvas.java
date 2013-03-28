package stage1;

import javax.microedition.lcdui.Canvas;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.microedition.lcdui.Graphics;
import mainMenu.MainMidlet;

/**
 *
 * @author Administrator
 */
public class WaitCanvas extends Canvas {

    private String tipString = "null";
    private MainMidlet midlet;

    public WaitCanvas(MainMidlet mainMidlet) {
        this.setFullScreenMode(true);
        midlet = mainMidlet;
    }
    private MazeServer server;
    private MazeClient client;

    protected void paint(Graphics g) {
        g.setColor(0xfff);
        g.fillRect(0, 0, 176, 220);
        g.setColor(0xffffff);
        g.drawString(tipString, 30, 110, Graphics.TOP | Graphics.LEFT);
        g.drawString("取消", 140, 200, Graphics.TOP | Graphics.LEFT);
    }

    public void setString(String str) {
        this.tipString = str;
        this.repaint();
    }

    protected void keyPressed(int code) {
        if (code == -7)//右软键
        {

            if (client != null) {
                client.cancelSearch();
            }
            if (server != null) {
                server.cancelService();
            }
            midlet.mainMenuShow(8);//进入主菜单
        }
    }

    public void setClient(MazeClient client) {
        this.client = client;
    }

    public void setServer(MazeServer server) {
        this.server = server;
    }
}