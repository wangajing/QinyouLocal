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
import mainMenu.DataStore;
import mainMenu.MainMidlet;
import res.MusicControl;

/**
 *
 * @author 版权孙杰所有2008年8月4日
 */
public class HorseRace extends GameCanvas implements Runnable {

    private Graphics g = this.getGraphics();
    private Thread thread;//内在线程
    private Image pass = null;
    private short angle;//实现boss子弹的旋转效果
    private byte stageflag = 1;
    private Image ready = null;
    private byte second;
    private byte minute;
    private byte timeflag;
    private Image go = null;
    private byte jumpflag;//跳起来的标志
    private byte jumpleft;//剩余的可用的跳的次数
    private byte life;
    private byte lifey;
    private byte lifetimes;
    private short score;
    private boolean isStart = true;
    private short machex;//马车坐标
    private short machey;
    private boolean hasSecondStageInited;
    private short currentmachex;
    private short currentmachey;
    private int roady;//路的y值
    private Image background = null;//背景
    private Image mache = null;//马车
    private Image macheleft = null;
    private Image macheright = null;
    private Image road = null;
    private Image gameOverGround = null;
    private Image enemysmall = null;
    private Image bossBullet = null;
    private Image macheNormal;
    private static final int[][] map = { //背景图片组合地图
        {0, 0, 0},
        {0, 0, 0},
        {0, 0, 0},
        {0, 0, 0},
        {0, 0, 0},
        {0, 0, 0},
        {0, 0, 0},
        {0, 0, 0},
        {0, 0, 0},
        {0, 0, 0},
        {0, 0, 0},
    };
    private Image treebg;
    private Image daoju;
    private Image[] tree = new Image[6];
    private Image[] flag = new Image[3];
    private Image[] pubu = new Image[5];
    private Image[] daojuarry = new Image[7];
    private int currentPosition = 0;//当前的位置
    private int enemycurrentPosition = 0;
    private Image roadbg = null;
    private short[] treelx = {-20, -20, -20, -20, -20, -20};//左边树的坐标
    private short[] treely = {-600, -600, -600, -600, -600, -600};
    private short[] treerx = {-20, -20, -20, -20, -20, -20};//右边树的坐标
    private short[] treery = {-600, -600, -600, -600, -600, -600};
    private short[] times = {0, 0, 0, 0, 0, 0};//每棵树向下移动的次数
    private short[] daojutimes = new short[56];//每个道具向下移动的次数
    private short[] daojux = new short[56];
    private short[] daojuy = new short[56];
    private short[] position = new short[56];
    private short[] treeposition = {0, 50, 100, 150, 200, 250};
    private boolean[] isInited = new boolean[56];
    private boolean isStop;//是否遇到了暂停的道具
    private boolean isSpeedUp;//是否已经加速
    private byte[] typeOfDaoju = {
        0, 2, 1, 0, 3, 5, 1, 4, 2, 3,
        1, 5, 6, 5, 0, 3, 3, 2, 4, 0,
        3, 1, 1, 2, 1, 3, 2, 6, 5,
        6, 4, 0, 2, 3, 0, 3, 1, 2,
        0, 3, 2, 1, 4, 3, 2, 5, 0, 5,
        6, 1, 2, 0, 0, 4, 4, 0
    };//七种道具的顺序
    private short[] daojufirstx = {
        58, 88, 88, 118, 103, 88, 88, 103, 118, 58,
        73, 88, 88, 88, 118, 118, 58, 73, 88, 88,
        73, 103, 73, 103, 88, 118, 73, 88, 88,
        88, 88, 103, 73, 58, 88, 103, 103, 118,
        73, 73, 58, 58, 73, 88, 73, 88, 88, 88,
        88, 73, 103, 118, 58, 88, 103, 118
    };
    private int speed;//移动的速度
    private byte twoTimes;//用于计算加速的时间
    private Image woodleft = null;
    private Image woodright = null;
    private byte isCollided;//用来存贮第一关中28个道具中哪一个被碰撞
    private boolean cansee;//马车是否可见
    private byte timesOfRender;//发生碰撞后的重绘次数
    private byte canseeflag;
    private boolean macheLeftOrRight;
    private boolean enemyLeftOrRight;
    private byte stopTimes;
    private int lastCollided;
    private byte numoftype = 56;//总道具的类型
    private short circleWay = 5700;//总的循环一圈摆放道具的路程
    private short delayflag;//实现延时的时间标志
    private Image flagpic = null;
    private byte flagnum;//实现提示栏时间长短的控制
    private short stickflag;//实现提示栏的显示时间
    private byte startflag;
    private short enemyx;//敌人坐标
    private short enemyy;
    private boolean indexenemy;
    private short pointx;
    private short pointy;
    private short currentpointx;
    private short currentpointy;
    private short knifex;//
    private short knifey;
    private short currentknifex;//刀子的当前坐标
    private short currentknifey;
    private short headLeftTopx;
    private short headLeftTopy;
    private short bodyLeftTopx;
    private short bodyLeftTopy;
    private short bodylife;
    private short headlife;
    private short knifeNum;
    private short lastTime;
    private Image frontscence = null;
    private Image enemy = null;
    private Image knife = null;
    private Image miss = null;
    private boolean stage2Collided;
    private Image point;
    private float k;
    private byte distanceflag;//第二关中敌人的与自己距离的标记
    private byte changedistanceflag;
    private boolean distanceChangeDirect;
    private boolean plusOrMinusDirect;
    private byte knifenum;
    private int headw;
    private int headh;
    private int bodyw;
    private int bodyh;
    private boolean isKnifeinScreen;
    private int enemylife;
    private Image pubupic = null;//瀑布的图片
    private boolean missrenderflag;
    private boolean currentMusicState;
    private MusicControl musicControl;
    private MusicControl hitmusic;
    private HorseRacePause pauseMenu;
    private byte currentGameState;
    private Image sfImage = null;
    private boolean connected;
    private boolean isMultiGame;
    private MainMidlet mainMidlet;
    private boolean synchronization;//蓝牙模式下同步游戏的信号
    private int competitorX;//另一玩家的生命值
    private byte competitorLifeTimes;//另一玩家的生命次数
    private byte competitorLife;//另一玩家的生命值
    private short totalScore;
    private byte circleflag;
    private short bulletox;
    private short bulletoy;
    private byte shortflag;
    private int timescore;
    private Image competitormache = null;
    private byte enemyjumpleft;

    public HorseRace(MainMidlet mainMidlet, boolean isMultiGame) throws IOException {
        super(false);
        this.mainMidlet = mainMidlet;
        this.isMultiGame = isMultiGame;

        this.setFullScreenMode(true);
        initFirstStage();
    }

    public byte isFailed() {
        if (currentGameState == 2) {
            return currentGameState;
        } else {
            return 0;
        }
    }

    public void isMultiFailed() {
        if (((competitorLifeTimes * 100 + competitorLife) == 0 && enemycurrentPosition < 13000) || ((lifetimes * 100 + life) != 0 && currentPosition >= 13000)) {
            currentGameState = 3;
        }
        if ((life == 0 && currentPosition < 13000) || ((competitorLifeTimes * 100 + competitorLife) != 0 && enemycurrentPosition >= 13000)) {
            currentGameState = 2;
        }

    }

    protected int getCurrentPosition() {
        return currentPosition;
    }

    protected byte getLife() {
        return life;
    }

    protected int getX() {
        return (currentmachex);
    }

    protected byte getLifetimes() {
        return lifetimes;
    }

    protected byte getCurrentGameState() {
        return currentGameState;
    }

    public byte getJump() {
        return jumpleft;
    }

    private void isCollidedForStage2() {
        isColliedBullet((short) 15);
        stage2Collided = false;
        if (pointy <= headLeftTopy + headh && pointy >= headLeftTopy && currentknifey >= headLeftTopy && currentknifey <= headLeftTopy + headh && pointx >= headLeftTopx && pointx <= headLeftTopx + headw) {
            enemylife = Math.max(0, enemylife - 100);

            stage2Collided = true;
            currentknifex = -100;
            currentknifey = -100;
            timesOfRender = 0;
        }
        if (pointy <= bodyLeftTopy + bodyh && pointy >= bodyLeftTopy && currentknifey >= bodyLeftTopy && currentknifey <= bodyLeftTopy + bodyh && pointx >= bodyLeftTopx && pointx <= bodyLeftTopx + bodyw) {
            enemylife = Math.max(0, enemylife - 40);

            stage2Collided = true;
            currentknifex = -100;
            currentknifey = -100;
            timesOfRender = 0;
        }

    }

    private void keyPressProcessForStage2() throws IOException {
        int keyState = this.getKeyStates();
        if ((keyState & UP_PRESSED) != 0) {
            currentpointy = (short) Math.max(50, currentpointy - 2);
        } else if ((keyState & DOWN_PRESSED) != 0) {
            currentpointy = (short) Math.min(140, currentpointy + 2);
        } else if ((keyState & LEFT_PRESSED) != 0) {
            currentpointx = (short) Math.max(64, currentpointx - 2);
            currentmachex = machex = (short) Math.max(28, machex - 5);
            mache = macheleft;
        } else if ((keyState & RIGHT_PRESSED) != 0) {
            currentmachex = machex = (short) Math.min(148, machex + 5);
            mache = macheright;
            currentpointx = (short) Math.min(112, currentpointx + 2);

        } else if ((keyState & FIRE_PRESSED) != 0) {
            if (!isKnifeinScreen && knifenum > 0) {
                pointx = currentpointx;
                pointy = currentpointy;
                knifex = currentknifex = machex;
                knifey = currentknifey = machey;
                if (currentMusicState == true) {
                    hitmusic.musicStop();
                    hitmusic.changeMusic("/res/life--.wav");
                    hitmusic.musicStart();
                }
                k = (float) (currentknifex - pointx) / (float) (currentknifey - pointy);//刀子y方向每移动1个方向则k上移动k个方向
                knifenum = (byte) Math.max(0, knifenum - 1);
            }
        } else if (keyState == 0) {
            mache = macheNormal;
            macheLibrate();
        }

    }

    private void renderForStage2(Graphics g) {
        g.setColor(0xffffff);
        g.fillRect(0, 0, 176, 220);
        renderRoad(g);
        renderpubu();
        g.drawImage(background, 0, 0, Graphics.LEFT | Graphics.TOP);
        if (cansee) {
            renderSecondStageEnemy();
        }
        renderPoint();
        if (isKnifeinScreen && stage2Collided == false) {
            renderKnife();
        }
        g.setColor(0x0000ff);
        g.fillRect(5, 5, (int) ((enemylife / 100.0f) * 40), 5);
        g.drawRect(5, 5, 40, 5);
        g.setColor(0xff0000);
        g.fillRect(5, 15, (int) ((life / 100.0f) * 40), 5);
        g.drawRect(5, 15, 40, 5);
        g.setColor(0x00ff00);
        g.drawString("TRY" + lifetimes, 50, 15, Graphics.LEFT | Graphics.TOP);
        g.drawString("Knife:" + knifenum, 10, 25, Graphics.LEFT | Graphics.TOP);
        g.drawString("Score:" + score, 110, 0, Graphics.LEFT | Graphics.TOP);
        renderTime();
        g.setColor(0x00ff00);
        g.drawLine(03, 43, 173, 43);
        g.setColor(0xff0000);
        g.fillRect(173, 42, 2, 2);
            renderPosition(0xff0000, currentPosition / 100 + 3);//画当前的位置，也由第二个参数得出最大的路程
        g.drawImage(mache, currentmachex, currentmachey, Graphics.HCENTER | Graphics.TOP);
        if (bulletoy != 0) {
            renderBossBullet(bulletox, bulletoy, (short) 15);
        }
        renderflag();
        if (currentGameState == 1) {
            pauseMenu.render();
        }
        if (currentGameState == 2) {
            showSuccessOrFailed(g, (byte) -2);//输掉！

        }
        if (currentGameState == 3) {
            renderPassGame();
        }

        g.setColor(0);


    }

    private void renderTree() {//绘制路边的场景树
        g.drawImage(tree[0], treelx[0], treely[0], Graphics.VCENTER | Graphics.HCENTER);
        g.drawImage(tree[1], treelx[1], treely[1], Graphics.VCENTER | Graphics.HCENTER);
        g.drawImage(tree[2], treelx[2], treely[2], Graphics.VCENTER | Graphics.HCENTER);
        g.drawImage(tree[3], treelx[3], treely[3], Graphics.VCENTER | Graphics.HCENTER);
        g.drawImage(tree[4], treelx[4], treely[4], Graphics.VCENTER | Graphics.HCENTER);
        g.drawImage(tree[5], treelx[5], treely[5], Graphics.VCENTER | Graphics.HCENTER);
        g.drawImage(tree[5], treerx[0], treery[0], Graphics.VCENTER | Graphics.HCENTER);
        g.drawImage(tree[4], treerx[1], treery[1], Graphics.VCENTER | Graphics.HCENTER);
        g.drawImage(tree[3], treerx[2], treery[2], Graphics.VCENTER | Graphics.HCENTER);
        g.drawImage(tree[2], treerx[3], treery[3], Graphics.VCENTER | Graphics.HCENTER);
        g.drawImage(tree[1], treerx[4], treery[4], Graphics.VCENTER | Graphics.HCENTER);
        g.drawImage(tree[0], treerx[5], treery[5], Graphics.VCENTER | Graphics.HCENTER);
    }

    private void layTree() {
        int residue = (int) (currentPosition % 200);
        if (!isStop) {
            for (int i = 0; i < 6; i++) {
                if (residue == 45 * i) {
                    initTree(i);
                }
            }
            for (int i = 0; i < 6; i++) {
                if (treely[i] <= 230) {
                    moveTree(i, 35);
                }
                if (treery[i] <= 230) {
                    moveTree(i, 141);
                    times[i]++;
                }
            }
        }
    }

    private void layDaoJu() {
        if (!isStop) {
            for (int i = 0; i < numoftype; i++) {
                if (currentPosition % circleWay >= (position[i] - 5) && currentPosition % circleWay <= (position[i] + 5)) //此处易导致加速时的错误
                {
                    initDaoJu(i, daojux[i]);
//                    break;
                }//
            }
            for (int i = 0; i < numoftype; i++) {
                if (isInited[i] == true) {
                    moveDaoJu(i, daojufirstx[i]);
                }
            }
            for (int i = 0; i < numoftype; i++) {//将超出屏幕范围的树全部置为未初始化，即不可移动
                if (daojux[i] < -10 || daojux[i] > 220) {
                    isInited[i] = false;
                }
            }
        }

    }

    public void run() {

        try {
            long currentTime = System.currentTimeMillis();
            if (!isMultiGame) {
                runForSingle();
            } else {
                runForMulti();
            }
            Thread.sleep(50);
        } catch (Exception ex) {
            ex.printStackTrace();
        //("????????????");
        }

    }

    private void runForMulti() throws IOException, InterruptedException {
        while (isStart) {
            if (!synchronization) {
                renderWait(g);
            } else {
                if (currentGameState == 3 || currentGameState == 2) {
                    renderMultiOver(g);
                } else {
                    if (stageflag == 1) {
                        isMultiFailed();//如果在第一关
                        if (delayflag >= 0) {

                            if (stopTimes > 20 || stopTimes < 0) {
                                keyPressProcessForStage1();
                            }

                            logicProcessForStage1();
                        }
                        if (stickflag >= 0) {
                            flagnum = -1;
                        }
                        renderForMulti(g);
                        if (delayflag > 120) {
                            delayflag = 0;//当delayflag大于120时置0以免运算出界
                        }
                        delayflag++;
                        if (stickflag > 120) {
                            stickflag = 0;//当delayflag大于120时置0以免运算出界
                        }
                        stickflag++;
                    } else if (stageflag == 2) {//如果在第二关执行
                        if (!hasSecondStageInited) {
                            changeStage();
                        }
                        keyPressProcessForStage2();
                        logicProcessForStage2();
                        renderForStage2(g);
                        if (stickflag > 120) {
                            stickflag = 0;//当delayflag大于120时置0以免运算出界
                        }
                        stickflag++;
                    }
                
                }
            }
            MainMidlet.drawLogo(g, 40, 50, 5);
            flushGraphics();
            Thread.sleep(50);

        }

    }

    private void runForSingle() throws IOException, InterruptedException {
        while (isStart) {
            if (currentGameState == 1) //                || currentGameState == 2) 
            { //暂停的状态菜单
                if (pauseMenu == null) {
                    pauseMenu = new HorseRacePause(g, this);
                }
                if(stageflag==1)
                {
                renderForStage1(g);
                }
                else if(stageflag==2)
                {
                    renderForStage2(g);
                }
            } else {
                if (stageflag == 1) {//如果过在第一关
                    if (delayflag >= 0) {

                        if (stopTimes > 20 || stopTimes < 0) {
                            if (currentGameState != 2) {
                                keyPressProcessForStage1();
                            }
                        }
                        if (currentGameState != 2) {
                            logicProcessForStage1();
                        }
                    }
                    if (stickflag >= 0) {
                        flagnum = -1;
                    }
                    renderForStage1(g);
                    if (delayflag > 120) {
                        delayflag = 0;//当delayflag大于120时置0以免运算出界
                    }
                    delayflag++;
                    if (stickflag > 120) {
                        stickflag = 0;//当delayflag大于120时置0以免运算出界
                    }
                    stickflag++;
                } else if (stageflag == 2) {//如果在第二关执行
                    if (!hasSecondStageInited) {
                        changeStage();
                    }
                    if (currentGameState != 2 && currentGameState != 3) {
                        keyPressProcessForStage2();
                        logicProcessForStage2();
                    }
                    renderForStage2(g);
                    if (stickflag >= 0) {
                        flagnum = -1;
                    }
                    if (stickflag > 120) {
                        stickflag = 0;//当delayflag大于120时置0以免运算出界
                    }
                    stickflag++;
                }
            }
            MainMidlet.drawLogo(g, 40, 50, 5);
            flushGraphics();
            Thread.sleep(50);
        }
    }

    public void time() {
        timeflag++;
        if (timeflag == 20) {
            timeflag = 0;
            second++;
            if (second == 60) {
                second = 0;
                minute++;
            }
        }
    }

    public void renderTime() {
        if (second < 10) {
            g.drawString("0" + minute + ":0" + second, 140, 15, Graphics.LEFT | Graphics.TOP);
        } else {
            g.drawString("0" + minute + ":" + second, 140, 15, Graphics.LEFT | Graphics.TOP);
        }
    }

    public void initFirstStage() throws IOException {
//        gameOverGround = Image.createImage("/res/gameoverground.png");
        minute = 0;
        second = 0;
        timeflag = 0;

        currentMusicState = false;
        if (mainMidlet.needMusic) {
            turnOnOrOffMusic();
        }
        if (isMultiGame) {
            competitormache = Image.createImage("/res/enemymache.png");
            competitorX = 88;//初始化多人模式下敌人的状态
            competitorLife = 100;//
            competitorLifeTimes = 2;//
            enemyjumpleft = 10;
            synchronization = false;//初始没有同步
        }
        currentGameState = 0;//表示游戏处于正常态
        startflag = -60;
        isKnifeinScreen = false;
        distanceflag = 0;
        plusOrMinusDirect = false;
        distanceChangeDirect = false;//第二关中敌人改变距离的方向
        hasSecondStageInited = false;//第二关还未初始化
        flagnum = -1;//提示栏图标序号，-1表示不显示
        macheLeftOrRight = false;
        enemyLeftOrRight = false;
        jumpflag = 0;
        jumpleft = 10;
        stopTimes = 41;
        enemyx = 88;
        twoTimes = 60;//可以算得加速的时间为3秒
        life = 100;//初始化生命值
        lifetimes = 2;//初始化生命次数
        score = 0;//初始化得分
        timesOfRender = 6;//用来实现车的闪烁
        canseeflag = 0;
        isCollided = -100;//初始时无碰撞
        flagpic = Image.createImage("/res/flag.png");
        treebg = Image.createImage("/res/tree.png");
        daoju = Image.createImage("/res/daoju.png");
        background = Image.createImage("/res/background.png");
        mache = Image.createImage("/res/mache.png");
        macheleft = Image.createImage("/res/macheleft.png");
        macheright = Image.createImage("/res/macheright.png");
        road = Image.createImage("/res/road.png");
        woodleft = Image.createImage("/res/woodleft.png");
        woodright = Image.createImage("/res/woodright.png");
        enemysmall = Image.createImage("/res/enemysmall.png");
        macheNormal = Image.createImage("/res/mache.png");
        currentmachex = machex = 88;//马车的初始坐标
        currentmachey = machey = 185;
        cansee = true;
        speed = 0;
        currentPosition = 0;//初始化当前位置为0
        for (int i = 0; i < 3; i++) {
            flag[i] = Image.createImage(flagpic, i * 20, 0, 20, 20, Sprite.TRANS_NONE);
        }
        flagpic = null;
        for (int i = 0; i < 6; i++) {//初始化树的数组
            tree[i] = Image.createImage(treebg, i * 27, 0, 27, 32, Sprite.TRANS_NONE);
        }
        treebg = null;
        Image image = Image.createImage("/res/road.png");
        for (int i = 0; i < 4; i++) {
            daojuarry[i] = Image.createImage(daoju, i * 20, 0, 20, 20, Sprite.TRANS_NONE);
        }
        daoju = null;
        daojuarry[4] = Image.createImage("/res/bingmayong.png");
        daojuarry[5] = Image.createImage(woodleft);
        daojuarry[6] = Image.createImage(woodright);
        woodleft = null;
        woodright = null;
        daoju=null;
        roadbg = Image.createImage(180, 220);
        Graphics g2d = roadbg.getGraphics();
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 3; j++) {
                if (map[i][j] == 0) {
                    g2d.drawImage(road, j * 60, i * 20, Graphics.TOP | Graphics.LEFT);
                }
            }
        }

        for (int i = 0; i < numoftype; i++) {
            daojux[i] = daojufirstx[i];
        }
        for (int i = 0; i < numoftype; i++) {
            daojuy[i] = -6000;
        }
        for (int i = 0; i < numoftype; i++) {
            isInited[i] = false;
        }
        isStop = false;
       //isSpeedUp = false;
        for (int i = 0; i < numoftype; i++) {
            position[i] = (short) (50 + 100 * i);
        }
    }
     public void changeStage() throws IOException {
        shortflag = 0;//boss发子弹的标志
        bulletox = 0;
        bulletoy = 0;
        circleflag = 0;//实现子弹旋转的延时，
        timesOfRender = 6;
        canseeflag = 0;
        cansee = true;//敌人可见状态
        flagnum = 6;//出现提示栏
        stickflag = -20;
        speed = 10;
        enemylife = 100;
        hasSecondStageInited = true;
        distanceflag = 0;//表示敌人在离自己多远的地方
        changedistanceflag = 0;//控制改变敌人离自己的距离
        plusOrMinusDirect = false;
        flagpic = null;//将第一关不用的图片清除
        flag = null;
        treebg = null;
        daojuarry = null;
        tree = null;
        daoju = null;
        background = null;
        woodleft = null;
        woodright = null;
        treelx = null;
        treely = null;
        treerx = null;
        treery = null;
        times = null;
        daojutimes = null;
        daojux = null;
        daojuy = null;
        position = null;
        treeposition = null;
        isInited = null;
        typeOfDaoju = null;
        daojufirstx = null;
        machex = 88;//马车的初始坐标
        machey = 185;
        enemyx = 88;//初始化敌人坐标
        enemyy = 80;
        currentpointx = pointx = 88;//初始化瞄准器坐标
        currentpointy = pointy = 70;
        knifex = -200;//初始化匕首的坐标
        knifey = -200;
        currentknifex = -200;
        currentknifey = -200;
        knifenum = 5;//刀的数目
        headLeftTopx = (short) (enemyx - 5);//初始化头部矩形区域
        headLeftTopy = enemyy;
        headw = 10;
        headh = 20;
        bodyLeftTopx = (short) (enemyx - 20);//初始化车身矩形区域
        bodyLeftTopy = (short) (enemyy + 20);
        bodyw = 40;
        bodyh = 20;
        bodylife = 20;//初始化敌人生命
        headlife = 10;
        knifeNum = 100;
        stage2Collided = false;//初始化碰撞检测标志
        lastTime = -20;//初始化上次fire键按下时间
        background = Image.createImage("/res/nextbackground.png");
        pubupic = Image.createImage("/res/pubu.png");
        for (int i = 0; i < 5; i++) {
            pubu[i] = Image.createImage(pubupic, 0, i * 15, 67, 15, Sprite.TRANS_NONE);
        }
        enemy = Image.createImage("/res/enemy.png");
        point = Image.createImage("/res/point.png");

    }

    private void keyPressProcessForStage1() throws IOException {
        int keyState = this.getKeyStates();
        if ((keyState & UP_PRESSED) != 0) {
            if (jumpleft > 0) {
                if (jumpflag >= 0) {
                    jumpflag = -15;
                    jumpleft = (byte) Math.max(0, jumpleft - 1);
                    currentmachey = 165;
                }
            }

        } else if ((keyState & DOWN_PRESSED) != 0) {
        } else if ((keyState & LEFT_PRESSED) != 0) {

            currentmachex = machex = (short) Math.max(28, machex - 5);
            mache = macheleft;
        } else if ((keyState & RIGHT_PRESSED) != 0) {
            currentmachex = machex = (short) Math.min(148, machex + 5);
            mache = macheright;

        } else if ((keyState & FIRE_PRESSED) != 0) {
        } else if (keyState == 0) {
            mache = macheNormal;
            macheLibrate();
        }

    }

    private void logicProcessForStage1() {


        enemyLibrate();
        isJumped();

        if (stopTimes > 100) {//暂停1秒后恢复原速
            stopTimes = 20;
        }
        if (stopTimes == 19) {
            isStop = false;
            speed = 5;

        }
        stopTimes++;
        if (startflag == 0) {
            speed = 5;
        }
        if (startflag <= 1) {
            startflag++;
        }
        if (startflag >= 0) {
            time();
        }
//        currentPosition += speed;
        if (roady >= 80) {
            roady = 0;
        }
        if (!isStop) {//如果是暂停状态则路面不移动
            roady += speed;//路背景的移动
        }
        layTree();

        layDaoJu();
        currentPosition += speed;

        if (twoTimes > 100) {//加速的3秒后恢复速度
            twoTimes = 60;
        }
        if (twoTimes == 59) {
            speed = 5;
            if (currentMusicState == true) {
                musicControl.musicChangeRate(100000);
            }
        }
        twoTimes++;
        if (jumpflag >= 0) {
            isCollidedForStage1();//碰撞检测
        }
        if (timesOfRender > 100) {//实现闪烁效果
            timesOfRender = 6;
        }
        if (timesOfRender < 6) {
            if (canseeflag % 2 == 0) {
                cansee = true;
            } else {
                cansee = false;
            }
        } else {
            cansee = true;
        }
        canseeflag++;
        timesOfRender++;

        if (canseeflag > 5) {
            canseeflag = 0;
        }
        for (int i = 0; i < numoftype; i++) {//如果第i个发生碰撞则将第i个道具移出屏幕并将碰撞的标志清除
            if (isCollided == i) {
                daojux[i] = -10000;
                daojuy[i] = -10000;
                isCollided = -100;
            }
        }
        if (currentPosition > 13000) {
            stageflag = 2;
        } else if (life == 0) {
            currentGameState = 2;//如果第一关中生命值为0则游戏失败
            speed = 0;
        //("游戏失败");
        }

    }

    protected void keyPressed(int code) {
        switch (code) {
            case -7: //右软键
                if (currentGameState == 0) {//如果是正常状态，选择右软键，游戏暂停

                    if (!this.isMultiGame || (this.isMultiGame && (!this.connected))) {
                        this.gamePause();
                    }
                    if (!synchronization && isMultiGame) {
                        this.gameStop();
                        mainMidlet.mainMenuShow(8);
                    }
                } else if (currentGameState == 2) {//如果当前失败的话，选择游戏结束
                    gameStop();
                    if (isMultiGame) {

                        mainMidlet.mainMenuShow(8);//进入主菜单的选择服务端还是客户端    
                    } else {
                        mainMidlet.mainMenuShow(7);
                    }
                } else if (currentGameState == 3 && isMultiGame) {//如果成功，多人游戏，退出游戏
                    gameStop();
                    mainMidlet.mainMenuShow(8);//进入主菜单的选择服务端还是客户端
                } 
                else if (currentGameState == 3) {//如果成功，显示成功的画面
                    gameStop();
                    if (isMultiGame) {
                        mainMidlet.mainMenuShow(8);//进入主菜单的选择服务端还是客户端    
                    } else {
                        mainMidlet.stage3AnimationShow(6);
                    }
                }
                break;
            case -6://左软键
                if (currentGameState == 2) {//单人游戏下游戏失败的话，选择游戏重新开始
                    this.gameStop();
                    mainMidlet.stage3GameShow();
                }
                if (currentGameState == 3) {//如果成功，显示成功的画面
                    gameStop();
//                    mainMidlet.mainMenuShow(7);
                    if (isMultiGame) {
                        mainMidlet.mainMenuShow(8);//进入主菜单的选择服务端还是客户端    
                    } else {
                        mainMidlet.stage3AnimationShow(6);
                    }
                }
                break;
            case -1://上:响应暂停时的菜单响应
            case KEY_NUM2:
                if (currentGameState == 1) {//暂停状态
                    if (pauseMenu != null) {
                        pauseMenu.onKeyPressed(UP);
                    }
                }
                break;
            case -2://下
            case KEY_NUM8:
                if (currentGameState == 1) {
                    if (pauseMenu != null) {
                        pauseMenu.onKeyPressed(DOWN);
                    }
                }
                break;
            case -3://左
            case KEY_NUM4:
                if (currentGameState == 1) {
                    if (pauseMenu != null) {
                        pauseMenu.onKeyPressed(LEFT);
                    }

                }
                break;
            case -4://右
            case KEY_NUM6:
                if (currentGameState == 1) {
                    if (pauseMenu != null) {
                        pauseMenu.onKeyPressed(RIGHT);
                    }
                }
                break;
            case -5://fire
                if (currentGameState == 1 && pauseMenu != null) {
                    if (pauseMenu.getIndex() == 1) {
                        currentGameState = 0;
                        pauseMenu = null;
                        code = this.getKeyStates();
                        System.gc();
                    } else if (pauseMenu.getIndex() == 3) {
                        this.gameStop();
                        //("你选择了退出游戏");
                        mainMidlet.mainMenuShow(7);
                    }
                }
                break;
        }

    }

    private void logicProcessForStage2() {
        isCollidedForStage2();
        time();
        if (timesOfRender > 100) {
            timesOfRender = 6;
        }
        if (timesOfRender < 6) {
            if (canseeflag % 2 == 0) {
                cansee = true;
            } else {
                cansee = false;
            }
        } else {
            cansee = true;
        }
        canseeflag++;
        timesOfRender++;

        if (canseeflag > 5) {
            canseeflag = 0;
        }
        moveStage2Enemy();
        canBossShort();
        moveBullet();
        if (currentknifex > 0 && currentknifex < 176 && currentknifey > pointy && currentknifey < 220) {
            moveKnife(pointx, pointy, knifex, knifey);
            isKnifeinScreen = true;
        } else {
            currentknifex = -100;
            currentknifey = -100;
            pointy = -100;//修改
            isKnifeinScreen = false;
        }
        if (roady >= 80) {//路背景的移动
            roady = 0;
        }
        roady += speed;
        currentPosition += speed;
        Image pubutemp = pubu[4];//实现瀑布数组的变换
        for (int i = 4; i > 0; i--) {
            pubu[i] = pubu[i - 1];
        }
        pubu[0] = pubutemp;
        if ((knifenum == 0 && !isKnifeinScreen && enemylife > 0) || currentPosition >= 17000 || (lifetimes + life) == 0) {
            currentGameState = 2;

            speed = 0;//游戏失败
        //("游戏失败");
        } else if (enemylife == 0 && currentPosition <= 17000) {
            currentGameState = 3;
            getScore();//游戏成功
            speed = 0;
        }
    //("位置" + currentPosition);
    }

    private void renderForStage1(Graphics g) {
        g.setColor(0xffffff);
        g.fillRect(0, 0, 176, 220);
        renderRoad(g);
        g.drawImage(background, 0, 0, Graphics.LEFT | Graphics.TOP);
        renderTree();
        renderDaoJu();
        g.setColor(0xff0000);
        renderStart();
        g.fillRect(5, 5, (int) ((life / 100.0f) * 40), 5);
        g.drawRect(5, 5, 40, 5);
        g.setColor(0);
        g.drawString("" + lifetimes, 50, 0, Graphics.LEFT | Graphics.TOP);
        renderTime();
        g.drawString("$" + score, 140, 0, Graphics.LEFT | Graphics.TOP);
        g.drawString("JUMPS:" + jumpleft, 0, 15, Graphics.LEFT | Graphics.TOP);
        renderflag();
        g.drawImage(enemysmall, enemyx, 80, Graphics.HCENTER | Graphics.VCENTER);

        g.setColor(0x00ff00);
        g.drawLine(3, 43, 173, 43);
        g.setColor(0xff0000);
        g.fillRect(173, 42, 2, 2);
        renderPosition(0x0000ff, currentPosition / 100 + 3);//画当前的位置，也由第二个参数得出最大的路程
        if (cansee) {
            g.drawImage(mache, currentmachex, currentmachey, Graphics.HCENTER | Graphics.TOP);
        }
        if (currentGameState == 1) {
            pauseMenu.render();
        }
        if (currentGameState == 2) {
            showSuccessOrFailed(g, (byte) -2);//输掉！
        //("这这里失败");
        }

        g.setColor(0);

    }

    private void renderForMulti(Graphics g) {
        g.setColor(0xffffff);
        g.fillRect(0, 0, 176, 220);
        renderRoad(g);
        g.drawImage(background, 0, 0, Graphics.LEFT | Graphics.TOP);
        renderTree();
        renderDaoJu();
        g.setColor(0xff0000);
        renderStart();
        g.fillRect(5, 5, (int) ((life / 100.0f) * 40), 5);
        g.drawRect(5, 5, 40, 5);
        g.setColor(0x0000ff);
        g.fillRect(100, 5, (int) ((competitorLife / 100.0f) * 40), 5);
        g.drawRect(100, 5, 40, 5);
        g.setColor(0);
        g.drawString("try" + lifetimes, 50, 0, Graphics.LEFT | Graphics.TOP);
        g.drawString("try" + competitorLifeTimes, 145, 0, Graphics.LEFT | Graphics.TOP);
        //   renderTime();

        g.drawString("jumps:" + jumpleft, 5, 15, Graphics.LEFT | Graphics.TOP);
        g.drawString("jumps:" + enemyjumpleft, 110, 15, Graphics.LEFT | Graphics.TOP);
        renderflag();
        g.setColor(0x00ff00);
        g.drawLine(3, 43, 173, 43);
        g.setColor(0xff0000);
        g.fillRect(173, 42, 2, 2);
        renderPosition(0xff0000, currentPosition / 100 + 3);//画当前的位置，也由第二个参数得出最大的路程
        renderPosition(0x0000ff, enemycurrentPosition / 100 + 3);
        if (cansee) {
            g.drawImage(mache, currentmachex, currentmachey, Graphics.HCENTER | Graphics.TOP);
        }
        int x;
        if (enemycurrentPosition - currentPosition > 20) {
            if (competitormache == null) {
                try {
                    competitormache = Image.createImage("/res/enemymache.png");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (competitorX > 88) {
                x = (int) ((competitorX - 88) / 1.5f + 88);
            } else if (competitorX < 88) {
                x = (int) (88 - (88 - competitorX) / 1.5f);
            } else {
                x = 88;
            }
            g.drawImage(competitormache, x, 70, Graphics.HCENTER | Graphics.TOP);
        } 

        else {
            competitormache = null;
        }
        g.setColor(0);

    }

    private void renderRoad(Graphics g) {
        g.drawImage(roadbg, 0, roady, Graphics.LEFT | Graphics.TOP);
        g.drawImage(roadbg, 0, roady - 220, Graphics.LEFT | Graphics.TOP);


    }

    private void moveDaoJu(int i, int x) {
        float temp;
        if (speed != 0) {
            temp = 10 / (float) (160 / speed);//每次x方向道具移动的距离 
        } else {
            temp = 0;
        }
        if (x < 88) {
            daojux[i] = (short) (x - daojutimes[i] * temp);
            daojuy[i] += speed;
            daojutimes[i]++;
        }
        if (x == 88) {
            daojuy[i] += speed;
        }
        if (x > 88) {
            daojux[i] = (short) (x + daojutimes[i] * temp);
            daojuy[i] += speed;
            daojutimes[i]++;
        }

    }

    private void moveKnife(short ax, short ay, short bx, short by) {
        if (ax == bx)//ax，ay表示准心的初始坐标；bx，by是刀子的初始坐标
        {
            currentknifey -= 5;
        } else if (ax < bx) {
            currentknifey -= 5;
            currentknifex = (short) (bx + k * (currentknifey - by));
        } else {
            currentknifey -= 5;
            currentknifex = (short) (bx + k * (currentknifey - by));
        }
    }

    private void renderPoint() {
        g.drawImage(point, currentpointx, currentpointy, Graphics.HCENTER | Graphics.VCENTER);
    }

    private void renderpubu() {
        for (int i = 0; i < 5; i++) {
            g.drawImage(pubu[i], 88, i * 15, Graphics.HCENTER | Graphics.TOP);
        }
    }

    private void renderDaoJu() {
        for (int i = 0; i < numoftype; i++) {
            g.drawImage(daojuarry[typeOfDaoju[i]], daojux[i], daojuy[i], Graphics.VCENTER | Graphics.HCENTER);
        }

    }

    private void initDaoJu(int i, short x)//选择的第几个道具和起初始的中心坐标
    {
        daojux[i] = x;
        daojuy[i] = 60;
        daojutimes[i] = 0;
        isInited[i] = true;
    }

    private void initTree(int i) {
        treelx[i] = 35;
        treely[i] = 60;
        times[i] = 0;
        treerx[i] = 141;
        treery[i] = 60;
    }

    private void moveTree(int i, int x) {
        float temp;
        if (speed != 0) {
            temp = 30 / (float) (160 / speed);//每次x方向道具移动的距离 
        } else {
            temp = 0;
        }
        if (x < 88 && temp != 0) {
            treelx[i] = (short) (x - times[i] * temp);
            treely[i] += speed;
        }
        if (x > 88 && temp != 0) {
            treerx[i] = (short) (x + times[i] * temp);
            treery[i] += speed;
        }
    }

    private void isCollidedForStage1() {
        for (int i = 0; i < numoftype; i++) {
            if (typeOfDaoju[i] == 0 && isIntersectingRect(daojux[i] - 10, daojuy[i] - 10, 20, 20, machex - 11, machey, 22, 30)) {
                isCollided = (byte) i;
                score += 100;

                flagnum = 3;
                if (currentMusicState == true) {
                    hitmusic.musicStop();
                    hitmusic.changeMusic("/res/score++.wav");
                    hitmusic.musicStart();
                }
                flagstick((byte) 1);
            // flagnum=-2;
            }
            if (typeOfDaoju[i] == 1 && isIntersectingRect(daojux[i] - 10, daojuy[i] - 10, 20, 20, machex - 11, machey, 22, 30)) {
                isCollided = (byte) i;
                timesOfRender = 0;//闪烁
                stopTimes = 0;
                speed = 0;
                isStop = true;
                flagnum = 0;
                if (currentMusicState == true) {
                    hitmusic.musicStop();
                    hitmusic.changeMusic("/res/stop.wav");
                    hitmusic.musicStart();
                    musicControl.musicChangeRate(100000);
                }
                flagstick((byte) 1);
            }
            if (typeOfDaoju[i] == 2 && isIntersectingRect(daojux[i] - 7, daojuy[i] - 10, 14, 20, machex - 11, machey, 22, 30)) {
                isCollided = (byte) i;
//                timesOfRender = 0;
                twoTimes = 0;
                speed = 10;
                flagnum = 2;

                if (currentMusicState == true) {
                    hitmusic.musicStop();
                    hitmusic.changeMusic("/res/speed++.wav");
                    hitmusic.musicStart();
                    musicControl.musicChangeRate(150000);
                }
                flagstick((byte) 3);
            }
            if (typeOfDaoju[i] == 3 && isIntersectingRect(daojux[i] - 6, daojuy[i] - 10, 12, 20, machex - 11, machey, 22, 30)) {
                isCollided = (byte) i;
                life = (byte) Math.min(100, life + 10);
                flagnum = 4;
                flagstick((byte) 1);
                if (currentMusicState == true) {
                    hitmusic.musicStop();
                    hitmusic.changeMusic("/res/life++.wav");
                    hitmusic.musicStart();
                }
            }
            if (typeOfDaoju[i] == 4 && isIntersectingRect(daojux[i] - 6, daojuy[i] - 17, 10, 34, machex - 11, machey, 22, 30)) {
                isCollided = (byte) i;
                currentPosition = 0;
                timesOfRender = 6;
                score = 0;
                canseeflag = 0;
                isCollided = -100;//初始时无碰撞
                machex = 88;//马车的初始坐标
                machey = 185;
                cansee = true;
                speed = 5;
                if (lifetimes > 0) {
                    life = 100;
                } else {
                    life = 0;
                }
                lifetimes = (byte) Math.max(0, lifetimes - 1);
                for (int j = 0; j < numoftype; j++) {
                    daojuy[j] = -6000;
                    isInited[j] = false;
                }
                flagnum = 1;
                if (currentMusicState == true) {
                    hitmusic.musicStop();
                    hitmusic.changeMusic("/res/back.wav");
                    hitmusic.musicStart();
                    musicControl.musicChangeRate(100000);
                }
                delay((byte) 1);
                flagstick((byte) 1);
            }
            if (typeOfDaoju[i] == 5 && (isIntersectingRect(daojux[i] + 30, daojuy[i] - 9, 50, 18, machex - 11, machey, 22, 30) || isIntersectingRect(daojux[i] - 50, daojuy[i] - 9, 50, 18, machex - 11, machey, 22, 30))) {
                isCollided = (byte) i;
                timesOfRender = 0;
                if (currentMusicState == true) {
                    hitmusic.musicStop();
                    hitmusic.changeMusic("/res/life--.wav");
                    hitmusic.musicStart();
                }
                if (lifetimes == 0) {
                    life = (byte) Math.max(0, life - 30);

                } else {

                    life -= 30;
                    flagnum = 5;
                    flagstick((byte) 1);
                    if (life <= 0) {
                        life = 100;
                        if (currentMusicState == true) {
                            musicControl.musicChangeRate(150000);
                        }
                        lifetimes--;
                        currentPosition = 0;
                        score = 0;
                        timesOfRender = 6;
                        canseeflag = 0;
                        isCollided = -100;//初始时无碰撞
                        machex = 88;//马车的初始坐标
                        machey = 185;
                        cansee = true;
                        speed = 5;
                        for (int j = 0; j < numoftype; j++) {
                            daojuy[j] = -6000;
                            isInited[j] = false;
                        }
                        flagnum = 1;
                        delay((byte) 1);
                        flagstick((byte) 1);
                    }
                }
            }
            if (typeOfDaoju[i] == 6 && (isIntersectingRect(daojux[i], daojuy[i] - 9, 50, 18, machex - 11, machey, 22, 30) || isIntersectingRect(daojux[i] - 50, daojuy[i] - 9, 20, 18, machex - 11, machey, 22, 30))) {
                isCollided = (byte) i;
                timesOfRender = 0;
                if (currentMusicState == true) {
                    hitmusic.musicStop();
                    hitmusic.changeMusic("/res/life--.wav");
                    hitmusic.musicStart();
                }
                if (lifetimes == 0) {
                    life = (byte) Math.max(0, life - 30);

                } else {
                    life -= 30;
                    flagnum = 5;
                    flagstick((byte) 1);
                    if (life <= 0) {
                        life = 100;

                        lifetimes--;
                        currentPosition = 0;
                        score = 0;
                        timesOfRender = 6;
                        canseeflag = 0;
                        isCollided = -100;//初始时无碰撞
                        machex = 88;//马车的初始坐标
                        machey = 185;
                        cansee = true;
                        speed = 5;

                        for (int j = 0; j < numoftype; j++) {
                            daojuy[j] = -6000;
                            isInited[j] = false;
                        }
                        flagnum = 1;
                        delay((byte) 1);
                        flagstick((byte) 1);
                    }
                }

            }
        }

    }

    public static final boolean isIntersectingRect(int ax, int ay, int aw,
            int ah, int bx, int by, int bw, int bh) {//   a矩形左上角y坐标
// aw a矩形宽度
//ah a矩形高度
// b矩形左上角x坐标
// by b矩形左上角y坐标
// bw b矩形宽度
//bh b矩形高度
        if (by + bh < ay ||
                by > ay + ah ||
                bx + bw < ax ||
                bx > ax + aw) {
            return false;
        }
        return true;
    }

    private void macheLibrate() {//实现马车的振动效果
        if (macheLeftOrRight && currentmachex < (machex + 1)) {
            currentmachex++;
        } else if (macheLeftOrRight && currentmachex == (machex + 1)) {
            currentmachex--;
            macheLeftOrRight = false;
        } else if (!macheLeftOrRight && currentmachex > (machex - 1)) {
            currentmachex--;
        } else if (!macheLeftOrRight && currentmachex == (machex - 1)) {
            currentmachex++;
            macheLeftOrRight = true;
        }
    }

    private void enemyLibrate() {
        if (enemyLeftOrRight && enemyx < 89) {
            enemyx++;
        } else if (enemyLeftOrRight && enemyx == 89) {
            enemyx--;
            enemyLeftOrRight = false;
        } else if (!enemyLeftOrRight && enemyx > 87) {
            enemyx--;
        } else if (!enemyLeftOrRight && enemyx == 87) {
            enemyx++;
            enemyLeftOrRight = true;
        }
    }

    private void isJumped() {
        if (jumpflag < 0) {
            jumpflag++;
        } else {
            currentmachey = 185;
        }
    }

    private void renderPosition(int RGB, int Position) {
        g.setColor(RGB);
        g.fillRect(Position - 2, 39, 4, 8);
    }

    private void delay(byte second)//小于5秒
    {
        if (delayflag >= 0) {
            delayflag = (short) (-second * 20);
        }

    }

    private void flagstick(byte second) {
//        if (stickflag >= 0) {
        stickflag = (short) (-second * 20);
//        }
    }

    private void moveStage2Enemy() {
        if (changedistanceflag == 2) {
            if (distanceChangeDirect) {
                if (distanceflag == 1) {
                    distanceChangeDirect = !distanceChangeDirect;
                } else {
                    distanceflag++;
                    changedistanceflag = 0;
                }
            } else {
                if (distanceflag == -1) {
                    distanceChangeDirect = !distanceChangeDirect;
                } else {
                    distanceflag--;
                    changedistanceflag = 0;
                }
            }
        }
        if (distanceflag == 0) {
            enemyy = 80;
            if (plusOrMinusDirect) {
                if (enemyx <= 110) {//正向移动时如果没有达到最右边那么一直右移否则变换正负移动方向
                    enemyx += 2;
                } else {
                    plusOrMinusDirect = !plusOrMinusDirect;
                    changedistanceflag++;
                }
            } else {
                if (enemyx >= 66) {
                    enemyx -= 2;
                } else {
                    plusOrMinusDirect = !plusOrMinusDirect;
                    changedistanceflag++;
                }

            }
        }
        if (distanceflag == 1) {
            enemyy = 60;
            if (plusOrMinusDirect) {
                if (enemyx <= 105) {//正向移动时如果没有达到最右边那么一直右移否则变换正负移动方向
                    enemyx += 2;
                } else {
                    plusOrMinusDirect = !plusOrMinusDirect;
                    changedistanceflag++;
                }
            } else {
                if (enemyx >= 71) {
                    enemyx -= 2;
                } else {
                    plusOrMinusDirect = !plusOrMinusDirect;
                    changedistanceflag++;
                }
            }
        }
        if (distanceflag == -1) {
            enemyy = 100;
            if (plusOrMinusDirect) {
                if (enemyx <= 115) {//正向移动时如果没有达到最右边那么一直右移否则变换正负移动方向
                    enemyx += 2;
                } else {
                    plusOrMinusDirect = !plusOrMinusDirect;
                    changedistanceflag++;
                }
            } else {
                if (enemyx >= 61) {
                    enemyx -= 2;
                } else {
                    plusOrMinusDirect = !plusOrMinusDirect;
                    changedistanceflag++;
                }
            }
        }
        headLeftTopx = (short) (enemyx - 5);//移动头部矩形区域
        headLeftTopy = enemyy;
        bodyLeftTopx = (short) (enemyx - 20);//移动车身矩形区域
        bodyLeftTopy = (short) (enemyy + 20);
    }

    private void canBossShort() {
        if (distanceflag == -1 && plusOrMinusDirect && enemyx == 88) {
            bulletox = enemyx;
            bulletoy = enemyy;
            shortflag = 1;
        }
        if (distanceflag == 0 && plusOrMinusDirect && enemyx == 110) {
            bulletox = enemyx;
            bulletoy = enemyy;
            shortflag = 2;
        }
        if (distanceflag == 1 && plusOrMinusDirect && enemyx == 70) {
            bulletox = enemyx;
            bulletoy = enemyy;
            shortflag = 3;
        }
    }

    private void moveBullet() {
        if (shortflag == 1) {
            bulletoy += 5;
            if (bulletoy >= 220) {
                shortflag = 0;
                bulletox = 0;
                bulletoy = 0;
            }
        }
        if (shortflag == 2) {
            bulletoy += 5;
            bulletox -= 2;
            if (bulletoy >= 220) {
                shortflag = 0;
                bulletox = 0;
                bulletoy = 0;
            }

        }
        if (shortflag == 3) {
            bulletoy += 5;
            bulletox += 2;
            if (bulletoy >= 220) {
                shortflag = 0;
                bulletox = 0;
                bulletoy = 0;
            }
        }
    }

    private void renderflag() {
        if (flagnum != -1 && flagnum < 3) {
            g.drawImage(flag[flagnum], 88, 15, Graphics.VCENTER | Graphics.HCENTER);
        }
        g.setColor(0x0000ff);
        if (flagnum == 0) {
            g.drawString("暂停！", 80, 100, Graphics.TOP | Graphics.LEFT);
        } else if (flagnum == 1) {
            g.drawString("返回起始！", 70, 100, Graphics.TOP | Graphics.LEFT);
        } else if (flagnum == 2) {
            g.drawString("加速！", 80, 100, Graphics.TOP | Graphics.LEFT);
        } else if (flagnum == 3) {
            g.drawString("金币+100", 70, 100, Graphics.LEFT | Graphics.TOP);
        } else if (flagnum == 4) {
            g.drawString("生命值+10", 70, 100, Graphics.LEFT | Graphics.TOP);
        } else if (flagnum == 5) {
            g.drawString("生命值-30", 70, 100, Graphics.LEFT | Graphics.TOP);
        } else if (flagnum == 6) {
            g.drawString("在到达终点前你必须击败敌人", 88, 100, Graphics.HCENTER | Graphics.TOP);

        }
    }

    private void renderSecondStageEnemy() {
        g.drawImage(enemy, enemyx, enemyy, Graphics.HCENTER | Graphics.TOP);
    }

    private void renderKnife() {
        if (knife == null) {
            try {
                knife = Image.createImage("/res/knife.png");
            } catch (IOException ex) {
            }
        }
        g.drawImage(knife, currentknifex, currentknifey, Graphics.HCENTER | Graphics.VCENTER);
    }

    public boolean getCurrentMusicState() {
        return currentMusicState;
    }

    public boolean turnOnOrOffMusic() {
        if (currentMusicState == true) {
            musicControl.musicStop();
            musicControl = null;
            hitmusic.musicStop();
            hitmusic = null;
            currentMusicState = false;
            return currentMusicState;
        } else {
            musicControl = new MusicControl(true);

            hitmusic = new MusicControl(false);
            // hitmusic.changeMusic("score++.wav");
            musicControl.changeMusic("/res/bg2.mid");
            musicControl.musicStart();
            musicControl.changeMusicVolume(mainMidlet.musicVolume);
            musicControl.musicChangeRate(100000);
//            hitmusic.musicStart();
            currentMusicState = true;
            return currentMusicState;
        }
    }

    private void showSuccessOrFailed(Graphics g, byte type) {
        if (sfImage == null) {
            try {
                Image tempImage = Image.createImage("/res/sf.png");
                if (type > 0) {
                    sfImage = Image.createImage(tempImage, 0, 42, 120, 42, Sprite.TRANS_NONE);
                } else {
                    sfImage = Image.createImage(tempImage, 0, 0, 120, 42, Sprite.TRANS_NONE);
                }

                tempImage = null;
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        g.setColor(0x00fead);
        if (type < 0) { //游戏失败
            g.drawImage(sfImage, this.getWidth() >> 1, this.getHeight() >> 1, Graphics.VCENTER | Graphics.HCENTER);
            g.drawString("是", 0, 200, Graphics.TOP | Graphics.LEFT);
            g.drawString("否", 150, 200, Graphics.TOP | Graphics.LEFT);
        }
//             else {
//                g.drawString("游戏失败！", this.getWidth() >> 1 - 24, this.getHeight() >> 1, Graphics.TOP | Graphics.LEFT);
//                g.drawString("退出", 140, 200, Graphics.TOP | Graphics.LEFT);
//            }
    }

    public void gamePause() {
        currentGameState = 1;
    }

    public void gameStop() {
        this.isStart = false;
        if (musicControl != null) {
            musicControl.musicStop();
            hitmusic.musicStop();
        }
        musicControl = null;
        hitmusic = null;
        thread = null;
        if (isMultiGame) {
            mainMidlet.cancelBTService();
        }
        System.gc();
        if (currentGameState == 3 && !isMultiGame) {//成功的话，写入游戏记录
            DataStore dataStore = new DataStore();
            dataStore.writeScoreRecord(mainMidlet.playerName, totalScore);
        }
    }

    public void gameStart() {
        currentGameState = 0;
        thread = new Thread(this);
        thread.start();
    }

    private void getScore() {
        totalScore = (short) (score + lifetimes * 100 + knifenum * 100 + life);
        if (timeflag <= 1800) {
            totalScore += 1000;
            timescore = 1000;
        } else if (timeflag < 2000) {
            totalScore += 500;
            timescore = 500;
        } else if (timeflag < 2500) {
            totalScore += 200;
            totalScore = 200;
        }
    }

    private void renderPassGame() {//绘制通过单人模式的游戏时的画面
        g.setColor(0);
        if (gameOverGround == null) {
            try {
                gameOverGround = Image.createImage("/res/gameoverground.png");
            } catch (IOException ex) {
            }
        }
        g.drawImage(gameOverGround, 0, 0, Graphics.LEFT | Graphics.TOP);
        g.drawString("剩余生命" + (lifetimes * 100 + life) + "  " + (lifetimes * 100 + life), 88, 10, Graphics.HCENTER | Graphics.TOP);
        g.drawString("时间   " + minute + ":" + second + "  " + timescore, 88, 30, Graphics.HCENTER | Graphics.TOP);
        g.drawString("剩余飞刀" + knifenum + "  " + (knifenum * 100), 88, 50, Graphics.HCENTER | Graphics.TOP);
        g.drawString("金币得分" + score, 88, 70, Graphics.HCENTER | Graphics.TOP);
        g.drawString("总得分  " + totalScore, 88, 90, Graphics.HCENTER | Graphics.TOP);
        try {
            if (pass == null) {
                pass = Image.createImage("/res/pass.png");
            }
        } catch (IOException ex) {
        }
        g.drawImage(pass, 88, 190, Graphics.HCENTER | Graphics.TOP);
        g.drawString("继续游戏", 0, 200, Graphics.TOP | Graphics.LEFT);

    }

    private void renderMultiOver(Graphics g) {//绘制多人游戏中游戏结束时的画面
        g.setColor(0xffffff);
        g.fillRect(0, 0, 176, 220);
        g.setColor(0xff0000);
        if (gameOverGround == null) {
            try {
                gameOverGround = Image.createImage("/res/gameoverground.png");
            } catch (IOException ex) {
            }
        }
        g.drawImage(gameOverGround, 0, 0, Graphics.LEFT | Graphics.TOP);
        if (currentGameState == 3) {
            g.drawString("恭喜胜利！", 88, 110, Graphics.HCENTER | Graphics.TOP);
        } else if (currentGameState == 2) {
            g.drawString("再接再厉！", 88, 110, Graphics.HCENTER | Graphics.TOP);
        }
        g.drawString("退出", 140, 200, Graphics.TOP | Graphics.LEFT);

    }

    public void setConnected(boolean i) {
        this.connected = i;
    }

    public void setFriendState(int currentPosition, byte lifetimes, byte life, int x, byte jump) {//设置多人游戏中对方的游戏状态
        this.enemycurrentPosition = currentPosition;
        this.competitorX = x;
        this.competitorLifeTimes = lifetimes;
        this.competitorLife = life;
        this.enemyjumpleft = jump;
    }

    private void renderStart() {//游戏开始前的准备开始阶段
        if (startflag < -5) {
            try {
                if (ready == null) {
                    ready = Image.createImage("/res/ready.png");
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (currentMusicState == true && startflag == -40) {
                hitmusic.changeMusic("/res/ready.wav");
                hitmusic.musicStart();
            }
            g.drawImage(ready, 88, 110, Graphics.HCENTER | Graphics.VCENTER);
        } else if (startflag >= -5 && startflag <= 0) {
            if (currentMusicState == true && startflag == 0) {
                hitmusic.changeMusic("/res/go.wav");
                hitmusic.musicStart();
            }
            try {

                if (go == null) {
                    go = Image.createImage("/res/go.png");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            g.drawImage(go, 88, 110, Graphics.HCENTER | Graphics.VCENTER);
        } else if (startflag > 0) {
            go = null;
            ready = null;
        }
    }

    public void setSignal() {//设置蓝牙传输的同步信号，即蓝牙已经同步
        synchronization = true;
    }

    public void renderWait(Graphics g) { //  绘制多人游戏中等待对方加入时的场景;
        g.setColor(0xfff);
        g.fillRect(0, 0, 176, 220);
        g.setColor(0xffffff);
        g.drawString("等待客户端加入游戏...", 30, 110, Graphics.TOP | Graphics.LEFT);
        g.drawString("取消", 140, 200, Graphics.TOP | Graphics.LEFT);

    }

    public void circleBullet() {
        circleflag++;
        if (circleflag == 5) {
            circleflag = 0;
            angle += 30;
            if (angle == 360) {
                angle = 0;
            }
        }
    }

    public void renderBossBullet(short ox, short oy, short r) {
        if (ox > 0) {
            if (bossBullet == null) {
                try {
                    bossBullet = Image.createImage("/res/horseRaceBossBullet.png");
                } catch (IOException ex) {
                }
            }
            g.drawImage(bossBullet, (int) (ox - Math.cos(angle) * r), (int) (oy - Math.sin(angle) * r), Graphics.HCENTER | Graphics.VCENTER);
            g.drawImage(bossBullet, (int) (ox - Math.cos(angle + 90) * r), (int) (oy - Math.sin(angle + 90) * r), Graphics.HCENTER | Graphics.VCENTER);
            g.drawImage(bossBullet, (int) (ox - Math.cos(angle + 180) * r), (int) (oy - Math.sin(angle + 180) * r), Graphics.HCENTER | Graphics.VCENTER);
        }
    }

    private void isColliedBullet(short r) {
        if (isIntersectingRect((int) (bulletox - Math.cos(angle) * r - 3), (int) (bulletoy - Math.sin(angle) * r - 8), 6, 16, machex - 11, machey, 22, 30)) {
            if (lifetimes == 0) {
                life = 0;
            } else {
                life = 100;
            }
            lifetimes = (byte) Math.max(0, lifetimes - 1);
            bulletox = 0;
            bulletoy = 0;
            shortflag = 0;
            if (currentMusicState == true) {
                hitmusic.changeMusic("/res/life--.wav");
                hitmusic.musicStart();
            }
        } else if (isIntersectingRect((int) (bulletox - Math.cos(angle + 90) * r - 3), (int) (bulletoy - Math.sin(angle + 90) * r - 8), 6, 16, machex - 11, machey, 22, 30)) {
            if (lifetimes == 0) {
                life = 0;
            } else {
                life = 100;
            }
            lifetimes = (byte) Math.max(0, lifetimes - 1);
            bulletox = 0;
            bulletoy = 0;
            shortflag = 0;
            if (currentMusicState == true) {
                hitmusic.changeMusic("/res/life--.wav");
                hitmusic.musicStart();
            }
        } else if (isIntersectingRect((int) (bulletox - Math.cos(angle + 180) * r - 3), (int) (bulletoy - Math.sin(angle + 180) * r - 8), 6, 16, machex - 11, machey, 22, 30)) {
            if (lifetimes == 0) {
                life = 0;
            } else {
                life = 100;
            }
            lifetimes = (byte) Math.max(0, lifetimes - 1);
            bulletox = 0;
            bulletoy = 0;
            shortflag = 0;
            if (currentMusicState == true) {
                hitmusic.changeMusic("/res/life--.wav");
                hitmusic.musicStart();
            }
        }
        circleBullet();
    }
} 
