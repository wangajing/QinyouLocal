package stage3;

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
public class Stage3AnimationCanvas extends GameCanvas implements Runnable {

    private HorseRace gameScreen;
    public final String sayings1[] = {
        "获得第二块和氏璧后，发现翅膀因飞行时间太长已不能用，于是找了一辆马车，驾着马车找到了苏秦......"
    };
    public final String sayings2[] = {
        "小姑娘找我有何事？",
        "听闻李斯先生说你这里有和氏璧，能否借我一用？",
        "既是李斯推荐，定当借你一用！",
    };    //显示 黑色屏幕
    public final String sayings3[] = {
        "啊？你是谁？为何暗杀苏先生？",
        "这块价值连城的和氏璧，谁不想要？",
        "哪里逃！快给我！",
        "你也想要？那得看你有没有本事追到我了!"
    };
    public final String sayings4[] = {
        "射杀蒙面人之后，雪儿得到了第三块和氏璧，并且得到了完整的和氏璧。她把和氏璧放在了一起……",
        "正在此时，突然一辆马车从苏秦旁边飞过，一个蒙面人用飞刀将苏秦射伤，并抢走了正要递给雪儿的和氏璧！"
    };
    private int index = 0; //表示第几个动画，不同的index，对应不同的动画
    private Image girlFlyImage = null;
    private Image girlHeadImage = null;
    private Image suqinImage = null;
    private Image cikeImage = null;
    private Image girlDialogImage = null;//表示女孩使用的对话框
    private Image suqinDialogImage = null;//表示石头使用的对话框
//    private Image blackScreenImage = null;
    private Image dieImage = null;
    private Image cikeDialogImage = null;//表示angel使用的对话框
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

    public Stage3AnimationCanvas(MainMidlet mainMidlet, int index) {
        super(false);
        this.mainMidlet = mainMidlet;
        this.setFullScreenMode(true);
        this.index = index;
        init();
    }

    public void init() {
        //0为过场马车动画和与苏秦对话初始化 6为结束过场动画初始化
        if (index == 0 || index == 6) {
            try {
                girlFlyImage = Image.createImage("/res/gameoverground.png");
                girlHeadImage = Image.createImage("/res/girlhead.png");
                suqinImage = Image.createImage("/res/suqin.png");
                Image temp = Image.createImage("/res/dialog1.png");
                suqinDialogImage = Image.createImage(temp, 160, 0, 160, 80, Sprite.TRANS_NONE);
                girlDialogImage = Image.createImage(temp, 0, 0, 160, 80, Sprite.TRANS_NONE);
                dialogTurn = true;
                currentLine = 0;
                counter = 30;
//                blackScreenImage = Image.createImage("/res/blackScreen.png");
                dieImage = Image.createImage("/res/dieImage.png");
            //  noticImage=Image.createImage("/res/blackScreen.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        //刺客与女孩对话初始化
        if (index == 4) {
            try {
                girlHeadImage = Image.createImage("/res/girlhead.png");
                cikeImage = Image.createImage("/res/cike.png");
                Image temp = Image.createImage("/res/dialog1.png");
                cikeDialogImage = Image.createImage(temp, 160, 0, 160, 80, Sprite.TRANS_NONE);
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
        //0为过场马车动画 6为结束过场动画
        if (index == 0 || index == 6) {
            g.setColor(0);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.drawImage(girlFlyImage, 0, 0, Graphics.TOP | Graphics.LEFT);
            g.setColor(0xffffff);
            if (index == 0) {
                g.drawString(sayings1[0], x, 200, Graphics.LEFT | Graphics.TOP);
            } else {
                g.drawString(sayings4[0], x, 200, Graphics.LEFT | Graphics.TOP);
            }
            //(x);
        }
        //苏秦与女孩的死前对话
        if (index == 1) {
            g.setColor(0x0); //填涂黑色背景
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
//            g.drawImage(backgroundImage, 0, 0, Graphics.TOP | Graphics.LEFT);
            if (girlHeadImage != null) {//绘制女孩图像
                g.drawImage(girlHeadImage, 0, GIRL_HEAD_POSITION_Y, Graphics.TOP | Graphics.LEFT);
            }
            if (suqinImage != null) { //绘制苏秦图像
                g.drawImage(suqinImage, 100, GIRL_HEAD_POSITION_Y, Graphics.TOP | Graphics.LEFT);
            }
            if (dialogTurn == true && suqinDialogImage != null) {
                g.drawImage(suqinDialogImage, 5, GIRL_HEAD_POSITION_Y + 64, Graphics.TOP | Graphics.LEFT);
            } else if (dialogTurn == false && girlDialogImage != null) {
                g.drawImage(girlDialogImage, 5, GIRL_HEAD_POSITION_Y + 64, Graphics.TOP | Graphics.LEFT);
            }
            drawDialogString(g, sayings2[currentLine]);
        }
        // 突然事件 黑屏过场
        if (index == 2) {
            g.setColor(0x0); //填涂黑色背景
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
//            if (blackScreenImage != null) {
//                g.drawImage(blackScreenImage, 0, 0, Graphics.TOP | Graphics.LEFT);
//            }
            x = 0;
        }
        //闪过苏秦牺牲场面
        if (index == 3) {
            g.setColor(0x0); //填涂黑色背景
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            if (dieImage != null) {//绘制李斯死的图像
                g.drawImage(dieImage, this.getWidth() / 2, this.getHeight() / 2, Graphics.VCENTER | Graphics.HCENTER);
            }
            g.setColor(0xffffff);
            g.drawString(sayings4[1], x, 200, Graphics.TOP | Graphics.LEFT);
        }
        //刺客与女孩最后的对话
        if (index == 4) {
            g.setColor(0x0); //填涂黑色背景
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
//            g.drawImage(backgroundImage, 0, 0, Graphics.TOP | Graphics.LEFT);
            if (girlHeadImage != null) {//绘制女孩图像
                g.drawImage(girlHeadImage, 100, GIRL_HEAD_POSITION_Y, Graphics.TOP | Graphics.LEFT);
            }
            if (cikeImage != null) { //绘制刺客图像
                g.drawImage(cikeImage, 0, GIRL_HEAD_POSITION_Y, Graphics.TOP | Graphics.LEFT);
            }
            if (dialogTurn == true && girlDialogImage != null) {
                g.drawImage(girlDialogImage, 5, GIRL_HEAD_POSITION_Y + 64, Graphics.TOP | Graphics.LEFT);
            } else if (dialogTurn == false && cikeDialogImage != null) {
                g.drawImage(cikeDialogImage, 5, GIRL_HEAD_POSITION_Y + 64, Graphics.TOP | Graphics.LEFT);
            }
            drawDialogString(g, sayings3[currentLine]);
        }

        flushGraphics();
    }

    private void logicProcess() throws IOException {
        //0为女孩过场语言  6为战后女孩过场语言
        if (index == 0 || index == 6) {
            x -= 5;
            if (x <= -sayings1[0].length() * 12) {
                index++;
                dialogTurn = false;
                if (index == 7) {
                    stopScene();
                    mainMidlet.gameSuccessShow();
                }
            }

        }
        //苏秦与女孩的死前对话
        if (index == 1) {
            counter--;
            if (counter == 0) {
                if (currentLine == sayings2.length - 1) {
                    index++;
                    counter = 15;
                }
                counter = 15;
                currentLine++;
                dialogTurn = !dialogTurn;
            }
        } //黑屏过场一定时间
        else if (index == 2) {
            counter--;
            if (counter == 0) {
                index++;
                counter = 15;
                x = 150;
            }
        } else if (index == 3) {//苏秦牺牲场面过场一定时间
            x -= 5;
            if (x <= -sayings4[1].length() * 12) {
                index++;
                counter = 30;
                currentLine = 0;
                dialogTurn = true;
            }
        } //刺客与女孩对话
        else if (index == 4) {
            if (cikeDialogImage == null || girlDialogImage == null) {
                cikeImage = Image.createImage("/res/cike.png");
                Image temp = Image.createImage("/res/dialog1.png");
                cikeDialogImage = Image.createImage(temp, 160, 0, 160, 80, Sprite.TRANS_NONE);
                girlDialogImage = Image.createImage(temp, 0, 0, 160, 80, Sprite.TRANS_NONE);
            }
            counter--;
            if (counter == 0) {
                if (currentLine == sayings3.length - 1) {
                    index++;
                }
                counter = 30;
                currentLine++;
                dialogTurn = !dialogTurn;
            }
        } else if (index == 5) {
            counter--;
            if (counter == 0) {
                this.stopScene();
                mainMidlet.stage3GameShow();
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
                try {
                    logicProcess();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
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
        if (mainMidlet.needMusic) {
            musicControl = new MusicControl(true);
            musicControl.changeMusic("4.mid");
            musicControl.musicStart();
        }
    }

    public void stopScene() {
        if (musicControl != null) {
            musicControl.musicStop();
        }
        musicControl = null;
        isRunning = false;
        thread = null;
        System.gc();
    }

    protected void keyPressed(int code) {
        if(code==-7&&index!=7)
        {
            stopScene();
            mainMidlet.stage3GameShow();
        }
        else if(code==-7&&index==7){
            stopScene();
            mainMidlet.mainMenuShow(7);
        }
    }
    
    
}
