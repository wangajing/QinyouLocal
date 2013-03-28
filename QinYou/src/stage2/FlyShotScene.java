package stage2;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.microedition.lcdui.game.Sprite;
import mainMenu.MainMidlet;
import res.MusicControl;

/**
 *
 * @author Administrator
 */
public class FlyShotScene extends GameCanvas implements Runnable {

    private FlyShotCanvas gameScreen;
    public final String sayings1[] = {
        "离开了迷宫后的雪儿，借助李斯送他的翅膀，很快的找到了赵公主所在的秦王皇陵，然而却看到一群蝙蝠朝他飞了过来……"
    };
    public final String sayings2[] = {
        "好险啊，不知道从哪里掉下来这么多石头！不管再遇到什么，我一定要勇敢的闯过去！",
        "哈哈哈！",
        "谁？",
        "外来人！没看出你还有两下子，有本事就来试试我的刀阵吧！",
        "即使这里是刀山火海，我也要试试！"
    };
    public final String sayings3[] = {
        "这里的怪物实在好多啊！",
        "外来人！你闯入帝陵，惊动始皇神灵，按大秦律当斩！",
        "好美丽的姐姐！你一定是赵公主啦！我来自未来，想借和氏璧一用使我回到未来。",
        "大秦律法自有约束，接下来怎样就要看你的造化了！"
    };
    public final String sayings4[] = {
        "你很勇敢，我将送你这块和氏璧，最后的一块在苏秦那里。你去找他吧",
        "谢谢赵公主姐姐！那我去找苏秦啦，再见！"
    };
    private int index = 0; //表示第几个动画，不同的index，对应不同的动画
    private Image girlFlyImage = null;
    private Image girlHeadImage = null;
    private Image stoneImage = null;
    private Image angelImage = null;
    private Image backgroundImage = null;//背景图片
    private Image girlDialogImage = null;//表示女孩使用的对话框
    private Image stoneDialogImage = null;//表示石头使用的对话框
    private Image angelDialogImage = null;//表示angel使用的对话框
    private boolean dialogTurn = false; //在对话的时候表示轮到谁了
    private int currentLine = 1;
    private int counter = 30; //3mps
    private int x = 176;  //表示画得子所在的位置x
    private final int GIRL_HEAD_POSITION_Y = 80;
    private int CHAR_NUMBER_PER_LINE = 12;
    private int CHAR_HEIGHT = 14;
    public boolean isRunning = true;
    private MainMidlet mainMidlet;
    private Thread thread;
    private MusicControl musicControl;

    public FlyShotScene(MainMidlet mainMidlet, int index) {
        super(false);
        this.mainMidlet = mainMidlet;
        this.setFullScreenMode(true);
        this.index = index;
        if (mainMidlet.needMusic && index != 1 && index != 2) {
            musicControl = new MusicControl(true);
            musicControl.changeMusic("/res/4.mid");
            musicControl.musicStart();
            musicControl.changeMusicVolume(mainMidlet.musicVolume);
        }
        init();
    }

    public void init() {
        //女孩征程前的会话
        if (index == 0) {
            try {
                girlFlyImage = Image.createImage("/res/girlfly.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        //石头巨人与女孩的战前对话
        if (index == 1) {
            try {
                girlHeadImage = Image.createImage("/res/girlhead.png");
                stoneImage = Image.createImage("/res/stone.png");
                Image temp = Image.createImage("/res/dialog1.png");
                stoneDialogImage = Image.createImage(temp, 160, 0, 160, 80, Sprite.TRANS_NONE);
                girlDialogImage = Image.createImage(temp, 0, 0, 160, 80, Sprite.TRANS_NONE);
                dialogTurn = true;
                currentLine = 0;
                counter = 30;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        //angel与女孩的战前对话
        if (index == 2) {
            try {
                girlHeadImage = Image.createImage("/res/girlhead.png");
                angelImage = Image.createImage("/res/angelImage.png");
                Image temp = Image.createImage("/res/dialog1.png");
                angelDialogImage = Image.createImage(temp, 160, 0, 160, 80, Sprite.TRANS_NONE);
                girlDialogImage = Image.createImage(temp, 0, 0, 160, 80, Sprite.TRANS_NONE);
                dialogTurn = true;
                currentLine = 0;
                counter = 30;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        //angel与女孩最后的对话
        if (index == 3) {
            try {
                girlHeadImage = Image.createImage("/res/girlhead.png");
                angelImage = Image.createImage("/res/angelImage.png");
                Image temp = Image.createImage("/res/dialog1.png");
                angelDialogImage = Image.createImage(temp, 160, 0, 160, 80, Sprite.TRANS_NONE);
                girlDialogImage = Image.createImage(temp, 0, 0, 160, 80, Sprite.TRANS_NONE);
                dialogTurn = false;
                currentLine = 0;
                counter = 30;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void render(Graphics g) {
        //女孩征程前的会话
        if (index == 0) {
            g.setColor(0);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.drawImage(girlFlyImage, this.getWidth() / 2, this.getHeight() / 2, Graphics.VCENTER | Graphics.HCENTER);
            g.setColor(0xffffff);
            g.drawString(sayings1[0], x, 200, Graphics.LEFT | Graphics.TOP);
        //(x);
        }
        //石头巨人与女孩的战前对话
        if (index == 1) {
            g.setColor(0x0); //填涂黑色背景
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
//            g.drawImage(backgroundImage, 0, 0, Graphics.TOP | Graphics.LEFT);
            if (girlHeadImage != null) {//绘制女孩图像
                g.drawImage(girlHeadImage, 100, GIRL_HEAD_POSITION_Y, Graphics.TOP | Graphics.LEFT);
            }
            if (stoneImage != null) { //绘制石头巨人图像
                g.drawImage(stoneImage, 0, GIRL_HEAD_POSITION_Y, Graphics.TOP | Graphics.LEFT);
            }
            if (dialogTurn == true && girlDialogImage != null) {
                g.drawImage(girlDialogImage, 5, GIRL_HEAD_POSITION_Y + 64, Graphics.TOP | Graphics.LEFT);
            } else if (dialogTurn == false && stoneDialogImage != null) {
                g.drawImage(stoneDialogImage, 5, GIRL_HEAD_POSITION_Y + 64, Graphics.TOP | Graphics.LEFT);
            }
            drawDialogString(g, sayings2[currentLine]);
        }
        //angel与女孩的战前对话
        if (index == 2) {
            g.setColor(0x0); //填涂黑色背景
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
//            g.drawImage(backgroundImage, 0, 0, Graphics.TOP | Graphics.LEFT);
            if (girlHeadImage != null) {//绘制女孩图像
                g.drawImage(girlHeadImage, 100, GIRL_HEAD_POSITION_Y, Graphics.TOP | Graphics.LEFT);
            }
            if (angelImage != null) { //绘制仙女图像
                g.drawImage(angelImage, 0, GIRL_HEAD_POSITION_Y, Graphics.TOP | Graphics.LEFT);
            }
            if (dialogTurn == true && girlDialogImage != null) {
                g.drawImage(girlDialogImage, 5, GIRL_HEAD_POSITION_Y + 64, Graphics.TOP | Graphics.LEFT);
            } else if (dialogTurn == false && angelDialogImage != null) {
                g.drawImage(angelDialogImage, 5, GIRL_HEAD_POSITION_Y + 64, Graphics.TOP | Graphics.LEFT);
            }
            drawDialogString(g, sayings3[currentLine]);
        }
        //angel与女孩最后的对话
        if (index == 3) {
            g.setColor(0x0); //填涂黑色背景
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
//            g.drawImage(backgroundImage, 0, 0, Graphics.TOP | Graphics.LEFT);
            if (girlHeadImage != null) {//绘制女孩图像
                g.drawImage(girlHeadImage, 100, GIRL_HEAD_POSITION_Y, Graphics.TOP | Graphics.LEFT);
            }
            if (angelImage != null) { //绘制仙女图像
                g.drawImage(angelImage, 0, GIRL_HEAD_POSITION_Y, Graphics.TOP | Graphics.LEFT);
            }
            if (dialogTurn == true && girlDialogImage != null) {
                g.drawImage(girlDialogImage, 5, GIRL_HEAD_POSITION_Y + 64, Graphics.TOP | Graphics.LEFT);
            } else if (dialogTurn == false && angelDialogImage != null) {
                g.drawImage(angelDialogImage, 5, GIRL_HEAD_POSITION_Y + 64, Graphics.TOP | Graphics.LEFT);
            }
            drawDialogString(g, sayings4[currentLine]);
        }
        flushGraphics();
    }

    private void logicProcess() {
        //女孩征程前的会话
        if (index == 0) {
            x -= 5;
            if (x <= -sayings1[0].length() * 12) {
                stopScene();
                if (musicControl != null) {
                    musicControl.musicStop();
                }
                mainMidlet.stage2GameShow(true);
                return;
            }
        }
        //石头巨人与女孩的战前对话
        if (index == 1) {
            counter--;
            if (counter == 0) {
                if (currentLine == sayings2.length - 1) {
                    stopScene();
                    if (musicControl != null) {
                        musicControl.musicStop();
                    }
                    mainMidlet.stage2GameShow(false);
                    return;
                }
                counter = 30;
                currentLine++;
                dialogTurn = !dialogTurn;
            }
        } //angel与女孩的战前对话
        else if (index == 2) {
            counter--;
            if (counter == 0) {
                if (currentLine == sayings3.length - 1) {
                    //("转到游戏");
                    stopScene();
                    if (musicControl != null) {
                        musicControl.musicStop();
                    }
                    mainMidlet.stage2GameShow(false);
                    return;
                }
                counter = 30;
                currentLine++;
                dialogTurn = !dialogTurn;
            }



        } //angel与女孩最后的对话
        else if (index == 3) {
            counter--;
            if (counter == 0) {
                if (currentLine == sayings4.length - 1) {
                    stopScene();
                    if (musicControl != null) {
                        musicControl.musicStop();
                    }
                    mainMidlet.mainMenuShow(7);
                    return;
                }
                counter = 30;
                currentLine++;
                dialogTurn = !dialogTurn;
            }
        }
    }

    private void drawDialogString(Graphics g, String str) {
        int count = str.length() / CHAR_NUMBER_PER_LINE;
        int i;
        int m = 15, n = 170;
        if (dialogTurn == true) {
            g.setColor(0x0000ff);
        } else {
            g.setColor(0);
        }
        if (count == 0) {
            g.drawSubstring(str, 0, str.length(), m, n + CHAR_HEIGHT, Graphics.LEFT | Graphics.TOP);
        } else {
            for (i = 0; i < count; i++) {
                g.drawSubstring(str, i * CHAR_NUMBER_PER_LINE, CHAR_NUMBER_PER_LINE, m, n + i * CHAR_HEIGHT, Graphics.LEFT | Graphics.TOP);
            }
            if (str.length() % CHAR_NUMBER_PER_LINE != 0) {
                g.drawSubstring(str, i * CHAR_NUMBER_PER_LINE, str.length() - i * CHAR_NUMBER_PER_LINE, m, n + i * CHAR_HEIGHT, Graphics.LEFT | Graphics.TOP);
            }
        }
    }

    public void run() {
        Graphics g = this.getGraphics();
        while (isRunning) {
            try {
                logicProcess();
                render(g);
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void startScene() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stopScene() {

        isRunning = false;
        thread = null;
    }

    protected void keyPressed(int code) {
        if (code == -7) {
            if (index == 0) {
                this.stopScene();
                if (musicControl != null) {
                    musicControl.musicStop();
                }
                mainMidlet.stage2GameShow(true);
            } else if (index == 3) {
                this.stopScene();
                if (musicControl != null) {
                    musicControl.musicStop();
                }
                mainMidlet.mainMenuShow(7);
            } else if (index == 1 || index == 2) {
                this.stopScene();
                if (musicControl != null) {
                    musicControl.musicStop();
                }
                mainMidlet.stage2GameShow(false);
            }

        }

    }
}
