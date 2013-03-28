package stage1;




import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Administrator
 */
public class PausedMenu {

    private Graphics g;
    private int index;

    public int getIndex() {
        return index;
    }
    private Image continueGameImage;
    private Image musicControlOnImage;
    private Image musicControlOffImage;
    private Image quitGameImage;
    private Image backgroundImage;
    public final int CANVAS_WIDTH = 176;
    public final int CANVAS_HEIGHT = 220;
    public final int INTEVERAL_HEIGHT = 30;
    public final int CONTINUE_POS_Y = 75;
    private Maze3DCanvas maze3D;

    public PausedMenu(Graphics g, Maze3DCanvas maze3D) {
        try {
            this.g = g;
            index = 1;
            this.maze3D = maze3D;
            Image image = Image.createImage("/res/menuText.png");
            continueGameImage = Image.createImage(image, 0, 15, 81, 15, Sprite.TRANS_NONE);
            musicControlOnImage = Image.createImage(image, 0, 255, 81, 15, Sprite.TRANS_NONE);
            musicControlOffImage = Image.createImage(image, 0, 270, 81, 15, Sprite.TRANS_NONE);
            quitGameImage = Image.createImage(image, 0, 45, 81, 15, Sprite.TRANS_NONE);
            backgroundImage = Image.createImage("/res/pausepic.png");
            image = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            //("这里发生了异常！");
        }
    }

    public void render() {
        g.drawImage(backgroundImage, CANVAS_WIDTH / 2, CONTINUE_POS_Y - 10, Graphics.TOP | Graphics.HCENTER);
        g.drawImage(continueGameImage, CANVAS_WIDTH / 2 + 10, CONTINUE_POS_Y, Graphics.VCENTER | Graphics.HCENTER);
        if (maze3D.getCurrentMusicState() == true) {
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
    }

    public void onKeyPressed(int code) {
        if (code == Canvas.UP) {
            if (index > 1) {
                index--;
            }
        } else if (code == Canvas.DOWN) {
            if (index < 3) {
                index++;
            }
        } else if (code == Canvas.LEFT || code == Canvas.RIGHT) {
            if (index == 2) {
                maze3D.turnOnOrOffMusic();
                
            }
        }
    }
}
