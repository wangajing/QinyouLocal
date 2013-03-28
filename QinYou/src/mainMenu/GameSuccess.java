package mainMenu;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.microedition.lcdui.game.Sprite;
import res.MusicControl;

/**
 *
 * @author Administrator
 */
public class GameSuccess extends GameCanvas implements Runnable {
    //屏幕的宽和高的常量
    public final int CANVAS_WIDTH = getWidth();
    public final int CANVAS_HEIGHT = getHeight();
    //每一帧所需要的时间，单位为毫秒
    public static int TIME_PER_FRAME = 100;
    //画笔
    private Graphics g = this.getGraphics();
    //游戏开始的标志
    private boolean isStart = true;
    Thread thread;
    private Image ImageAllThing = null;
    private Image ImageThing = null;
    private Image ImageFirst = null;
    private Image ImageSecond = null;
    private Image ImageThird = null;
    private int pictureFirstPosX = 0,  pictureFirstPosY = 60;
    private int pictureSecondPosX = 40,  pictureSecondPosY = 154;
    private int pictureThirdPosX = 120,  pictureThirdPosY = 60;
    private int pictureIndex = 0;
    private int pictureFirstIndex = 0;
    private int[] maskArray;
    private int color = 0xffffffff;
    public MainMidlet midlet;
    private int index = 0;
    private boolean isNeedMask = true;
    private MusicControl musicControl;
    private int y;
    //@todo:这里添加游戏其他的变量
    public GameSuccess(MainMidlet midlet) {
        super(false);//修改为true将会仅支持getKeyState的键盘状态查询
        //函数，而不再支持keyPressed等覆盖函数，这里为false以支持keyPressed函数
        this.midlet = midlet;
        this.setFullScreenMode(true);//设置全屏模式
        //@todo:在这里初始化您的其他戏数据
        if (midlet.needMusic) {
            musicControl = new MusicControl(true);
            musicControl.changeMusic("/res/0.mid");
            musicControl.musicStart();
            musicControl.changeMusicVolume(midlet.musicVolume);
        }
        init();
    }

    public void start() {
        thread.start();
    }

    /**
     * 该函数用于初始化游戏数据，建议所有的初始化都在这里写
     */
    private void init() {
        maskArray = new int[this.getHeight() * this.getWidth()];
    }

    /**
     * 处理游戏逻辑
     */
    public void logicProcess() {
        //@todo:这里写下游戏的逻辑代码
        if (pictureFirstIndex == 0) {
            if (pictureFirstPosX != 30) {
                pictureFirstPosX += 2;
            }
            if (pictureSecondPosY != 124) {
                pictureSecondPosY -= 2;
            }
            if (pictureThirdPosX != 90) {
                pictureThirdPosX -= 2;
            }
        }

        if (pictureThirdPosX == 90 && isNeedMask == true) {
            color += 0x11111100;
            for (int i = 0; i < this.getHeight() * this.getWidth(); i++) {
                maskArray[i] = color;
            }
            if ((color & 0xff000000) == 0xff000000) { //满足条件后，进入动画逻辑3
                index = 1;
                isNeedMask = false;
                y = 210;
            }
            midlet.showRestMem();
        }
    }

    /**
     * 整个屏幕的渲染函数
     */
    private void render(Graphics g) {
        g.setColor(0x000000);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(0x0);//将画笔恢复为黑色
        //@todo:添加屏幕的绘制代码
        //测试代码：
        //  renderThing(g); 
        if (pictureFirstIndex == 0 && isNeedMask == true) {
            renderThing(g);
        }
        if (pictureSecondPosY == 124 && isNeedMask == true) {
            renderMask(g);
        }
        if (index == 1) {
            isNeedMask = false;
            renderGameOver(g);
        }
        flushGraphics();//不要删除此代码

    }
    int x;

    private void renderMask(Graphics g) {

        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(0x0);//将画笔恢复为黑色
        try {
            ImageAllThing = Image.createImage("/res/allthing.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.drawImage(ImageAllThing, 30, 60, Graphics.LEFT | Graphics.TOP);
        g.drawRGB(maskArray, 0, this.getWidth(), 0, 0, this.getWidth(), this.getHeight(), true);
    }

    private void renderGameOver(Graphics g) {
        g.setColor(50, 220, 100);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(0x0);//将画笔恢复为黑色
//        g.drawString("        恭喜通关！        ", 10, 40, Graphics.LEFT | Graphics.TOP);
        if (y > -75) {
            g.drawString("借助和氏璧的强大力量，雪儿", 10, y, Graphics.LEFT | Graphics.TOP);
            g.drawString("开启了时空隧道，回到了现代，", 10, y + 15, Graphics.LEFT | Graphics.TOP);
            g.drawString("从此过上了幸福的生活，然而", 10, y + 30, Graphics.LEFT | Graphics.TOP);
            g.drawString("这次秦游让她记忆犹新，无法", 10, y + 45, Graphics.LEFT | Graphics.TOP);
            g.drawString("忘却，她渴望能够再有一次机", 10, y + 60, Graphics.LEFT | Graphics.TOP);
            g.drawString("会……", 10, y + 75, Graphics.LEFT | Graphics.TOP);
            y-=2;
        } else {
            g.drawString("按任意键继续", 50, 100, Graphics.LEFT | Graphics.TOP);
        }
    }

    private void renderThing(Graphics g) {
        try {
            ImageThing = Image.createImage("/res/thing.png");
            ImageFirst = Image.createImage(ImageThing, 0, 0, 103, 90, Sprite.TRANS_NONE);
            ImageSecond = Image.createImage(ImageThing, 103, 0, 59, 90, Sprite.TRANS_NONE);
            ImageThird = Image.createImage(ImageThing, 162, 0, 60, 90, Sprite.TRANS_NONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.drawImage(ImageSecond, pictureFirstPosX, pictureFirstPosY, Graphics.TOP | Graphics.LEFT);
        g.drawImage(ImageFirst, pictureSecondPosX, pictureSecondPosY, Graphics.TOP | Graphics.LEFT);
        g.drawImage(ImageThird, pictureThirdPosX, pictureThirdPosY, Graphics.TOP | Graphics.LEFT);

    }

    /**
     * 线程的run函数，这里完成游戏屏幕的循环
     */
    public void run() {
        while (isStart) {
            try {
                long currentTime = System.currentTimeMillis();
                logicProcess();
                render(g);
                Thread.sleep(TIME_PER_FRAME);
            } catch (Exception ex) {
                ex.printStackTrace();
                //("线程运行时异常！");
            }
        }
    }

    protected void keyPressed(int arg0) {
        if (index == 1) {
            if (musicControl != null) {
                musicControl.musicStop();
                musicControl = null;
            }
            midlet.mainMenuShow(1);
        }
    }
}
