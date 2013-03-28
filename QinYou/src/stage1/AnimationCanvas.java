package stage1;

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
public class AnimationCanvas extends GameCanvas implements Runnable {

    private MusicControl musicControl;
    public final String sayings[] = {
        "\"我得快点去找到和氏璧！\"，就在雪儿着急的跑着去寻找和氏璧之时，一不小心她掉入了一个陷阱中……",//0
        "\"这是什么地方？\"",//1
        "过了不时，不详从昏迷中醒来，发现自己就掉入了一个神秘的地方。,突然她看到了" +
        "个人朝她走来……",//2
        "这是什么地方？我怎么会在这里？",//3
        "你好，我是李斯，没想到我还能在始皇坟墓中看到一个活人！",//4
        "哦，李斯，你就是李斯！敬仰敬仰！你怎么会在这里啊？",//5
        "你看到的只是我的魂魄，被始皇帝腰斩之后，我的魂魄被困在这里而无法解脱……",//6
        "哦，那么如何帮助你是你的魂魄解脱呢？",//7
        "你要帮助我，就必须替我破除这个迷阵。",//8
        "如何破除这个迷阵呢？",//9
        "在这个迷阵的存放了我生前的八件遗物，这些遗物使我的魂魄困于此！",//10
        "那这些东西都在哪里呢？",//11
        "这是一个有魔法的罗盘，上面显示了八件遗物所在的位置，蓝色指针显示你的朝向。",//12
        "这样我就不会迷路了！",//13
        "上面的小蓝点那是你所在迷阵的位置，这正是这个魔盘的魔法之处！",//14
        "哦，这样我就可以更方便的知道自己在迷阵中的方位中了！",//15
        "迷宫还有其他的一些物品，他们都会帮助你快点找到物品所在之处。",//16
        "那就太好了！那我现在出发吧！",
        "在迷宫中有4个巨石在随机滚动，与其接触，你就被送至这里，你只有3次机会！",
        "哦，明白了，那还有别的吗？",
        "从这里走出倒计时就开始了。没有在一定时间内集齐八样遗物，你将丧命于此！",
        "哦。明白了！",
        "搜集起8个道具后在一定时间内返回于此，否则迷阵爆破，你仍可能丧命！",
        "看来，这次的寻找是充满危险的！",
        "不错！归来之后，我就会送给你想要的东西，并送你离开这里！祝你顺利！",
        "好，那我现在就出发！"
    };
    private final String[] sayings2 = {
        "小姑娘真不简单！能够成功的破除了这个迷阵！",//0
        "没事啦。哈哈。你说会送给我想要的东西，那是什么呢？",//1
        "就是你想要的。和氏璧和盛放和氏璧的罗盘！",//2
        "罗盘？这个罗盘怎么这么奇怪？",//3
        "不错，那就是你掉入的迷阵，我就是被施法魂魄困与这个罗盘的。",//4
        "那我怎么会掉入迷阵呢？",//5
        "呵呵，这个是个秘密。",//6
        "是不是我被你拖进来的？要不我怎么会进入一个罗盘中!",//7
        "哈哈，不说这个了。这是送给你第三件礼物：一双翅膀！",//8
        "这个东西，有什么作用么？",//9
        "借助这个东西，你可以去寻找赵公主和苏秦，他们那里你可以获得其他的2块和氏璧！",//10
        "哦，我明白了！不管怎样，谢谢您！",//11
        "我还要谢谢你了，现在我的魂魄自由啦……哈哈……再见！",//12
        "再见！下面要去找赵公主了！"//13
    };
    private int index; //表示第几个动画，不同的index，对应不同的动画
    private Image girlRunImage = null;//表示女孩奔跑的图片
    private Image girlHeadImage = null;//表示女孩头的图片
    private Image lisiHeadImage = null;//表示李斯头的图片
    private Image girlDialogImage = null;//表示女孩使用的对话框
    private Image lisiDialogImage = null;//表示李斯使用的对话框
    private Image backgroundImage = null;//背景图片
    private Image southPointImage = null; //指南针图片
    private Image partImage = null; //遗物的图片
    private int x = 176;  //表示画得子所在的位置x
    private int color = 0x0f000000;
    private int[] maskArray;
    private boolean dialogTurn = true; //在对话的时候表示轮到谁了。truegirl，falselisi
    private final int GIRL_HEAD_POSITION_Y = 80;
    private int CHAR_NUMBER_PER_LINE = 12;
    private int currentLine = 3;
    private int CHAR_HEIGHT = 14;
    private int counter = 30; //3mps
    private int COMPASS_LINE = 12;
    private boolean isRunning = true;
    private MainMidlet mainMidlet;
    private final int PART_LINE = 9;

    public AnimationCanvas(MainMidlet mainMidlet, int index) {
        super(false);
        this.mainMidlet = mainMidlet;
        this.setFullScreenMode(true);
        this.index = index;
        init();
        if (mainMidlet.needMusic) {
            musicControl = new MusicControl(true);
            musicControl.changeMusic("/res/anibg.mid");

            musicControl.musicStart();
            musicControl.changeMusicVolume(mainMidlet.musicVolume);
        }
    }

    public void init() {
        if (index == 0) {
            try {
                girlRunImage = Image.createImage("/res/girlrun.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            maskArray = new int[this.getHeight() * this.getWidth()];
            for (int i = 0; i < this.getHeight() * this.getWidth(); i++) {
                maskArray[i] = color;
            }
        }
        if (index == 4) {//李斯与女孩的最后的对话！
            try {
                girlHeadImage = Image.createImage("/res/girlhead.png");
                lisiHeadImage = Image.createImage("/res/lisi.png");
                Image temp = Image.createImage("/res/dialog1.png");
                lisiDialogImage = Image.createImage(temp, 160, 0, 160, 80, Sprite.TRANS_NONE);
                girlDialogImage = Image.createImage(temp, 0, 0, 160, 80, Sprite.TRANS_NONE);
                dialogTurn = false;
                currentLine = 0;
                counter = 30;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void render(Graphics g) {

        if (index == 0) { //过场动画1：女孩奔跑
            g.setColor(0);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.drawImage(girlRunImage, this.getWidth() / 2, this.getHeight() / 2, Graphics.VCENTER | Graphics.HCENTER);
//            g.drawRGB(maskArray, 0, this.getWidth(), 0, 0, this.getWidth(), this.getHeight(), true);
            g.setColor(0xffffff);
            g.drawString(sayings[0], x, 200, Graphics.LEFT | Graphics.TOP);
        } else if (index == 1) {//过场动画2：女孩掉入陷阱
            g.setColor(0xffffff);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.drawImage(girlRunImage, this.getWidth() / 2, this.getHeight() / 2, Graphics.VCENTER | Graphics.HCENTER);
            g.drawRGB(maskArray, 0, this.getWidth(), 0, 0, this.getWidth(), this.getHeight(), true);
        } else if (index == 2) {//过场动画3：女孩醒来
            g.setColor(0x0);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            if (backgroundImage == null) {
                try {
                    backgroundImage = Image.createImage("/res/bg3d.png");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            g.drawImage(backgroundImage, 0, 0, Graphics.TOP | Graphics.LEFT);
            if (girlHeadImage != null) {
                g.drawImage(girlHeadImage, 100, GIRL_HEAD_POSITION_Y, Graphics.TOP | Graphics.LEFT);
            }
            if (maskArray != null) {
                g.drawRGB(maskArray, 0, girlHeadImage.getWidth(), 100, GIRL_HEAD_POSITION_Y, girlHeadImage.getWidth(), girlHeadImage.getHeight(), true);
            }
            g.setColor(0xffffff);
            g.drawString(sayings[1], 0, this.getHeight() / 2, Graphics.TOP | Graphics.LEFT);
            g.drawString(sayings[2], x, 200, Graphics.TOP | Graphics.LEFT);
        } else if (index == 3) {//过场动画4：女孩开始了与李斯的对话
            g.setColor(0x0); //填涂黑色背景
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.drawImage(backgroundImage, 0, 0, Graphics.TOP | Graphics.LEFT);
            if (girlHeadImage != null) {//绘制女孩图像
                g.drawImage(girlHeadImage, 100, GIRL_HEAD_POSITION_Y, Graphics.TOP | Graphics.LEFT);
            }
            if (lisiHeadImage != null) { //当maskArray不等于空，李斯的图像慢慢显示出来
                g.drawImage(lisiHeadImage, 0, GIRL_HEAD_POSITION_Y, Graphics.TOP | Graphics.LEFT);
                if (maskArray != null) {
                    g.drawRGB(maskArray, 0, lisiHeadImage.getWidth(), 0, GIRL_HEAD_POSITION_Y, lisiHeadImage.getWidth(), lisiHeadImage.getHeight(), true);
                }
            }
            if (maskArray == null) { //进入显示对话阶段
                if (dialogTurn == true && girlDialogImage != null) {
                    g.drawImage(girlDialogImage, 5, GIRL_HEAD_POSITION_Y + 64, Graphics.TOP | Graphics.LEFT);
                } else if (dialogTurn == false && lisiDialogImage != null) {
                    g.drawImage(lisiDialogImage, 5, GIRL_HEAD_POSITION_Y + 64, Graphics.TOP | Graphics.LEFT);
                }
                if (currentLine <= sayings.length - 1) {
                    drawDialogString(g, sayings[currentLine]);
                }

                try {
                    renderImage(g);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (index == 4) {
            renderForIndex4(g);
        }

        flushGraphics();
    }

    public void logicProcess() {
        if (index == 0) { //动画逻辑1：字母的从左到右的显示
            x -= 5;
            if (x <= -sayings[0].length() * 12) {
                index = 1;
            }
        } else if (index == 1) {//动画逻辑2：渐变效果，有白到黑

            color += 0x10000000;
            for (int i = 0; i < this.getHeight() * this.getWidth(); i++) {
                maskArray[i] = color;
            }
            if ((color & 0xff000000) == 0xff000000) { //满足条件后，进入动画逻辑3
                index = 2;
                x = 176;
                maskArray = null;
            }
        } else if (index == 2) { //动画逻辑3：女孩醒来
            if (girlRunImage != null) { //把上一阶段没有用的资源释放掉
                girlRunImage = null;
            }
            if (girlHeadImage == null) {//初始化这一阶段的资源
                try {
                    color = 0xf0000000;
                    girlHeadImage = Image.createImage("/res/girlhead.png");
                    maskArray = new int[girlHeadImage.getWidth() * girlHeadImage.getHeight()];
                    for (int i = 0; i < maskArray.length; i++) {
                        maskArray[i] = color;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (maskArray != null) { //这一阶段的动画1：从黑到白
                color -= 0x10000000; //下面的代码处理图片的渐变
                for (int i = 0; i < maskArray.length; i++) {
                    maskArray[i] = color;
                }
            }
            if ((color | 0x00000000) == 0x00000000) { //这一阶段的动画2：显示女孩的字母
                maskArray = null;
                x -= 5;
                if (x <= -sayings[2].length() * 12) {
                    index = 3;
                }
            }

        } else if (index == 3) {  //动画逻辑4：女孩与李斯的对话
            if (lisiHeadImage == null) {//初始化这一阶段需要的资源
                try {
                    lisiHeadImage = Image.createImage("/res/lisi.png");
                    Image tempImage = Image.createImage("/res/dialog1.png");
                    girlDialogImage = Image.createImage(tempImage, 0, 0, 160, 80, Sprite.TRANS_NONE);
                    lisiDialogImage = Image.createImage(tempImage, 160, 0, 160, 80, Sprite.TRANS_NONE);
                    color = 0xf0000000;
                    maskArray = new int[lisiHeadImage.getWidth() * lisiHeadImage.getHeight()];//李斯的mask
                    for (int i = 0; i < maskArray.length; i++) {
                        maskArray[i] = color;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            //下面的代码处理图片的逻辑
            if (maskArray != null) { //这一阶段的动画1：从黑到白
                color -= 0x10000000; //下面的代码处理图片的渐变
                for (int i = 0; i < maskArray.length; i++) {
                    maskArray[i] = color;
                }
            }
            //下面的代码处理字幕的逻辑
            if ((color | 0) == 0) { //出现完毕，显示对话
                maskArray = null;
                counter--;
                if (counter == 0) {
                    currentLine++;
                    counter = 30;
                    dialogTurn = !dialogTurn;
                    if (currentLine == sayings.length - 1) {
                        isRunning = false;
                        musicControl.musicStop();
                        mainMidlet.firstStageGameShow();
                    }
                }
            }
        } else if (index == 4) {
            logicForIndex4();
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
                keyProcess();
                render(g);
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    protected void keyProcess() {
        int code = this.getKeyStates();
        if ((code & GameCanvas.FIRE_PRESSED) != 0) {
            if (index == 0 || index == 2) {
                index++;
            } else if (index == 3) {
                currentLine++;
                counter = 30;
                dialogTurn = !dialogTurn;
                if (currentLine > sayings.length - 1) {
                    isRunning = false;
                    musicControl.musicStop();
                    mainMidlet.firstStageGameShow();
                }
            } else if (index == 4) {
                currentLine++;
                counter = 30;
                dialogTurn = !dialogTurn;
                if (currentLine > sayings.length - 1) {
                    isRunning = false;
                    musicControl.musicStop();
                    mainMidlet.mainMenuShow(7);
                }
            }
        }
    }

    private void renderImage(Graphics g) throws IOException {
        if (currentLine >= COMPASS_LINE && currentLine <= COMPASS_LINE + 3) {
            if (southPointImage == null) {
                southPointImage = Image.createImage("/res/luopanEx.PNG");
                x = 0;
            }
            g.drawImage(southPointImage, x, 0, Graphics.TOP | Graphics.RIGHT);
            x += 1;
        } else if (currentLine >= PART_LINE && currentLine <= PART_LINE + 2) {
            if (partImage == null) {
                partImage = Image.createImage("/res/part.png");
                x = 0;
            }
            g.drawImage(partImage, x, 0, Graphics.TOP | Graphics.LEFT);
            x += 1;
        }
    }
    //index4的绘制
    private void renderForIndex4(Graphics g) {
        g.setColor(0);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (girlHeadImage != null) {//绘制女孩图像
            g.drawImage(girlHeadImage, 100, GIRL_HEAD_POSITION_Y, Graphics.TOP | Graphics.LEFT);
        }
        if (lisiHeadImage != null) { //绘制李斯头像
            g.drawImage(lisiHeadImage, 0, GIRL_HEAD_POSITION_Y, Graphics.TOP | Graphics.LEFT);
        }
        if (dialogTurn && girlDialogImage != null) {
            g.drawImage(girlDialogImage, 5, GIRL_HEAD_POSITION_Y + 64, Graphics.TOP | Graphics.LEFT);
        } else if (!dialogTurn && lisiDialogImage != null) {
            g.drawImage(lisiDialogImage, 5, GIRL_HEAD_POSITION_Y + 64, Graphics.TOP | Graphics.LEFT);
        }
        if (currentLine <= sayings2.length - 1) {
            drawDialogString(g, sayings2[currentLine]);
        } else {
            musicControl.musicStop();
            mainMidlet.mainMenuShow(7);
        }

        try {
            renderImageForIndex4(g);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void logicForIndex4() {
        counter--;
        if (counter == 0) {
            if (currentLine > sayings2.length - 1) {
                isRunning = false;
                musicControl.musicStop();
                mainMidlet.mainMenuShow(7);
            }
            counter = 30;
            currentLine++;
            dialogTurn = !dialogTurn;
        }
    }
    private Image heshibiImage;//和氏璧
    private Image chibangImage;//翅膀

    private void renderImageForIndex4(Graphics g) throws IOException {
        if (currentLine >= 0 && currentLine <= 5) {
            if (southPointImage == null) {
                heshibiImage = Image.createImage("/res/heshibi.png");
//                heshibiImage = Image.createImage(heshibiImage, 0, 0, 100, 90, Sprite.TRANS_NONE);
//                heshibiImage = ImageScale.scaleImage(heshibiImage, 50, 50);
                southPointImage = Image.createImage("/res/luopan.png");
                x = 0;
            }
            g.drawImage(heshibiImage, x, 0, Graphics.TOP | Graphics.RIGHT);
            g.drawImage(southPointImage, x, 0, Graphics.TOP | Graphics.LEFT);
            x += 1;
        } else if (currentLine == 8 || currentLine == 9 || currentLine == 10) {
            if (chibangImage == null) {
                chibangImage = Image.createImage("/res/chibang.png");
                x = 0;
            }
            g.drawImage(chibangImage, x, 0, Graphics.TOP | Graphics.RIGHT);
            x += 2;
        }
    }

    protected void keyPressed(int code) {
        if (code == -7 && index != 4) {
            isRunning = false;
            if (musicControl != null) {
                musicControl.musicStop();
            }
            mainMidlet.firstStageGameShow();
        } else if (code == -7 && index == 4) {
            isRunning = false;
            if (musicControl != null) {
                musicControl.musicStop();
                musicControl = null;
            }
            mainMidlet.mainMenuShow(7);
        }
    }
}