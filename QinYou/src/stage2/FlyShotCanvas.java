package stage2;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.microedition.lcdui.game.Sprite;
import mainMenu.DataStore;
import mainMenu.MainMidlet;
import res.MusicControl;

/**
 *
 * @author Administrator\
 */
public class FlyShotCanvas extends GameCanvas implements Runnable {

    private FlyShotScene gameScene;
    //屏幕的宽和高的常量
    public int CANVAS_WIDTH;
    public int CANVAS_HEIGHT;
    //每一帧所需要的时间，单位为毫秒
    public static int TIME_PER_FRAME = 100;
    //画笔
    private Graphics g = this.getGraphics();
    /**
     * 与整体游戏相关的变量
     */
    private boolean isStart = true;//游戏开始的标志
    private int currentPosition;
    /**游戏女孩相关的变量和参数*/
    private Image girlImage = null; //表示女孩的图片
    private Point girlPosition; //表示女孩所在屏幕的位置，图片的左上角的位置
    private byte girlHeight; //女孩的高
    private byte girlWidth;//女孩的宽
    private byte girlBulletSpeed;//女孩子弹的速度
    private final byte girlSpeed = 4;
    private Vector girlBulletPosition;//存储女孩子弹的向量
    private Image girlBulletImage;//女孩子弹的图片
    private short girlLife;
    private byte girlInjureValue;
    private short girlScore;
    private byte bombNumber = 3;
    private byte bombFlag = 0;
    private Image bomb;
    private Point[] bombPosition;
    /**游戏中与背景地图相关的变量 */
    private Image bg2_1_1;//背景图片
    private byte backgroundSpeed;
    private int bgImagePosY; //图片的位置y
    /** 与第一波boss相关的变量*/
    private Image boss1Image; //第一波boss的图片，共5个
    private Point[] boss1Position; //第一波boss的位置
    private short[] bullet1Postion = {0, 0, 0, 0, 0}; //第一波每个boss的子弹位置
    private Image boss1BulletImage; //第一波boss子弹的图片    
    private short[] INIT_POS_X_LEFT = {-140, -120, -80, -40, -20}; //第一波boss的初始化位置，相对于手机屏幕
    private short[] INIT_POS_X_RIGHT = {180, 200, 240, 280, 300};
    private byte[] INIT_POS_Y = {0, -20, -40, -20, 0};
    private byte[][] bullet1CollideFlag = new byte[5][5];
    private boolean[] boss1ExistFlag = {true, true, true, true, true};
    private final byte BOSS1_BULLET_SPEED = 8;//
    private final byte BOSS1_BULLET_INTERVAL = 20;
    private final byte BOSS1_SPEEDY = 2;
    private final byte BOSS1_SPEEDX = 2;
    private final byte FIRST_EMERGE_POSITION_Y1 = 0; //boss1出现的位置
    private final short FIRST_EMERGE_POSITION_Y2 = 120;
    private final short FIRST_EMERGE_POSITION_Y3 = 240;
    private final short FIRST_EMERGE_POSITION_Y4 = 360;
    private final byte bossScore = 20;
    private Sprite[] roidSprite = new Sprite[3]; //第二波石头怪的精灵
    private short SECOND_EMERGE_POSITION_Y = 490;//第二波怪出现的位置
    private short roidSpeed = 8;
    private Image boss3Image;//第三波boss的图片
    private Image boss3BulletImage;//第三波boss子弹的图片
    private short THIRD_EMERGE_POSITION_Y = 1000;//第三波怪出现的位置1000
    private short nextPosition = 190;
    private Point[] boss3Position;
    private Point boss3Position1;
    private byte boss3Life1 = 25;
    private boolean boss3ExistFlag1 = true;
    private final short[] BOSS3_X = {20, 80, 140};
    private final short[] BOSS3_Y = {0, -40, 0};
    private boolean[] boss3ExistFlag = {true, true, true};
    private final byte BOSS3_SPEED = 1;
    private short ringAngle1 = 0;
    private short ringAngle2 = 0;
    private short ringAngle3 = 0;
    private int FOURTH_EMERGE_POSITION_Y = 100000;//第四波怪出现的位置 100000
    private int bossDeathLine = 100000;
    private short moveSpace = 380;
    private short BOSSLIFE = 50;
    private boolean boss4ExistFlag = true;
    private Image boss4Image;//第四boss的图片
    private byte direction = 0;
    private final byte BOSS4_SPEEDY = 2;
    private final byte BOSS4_SPEEDX = 2;
    private Image boss4BulletImage;//第四boss子弹的图片
    private Image boss4CircleImage;//第四boss的环身链图片
    private Point boss4Position;
    private Point enimyCirclePos;
    private short[] radius;//圆形子弹
    private short line1;//直线型子弹参数
    private short line2;
    private short line3;
    private short line4;
    private short line5;
    //@todo:这里添加游戏其他的变量
    private Thread thread;
    private MainMidlet mainMidlet;
    private MusicControl musicControl;
    boolean currentMusicState;
    private MusicControl fxControl;
    private boolean success;

    public FlyShotCanvas(MainMidlet mainMidlet) {
        super(false);//修改为true将会仅支持getKeyState的键盘状态查询
        //函数，而不再支持keyPressed等覆盖函数，这里为false以支持keyPressed函数
        this.setFullScreenMode(true);//设置全屏模式
        //@todo:在这里初始化您的其他戏数据
        CANVAS_WIDTH = this.getWidth();
        CANVAS_HEIGHT = this.getHeight();
        this.mainMidlet = mainMidlet;
        currentMusicState = false;
        if (mainMidlet.needMusic) {
            turnOnOrOffMusic();
        }
        init();
    }

    boolean getCurrentMusicState() {
        return currentMusicState;
    }

    /**
     * 该函数用于初始化游戏数据，建议所有的初始化都在这里写
     */
    private void init() {
        try {
            //与女孩有关的初始化
            bomb = Image.createImage("/res/bomb.png");
            girlImage = Image.createImage("/res/flyGirl.png");
            girlBulletImage = Image.createImage("/res/bullet.png");
            girlWidth = (byte) girlImage.getWidth();
            girlHeight = (byte) girlImage.getHeight();
            girlPosition = new Point(CANVAS_WIDTH / 2 - girlWidth / 2, CANVAS_HEIGHT - girlHeight);
            girlBulletPosition = new Vector();
            //与背景有关的初始化
            Image image = Image.createImage("/res/bg2_1.png");
            Image[] image2 = new Image[4];
            Image[] image3 = new Image[4];
            for (int i = 0; i < 4; i++) {
                image2[i] = Image.createImage(image, i * 32, 0, 32, 32, Sprite.TRANS_NONE);
                image3[i] = Image.createImage(Image.createImage("/res/bg2_2.png"), i * 32, 0, 32, 32, Sprite.TRANS_NONE);
            }
            backgroundSpeed = 1;
            bg2_1_1 = Image.createImage(192, 320);
            Graphics g2d = bg2_1_1.getGraphics();
            bg2_1_2 = Image.createImage(192, 320);
            Graphics g2d2 = bg2_1_2.getGraphics();
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
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 6; j++) {
                    g2d.drawImage(image2[map[i][j] - 1], j * 32, i * 32, Graphics.TOP | Graphics.LEFT);
                    g2d2.drawImage(image3[map[i][j] - 1], j * 32, i * 32, Graphics.TOP | Graphics.LEFT);
                }
            }
            currentPosition = 0;
            girlLife = 200;//女孩生命值：100
            girlInjureValue = 5;
            girlBulletSpeed = 4;
            girlScore = 0;
            success = false;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    bullet1CollideFlag[i][j] = 1;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initFirst(int direction) {
        try {
            boss1Image = Image.createImage("/res/bat.png");
            boss1Position = new Point[5];
            for (int i = 0; i < 5; i++) {
                if (direction == 0) {
                    boss1Position[i] = new Point(INIT_POS_X_LEFT[i], INIT_POS_Y[i]);
                } else if (direction == 1) {
                    boss1Position[i] = new Point(INIT_POS_X_RIGHT[i], INIT_POS_Y[i]);
                }
            }
            boss1BulletImage = Image.createImage("/res/bullet.png");
        } catch (Exception ex) {
            ex.printStackTrace();
        //("这里异常了！");
        }
    }

    private void initThird() {
        try {
            boss3Image = Image.createImage("/res/smallStone.png");
            boss3Position = new Point[3];
//            boss3BulletImage = Image.createImage("/boss3BulletImage.png");
            for (int i = 0; i < 3; i++) {
                boss3Position[i] = new Point(BOSS3_X[i], BOSS3_Y[i]);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initThird1() {
        try {
            boss3Image = Image.createImage("/res/stone.png");
            boss3Position1 = new Point(CANVAS_WIDTH / 2 - boss3Image.getWidth() / 2, -boss3Image.getHeight());
//            boss3BulletImage = Image.createImage("/boss3BulletImage.png");
            enimyCirclePos = new Point(CANVAS_WIDTH / 2 - bomb.getWidth() / 2, CANVAS_HEIGHT / 2 - bomb.getHeight() / 2);
            bombPosition = new Point[5];
            bombPosition[0] = enimyCirclePos;
            bombPosition[1] = enimyCirclePos.move(0, 50);
            bombPosition[2] = enimyCirclePos.move(0, -50);
            bombPosition[3] = enimyCirclePos.move(50, 0);
            bombPosition[4] = enimyCirclePos.move(-50, 0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initFourth() {
        try {
            boss4Image = Image.createImage("/res/boss.png");
            boss4Position = new Point(CANVAS_WIDTH / 2 - boss4Image.getWidth() / 2, CANVAS_HEIGHT / 4 - boss4Image.getHeight() / 2);
            enimyCirclePos = new Point(CANVAS_WIDTH / 2 - bomb.getWidth() / 2, CANVAS_HEIGHT / 2 - bomb.getHeight() / 2);
            bombPosition = new Point[5];
            bombPosition[0] = enimyCirclePos;
            bombPosition[1] = enimyCirclePos.move(0, 50);
            bombPosition[2] = enimyCirclePos.move(0, -50);
            bombPosition[3] = enimyCirclePos.move(50, 0);
            bombPosition[4] = enimyCirclePos.move(-50, 0);
            radius = new short[3];
            boss4BulletImage = Image.createImage("/res/bossBullet1.png");
            boss4CircleImage = Image.createImage("/res/boss4CircleImage.png");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initSecond() {
        try {
            boss1Image = Image.createImage("/res/Roid.png");
            roidSprite[0] = new Sprite(boss1Image, 42, 35);
            roidSprite[1] = new Sprite(boss1Image, 42, 35);
            roidSprite[2] = new Sprite(boss1Image, 42, 35);

            roidSprite[0].setPosition(0, 0);
            roidSprite[1].setPosition(CANVAS_WIDTH / 2 - boss1Image.getWidth() / 2, 0);
            roidSprite[2].setPosition(CANVAS_WIDTH - boss1Image.getWidth(), 0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 按键处理函数，用于按键后的逻辑处理
     */
    private long lastFirePressTime = 0;

    private void keyPressProcess() {
        int keyState = this.getKeyStates();
        if ((keyState & UP_PRESSED) != 0) {
            //@todo:当按方向键上时的逻辑处理
            girlPosition.y -= girlSpeed;
            if (girlPosition.y < 0) {
                girlPosition.y += girlSpeed;
            }
        }
        if ((keyState & DOWN_PRESSED) != 0) {
            //@todo:当按方向键下时的逻辑处理
            girlPosition.y += girlSpeed;
            if (girlPosition.y > CANVAS_HEIGHT - girlHeight) {
                girlPosition.y -= girlSpeed;
            }
        }
        if ((keyState & LEFT_PRESSED) != 0) {
            //@todo:当按方向键左时的逻辑处理
            girlPosition.x -= girlSpeed;
            if (girlPosition.x < 0) {
                girlPosition.x += girlSpeed;
            }
        }
        if ((keyState & RIGHT_PRESSED) != 0) {
            //@todo:当按方向键右时的逻辑处理
            girlPosition.x += girlSpeed;
            if (girlPosition.x > CANVAS_WIDTH - girlWidth) {
                girlPosition.x -= girlSpeed;
            }
        }
        if ((keyState & FIRE_PRESSED) != 0) {
            //@todo:当按下开火键时的逻辑处理
            if (System.currentTimeMillis() - lastFirePressTime > 500) {
                lastFirePressTime = System.currentTimeMillis();
                girlBulletPosition.addElement(new Point(girlPosition.addX(girlImage.getWidth() / 2)));
            }
        }
        if ((keyState & GAME_A_PRESSED) != 0) {
            //@todo:当按下数字键1时的逻辑处理
            if (currentPosition >= THIRD_EMERGE_POSITION_Y && currentPosition < FOURTH_EMERGE_POSITION_Y) {
                bombFlag = 3;
            }
            if (currentPosition >= FOURTH_EMERGE_POSITION_Y) {
                bombFlag = 4;
            }
        }
    }

    /**
     * 处理游戏逻辑
     */
    public void logicProcess() {
        girlLogicProcess();
        if (currentPosition == FIRST_EMERGE_POSITION_Y1 || currentPosition == FIRST_EMERGE_POSITION_Y3) {
            for (int i = 0; i < 5; i++) {
                boss1ExistFlag[i] = true;
            }
            initFirst(0);
        } else if (currentPosition == FIRST_EMERGE_POSITION_Y2 || currentPosition == FIRST_EMERGE_POSITION_Y4) {
            for (int i = 0; i < 5; i++) {
                boss1ExistFlag[i] = true;
            }
            initFirst(1);
        } else {
        }
        if ((currentPosition >= FIRST_EMERGE_POSITION_Y1 && currentPosition < FIRST_EMERGE_POSITION_Y2) || (currentPosition >= FIRST_EMERGE_POSITION_Y3 && currentPosition < FIRST_EMERGE_POSITION_Y4)) {
            firstLeftMoveProcess();
            firstLogicProcess();
        } else if ((currentPosition >= FIRST_EMERGE_POSITION_Y2 && currentPosition < FIRST_EMERGE_POSITION_Y3) || (currentPosition >= FIRST_EMERGE_POSITION_Y4 && currentPosition < SECOND_EMERGE_POSITION_Y)) {
            firstRightMoveProcess();
            firstLogicProcess();
        } else {
        }
        if (currentPosition == SECOND_EMERGE_POSITION_Y) {
            initSecond();
        }
        if (currentPosition >= SECOND_EMERGE_POSITION_Y && currentPosition < THIRD_EMERGE_POSITION_Y) {
            secondMoveCollideProcess();
        }
        if (currentPosition == THIRD_EMERGE_POSITION_Y) {
            initThird();
            stopGame();
            mainMidlet.stage2AnimationShow(1);
        }
        if (currentPosition >= THIRD_EMERGE_POSITION_Y && currentPosition < THIRD_EMERGE_POSITION_Y + nextPosition) {
            thirdLogicProcess();
        }
        if (currentPosition == THIRD_EMERGE_POSITION_Y + nextPosition) {
            initThird1();
        }
        if (currentPosition >= THIRD_EMERGE_POSITION_Y + nextPosition && currentPosition < FOURTH_EMERGE_POSITION_Y) {
            thirdLogicProcess1();
        }
        if (currentPosition == FOURTH_EMERGE_POSITION_Y) {
            initFourth();
            stopGame();
            mainMidlet.stage2AnimationShow(2);
        }
        if (currentPosition >= FOURTH_EMERGE_POSITION_Y) {
            fourthLogicProcess();
        }
        currentPosition += backgroundSpeed;
    }

    private void firstLogicProcess() {
        for (int i = 0; i < 5; i++) {
            //蝙蝠boss与女孩的碰撞处理：蝙蝠boss消失 女孩生命值减少
            if (boss1ExistFlag[i] == true && girlLife > 0 && collideWith(girlImage, boss1Image, girlPosition, boss1Position[i])) {
                playFXSound(1);
                boss1ExistFlag[i] = false;
                girlLife -= girlInjureValue;
            }
            int interval = bullet1Postion[i];
            for (int j = 0; j < 5; j++) {
                //蝙蝠boss子弹与女孩碰撞的效果：女孩生命值减少 蝙蝠子弹消失
                Point point = new Point();
                point.x = boss1Position[i].x + 10;
                point.y = boss1Position[i].y;
                if (girlLife > 0 && bullet1CollideFlag[i][j] == 1 && boss1ExistFlag[i] == true && girlPosition.y >= boss1Position[i].y && collideWith(girlImage, boss1BulletImage, girlPosition, point.addY(interval))) {
                    playFXSound(0);
                    girlLife -= girlInjureValue;
                    bullet1CollideFlag[i][j] = 0;
                }
                interval -= BOSS1_BULLET_INTERVAL;
            }
        }
        //女孩子弹与蝙蝠boss的碰撞处理：蝙蝠消失 女孩得分增加
        for (int i = 0; i < girlBulletPosition.size(); i++) {
            Point p = (Point) girlBulletPosition.elementAt(i);
            for (int j = 0; j < 5; j++) {
                if (boss1ExistFlag[j] == true && collideWith(boss1Image, girlBulletImage, boss1Position[j], p)) {
                    playFXSound(0);
                    boss1ExistFlag[j] = false;
                    girlBulletPosition.removeElementAt(i);
                    girlScore += bossScore;
                }
            }
        }
    }

    private void firstLeftMoveProcess() {
        //蝙蝠boss及子弹的位置变化
        for (int i = 0; i < 5; i++) {
            boss1Position[i].y += BOSS1_SPEEDY;
            boss1Position[i].x += BOSS1_SPEEDX;
            bullet1Postion[i] += BOSS1_BULLET_SPEED;

            if (bullet1Postion[i] >= 200) {
                bullet1Postion[i] = 0;
                for (int j = 0; j < 5; j++) {
                    bullet1CollideFlag[i][j] = 1;
                }
            }
        }
    }

    private void firstRightMoveProcess() {
        //蝙蝠boss及子弹的位置变化
        for (int i = 0; i < 5; i++) {
            boss1Position[i].y += BOSS1_SPEEDY;
            boss1Position[i].x -= BOSS1_SPEEDX;
            bullet1Postion[i] += BOSS1_BULLET_SPEED;
            if (bullet1Postion[i] >= 200) {
                bullet1Postion[i] = 0;
                for (int j = 0; j < 5; j++) {
                    bullet1CollideFlag[i][j] = 1;
                }
            }
        }
    }

    private void ring1LogicProcess() {
        ringAngle1 += 5;
        if (ringAngle1 == 240) {
            ringAngle1 = 0;
        }
    }

    private void ring2LogicProcess() {
        ringAngle2 += 5;
        if (ringAngle2 == 240) {
            ringAngle2 = 0;
        }
    }

    private void ring3LogicProcess() {
        ringAngle3 += 5;
        if (ringAngle3 >= 240) {
            ringAngle3 = 0;
        }
    }

    private void thirdLogicProcess() {
        for (int i = 0; i < 3; i++) {
            //石头人boss与女孩的碰撞处理：石头人消失 女孩死亡
            if (boss3ExistFlag[i] == true && girlLife > 0 && collideWith(boss3Image, girlImage, boss3Position[i], girlPosition)) {
                playFXSound(0);
                boss3ExistFlag[i] = false;
                girlLife = 0;

            }
        }
        for (int i = 0; i < 3; i++) {
            boss3Position[i].y += BOSS3_SPEED;
        }
        if (currentPosition >= THIRD_EMERGE_POSITION_Y + CANVAS_HEIGHT / 2) {
            boss3Position[0].y += BOSS3_SPEED + 4;
            boss3Position[2].y += BOSS3_SPEED + 4;
        }
        if (currentPosition >= THIRD_EMERGE_POSITION_Y + (CANVAS_HEIGHT / 8) * 5) {
            boss3Position[1].y += BOSS3_SPEED + 4;
        }
    }

    private void thirdLogicProcess1() {
        if (currentPosition >= bossDeathLine) {
            boss3Life1 = 0;
        }
        if (boss3Position1.y < CANVAS_HEIGHT / 3 - boss3Image.getHeight() / 2) {
            boss3Position1.y += 1;
        }
        if (boss3Position1.x <= 0) {
            direction = 0;
        } else if (boss3Position1.x >= CANVAS_WIDTH - boss3Image.getWidth()) {
            direction = 1;
        }
        if (direction == 0) {
            boss3Position1.x += BOSS3_SPEED;
        } else if (direction == 1) {
            boss3Position1.x -= BOSS3_SPEED;
        }
        //石头人boss与女孩的碰撞处理：石头人消失 女孩死亡
        if (boss3ExistFlag1 == true && girlLife > 0 && collideWith(boss3Image, girlImage, boss3Position1, girlPosition)) {
            playFXSound(0);
            boss3ExistFlag1 = false;
            girlLife = 0;
        }
        //石头人与女孩子弹发生碰撞的处理：石头生命值减1 女孩得分增加 子弹消失
        for (int i = 0; i < girlBulletPosition.size(); i++) {
            Point p = (Point) girlBulletPosition.elementAt(i);
            if (boss3ExistFlag1 == true && collideWith(boss3Image, girlBulletImage, boss3Position1, p) && boss3Life1 != 0) {
                playFXSound(2);
                boss3Life1 -= 1;
                girlScore += bossScore;
                girlBulletPosition.removeElement(p);
            }
            if (boss3Life1 == 0) {
                boss3ExistFlag1 = false;
            }
        }
        if (boss3Life1 == 0) {
            FOURTH_EMERGE_POSITION_Y = currentPosition;
            boss3Life1 = 1;
        }
    }

    private void fourthLogicProcess() {
        //女孩子弹与终极boss的碰撞处理：boss减命 女孩得分增加 子弹消失
        try {
            for (int i = 0; i < girlBulletPosition.size(); i++) {
                Point p = (Point) girlBulletPosition.elementAt(i);
                if (boss4ExistFlag == true && collideWith(boss4Image, girlBulletImage, boss4Position, p) && BOSSLIFE != 0) {
                    playFXSound(2);
                    BOSSLIFE -= 1;
                    girlScore += bossScore;
                    girlBulletPosition.removeElement(p);
                }
                if (BOSSLIFE == 0) {
                    boss4ExistFlag = false;
                }
            }
            //女孩与终极boss的碰撞处理：女孩死亡 boss减命
            if (boss4ExistFlag == true && girlLife > 0 && collideWith(boss4Image, girlImage, boss4Position, girlPosition) && BOSSLIFE != 0) {
                playFXSound(0);
                BOSSLIFE -= 1;
                girlLife = 0;
            } else if (BOSSLIFE == 0) {
                boss4ExistFlag = false;
            }
            if (currentPosition >= FOURTH_EMERGE_POSITION_Y + 1060 && currentPosition < FOURTH_EMERGE_POSITION_Y + 1060 + moveSpace) {
                if (boss4Position.x < CANVAS_WIDTH) {
                    boss4Position.y += BOSS4_SPEEDY;
                    boss4Position.x += BOSS4_SPEEDX;
                }
                if (boss4Position.x == CANVAS_WIDTH) {
                    boss4Position.y = 0;
                    boss4Position.x = 0;
                }
            }
            if (currentPosition >= FOURTH_EMERGE_POSITION_Y && currentPosition < FOURTH_EMERGE_POSITION_Y + 1060) {
                if (boss4Position.x <= 0) {
                    direction = 0;
                } else if (boss4Position.x >= CANVAS_WIDTH - boss4Image.getWidth()) {
                    direction = 1;
                }
                if (direction == 0) {
                    boss4Position.x += BOSS4_SPEEDX;
                } else if (direction == 1) {
                    boss4Position.x -= BOSS4_SPEEDX;
                }
            }
            if (currentPosition >= FOURTH_EMERGE_POSITION_Y + 1060 + moveSpace) {//&& currentPosition < FOURTH_EMERGE_POSITION_Y + 1815
                if (currentPosition == FOURTH_EMERGE_POSITION_Y + 1060 + moveSpace) {
                    boss4Position.y = 0;
                    boss4Position.x = CANVAS_WIDTH - boss4Image.getWidth();
                }
                if (boss4Position.x > 0) {
                    boss4Position.y += BOSS4_SPEEDY;
                    boss4Position.x -= BOSS4_SPEEDX;
                }
                if (boss4Position.x == 0) {
                    boss4Position.y = 0;
                    boss4Position.x = CANVAS_WIDTH - boss4Image.getWidth();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     *第二波怪：石头阵的逻辑处理 
     */
    private void secondMoveCollideProcess() {//石头运动效果及与女生碰撞效果 
        for (int i = 0; i < 3; i++) {
            // 石头运动
            roidSprite[i].move(i + roidSpeed, roidSpeed - i);
            secondBoundsProcess(roidSprite[i]);
            if (i == 1) {
                roidSprite[i].prevFrame();
            } else {
                roidSprite[i].nextFrame();
            }
            // 女孩与石头的碰撞处理：女孩生命值减少 石头消失
            if (girlLife > 0 && roidSprite[i].collidesWith(girlImage, girlPosition.x, girlPosition.y, true)) {
                playFXSound(0);
                girlLife -= girlInjureValue;
                roidSprite[i].setPosition(0, 0);
            }
        }
    }

    private void secondBoundsProcess(Sprite sprite) {
        if (sprite.getX() < -sprite.getWidth()) {
            sprite.setPosition(getWidth(), sprite.getY());
        } else if (sprite.getX() > getWidth()) {
            sprite.setPosition(-sprite.getWidth(), sprite.getY());
        }
        if (sprite.getY() < -sprite.getHeight()) {
            sprite.setPosition(sprite.getX(), getHeight());
        } else if (sprite.getY() > getHeight()) {
            sprite.setPosition(sprite.getX(), -sprite.getHeight());
        }
    }

    private void line1ShotLogicProcess() {
        line1 += 3;
        if (line1 >= 260) {
            line1 = 0;
        }
    }

    private void line2ShotLogicProcess() {
        line2 += 3;
        if (line2 >= 260) {
            line2 = 0;
        }
    }

    private void line3ShotLogicProcess() {
        line3 += 3;
        if (line3 >= 260) {
            line3 = 0;
        }
    }

    private void line4ShotLogicProcess() {
        line4 += 3;
        if (line4 >= 260) {
            line4 = 0;
        }
    }

    private void line5ShotLogicProcess() {
        line5 += 3;
        if (line5 >= 260) {
            line5 = 0;
        }
    }

    private void circleShotLogicProcess() {
        if (radius[0] < 12) {
            radius[0] += 3;
            return;
        } else if (radius[0] < 24) {
            radius[0] += 3;
            radius[1] += 3;
            return;
        } else if (radius[0] >= 24) {
            radius[0] += 3;
            radius[1] += 3;
            radius[2] += 3;
        }
        for (int i = 0; i < 3; i++) {
            if (radius[i] > 160) {
                radius[i] = 0;
            }
        }
    }

    /**
    绘图函数
     */
    private void shotBulletForCircle(Image bullet1, Graphics g, int rad) {
        int sinOfrad = (int) (Math.sin(Math.toRadians(30)) * rad);
        int cosOfrad = (int) (Math.cos(Math.toRadians(30)) * rad);
        if (rad <= 160) {
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, enimyCirclePos.x + rad, enimyCirclePos.y)) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, enimyCirclePos.x + rad, enimyCirclePos.y, Graphics.VCENTER | Graphics.HCENTER);
            }
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, enimyCirclePos.x - rad, enimyCirclePos.y)) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, enimyCirclePos.x - rad, enimyCirclePos.y, Graphics.VCENTER | Graphics.HCENTER);
            }
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, enimyCirclePos.x, enimyCirclePos.y + rad)) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, enimyCirclePos.x, enimyCirclePos.y + rad, Graphics.VCENTER | Graphics.HCENTER);
            }
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, enimyCirclePos.x, enimyCirclePos.y - rad)) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, enimyCirclePos.x, enimyCirclePos.y - rad, Graphics.VCENTER | Graphics.HCENTER);
            }
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, enimyCirclePos.x + cosOfrad, enimyCirclePos.y - sinOfrad)) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, enimyCirclePos.x + cosOfrad, enimyCirclePos.y - sinOfrad, Graphics.VCENTER | Graphics.HCENTER);
            }
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, enimyCirclePos.x - cosOfrad, enimyCirclePos.y + sinOfrad)) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, enimyCirclePos.x - cosOfrad, enimyCirclePos.y + sinOfrad, Graphics.VCENTER | Graphics.HCENTER);
            }
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, enimyCirclePos.x + sinOfrad, enimyCirclePos.y - cosOfrad)) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, enimyCirclePos.x + sinOfrad, enimyCirclePos.y - cosOfrad, Graphics.VCENTER | Graphics.HCENTER);
            }
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, enimyCirclePos.x - sinOfrad, enimyCirclePos.y + cosOfrad)) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, enimyCirclePos.x - sinOfrad, enimyCirclePos.y + cosOfrad, Graphics.VCENTER | Graphics.HCENTER);
            }
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, enimyCirclePos.x - sinOfrad, enimyCirclePos.y - cosOfrad)) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, enimyCirclePos.x - sinOfrad, enimyCirclePos.y - cosOfrad, Graphics.VCENTER | Graphics.HCENTER);
            }
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, enimyCirclePos.x + sinOfrad, enimyCirclePos.y + cosOfrad)) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, enimyCirclePos.x + sinOfrad, enimyCirclePos.y + cosOfrad, Graphics.VCENTER | Graphics.HCENTER);
            }
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, enimyCirclePos.x - cosOfrad, enimyCirclePos.y - sinOfrad)) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, enimyCirclePos.x - cosOfrad, enimyCirclePos.y - sinOfrad, Graphics.VCENTER | Graphics.HCENTER);
            }
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, enimyCirclePos.x + cosOfrad, enimyCirclePos.y + sinOfrad)) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, enimyCirclePos.x + cosOfrad, enimyCirclePos.y + sinOfrad, Graphics.VCENTER | Graphics.HCENTER);
            }
        }
    }

    private void shotBulletForSector(Image bullet1, Graphics g, float angle, int rad) {

        for (int i = 0; i < 5; i++) {
            double r = Math.toRadians(angle);
            int sinOfrad = (int) (Math.sin(r) * rad);
            int cosOfrad = (int) (Math.cos(r) * rad);
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, enimyCirclePos.x - sinOfrad, enimyCirclePos.y + cosOfrad)) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, enimyCirclePos.x - sinOfrad, enimyCirclePos.y + cosOfrad, Graphics.VCENTER | Graphics.HCENTER);
            }
            rad -= 10;
            if (rad <= 0) {
                break;
            }
        }
    }

    private void shotBulletForRing(Image BulletImage, Point enimyCirclePos, Graphics g, float angle, int rad) {
        double r = Math.toRadians(angle);
        int sinOfrad = (int) (Math.sin(r) * rad);
        int cosOfrad = (int) (Math.cos(r) * rad);
        if (girlLife > 0 && collidePointWith(girlImage, BulletImage, girlPosition, enimyCirclePos.x - sinOfrad, enimyCirclePos.y + cosOfrad)) {
            playFXSound(0);
            girlLife -= girlInjureValue;//女孩生命值减少 子弹消失
        } else {
            g.drawImage(BulletImage, enimyCirclePos.x - sinOfrad, enimyCirclePos.y + cosOfrad, Graphics.VCENTER | Graphics.HCENTER);
        }
        if (girlLife > 0 && collidePointWith(girlImage, BulletImage, girlPosition, enimyCirclePos.x - cosOfrad, enimyCirclePos.y - sinOfrad)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(BulletImage, enimyCirclePos.x - cosOfrad, enimyCirclePos.y - sinOfrad, Graphics.VCENTER | Graphics.HCENTER);
        }
        if (girlLife > 0 && collidePointWith(girlImage, BulletImage, girlPosition, enimyCirclePos.x + sinOfrad, enimyCirclePos.y - cosOfrad)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(BulletImage, enimyCirclePos.x + sinOfrad, enimyCirclePos.y - cosOfrad, Graphics.VCENTER | Graphics.HCENTER);
        }
        if (girlLife > 0 && collidePointWith(girlImage, BulletImage, girlPosition, enimyCirclePos.x + cosOfrad, enimyCirclePos.y + sinOfrad)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(BulletImage, enimyCirclePos.x + cosOfrad, enimyCirclePos.y + sinOfrad, Graphics.VCENTER | Graphics.HCENTER);
        }
    }

    private void shotBulletForSin(Image bullet1, Graphics g, float angle, int rad) {
        for (int i = 0; i < 12; i++) {
            double r = Math.toRadians(angle * 3);
            int sinOfrad = (int) (Math.sin(r) * rad);
            // int cosOfrad = (int) (Math.cos(r+Math.PI) * rad);
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, enimyCirclePos.x + sinOfrad, enimyCirclePos.y + (int) angle)) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, enimyCirclePos.x + sinOfrad, enimyCirclePos.y + (int) angle, Graphics.VCENTER | Graphics.HCENTER);
            }
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, enimyCirclePos.x - sinOfrad, enimyCirclePos.y + (int) angle)) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, enimyCirclePos.x - sinOfrad, enimyCirclePos.y + (int) angle, Graphics.VCENTER | Graphics.HCENTER);
            }
            angle -= 10;
            if (angle <= 0) {
                break;
            }
        }
    }

    private void shotBulletForChuanYuan(Graphics g, int rad) {
        for (int i = 0; i < 4; i++) {
            drawCircle(boss4BulletImage, g, 10, enimyCirclePos.x, enimyCirclePos.y + rad);
            rad -= 20;
            if (rad <= 0) {
                break;
            }
        }
    }

    private void drawCircle(Image bullet1, Graphics g, int rad, int x, int y) {
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, x + rad, y)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, x + rad, y, Graphics.VCENTER | Graphics.HCENTER);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, x - rad, y)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, x - rad, y, Graphics.VCENTER | Graphics.HCENTER);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, x, y + rad)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, x, y + rad, Graphics.VCENTER | Graphics.HCENTER);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, x, y - rad)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, x, y - rad, Graphics.VCENTER | Graphics.HCENTER);
        }
        int i = (int) (0.707 * rad);
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, x + i, y + i)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, x + i, y + i, Graphics.VCENTER | Graphics.HCENTER);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, x - i, y - i)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, x - i, y - i, Graphics.VCENTER | Graphics.HCENTER);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, x + i, y - i)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, x + i, y - i, Graphics.VCENTER | Graphics.HCENTER);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, x - i, y + i)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, x - i, y + i, Graphics.VCENTER | Graphics.HCENTER);
        }
    }

    private void drawX2(Image bullet1, Graphics g, int y, Point center) {
        for (int i = 0; i < 5; i++) {
            int x = (int) Math.sqrt((double) y * 20);
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x + x, (int) (center.y + y))) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, center.x + x, (int) (center.y + y), Graphics.TOP | Graphics.LEFT);
            }
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x - x, (int) (center.y + y))) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, center.x - x, (int) (center.y + y), Graphics.TOP | Graphics.LEFT);
            }
            x = (int) Math.sqrt((double) y);
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x + x, (int) (center.y + y))) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, center.x + x, (int) (center.y + y), Graphics.TOP | Graphics.LEFT);
            }
            if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x - x, (int) (center.y + y))) {
                playFXSound(0);
                girlLife -= girlInjureValue;
            } else {
                g.drawImage(bullet1, center.x - x, (int) (center.y + y), Graphics.TOP | Graphics.LEFT);
            }
            y -= 10;
            if (y <= 0) {
                break;
            }
        }
    }
    public static double COS_3_30 = 0.6495;
    public static double COS_3_60 = 0.125;

    private void drawStar(Image bullet1, Graphics g, int rad, Point center) {
        int cos3 = (int) (COS_3_30 * rad);
        int sin3 = (int) (COS_3_60 * rad);
        int cos345 = (int) (0.3535 * rad);
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x + rad, center.y)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x + rad, center.y, Graphics.TOP | Graphics.LEFT);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x - rad, center.y)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x - rad, center.y, Graphics.TOP | Graphics.LEFT);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x, center.y + rad)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x, center.y + rad, Graphics.TOP | Graphics.LEFT);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x, center.y - rad)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x, center.y - rad, Graphics.TOP | Graphics.LEFT);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x + cos345, center.y - cos345)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x + cos345, center.y - cos345, Graphics.TOP | Graphics.LEFT);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x - cos345, center.y + cos345)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {

            g.drawImage(bullet1, center.x - cos345, center.y + cos345, Graphics.TOP | Graphics.LEFT);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x - cos345, center.y - cos345)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x - cos345, center.y - cos345, Graphics.TOP | Graphics.LEFT);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x + cos345, center.y + cos345)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x + cos345, center.y + cos345, Graphics.TOP | Graphics.LEFT);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x + cos3, center.y - sin3)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x + cos3, center.y - sin3, Graphics.TOP | Graphics.LEFT);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x + sin3, center.y - cos3)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {

            g.drawImage(bullet1, center.x + sin3, center.y - cos3, Graphics.TOP | Graphics.LEFT);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x - sin3, center.y - cos3)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x - sin3, center.y - cos3, Graphics.TOP | Graphics.LEFT);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x - cos3, center.y - sin3)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x - cos3, center.y - sin3, Graphics.TOP | Graphics.LEFT);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x - cos3, center.y + sin3)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x - cos3, center.y + sin3, Graphics.TOP | Graphics.LEFT);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x - sin3, center.y + cos3)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x - sin3, center.y + cos3, Graphics.TOP | Graphics.LEFT);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x + sin3, center.y + cos3)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x + sin3, center.y + cos3, Graphics.TOP | Graphics.LEFT);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x + cos3, center.y + sin3)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x + cos3, center.y + sin3, Graphics.TOP | Graphics.LEFT);
        }
    }

    private void drawHeart(Image bullet1, Graphics g, int a, Point center) {
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x, center.y - a)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x, center.y - a, Graphics.VCENTER | Graphics.HCENTER);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x, center.y + 3 * a)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x, center.y + 3 * a, Graphics.VCENTER | Graphics.HCENTER);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x - 2 * a, center.y)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x - 2 * a, center.y, Graphics.VCENTER | Graphics.HCENTER);
        }
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x + 2 * a, center.y)) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x + 2 * a, center.y, Graphics.VCENTER | Graphics.HCENTER);
        }

        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x - (int) (0.134 * a), center.y - (int) (1.232 * a))) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x - (int) (0.134 * a), center.y - (int) (1.232 * a), Graphics.VCENTER | Graphics.HCENTER);
        }//60du
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x - (int) (0.866 * a), center.y - (int) (1.5 * a))) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x - (int) (0.866 * a), center.y - (int) (1.5 * a), Graphics.VCENTER | Graphics.HCENTER);
        }//60du
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x - (int) (2.598 * a), center.y - (int) (-0.5 * a))) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x - (int) (2.598 * a), center.y - (int) (-0.5 * a), Graphics.VCENTER | Graphics.HCENTER);
        }//60du
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x - (int) (1.866 * a), center.y - (int) (-2.232 * a))) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x - (int) (1.866 * a), center.y - (int) (-2.232 * a), Graphics.VCENTER | Graphics.HCENTER);
        }//60du

        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x + (int) (0.134 * a), center.y - (int) (1.232 * a))) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x + (int) (0.134 * a), center.y - (int) (1.232 * a), Graphics.VCENTER | Graphics.HCENTER);
        }//60du
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x + (int) (0.866 * a), center.y - (int) (1.5 * a))) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x + (int) (0.866 * a), center.y - (int) (1.5 * a), Graphics.VCENTER | Graphics.HCENTER);
        }//60du
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x + (int) (2.598 * a), center.y - (int) (-0.5 * a))) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x + (int) (2.598 * a), center.y - (int) (-0.5 * a), Graphics.VCENTER | Graphics.HCENTER);
        }//60du
        if (girlLife > 0 && collidePointWith(girlImage, bullet1, girlPosition, center.x + (int) (1.866 * a), center.y - (int) (-2.232 * a))) {
            playFXSound(0);
            girlLife -= girlInjureValue;
        } else {
            g.drawImage(bullet1, center.x + (int) (1.866 * a), center.y - (int) (-2.232 * a), Graphics.VCENTER | Graphics.HCENTER);
        }//60du
    }

    /**
     * 女孩相关的逻辑处理，包括女孩发射子弹等的处理
     */
    private void girlLogicProcess() {
        //女孩子弹的逻辑处理
        for (int i = 0; i < girlBulletPosition.size(); i++) {
            Point p = (Point) (girlBulletPosition.elementAt(i));
            p.y -= girlBulletSpeed;
            if (p.y < 0 || p.y > 220) {
                girlBulletPosition.removeElement(p);
            }
        //(girlBulletPosition.size());
        }
    }

    /**
     * 整个屏幕的渲染函数
     */
    private void render(Graphics g) {
        g.setColor(0xffffff);
        g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        //  g.setColor(0x0);//将画笔恢复为黑色
        //@todo:添加屏幕的绘制代码
        //测试代码：
        renderBackgound(g);

        if (currentPosition > FIRST_EMERGE_POSITION_Y1 && currentPosition < SECOND_EMERGE_POSITION_Y) {
            renderFirst(g);
        }
        else if (currentPosition > SECOND_EMERGE_POSITION_Y && currentPosition < THIRD_EMERGE_POSITION_Y) {
            renderSecond(g);
        }
        else if (currentPosition > THIRD_EMERGE_POSITION_Y && currentPosition < THIRD_EMERGE_POSITION_Y + nextPosition) {
            renderThird(g);
        }
        else if (currentPosition > THIRD_EMERGE_POSITION_Y + nextPosition &&currentPosition<FOURTH_EMERGE_POSITION_Y) {
            renderThird1(g);
        }
        else if (currentPosition > FOURTH_EMERGE_POSITION_Y ) {
            renderFourth(g);
        }
        renderScore(g);
        renderGirl(g);
    }

    private void renderScore(Graphics g) {
        g.setColor(0xff0000);
        g.drawString("SCORE:" + girlScore, 100, 0, Graphics.TOP | Graphics.LEFT);
        g.drawString("LIFE:" + girlLife, 0, 0, Graphics.TOP | Graphics.LEFT);
//        g.drawString("Position:" + currentPosition, 100, 0, Graphics.TOP | Graphics.LEFT);
    }

    private void renderGirl(Graphics g) {
        //画女孩
        if (girlLife > 0) {
            g.drawImage(girlImage, girlPosition.x, girlPosition.y, Graphics.TOP | Graphics.LEFT);
        } else {
            if (sfImage == null) {
                try {
                    Image tempImage = Image.createImage("/res/sf.png");
                    sfImage = Image.createImage(tempImage, 0, 0, 120, 42, Sprite.TRANS_NONE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            g.drawImage(sfImage, this.getWidth() >> 1, this.getHeight() >> 1, Graphics.VCENTER | Graphics.HCENTER);
            g.drawString("是", 0, 200, Graphics.TOP | Graphics.LEFT);
            g.drawString("否", 150, 200, Graphics.TOP | Graphics.LEFT);
            this.stopGame();
            return;
        }
        for (int i = 0; i < girlBulletPosition.size(); i++) {
            Point p = (Point) girlBulletPosition.elementAt(i);
            drawImage(g, girlBulletImage, p);//画出女孩子弹的图像
        }
    }

    private void renderFirst(Graphics g) {
        for (int i = 0; i < 5; i++) {
            if (boss1ExistFlag[i] == true)//画出boss的图像
            {
                g.drawImage(boss1Image, boss1Position[i].x, boss1Position[i].y, Graphics.LEFT | Graphics.TOP);
            }
            int interval = bullet1Postion[i];
            for (int j = 0; j < 5; j++) {
                if (boss1ExistFlag[i] == false && bullet1CollideFlag[i][j] == 0) {
                    break;
                }
                if (boss1ExistFlag[i] == true && bullet1CollideFlag[i][j] == 1) {//画出boss子弹的图像
                    g.drawImage(boss1BulletImage, boss1Position[i].x + boss1Image.getWidth() / 2, boss1Position[i].y + interval, Graphics.LEFT | Graphics.TOP);
                }
                interval -= BOSS1_BULLET_INTERVAL;
                if (interval <= 0) {
                    break;
                }
            }
        }
    }
    Image boss3BulletImage1[];

    private void renderThird(Graphics g) {
        if (boss3BulletImage1 == null) {
            try {
                boss3BulletImage1 = new Image[3];
                boss3BulletImage1[0] = Image.createImage("/res/boss3BulletImage1.png");
                boss3BulletImage1[1] = Image.createImage("/res/boss3BulletImage2.png");
                boss3BulletImage1[2] = Image.createImage("/res/boss3BulletImage3.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        g.drawString("Bomb:" + bombNumber, 25, 200, Graphics.TOP | Graphics.LEFT);
        for (int i = 0; i < 3; i++) {
            if (boss3ExistFlag[i] == true) {
                g.drawImage(boss3Image, boss3Position[i].x, boss3Position[i].y, Graphics.LEFT | Graphics.TOP);
                ring1LogicProcess();
                shotBulletForRing(boss3BulletImage1[i], boss3Position[i].move(boss3Image.getWidth() / 2, boss3Image.getHeight() / 2), g, ringAngle1, 30);
            }
        }

        //启动炸弹：石头人消失 炸弹数减1
        if (bombFlag == 3 && bombNumber > 0 && bombNumber <= 3) {
            for (int i = 0; i < 3; i++) {
                g.drawImage(bomb, boss3Position[i].x, boss3Position[i].y, Graphics.LEFT | Graphics.TOP);
                boss3ExistFlag[i] = false;
            }
            bombNumber--;
            bombFlag = 0;
        }
    }

    private void renderThird1(Graphics g) {
        if (boss3ExistFlag1 == true) {

            g.drawString("BossLife:" + boss3Life1, 80, 200, Graphics.TOP | Graphics.LEFT);
            g.drawString("Bomb:" + bombNumber, 25, 200, Graphics.TOP | Graphics.LEFT);
            g.drawImage(boss3Image, boss3Position1.x, boss3Position1.y, Graphics.LEFT | Graphics.TOP);
            ring1LogicProcess();

            boss3BulletImage = boss3BulletImage1[0];
            shotBulletForRing(boss3BulletImage, boss3Position1.move(boss3Image.getWidth() / 2, boss3Image.getHeight() / 2), g, ringAngle1, 30);
            enimyCirclePos = boss3Position1.move(boss3Image.getWidth() / 2, boss3Image.getHeight() / 2);
            //基因曲线  释放两条95
            ring3LogicProcess();
            boss3BulletImage = boss3BulletImage1[1];
            shotBulletForSin(boss3BulletImage, g, ringAngle3, 30);
            //星形线90  循环两次
            line5ShotLogicProcess();

            boss3BulletImage = boss3BulletImage1[2];
            drawStar(boss3BulletImage, g, line5, enimyCirclePos);
            //启动炸弹：石头人消失 炸弹数减1
            if (bombFlag == 3 && bombNumber > 0 && bombNumber <= 3) {
                for (int i = 0; i <
                        5; i++) {
                    g.drawImage(bomb, bombPosition[i].x, bombPosition[i].y, Graphics.LEFT | Graphics.TOP);
                }

                ringAngle3 = 0;
                line5 = 0;
                bombNumber--;

                bombFlag = 0;
            }



        }
    }
    Image bossBulletImageArray[];

    private void renderFourth(Graphics g) {
        this.boss3BulletImage = null;
        this.boss3BulletImage1 = null;
        this.boss3ExistFlag = null;
        this.boss3Position = null;
        if (bossBulletImageArray == null) {
            bossBulletImageArray = new Image[3];
            try {
                bossBulletImageArray[0] = Image.createImage("/res/bossBullet1.png");
                bossBulletImageArray[1] = Image.createImage("/res/bossBullet2.png");
                bossBulletImageArray[2] = Image.createImage("/res/bossBullet3.png");
            } catch (IOException ex) {
            }
        }
        if (boss4ExistFlag == true) {
            g.setColor(0x0000ff);
            g.drawString("Bomb:" + bombNumber, 25, 200, Graphics.TOP | Graphics.LEFT);
            g.drawString("BossLife:" + BOSSLIFE, 80, 200, Graphics.TOP | Graphics.LEFT);
            g.drawImage(boss4Image, boss4Position.x, boss4Position.y, Graphics.LEFT | Graphics.TOP);
            enimyCirclePos = boss4Position.move(boss4Image.getWidth() / 2, boss4Image.getHeight() / 2);
            //旋转线  周身环绕
            if (currentPosition >= FOURTH_EMERGE_POSITION_Y + 0) {
                ring2LogicProcess();
                shotBulletForRing(boss4CircleImage, enimyCirclePos, g, ringAngle2, 30);
            }

            if (currentPosition >= FOURTH_EMERGE_POSITION_Y + 0 && currentPosition < FOURTH_EMERGE_POSITION_Y + 250) {
                if (bombFlag == 4 && bombNumber > 0 && bombNumber <= 3) {
                    for (int i = 0; i <  5; i++) {
                        g.drawImage(bomb, bombPosition[i].x, bombPosition[i].y, Graphics.LEFT | Graphics.TOP);
                    }
                    line2 = 0;
                    line3 = 0;
                    bombNumber--;
                    bombFlag = 0;
                } else {

                    enimyCirclePos = boss4Position.move(boss4Image.getWidth() / 2, boss4Image.getHeight() / 2);
                    // 串圆线80 循环两次
                    boss4BulletImage =bossBulletImageArray[0];
                    line2ShotLogicProcess();

                    shotBulletForChuanYuan(g, line2);
                    //散形线80
                    boss4BulletImage =
                            bossBulletImageArray[0];
                    line3ShotLogicProcess();

                    shotBulletForSector(boss4BulletImage, g, 180, line3);
                    shotBulletForSector(boss4BulletImage, g, 225, line3);
                    shotBulletForSector(boss4BulletImage, g, 270, line3);
                    shotBulletForSector(boss4BulletImage, g, 315, line3);
                    shotBulletForSector(boss4BulletImage, g, 0, line3);
                    shotBulletForSector(boss4BulletImage, g, 45, line3);
                    shotBulletForSector(boss4BulletImage, g, 90, line3);
                    shotBulletForSector(boss4BulletImage, g, 135, line3);


                }
            }
            if (currentPosition >= FOURTH_EMERGE_POSITION_Y + 250 && currentPosition < FOURTH_EMERGE_POSITION_Y + 500) {

                if (bombFlag == 4 && bombNumber > 0 && bombNumber <= 3) {
                    for (int i = 0; i < 5; i++) {
                        g.drawImage(bomb, bombPosition[i].x, bombPosition[i].y, Graphics.LEFT | Graphics.TOP);
                    }
                    ringAngle3 = 0;
                    line4 = 0;
                    bombNumber--;
                    bombFlag = 0;
                } else {
                    enimyCirclePos = boss4Position.move(boss4Image.getWidth() / 2, boss4Image.getHeight() / 2);
                    //基因曲线  释放两条95
                    boss4BulletImage = bossBulletImageArray[1];
                    ring3LogicProcess();
                    shotBulletForSin(boss4BulletImage, g, ringAngle3, 30);
                    boss4BulletImage = bossBulletImageArray[0];   
                    //心形线20  循环两次
                    line4ShotLogicProcess();
                    drawHeart(boss4BulletImage, g, line4, enimyCirclePos);
                }



            }
            if (currentPosition >= FOURTH_EMERGE_POSITION_Y + 500 && currentPosition < FOURTH_EMERGE_POSITION_Y + 750) {
                if (bombFlag == 4 && bombNumber > 0 && bombNumber <= 3) {
                    for (int i = 0; i < 5; i++) {
                        g.drawImage(bomb, bombPosition[i].x, bombPosition[i].y, Graphics.LEFT | Graphics.TOP);
                    }
                    line1 = 0;
                    line5 = 0;
                    bombNumber--;
                    bombFlag = 0;
                } else {
                    //x2四线80 循环两次
                    enimyCirclePos = boss4Position.move(boss4Image.getWidth() / 2, boss4Image.getHeight() / 2);
                    boss4BulletImage =
                            bossBulletImageArray[2];
                    line1ShotLogicProcess();

                    drawX2(boss4BulletImage, g, line1, enimyCirclePos);
                    //星形线90  循环两次
                    boss4BulletImage =
                            bossBulletImageArray[0];
                    line5ShotLogicProcess();

                    drawStar(boss4BulletImage, g, line5, enimyCirclePos);
                }
            }
            //圆环线755   终极大招一次
            if (currentPosition >= FOURTH_EMERGE_POSITION_Y + 750) {
                if (bombFlag == 4 && bombNumber > 0 && bombNumber <= 3) {
                    for (int i = 0; i < 5; i++) {
                        g.drawImage(bomb, bombPosition[i].x, bombPosition[i].y, Graphics.LEFT | Graphics.TOP);
                    }

                    for (int i = 0; i < 3; i++) {
                        radius[i] = 0;
                    }

                    bombNumber--;
                    bombFlag =
                            0;
                } else {
                    circleShotLogicProcess();
                    enimyCirclePos = boss4Position.move(boss4Image.getWidth() / 2, boss4Image.getHeight() / 2);
                    for (int i = 0; i < 3; i++) {
                        if (radius[i] != 0) {
                            if (i == 0) {
                                try {
                                    boss4BulletImage = Image.createImage("/res/bossBullet1.png");
                                    shotBulletForCircle(boss4BulletImage, g, radius[i]);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }

                            } else if (i == 1) {
                                try {
                                    boss4BulletImage = Image.createImage("/res/bossBullet2.png");
                                    shotBulletForCircle(boss4BulletImage, g, radius[i]);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }

                            } else if (i == 2) {
                                try {
                                    boss4BulletImage = Image.createImage("/res/bossBullet3.png");
                                    shotBulletForCircle(boss4BulletImage, g, radius[i]);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }

                            }
                        }
                    }
                }
            }
        } else {
            if (sfImage == null) {
                Image tempImage;
                try {
                    tempImage = Image.createImage("/res/sf.png");
                    sfImage =
                            Image.createImage(tempImage, 0, 42, 120, 42, Sprite.TRANS_NONE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
            g.drawImage(sfImage, this.getWidth() >> 1, this.getHeight() >> 1, Graphics.VCENTER | Graphics.HCENTER);
            g.setColor(0xff0000);
            g.drawString("分数：" + girlScore, 70, 140, Graphics.TOP | Graphics.LEFT);
            g.setColor(0x00ff00);
            g.drawString("继续游戏", 0, 200, Graphics.TOP | Graphics.LEFT);
            success = true;
        }

    }
    private Image sfImage;

    private void renderSecond(Graphics g) {
        for (int i = 0; i < 3; i++) {
            roidSprite[i].paint(g);
        }

    }

    private void drawImage(Graphics g, Image image, Point p) {
//        g.drawImage(image, p.x + girlWidth / 2, p.y, Graphics.TOP | Graphics.LEFT);
        g.drawImage(image, p.x, p.y, Graphics.TOP | Graphics.LEFT);
    }
    Image bg2_3;
    Image bg2_1_2;

    private void renderBackgound(Graphics g) {
        mainMidlet.showRestMem();
        System.out.println(girlBulletPosition.size());
        if (currentPosition <= THIRD_EMERGE_POSITION_Y) {
            if ((currentPosition % 2) == 0) {
                g.drawImage(bg2_1_1, 0, bgImagePosY, Graphics.TOP | Graphics.LEFT);
                g.drawImage(bg2_1_1, 0, bgImagePosY - 320, Graphics.TOP | Graphics.LEFT);
            } else {
                g.drawImage(bg2_1_2, 0, bgImagePosY, Graphics.TOP | Graphics.LEFT);
                g.drawImage(bg2_1_2, 0, bgImagePosY - 320, Graphics.TOP | Graphics.LEFT);
            }

            bgImagePosY += backgroundSpeed;
            if (bgImagePosY == 320) {
                bgImagePosY = 0;
            }

            if (currentPosition == THIRD_EMERGE_POSITION_Y) {
                try {
                    Image image = Image.createImage("/res/bg2_22.png");
                    Image[] image3 = new Image[4];
                    for (int i = 0; i <
                            4; i++) {
                        image3[i] = Image.createImage(image, i * 32, 0, 32, 32, Sprite.TRANS_NONE);
                    }

                    bg2_1_1 = Image.createImage(192, 320);
                    byte[][] tiles = {
                        {1, 3, 1, 1, 4, 1},
                        {1, 1, 1, 2, 1, 1},
                        {1, 1, 4, 1, 1, 1},
                        {1, 1, 1, 3, 1, 1},
                        {1, 1, 1, 1, 4, 1},
                        {2, 1, 3, 1, 1, 1},
                        {1, 1, 1, 1, 3, 1},
                        {1, 1, 2, 1, 1, 1},
                        {1, 4, 1, 1, 3, 1},
                        {1, 1, 1, 1, 2, 1}
                    };
                    Graphics g2d = bg2_1_1.getGraphics();
                    for (int i = 0; i <
                            10; i++) {
                        for (int j = 0; j <
                                6; j++) {
                            g2d.drawImage(image3[tiles[i][j] - 1], j * 32, i * 32, Graphics.TOP | Graphics.LEFT);
                        }

                    }
                    this.boss1Image = null;
                    this.boss1BulletImage = null;
                    this.boss1Position = null;
                    this.boss1ExistFlag = null;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                bgImagePosY = 0;
                bg2_1_2 =
                        null;
            }

        } else if (currentPosition <= FOURTH_EMERGE_POSITION_Y) {
            g.drawImage(bg2_1_1, 0, bgImagePosY, Graphics.TOP | Graphics.LEFT);
            g.drawImage(bg2_1_1, 0, bgImagePosY - 320, Graphics.TOP | Graphics.LEFT);
            bgImagePosY +=
                    backgroundSpeed;
            if (bgImagePosY == 320) {
                bgImagePosY = 0;
            }

        } else {
            if (bg2_3 == null) {
                try {
                    bg2_3 = Image.createImage("/res/bg2_3.png");
                    bg2_1_1 = null;
                    bird = new Image[2];
                    Image image = Image.createImage("/res/bird.png");
                    bird[0] = Image.createImage(image, 0, 0, 21, 15, Sprite.TRANS_NONE);
                    bird[1] = Image.createImage(image, 21, 0, 21, 15, Sprite.TRANS_NONE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }


            }
            g.drawImage(bg2_3, 0, 0, Graphics.TOP | Graphics.LEFT);
            g.drawImage(bird[currentPosition % 2], currentPosition % 300, currentPosition % 200, Graphics.TOP | Graphics.LEFT);
        }
    }
    Image bird[];

    /**
     * 线程的run函数，这里完成游戏屏幕的循环
     */
    public void run() {
        while (isStart) {
            try {
                long startTime = System.currentTimeMillis();
                keyPressProcess();
                logicProcess();
                render(g);
                MainMidlet.drawLogo(g, 40, 50, 2);
                flushGraphics();//不要删除此代码
                System.out.println("currentPosition:"+currentPosition);    
                long endTime = System.currentTimeMillis();//执行结束时间
                long duration = (long) (endTime - startTime);//计算执行游戏操作需要的时间
                if (duration < TIME_PER_FRAME) {
                    try {
                        Thread.sleep(TIME_PER_FRAME - duration);
                    }//保持一定的循环占用时间
                    catch (InterruptedException ie) {
                    }//捕获异常
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            //("线程运行时异常！");
            }

        }
    }

    /**
     * 该函数用来判断两幅图片是否碰撞
     * @param image1 图片1
     * @param image2 图片2 
     * @param p1 图片1的左上角位置
     * @param p2 图片2的左上角位置
     * @return 如果为真，碰撞了，否则没有碰撞
     */
    private boolean collideWith(Image image1, Image image2, Point p1, Point p2) {
        int w1 = image1.getWidth();
        int w2 = image2.getWidth();
        int h1 = image1.getHeight();
        int h2 = image2.getHeight();
        if (p2.x + w2 / 2 > p1.x && p2.x + w2 / 2 < p1.x + w1 && p2.y + h2 / 2 > p1.y && p2.y + h2 / 2 < p1.y + h1) {

            return true;
        } else {
            return false;
        }

    }

    private boolean collidePointWith(Image image1, Image image2, Point p1, int px, int py) {
        int w1 = image1.getWidth();
        int w2 = image2.getWidth();
        int h1 = image1.getHeight();
        int h2 = image2.getHeight();
        if (px + w2 / 2 > p1.x && px + w2 / 2 < p1.x + w1 && py + h2 /
                2 > p1.y && py + h2 / 2 < p1.y + h1) {
            return true;
        } else {
            return false;
        }

    }

    public void startGame() {
        isStart = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stopGame() {
        isStart = false;
        thread = null;
    }

    public boolean turnOnOrOffMusic() {
        if (currentMusicState == true) {
            musicControl.musicStop();
            musicControl = null;
            currentMusicState = false;
            fxControl.musicStop();

            fxControl = null;
            return currentMusicState;
        } else {
            musicControl = new MusicControl(true);
            fxControl = new MusicControl(false);
            musicControl.changeMusic("/res/stage2.mid");
            musicControl.musicStart();
            musicControl.changeMusicVolume(mainMidlet.musicVolume);
            currentMusicState = true;
            return currentMusicState;
        }

    }

    protected void keyPressed(int code) {

        if (code == -7) {//右软键
            if (girlLife > 0) {
                this.stopGame();
                mainMidlet.stage2PausedMenuShow();
            } else {
                this.stopGame();
                if (currentMusicState == true) {
                    turnOnOrOffMusic();
                }
                mainMidlet.mainMenuShow(7);
            }
        }

        if (code == -6) { //左软键
            if (girlLife <= 0) {
                this.stopGame();
                if (currentMusicState == true) {
                    turnOnOrOffMusic();
                }

                mainMidlet.stage2GameShow(true);
            } else {
                if (success) {
                    DataStore d = new DataStore();
                    d.writeScoreRecord(mainMidlet.playerName, girlScore);
                    stopGame();
                    if (currentMusicState == true) {
                        turnOnOrOffMusic();
                    }
                    mainMidlet.stage2AnimationShow(3);
                }

            }
        }
    }

    private void playFXSound(int type) {
        if (currentMusicState == true && fxControl != null) {
            if (type == 0) {
                fxControl.musicStop();
                fxControl.changeMusic("/res/girlHurt.wav");
            } else if (type == 1) {
                fxControl.changeMusic("/res/bat.wav");
            } else if (type == 2) {
                fxControl.changeMusic("/res/bossHurt.wav");
            } else if (type == 3) {
                fxControl.changeMusic("/res/angle.wav");
            } else if (type == 4) {
                fxControl.changeMusic("/res/shot.wav");
            }
            fxControl.musicStart();
        }
    }
}
