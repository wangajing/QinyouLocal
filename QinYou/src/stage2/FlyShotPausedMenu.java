package stage2;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import mainMenu.MainMidlet;

/**
 *
 * @author Administrator
 */
public class FlyShotPausedMenu extends GameCanvas implements Runnable {

    private Graphics g = this.getGraphics();
    private int index;

    public int getIndex() {
        return index;
    }
    private Image backImage;//底层背景图片
    byte[][] map = { //背景图片组合地图
        {4, 4, 2, 4, 1, 4,},
        {4, 4, 4, 4, 4, 4,},
        {4, 4, 4, 4, 3, 4,},
        {4, 2, 4, 4, 4, 4,},
        {4, 4, 4, 3, 4, 4,},
        {4, 4, 4, 4, 4, 4,},
        {4, 1, 4, 4, 4, 2,},
        {4, 4, 4, 3, 4, 4,},
        {4, 4, 4, 4, 4, 4,},
        {4, 4, 2, 4, 1, 4,}
    };
    private Image continueGameImage;
    private Image musicControlOnImage;
    private Image musicControlOffImage;
    private Image quitGameImage;
    private Image backgroundImage;
    public final int CANVAS_WIDTH = 176;
    public final int CANVAS_HEIGHT = 220;
    public final int INTEVERAL_HEIGHT = 30;
    public final int CONTINUE_POS_Y = 75;
    private FlyShotCanvas gameCanvas;
    private MainMidlet mainMidlet;

    public FlyShotPausedMenu(MainMidlet mainMidlet, FlyShotCanvas gameCanvas) {
        super(false);
        this.setFullScreenMode(true);
        this.mainMidlet = mainMidlet;
        try {
            //与背景有关的初始化
            Image image = Image.createImage("/res/bg2_1.png");
            Image[] image2 = new Image[4];
            for (int i = 0; i < 4; i++) {
                image2[i] = Image.createImage(image, i * 32, 0, 32, 32, Sprite.TRANS_NONE);
            }

            backImage = Image.createImage(192, 320);
            Graphics g2d = backImage.getGraphics();
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 6; j++) {
                    g2d.drawImage(image2[map[i][j] - 1], j * 32, i * 32, Graphics.TOP | Graphics.LEFT);
                }
            }
            index = 1;
            this.gameCanvas = gameCanvas;
            image =
                    Image.createImage("/res/menuText.png");
            continueGameImage =
                    Image.createImage(image, 0, 15, 81, 15, Sprite.TRANS_NONE);
            musicControlOnImage =
                    Image.createImage(image, 0, 255, 81, 15, Sprite.TRANS_NONE);
            musicControlOffImage =
                    Image.createImage(image, 0, 270, 81, 15, Sprite.TRANS_NONE);
            quitGameImage =
                    Image.createImage(image, 0, 45, 81, 15, Sprite.TRANS_NONE);
            backgroundImage =
                    Image.createImage("/res/pausepic.png");
            image =
                    null;
        } catch (Exception ex) {
            ex.printStackTrace();
        //("这里发生了异常！");
        }
    }

    public void render(Graphics g) {
        g.drawImage(backImage, 0, 0, Graphics.TOP | Graphics.LEFT);
        g.drawImage(backgroundImage, CANVAS_WIDTH / 2, CONTINUE_POS_Y - 10, Graphics.TOP | Graphics.HCENTER);
        g.drawImage(continueGameImage, CANVAS_WIDTH / 2 + 10, CONTINUE_POS_Y, Graphics.VCENTER | Graphics.HCENTER);
        if (gameCanvas.getCurrentMusicState() == true) {
            g.drawImage(musicControlOffImage, CANVAS_WIDTH / 2 + 10, CONTINUE_POS_Y + INTEVERAL_HEIGHT, Graphics.VCENTER | Graphics.HCENTER);
        } else {
            g.drawImage(musicControlOnImage, CANVAS_WIDTH / 2 + 10, CONTINUE_POS_Y + INTEVERAL_HEIGHT, Graphics.VCENTER | Graphics.HCENTER);
        }

        g.drawImage(quitGameImage, CANVAS_WIDTH / 2 + 10, CONTINUE_POS_Y + INTEVERAL_HEIGHT * 2, Graphics.VCENTER | Graphics.HCENTER);
        g.setColor(0x0000ff);
        if (index == 1) {
            g.fillRect(CANVAS_WIDTH / 2 - 45, CONTINUE_POS_Y - 8, 15, 15);
        } else if (index == 2) {
            g.fillRect(CANVAS_WIDTH / 2 - 45, CONTINUE_POS_Y + INTEVERAL_HEIGHT - 8, 15, 15);
        } else if (index == 3) {
            g.fillRect(CANVAS_WIDTH / 2 - 45, CONTINUE_POS_Y + 2 * INTEVERAL_HEIGHT - 8, 15, 15);
        }

        flushGraphics();
    }

    public void keyPressed(int code) {
        if (code == this.getKeyCode(Canvas.UP) || code == KEY_NUM2) {
            if (index > 1) {
                index--;
            }

        } else if (code == getKeyCode(Canvas.DOWN) || code == KEY_NUM8) {
            if (index < 3) {
                index++;
            }

        } else if (code == getKeyCode(Canvas.LEFT) || code == getKeyCode(Canvas.RIGHT) || code == KEY_NUM4 || code == KEY_NUM6) {
            if (index == 2) {
                gameCanvas.turnOnOrOffMusic();
            }

        } else if (code == getKeyCode(Canvas.FIRE) || code == KEY_NUM5) {
            if (index == 1) {
                mainMidlet.stage2GameShow(false);
            } else if (index == 3) {
                if (gameCanvas.currentMusicState == true) {
                    gameCanvas.turnOnOrOffMusic();
                }

                mainMidlet.mainMenuShow(7);
            }

        }
    }

    public void run() {
        while (true) {
            try {
                render(g);
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
