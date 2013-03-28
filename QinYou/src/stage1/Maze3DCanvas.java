package stage1;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.m3g.Background;
import javax.microedition.m3g.Camera;
import javax.microedition.m3g.Graphics3D;
import javax.microedition.m3g.Group;
import javax.microedition.m3g.Image2D;
import javax.microedition.m3g.Light;
import javax.microedition.m3g.Mesh;

import javax.microedition.m3g.Transform;
import mainMenu.DataStore;
import mainMenu.MainMidlet;
import res.MusicControl;

/**
 *
 * @author Administrator
 */
public class Maze3DCanvas extends GameCanvas implements Runnable {
    //绘制场景需要的3D参数
    private Light light;
    private Camera camera;
    private Background background;
    private Graphics3D myG3D;
    private short posX; //相机位置x，相当于人眼所在的位置
    private short posZ;//相机位置z，相当于人眼所在的位置
    //相机的角度
    private int cameraAngle;
    //构建场景需要的模型
    private Mesh groundPlane; //绘制地板需要的Mesh
    private Mesh wall;//绘制墙需要的mesh
    int[][] mazeMap;//数组表示的地图
    //碰撞检测需要的常量
    private static final byte MAX_INACCURACY = 5;//当墙与人的位置在这个范围内碰撞
    //与绘制生命值有关的量
    private static final short LIFE_VALUE_POS_X = 100;//生命值绘制的位置
    private static final short LIFE_VALUE_POS_Y = 0;
    private byte lifeValue = 3;        //生命值
    //与绘制分数有关的变量
    private short score = 0; //分数

    //-----------------物品的属性设计
    //7月10号（王阿晶）：根据设定，我们给玩家提供的小地图的时间为10秒，而玩家的全地图只有5秒，
    //两个不能同时出现，在小地图中可以显示玩家当前的位置，但在大地图中只显示地图而不显示位置
    //获得物体可以通过获得道具来获得。整个场景中道具的个数是确定的。加速道具为玩家提供5秒钟的加速
    //8月3日修改（王阿晶）：
    //为减少没用用的变量，这里把这三个变量合起来，使用同一个变量，更名为道具时间(propTime)
    private byte propTime = 0; //表示道具的时间
    private static final short TIME_PER_FRAME = 200;//每个帧时长，毫秒级
    private Group smallMap;//道具小地图
    private Group fullMap;//道具全地图
    private Group queryMask;//道具
    private Group coin;//硬币道具
    private Group box;//存放1/8的盒子
    private short[] propPosX;//道具所在位置x
    private short[] propPosZ;//道具所在的位置z
    private byte[] propType = {1, 1, 1, 2, 2, 2, 3, 3, 3};//1是大地图，2是小地图，3是随机道具
    private short[] coinPosX; //金币所在的位置X
    private short[] coinPosZ; //金币所在位置Y
    //下面的变量是要搜集起的八个部分的位置和其他的相关的变量
//    private int[] partPosX = {0, 0, 0, 650, 0, 0, 0, 0};
    private short[] partPosX;
    private short[] partPosZ;
    private short propAngle;//这个变量让上面的道具旋转起来！
    private byte currentGetPartNum = 0;//反映当前获得1/8部分的个数
    private byte stepLength = 5;//步长
    private Image accPropImage = null;
    private Image decPropImage = null;
    private Image reversePropImage = null;
    private Image fullMapImage = null;
    private Image smallMapImage = null;
    private Image compassImage = null;
    //计时 
    private long currentTime;//当前时间，表示游戏经历了多长时间了。
    private final short MAP_OFFSET_X = 0;
    private final short MAP_OFFSET_Y = 100;    //下面的变量用来定义boss，使其在场景中移动，当与玩家碰撞，玩家会减少生命
    private Mesh boss;
    private short bossPosX[];
    private short bossPosZ[];
    private byte currentBossDirection[];
    private short bossAngle[];//表示角度
    //下面的变量是用来提示用户的状态所用到的变量
    private byte promptTime = 0; //提示显示的时间
    private byte currentPromptType = 1; //提示的类型
    //下面的变量用来倒计时
    private short gameTimeLength; //游戏的时间长度
    private MainMidlet mainMidlet;
    private byte currentPhase = 1;
    private MusicControl musicControl;//音乐控制
    private Thread thread;//内在线程
    private boolean currentMusicState;//音乐的状态！
    public short firstPhaseTime;
    private PausedMenu pauseMenu;//暂停菜单
    //下面的变量定义游戏的状态
    //为了简化游戏的逻辑处理我们下面定义了一个变量：
    /**
     * currentMusicState只用于单人游戏中的变量表述他的可能取值是：
     * 0、正常态
     * 1、暂停态
     * 2、失败态
     * 3、成功态
     */
    private byte currentGameState;//当前的游戏状态
    private boolean stopped;
    private boolean isMultiGame;//表示游戏是不是多人游戏
    private boolean connected;//表示是否与对方连上
    //下面的变量用来标记在多人游戏时使用的变量
    private byte propID;//当某个道具被吃掉后，设置该变量等于该丢失的道具
    //下面的变量是用来标记友方的状态变量
    private short sPosX;//友方的位置x
    private short sPosZ;//友方的位置z
    private byte sLife;//友方的生命值
    private byte sPropID;//道具的ID
    private short sAngle;//角度
    private short sScore;//友方的得分
    private byte sLastPropID;//上一次的ID
    private byte sCurrentGameState;
    private boolean isInitState = false;
    private MusicControl fxControl;
    private Mesh centerPlane;

    /**
     * 初始化内容如下：
     * 3D、2D、游戏状态的初始化、当前时间初始化、屏幕模式、
     * 本构造函数供蓝牙服务器端构造使用
     */
    public Maze3DCanvas(MainMidlet mainMidlet, boolean isMultiGame) {
        super(false);
        try {
            this.setFullScreenMode(true);
            this.mainMidlet = mainMidlet;
            this.isMultiGame = isMultiGame;
            mazeMap = new Maze().getMazeMap();
            initData();
            isInitState = true;
            init3D();
            init2D();


        } catch (IOException ex) {
//            ex.printStackTrace();
//            //("资源加载异常！");
        }
    }

    public Maze3DCanvas(MainMidlet mainMidlet, int[][] map, boolean isMultiGame) {
        super(false);
        try {
            this.setFullScreenMode(true);
            this.mainMidlet = mainMidlet;
            this.isMultiGame = isMultiGame;
            this.mazeMap = map;
            initData();
            init3D();
            init2D();
        } catch (IOException ex) {
            return;
        }
    }

    /**
     * 这里进行3D绘制相关的初始化
     * 初始化的内容包括如下：
     * 灯光、照相机、背景、相机位置、画笔、光源、模型、boss的初始化方向
     * @throws java.io.IOException 加载资源时，抛出异常
     */
    private void init3D() throws IOException {
        //初始化灯光
        light = new Light();
        light.setColor(0xffffff);//设置光线的颜色为白色        
        light.setMode(Light.AMBIENT);
        light.setIntensity(1.25f);
        //初始化照相机
        camera = new Camera();
        //设置照相机为透视投影方式，
        //角度为60度，纵横比，近切面和远切面，进切面和元切面是相对与照相机来说的。
        camera.setPerspective(60.0f, // field of view
                (float) getWidth() / (float) getHeight(), // aspectRatio
                1.0f, // near clipping plane
                400); // far clipping plane
        //初始化背景
        background = new Background();
        Image image = Image.createImage("/res/bg3d.png");
        Image2D i2D = new Image2D(Image2D.RGB, image);
        background.setImage(i2D);
        //初始化画笔
        myG3D = Graphics3D.getInstance();
        //添加光源
        myG3D.resetLights();
        myG3D.addLight(light, null);
        //初始化3D的模型
        Model3DFactory planeModel = new Model3DFactory();
        groundPlane = planeModel.getGroundPlane();
        wall = planeModel.getPlane();
        smallMap = planeModel.getSmallMap();
        fullMap = planeModel.getFullMap();
        queryMask = planeModel.getQueryMask();
        coin = planeModel.getCoin();
        boss = planeModel.getBall();
        box = planeModel.getBox();
        centerPlane = planeModel.getCenterPlane();
    }

    /**
     * 这里进行2D绘制相关的初始化，主要是2D图片的初始化
     * @throws java.io.IOException
     */
    private void init2D() throws IOException {
        Image allImage = Image.createImage("/res/prop.png");
        reversePropImage = Image.createImage(allImage, 0, 0, 16, 16, Sprite.TRANS_NONE);
        decPropImage = Image.createImage(allImage, 0, 16, 16, 16, Sprite.TRANS_NONE);
        accPropImage = Image.createImage(allImage, 16, 0, 16, 16, Sprite.TRANS_NONE);
        fullMapImage = Image.createImage(allImage, 16, 16, 16, 16, Sprite.TRANS_NONE);
        smallMapImage = Image.createImage(allImage, 64, 0, 16, 16, Sprite.TRANS_NONE);
        compassImage = Image.createImage(50, 50);
        Graphics g = compassImage.getGraphics();
        Image southPointImage = Image.createImage("/res/luopan.png");
        g.drawImage(southPointImage, 0, 0, Graphics.TOP | Graphics.LEFT);
        g.setColor(0xff0000);//红色表示中心点
        g.fillRect(23, 23, 4, 4); //绘制中心点
        southPointImage = null;
        for (int m = 0; m < tempPosZ.length; m++) {
            g.setColor(0x0);
            g.fillRect(tempPosX[m] - 2, tempPosZ[m] - 2, 4, 4);
        }

        allImage = null;

    }

    public synchronized void initData() {
        currentMusicState = false;
        if (mainMidlet.needMusic) {
            turnOnOrOffMusic();
        }
        //初始化相机位置
        posX = 650;
        posZ = 650;
        //初始化boss的方向
        currentBossDirection = new byte[4];
        bossAngle = new short[4];
        //变量，使道具旋转
        propAngle = 0;
        cameraAngle = 0;
        gameTimeLength = 500;
        score = 0;
        lifeValue = 3;
        connected = true;
        promptTime = 0;
        tempVector = new Vector[4];
        tempVector[0] = new Vector();
        tempVector[1] = new Vector();
        tempVector[2] = new Vector();
        tempVector[3] = new Vector();
        tempVector[0].addElement(new PointInfo(2, 2, PointInfo.UP));
        tempVector[1].addElement(new PointInfo(2, 9, PointInfo.UP));
        tempVector[2].addElement(new PointInfo(9, 2, PointInfo.UP));
        tempVector[3].addElement(new PointInfo(9, 9, PointInfo.UP));

        //初始化boss位置：
        short BOSS_POS_X[] = {250, 250, 950, 950};
        short BOSS_POS_Z[] = {250, 950, 250, 950};
        bossPosX = BOSS_POS_X;
        bossPosZ = BOSS_POS_Z;

        short[] PROP_POS_X = {150, 750, 1050, 150, 450, 350, 550, 950, 850};//道具所在位置x
        short[] PROP_POS_Z = {850, 350, 150, 950, 1050, 550, 750, 650, 750};//道具所在的位置z
        propPosX = PROP_POS_X;
        propPosZ = PROP_POS_Z;

        short[] COIN_POS_X = {950, 450, 950, 850, 350, 950}; //金币所在的位置X
        short[] COIN_POS_Z = {550, 750, 850, 850, 850, 350}; //金币所在位置Z
        coinPosX = COIN_POS_X;
        coinPosZ = COIN_POS_Z;
        short[] PARTPOS_X = {450, 750, 1050, 1050, 750, 450, 150, 150};
        short[] PARTPOS_Z = {150, 150, 450, 750, 1050, 1050, 750, 450};
        partPosX = PARTPOS_X;
        partPosZ = PARTPOS_Z;
        pauseMenu = null;//初始化菜单
        currentGameState = 0;//表示游戏处于正常态
        currentGetPartNum = 0;    //初始化合作者游戏状态
        propID = -1;
        sPosZ = 650;
        sPosX = 650;
        sLife = 3;
        sPropID = -1;
        sAngle = 0;
        sLastPropID = -1;
        sCurrentGameState = 0;//正常状态
        currentPhase = 1;
        firstPhaseTime = 0;
        currentTime = System.currentTimeMillis();
        if (isInitState == true) {//这里这样做是为了当用户进入系统时显示
            currentPromptType = 20;
            promptTime = 20;
            isInitState = false;
        }
    }

    /**
     * 这个函数中增加3D的道具和金币,还有boss
     * @param g3d
     */
    private void addPropCoin3D(Graphics3D g3d) {
        Transform t = new Transform();
        for (int i = 0; i < 9; i++) {
            if (propType[i] == 0) {
                continue;
            }
            t.setIdentity();
            if (Math.abs(propPosX[i] - posX) <= 400 && Math.abs(propPosZ[i] - posZ) <= 400) {
                if (propType[i] == 1) { //画大地图
                    t.postTranslate(propPosX[i], 0, propPosZ[i]);
                    t.postRotate(propAngle, 0, 1, 0);
                    g3d.render(fullMap, t);
                } else if (propType[i] == 2) {   //画小地图
                    t.postTranslate(propPosX[i], 0, propPosZ[i]);
                    t.postRotate(propAngle, 0, 1, 0);
                    g3d.render(smallMap, t);
                } else if (propType[i] == 3) { //画随机道具
                    t.postTranslate(propPosX[i], 0, propPosZ[i]);
                    t.postRotate(propAngle, 0, 1, 0);
                    g3d.render(queryMask, t);
                }

            }
        }
        for (int i = 0; i < partPosZ.length; i++) {
            t.setIdentity();//下面画硬币
            if (i < coinPosZ.length) {
                if (coinPosX[i] != 0 && Math.abs(coinPosX[i] - posX) <= 400 && Math.abs(coinPosZ[i] - posZ) <= 400) {
                    t.postTranslate(coinPosX[i], 0, coinPosZ[i]);
                    t.postRotate(propAngle, 0, 1, 0);
                    t.postRotate(-90, 1, 0, 0);
                    g3d.render(coin, t);
                }
            }
            //@todo:加上放置盒子的代码！
            t.setIdentity();
            if (partPosX[i] != 0 && Math.abs(partPosX[i] - posX) <= 400 && Math.abs(partPosZ[i] - posZ) <= 400) {
                t.postTranslate(partPosX[i], -30, partPosZ[i]);
                t.postRotate(-90, 1, 0, 0);
                g3d.render(box, t);
            }
        }
        for (int i = 0; i < bossPosX.length; i++) { //画boss
            t.setIdentity();
            t.postTranslate(bossPosX[i], -10, bossPosZ[i]);//
            if (currentBossDirection[i] == PointInfo.UP || currentBossDirection[i] == PointInfo.DOWN) {
                t.postRotate(bossAngle[i], 1, 0, 0);
//                if (currentBossDirection[i] == PointInfo.DOWN) {
//                    bossAngle[i] += 10;
//                } else {
//                    bossAngle[i] -= 10;
//                }

            } else {
                t.postRotate(bossAngle[i], 0, 0, 1);
//                if (currentBossDirection[i] == PointInfo.LEFT) {
//                    bossAngle[i] += 10;
//                } else {
//                    bossAngle[i] -= 10;
//                }

            }
            g3d.render(boss, t);
        }

    }

    /**
     * 
     */
    private void keyProcess() {
        int code = this.getKeyStates();
        float temp1 = cosOf(cameraAngle) * stepLength;
        int dz;
        int dx;
        if (temp1 > 0) {
            dz = (int) (temp1 + 0.5);
        } else {
            dz = (int) (temp1 - 0.5);
        }
        temp1 = sinOf(cameraAngle) * stepLength;
        if (temp1 > 0) {
            dx = (int) (temp1 + 0.5);
        } else {
            dx = (int) (temp1 - 0.5);
        }

        if ((code & UP_PRESSED) != 0) {
            //("dx" + dx + ":dz" + dz);
            if (currentPromptType == 4 && propTime > 0) {//如果是反转道具的话
                if (onMoveDown(dx, dz)) {
                    onMoveDown(dx, dz);
                }
            } else if (currentPromptType == 3 && propTime > 0) {
                onMoveUp(dx, dz);
            } else if (currentPromptType == 2 && propTime > 0) {
                if (onMoveUp(dx, dz)) {
                    if (onMoveUp(dx, dz)) {
                        if (onMoveUp(dx, dz)) {
                            onMoveUp(dx, dz);
                        }
                    }
                }
            } else {
                if (onMoveUp(dx, dz)) {
                    onMoveUp(dx, dz);
                }

            }
        } else if ((code & DOWN_PRESSED) != 0) {
            //("x:" + posX + "z:" + posZ);
            if (currentPromptType == 4 && propTime > 0) {//如果是反转道具的话
                if (onMoveUp(dx, dz)) {
                    onMoveUp(dx, dz);
                }

            } else if (currentPromptType == 3 && propTime > 0) {
                onMoveDown(dx, dz);
            } else if (currentPromptType == 2 && propTime > 0) {
                if (onMoveDown(dx, dz)) {
                    if (onMoveDown(dx, dz)) {
                        if (onMoveDown(dx, dz)) {
                            onMoveDown(dx, dz);
                        }

                    }
                }

            } else {
                if (onMoveDown(dx, dz)) {
                    onMoveDown(dx, dz);
                }

            }

        }
        if ((code & LEFT_PRESSED) != 0) {
            cameraAngle += 18;
            if (cameraAngle >= 180) {
                cameraAngle -= 360;
            }
        }
        if ((code & RIGHT_PRESSED) != 0) {
            cameraAngle -= 18;
            if (cameraAngle <= -180) {
                cameraAngle += 360;
            }

        }
    }

    private boolean onMoveUp(int dx, int dz) {
        posX -= dx;
        posZ -= dz;

        if (isCollideWithWall()) {
            posX += dx;
            posZ += dz;
            return false;
        }
        return true;
    }

    private boolean onMoveDown(int dx, int dz) {
        posX += dx;
        posZ += dz;
        if (isCollideWithWall()) {
            posX -= dx;
            posZ -= dz;
            return false;
        }

        return true;

    }

    /**
     * 绘制整个游戏的屏幕，包括2D和3D
     * @param g
     */
    private void render(Graphics g) {
        //--------3D的绘制部分代码----------------
        g.setColor(0xffffff);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        myG3D.bindTarget(g);
        Transform t = new Transform();
        t.postTranslate(posX, 0, posZ);
        t.postRotate(cameraAngle, 0, 1, 0);
        myG3D.setCamera(camera, t);
        myG3D.clear(background);
        myG3D.setDepthRange(0.1f, 0.9f);

        renderScene3D(myG3D);
        addPropCoin3D(myG3D);
        myG3D.releaseTarget();
        //---------------------------------------
        //---------2D的绘制代码-------------------
        renderScene2D(g);
//        //(connected);
        if (isMultiGame && connected == false && currentGameState != 3) {//如果游戏没有成功的话
            g.setColor(0xff0000);
            g.drawString("断开连接！", 60, 200, Graphics.TOP | Graphics.LEFT);
        }

        if (currentGameState == 3) {
            showSuccessOrFailed(g, 1); //成功为正，失败位负
            return;

        }
        if (currentGameState == 2) {//如果失败的话
            if (!isMultiGame) {
                showSuccessOrFailed(g, -2);//输掉！
//             //("这这里失败");
                return;
            }
            if (isMultiGame && sCurrentGameState == 2) { //如果两个人都失败的话显示失败
                showSuccessOrFailed(g, -2);//多人游戏，当双方都失败后，显示！
//                //("这这里失败是多人游戏另一个人也挂了");
                return;
            }
            if (gameTimeLength <= 0) { //如果时间到的话
                showSuccessOrFailed(g, -2);
                return;
            }
            if (isMultiGame && sCurrentGameState == 3) {//如果第三个人成功的话
                showSuccessOrFailed(g, 1);//多人游戏，当双方都成功后，显示！
//                //("好了来了vf");
                return;
            }
        }
        if (currentGameState == 1) { //暂停的状态菜单
            if (pauseMenu == null) {
                pauseMenu = new PausedMenu(g, this);
            }
            pauseMenu.render();
        //("执行到这里了！暂停");
        }
        if (currentPhase == 2) {
            g.setColor(0xff0000);
            g.drawString("已完成,请快速回到原点！", 30, 160, Graphics.TOP | Graphics.LEFT);
        }


    }

    /**
     * 运行的线程
     */
    public void run() {
        if (isMultiGame) {
            runForMultiGame();
        } else {
            runForSingleGame();
        }

    }
    int counter = 0;
//    long time = System.currentTimeMillis();
    public void runForSingleGame() {
        Graphics g = this.getGraphics();
        while (!stopped) {
            try {
                long time = System.currentTimeMillis();
                if (currentGameState == 0) {//正常情况下，只需键盘处理，和逻辑处理
                    keyProcess();
                    logicProcess();

                    bossMoveLogic();//boss的移动逻辑处理

                    long current = System.currentTimeMillis(); //时间的逻辑处理
                    if (current - currentTime > 1000) {
                        gameTimeLength--;
                        currentTime = current;
                    }

                }
                render(g);
                MainMidlet.drawLogo(g, 40, 50, 2);
//                g.setColor(0xffffff);
//                g.drawString("fps:" + counter, 0, 0, Graphics.TOP | Graphics.LEFT);
                flushGraphics();
//                if ((System.currentTimeMillis() - time) <= 1000) {
//                    counter++;
//                } else {
//                    counter = 0;
//                    time = System.currentTimeMillis();
//                }
                Thread.sleep(TIME_PER_FRAME - System.currentTimeMillis() + time);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    public void runForMultiGame() {
        Graphics g = this.getGraphics();
        while (!stopped) {
            try {
                long time = System.currentTimeMillis();
                if (currentGameState == 0) {//正常情况下，只需键盘处理，和逻辑处理
                    keyProcess();
                    logicProcess();
                    multiGameLogicProcess();
                    bossMoveLogic();//boss的移动逻辑处理

                    long current = System.currentTimeMillis(); //时间的逻辑处理
                    if (current - currentTime > 1000) {
                        gameTimeLength--;
                        if (gameTimeLength <= 0) {
                            currentGameState = 2;
                        }
                        currentTime = current;
                    }
                    render(g);
                } else if (currentGameState == 2) {//如果当前为失败状态
                    if (sCurrentGameState == 0) {
                        multiGameLogicProcess();
                        bossMoveLogic();//boss的移动逻辑处理
                        long current = System.currentTimeMillis(); //时间的逻辑处理
                        if (current - currentTime > 1000) {
                            gameTimeLength--;
                            if (gameTimeLength <= 0) {
                                currentGameState = 2;
                            }
                            currentTime = current;
                        }
                        render(g);
                    }
                } else {
                    render(g);
                }
                flushGraphics();
                Thread.sleep(TIME_PER_FRAME - System.currentTimeMillis() + time);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    /**
     * 绘制2D的场景，所有2D的绘制都在这里进行
     * @param g
     */
    private void renderScene2D(Graphics g) {
        //绘制指南针
//        Image image = this.getCompass();
//        g.drawImage(image, 124, 0, Graphics.TOP | Graphics.LEFT);
        drawCompass(g);
        drawLifeValue(g);//画出生命值

        if (propTime > 0 && currentPromptType == 6)//绘制小地图
        {
            draw2DsmallMaze(g);
        }
//绘制全地图
        if (propTime > 0 && currentPromptType == 5) { //
            renderAllMaze2D(g);
        }

        drawTimeAndScore(g);
        drawItemState(g);
        showPrompt(g, currentPromptType);
        //下面的提醒是提醒当本机挂掉，而友方还没有挂掉时的镜头信息
        if (isMultiGame && lifeValue <= 0 && sCurrentGameState == 0) {
            g.drawString("队友镜头", 130, 50, Graphics.TOP | Graphics.LEFT);
        }

    }

    private Image getAllMaze2DImage() {
        Image image2 = Image.createImage(60, 60);
        Graphics g = image2.getGraphics();
        g.setColor(0x0);
        g.setColor(0x0000ff);
        for (int x = 1; x <
                Maze.MAZE_WIDTH; ++x) {
            for (int y = 1; y <
                    Maze.MAZE_HEIGHT; ++y) {
                if ((mazeMap[x][y] & 1) != 0) /* This cell has a top wall */ {
                    g.drawLine(x * 5, y * 5, x * 5 + 5, y * 5);
                }

                if ((mazeMap[x][y] & 2) != 0) /* This cell has a bottom wall */ {
                    g.drawLine(+x * 5, y * 5 + 5, x * 5 + 5, y * 5 + 5);
                }

                if ((mazeMap[x][y] & 4) != 0) /* This cell has a left wall */ {
                    g.drawLine(x * 5, y * 5, x * 5, y * 5 + 5);
                }

                if ((mazeMap[x][y] & 8) != 0) /* This cell has a right wall */ {
                    g.drawLine(x * 5 + 5, y * 5, x * 5 + 5, y * 5 + 5);
                }

            }
        }
        return image2;
    }
    private Image mazeMap2D;

    /**
     * 绘制整个迷宫的2D效果。用于观察2D的迷宫的外形
     * @param g 画笔
     */
    private void renderAllMaze2D(Graphics g) {
        if (mazeMap2D == null) {
            mazeMap2D = getAllMaze2DImage();
        }

        g.drawImage(mazeMap2D, MAP_OFFSET_X, MAP_OFFSET_Y, Graphics.TOP | Graphics.LEFT);
    }

    /**
     * 3D的场景的绘制。用于绘制3D的迷宫外形
     * @param g3d
     */
    private void renderScene3D(Graphics3D g3d) {
        int m = posX / 100;
        int n = posZ / 100;
        int i = 4;
        int j = 4;

        if (cameraAngle >= -45 && cameraAngle <= 45) {
            m = m - 2;
            n = n - 3;

        } else if (cameraAngle >= 45 && cameraAngle <= 135) {
            m = m - 3;
            n = n - 2;

        } else if (cameraAngle >= 135 || cameraAngle <= -135) {
            m = m - 2;

        } else if (cameraAngle <= -45 && cameraAngle >= -135) {
            n = n - 2;
        }

        if (m <= 1) {
            m = 1;
        }

        if (n <= 1) {
            n = 1;
        }

        int maxI = m;
        int maxJ = n;

//        //("m:" + m + " n:" + n);
        Transform t = new Transform();
        for (m = maxI; m < Maze.MAZE_WIDTH && m - maxI <= i; m++) {
            for (n = maxJ; n < Maze.MAZE_HEIGHT && n - maxJ <= j; n++) {
                t.setIdentity();
                t.postTranslate(m * 100 + 50, -50, n * 100 + 50);
                t.postRotate(90, 1, 0, 0);
                if (m == n && m == 6) {
                    g3d.render(centerPlane, t);
                } else {
                    g3d.render(groundPlane, t);
                }
                if ((mazeMap[m][n] & 1) != 0) /* This cell has a top wall */ {
                    t.setIdentity();
                    t.postTranslate(m * 100 + 50, 0, n * 100);
                    g3d.render(wall, t);
                }

                if ((mazeMap[m][n] & 2) != 0) /* This cell has a bottom wall */ {
                    t.setIdentity();
                    t.postTranslate(m * 100 + 50, 0, n * 100 + 100);
                    g3d.render(wall, t);
                }

                if ((mazeMap[m][n] & 4) != 0) /* This cell has a left wall */ {
                    t.setIdentity();
                    t.postTranslate(m * 100, 0, n * 100 + 50);
                    t.postRotate(90, 0, 1, 0);
                    g3d.render(wall, t);
                }

                if ((mazeMap[m][n] & 8) != 0) /* This cell has a right wall */ {
                    t.setIdentity();
                    t.postTranslate(m * 100 + 100, 0, n * 100 + 50);
                    t.postRotate(90, 0, 1, 0);
                    g3d.render(wall, t);
                }

            }
        }
    }

    private boolean isPass(PointInfo p, int d) {
        int x = p.x;
        int z = p.y;
        if ((p.direction & d) != 0) {//表示该方向已经走过了
            return false;
        } else if (d == PointInfo.UP) {
            if ((mazeMap[x][z] & 1) == 0 && z - 1 > 0 && (mazeMap[x][z - 1] & 2) == 0) {//shang侧可走
                return true;
            } else {
                return false;
            }
        } else if (d == PointInfo.DOWN) {
            if ((mazeMap[x][z] & 2) == 0 && z + 1 < Maze.MAZE_HEIGHT - 1 && (mazeMap[x][z + 1] & 1) == 0) {//后侧可走
                return true;
            } else {
                return false;
            }
        } else if (d == PointInfo.LEFT) {
            if ((mazeMap[x][z] & 4) == 0 && x - 1 > 0 && (mazeMap[x - 1][z] & 8) == 0) //左侧可走
            {
                return true;
            } else {
                return false;
            }

        } else if (d == PointInfo.RIGHT) {
            if ((mazeMap[x][z] & 8) == 0 && x + 1 < Maze.MAZE_WIDTH - 1 && (mazeMap[x + 1][z] & 4) == 0) //左侧可走
            {
                return true;
            } else {
                return false;
            }

        }
        return false;
    }
    private Vector[] tempVector;

    private byte computeBossDirection(int i) {
        if (tempVector[i].size() == 0) {
            tempVector[i].addElement(new PointInfo(bossPosX[i] / 100, bossPosZ[i] / 100, PointInfo.NOWAY));
        }

        PointInfo p = (PointInfo) (tempVector[i].lastElement());
//        //("x:" + p.x + "   y:" + p.y);
        if (isPass(p, PointInfo.UP)) {
            PointInfo p1 = p.move(0, -1, PointInfo.DOWN);
            p1.direction |= PointInfo.DOWN;
            tempVector[i].addElement(p1);
//            //("选择了UP");
            p.direction |= PointInfo.UP; //这个点的UP，已经走过了
            return PointInfo.UP;
        } else {
            p.direction |= PointInfo.UP; //这个点的UP，已经走过了
        }

        if (isPass(p, PointInfo.RIGHT)) {
            PointInfo p1 = p.move(1, 0, PointInfo.LEFT);
            p1.direction |= PointInfo.LEFT;
            tempVector[i].addElement(p1);
//            //("选择了RIGHT");
            p.direction |= PointInfo.RIGHT; //这个点的RIGHT，已经走过了
            return PointInfo.RIGHT;
        } else {
            p.direction |= PointInfo.RIGHT; //这个点的RIGHT，已经走过了
        }

        if (isPass(p, PointInfo.DOWN)) {
            PointInfo p1 = p.move(0, 1, PointInfo.UP);
            p1.direction |= PointInfo.UP;
            tempVector[i].addElement(p1);
            p.direction |= PointInfo.DOWN; //这个点的DOWN，已经走过了
//            //("选择了DOWN");
            return PointInfo.DOWN;
        } else {
            p.direction |= PointInfo.DOWN; //这个点的DOWN，已经走过了
        }

        if (isPass(p, PointInfo.LEFT)) {
            PointInfo p1 = p.move(-1, 0, PointInfo.RIGHT);
            p1.direction |= PointInfo.RIGHT;
            tempVector[i].addElement(p1);
            p.direction |= PointInfo.LEFT; //这个点的UP，已经走过了
//            //("选择了LFET");
            return PointInfo.LEFT;
        } else {
            p.direction |= PointInfo.LEFT; //这个点的UP，已经走过了
        }
        tempVector[i].removeElement(p);

        return p.initDirection;



    }

    private boolean isCollideWithBoss() {
        for (int i = 0; i <
                bossPosX.length; i++) {
            if (Math.abs(posZ - bossPosZ[i]) < 40 && Math.abs(bossPosX[i] - posX) < 40) {
                return true;
            }

        }
        return false;

    }

    private void logicProcess() {
        CollisionWithPorp();//与道具的碰撞检测逻辑处理
        //成功判断
        if (currentPhase == 2 && Math.abs(650 - posX) < 40 && Math.abs(650 - posZ) < 40) {
            currentGameState = 3;//成功
        }
//随即道具时间的处理
        if (propTime > 0) {
            propTime--;
        }
//失败的判断条件1：时间到
        if (gameTimeLength <= 0) { //如果游戏时间超过失败！
            currentGameState = 2;//失败
            currentPromptType = -1;
            promptTime = 20;
        }
//失败的判断条件2：没有生命值
        if (lifeValue == 0) {//生命值为0，失败！
            lifeValue = 0;
            promptTime = 20;
            currentGameState = 2;//失败
            currentPromptType = 19;
        }
        //道具boss的旋转逻辑处理
        propAngle += 10;//道具的角度处理
        for (int i = 0; i < bossPosZ.length; i++) {
            if (currentBossDirection[i] == PointInfo.DOWN || currentBossDirection[i] == PointInfo.LEFT) {
                bossAngle[i] += 10;
            } else {
                bossAngle[i] -= 10;
            }
        }
    }

    /**
     * 碰撞检测
     * @return 返回真，表示碰撞上，反之没有碰撞成功
     */
    private boolean isCollideWithWall() {
        int i = posX % 100;
        int j = posZ % 100;
        if (i > MAX_INACCURACY && i < 100 - MAX_INACCURACY && j > MAX_INACCURACY && j < 100 - MAX_INACCURACY) {
            return false;
        }
        if (posZ <= 100 || posX <= 100 || posX >= (Maze.MAZE_WIDTH - 1) * 100 || posZ >= (Maze.MAZE_HEIGHT - 1) * 100) {
            return true;
        }

        int x = posX / 100;
        int z = posZ / 100;
        //碰撞检测的原理：
        //碰撞检测基于当前的坐标的位置与周围围墙的坐标距离
        //通过cameraPosX，和cameraPosZ求出所在的数据结构坐标
        //发现的问题待解决：
        //碰撞检测部分的代码还需要进行修改，务必修改
        if (i <= MAX_INACCURACY) {
            if ((mazeMap[x][z] & 4) != 0 || (mazeMap[x - 1][z] & 8) != 0) /* This cell has a left wall */ {
                if (posX - x * 100 <= MAX_INACCURACY && cameraAngle != -180 && cameraAngle != 180 && cameraAngle != 0) {
                    return true;
                }
            }
        }
        if (i >= 100 - MAX_INACCURACY) {
            if ((mazeMap[x][z] & 8) != 0 || (mazeMap[x + 1][z] & 4) != 0) /* This cell has a right wall */ {
                if ((x * 100 + 100) - posX <= MAX_INACCURACY && cameraAngle != -180 && cameraAngle != 180 && cameraAngle != 0) {
                    return true;
                }

            }
        }
        if (j <= MAX_INACCURACY) {
            if ((mazeMap[x][z] & 1) != 0 || (mazeMap[x][z - 1] & 2) != 0) /* This cell has a top wall */ {
                if (posZ - z * 100 <= MAX_INACCURACY && cameraAngle != 90 && cameraAngle != -90) {
                    return true;
                }
            }
        }
        if (j >= 100 - MAX_INACCURACY) {
            if ((mazeMap[x][z] & 2) != 0 || (mazeMap[x][z + 1] & 1) != 0) /* This cell has a bottom wall */ {
                if (z * 100 + 100 - posZ <= MAX_INACCURACY && cameraAngle != 90 && cameraAngle != -90) {
                    return true;
                }

            }
        }

        return false;
    }

    private void bossMoveLogic() {
        for (int i = 0; i < bossPosX.length; i++) {
            if (bossPosZ[i] % 100 == 50 && bossPosX[i] % 100 == 50) {
                currentBossDirection[i] = computeBossDirection(i);
                if (currentBossDirection[i] == PointInfo.NOWAY) {
                    currentBossDirection[i] = computeBossDirection(i);
                }

            }

            if (currentBossDirection[i] == PointInfo.UP) {
                bossPosZ[i] -= 10;
            } else if (currentBossDirection[i] == PointInfo.RIGHT) {
                bossPosX[i] += 10;
            } else if (currentBossDirection[i] == PointInfo.DOWN) {
                bossPosZ[i] += 10;
            } else if (currentBossDirection[i] == PointInfo.LEFT) {
                bossPosX[i] -= 10;
            }

        }
    }

    /**
     * 画小地图
     * @param g
     */
    private void draw2DsmallMaze(Graphics g) {
        g.setColor(0x00ff00);
        int x = (int) (posX / 20.0f) + MAP_OFFSET_X;
        int y = (int) (posZ / 20.0f) + MAP_OFFSET_Y;
        g.fillArc(x - 2, y - 2, 4, 4, 0, 360);
        g.setColor(0xffff00);

        int i = posX / 100 - 2 < 1 ? 1 : posX / 100 - 2;
        int j = posZ / 100 - 2 < 1 ? 1 : posZ / 100 - 2;
        for (x = i; x <
                Maze.MAZE_WIDTH && x - i < 5; x++) {
            for (y = j; y <
                    Maze.MAZE_HEIGHT && y - j < 5; y++) {
                if ((mazeMap[x][y] & 1) != 0) /* This cell has a top wall */ {
                    g.drawLine(MAP_OFFSET_X + x * 5, MAP_OFFSET_Y + y * 5, MAP_OFFSET_X + x * 5 + 5, MAP_OFFSET_Y + y * 5);
                }

                if ((mazeMap[x][y] & 2) != 0) /* This cell has a bottom wall */ {
                    g.drawLine(MAP_OFFSET_X + x * 5, MAP_OFFSET_Y + y * 5 + 5, MAP_OFFSET_X + x * 5 + 5, MAP_OFFSET_Y + y * 5 + 5);
                }

                if ((mazeMap[x][y] & 4) != 0) /* This cell has a left wall */ {
                    g.drawLine(MAP_OFFSET_X + x * 5, MAP_OFFSET_Y + y * 5, MAP_OFFSET_X + x * 5, MAP_OFFSET_Y + y * 5 + 5);
                }

                if ((mazeMap[x][y] & 8) != 0) /* This cell has a right wall */ {
                    g.drawLine(MAP_OFFSET_X + x * 5 + 5, MAP_OFFSET_Y + y * 5, MAP_OFFSET_X + x * 5 + 5, MAP_OFFSET_Y + y * 5 + 5);
                }

            }
        }
    }
    private final int FIRST_TIME_LENGTH = 300;
    private final int SECOND_TIME_LENGTH = 60;

    /**
     * 画游戏进行时间到屏幕上
     */
    private void drawTimeAndScore(Graphics g) {
        g.setColor(0xffffff);
//        gameTimeLength = (int) (FIRST_TIME_LENGTH - (current - currentTime) / 1000);
        if (gameTimeLength <= 30) {
            g.setColor(0xff0000);
        }
        if (gameTimeLength < 0) {
            gameTimeLength = 0;
            currentGameState = 2;
        }
        g.drawString("TIME:" + gameTimeLength, 30, 0, Graphics.TOP | Graphics.LEFT);
        g.setColor(0x0000ff);
        g.drawString("SCORE:" + score, 30, 16, Graphics.TOP | Graphics.LEFT);
        if (isMultiGame) {
            g.setColor(0x00ff00);
            g.drawString("SCORE:" + sScore, 30, 32, Graphics.TOP | Graphics.LEFT);
        }

    }
//画物品的状态，包括加速减速，大地图显示、小地图显示和逆向
    private void drawItemState(Graphics g) {
        if (propTime > 0) {
            if (currentPromptType == 2) {
                g.drawImage(accPropImage, 10, 8, Graphics.TOP | Graphics.LEFT);
            } else if (currentPromptType == 3) {
                g.drawImage(decPropImage, 10, 8, Graphics.TOP | Graphics.LEFT);
            } else if (currentPromptType == 4) {
                g.drawImage(reversePropImage, 10, 8, Graphics.TOP | Graphics.LEFT);
            }

            if (currentPromptType == 6) {  //绘制小地图提示标志
                g.drawImage(smallMapImage, 10, 8, Graphics.TOP | Graphics.LEFT);
            }

            if (currentPromptType == 5) { //绘制大地图的提示标志
                g.drawImage(fullMapImage, 10, 8, Graphics.TOP | Graphics.LEFT);
            }

        }
    }
    /**
     * 获得指南针图片
     */
    private byte[] tempPosX = {14, 36, 48, 48, 36, 14, 2, 2};
    private byte[] tempPosZ = {2, 2, 14, 36, 48, 48, 36, 14};
    private static final short COMPASS_OFFSET_X = 126;
    private static final short COMPASS_OFFSET_Y = 0;
    private int lastGetNumber = 0;

    private void drawCompass(Graphics g) {

        if (lastGetNumber != currentGetPartNum) {
            lastGetNumber = currentGetPartNum;
            int temp = currentGetPartNum;
            Graphics g2 = compassImage.getGraphics();
            for (int m = 0; temp != 0 && m < tempPosZ.length; m++) { //画直线
                if (partPosX[m] == 0) {
                    g2.setColor(0x0000ff);
                    g2.fillRect(tempPosX[m] - 2, tempPosZ[m] - 2, 4, 4);
                    if (partPosX[(m + 1) % 8] == 0) {
                        g2.drawLine(tempPosX[m], tempPosZ[m], tempPosX[(m + 1) % 8], tempPosZ[(m + 1) % 8]);
                    } else {
                        m++;
                    }
                    temp--;
                }

            }
        }
        g.drawImage(compassImage, COMPASS_OFFSET_X, COMPASS_OFFSET_Y, Graphics.TOP | Graphics.LEFT);

        float i = (float) (sinOf(cameraAngle) * 15);
        float j = (float) (cosOf(cameraAngle) * 15);
        g.setColor(0x0000ff);
        g.drawLine(COMPASS_OFFSET_X + 25 - (int) i, COMPASS_OFFSET_Y + 25 - (int) j, COMPASS_OFFSET_X + 25, COMPASS_OFFSET_Y + 25);//画方向的箭头

        int m = (int) ((((posX - 100) / 100.0f)) * 5);
        int n = (int) ((((posZ - 100) / 100.0f)) * 5);
        g.setColor(0x0000ff);//蓝色表示自己
        g.fillRect(COMPASS_OFFSET_X + m - 2, COMPASS_OFFSET_Y + n - 2, 4, 4);

        if (isMultiGame) {
            m = (int) ((((sPosX - 100) / 100.0f)) * 5);
            n = (int) ((((sPosZ - 100) / 100.0f)) * 5);
            g.setColor(0x00ff00);//绿色表示队友
            g.fillRect(COMPASS_OFFSET_X + m - 2, COMPASS_OFFSET_Y + n - 2, 4, 4);
        }

    }

    /**
     * 画生命值
     * @param g
     */
    private void drawLifeValue(Graphics g) {
        g.setColor(0x0000ff);
        g.drawRect(LIFE_VALUE_POS_X, LIFE_VALUE_POS_Y, 6, 48);
        g.drawLine(LIFE_VALUE_POS_X, LIFE_VALUE_POS_Y + 16, LIFE_VALUE_POS_X + 6, LIFE_VALUE_POS_Y + 16);
        g.drawLine(LIFE_VALUE_POS_X, LIFE_VALUE_POS_Y + 32, LIFE_VALUE_POS_X + 6, LIFE_VALUE_POS_Y + 32);
        if (lifeValue == 3) {
            g.fillRect(LIFE_VALUE_POS_X, LIFE_VALUE_POS_Y, 6, 48);
        } else if (lifeValue == 2) {
            g.fillRect(LIFE_VALUE_POS_X, LIFE_VALUE_POS_Y + 16, 6, 32);
        } else if (lifeValue == 1) {
            g.fillRect(LIFE_VALUE_POS_X, LIFE_VALUE_POS_Y + 32, 6, 16);
        }

        if (isMultiGame) {
            g.setColor(0x00ff00);
            g.drawRect(LIFE_VALUE_POS_X + 10, LIFE_VALUE_POS_Y, 6, 48);
            if (sLife == 3) {
                g.fillRect(LIFE_VALUE_POS_X + 10, LIFE_VALUE_POS_Y, 6, 48);
            } else if (sLife == 2) {
                g.fillRect(LIFE_VALUE_POS_X + 10, LIFE_VALUE_POS_Y + 16, 6, 32);
            } else if (sLife == 1) {
                g.fillRect(LIFE_VALUE_POS_X + 10, LIFE_VALUE_POS_Y + 32, 6, 16);
            }

        }
    }

    protected void keyPressed(int code) {
        switch (code) {
            case -7: //右软键
                if (currentGameState == 0) {//如果是正常状态，选择右软键，游戏暂停
                    if (!this.isMultiGame || (this.isMultiGame && (!this.connected || this.isInitState))) {
                        this.gamePause();
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

                break;
            case -6://左软键
                if (currentGameState == 2 && !isMultiGame) {//单人游戏下游戏失败的话，选择游戏重新开始
                    gameStop();
                    mainMidlet.firstStageGameShow();
                }
                if (currentGameState == 3 && !isMultiGame) {//
                    gameStop();
                    mainMidlet.fisrtStageAnimationShow(4);
                }
            case -1://上:响应暂停时的菜单响应
            case Maze3DCanvas.KEY_NUM2:
                if (currentGameState == 1) {//暂停状态
                    if (pauseMenu != null) {
                        pauseMenu.onKeyPressed(UP);
                    }

                }
                break;
            case -2://下
            case Maze3DCanvas.KEY_NUM8:
                if (currentGameState == 1) {
                    if (pauseMenu != null) {
                        pauseMenu.onKeyPressed(DOWN);
                    }
                }
                break;
            case -3://左
            case Maze3DCanvas.KEY_NUM4:
                if (currentGameState == 1) {
                    if (pauseMenu != null) {
                        pauseMenu.onKeyPressed(LEFT);
                    }

                }
                break;
            case -4://右
            case Maze3DCanvas.KEY_NUM6:
                if (currentGameState == 1) {
                    if (pauseMenu != null) {
                        pauseMenu.onKeyPressed(RIGHT);
                    }
                }
                break;
            case -5://fire
            case Maze3DCanvas.KEY_NUM5:
                if (currentGameState == 1 && pauseMenu != null) {
                    if (pauseMenu.getIndex() == 1) {
                        currentGameState = 0;
                        pauseMenu = null;
                        System.gc();
                        this.getKeyStates();
                    } else if (pauseMenu.getIndex() == 3) {
                        this.gameStop();
                        if (isMultiGame) {
                            mainMidlet.mainMenuShow(8);//进入主菜单的选择服务端还是客户端    
                        } else {
                            mainMidlet.mainMenuShow(7);
                        }
                    }

                }
                break;
        }

    }

    /**
     * 与道具碰撞处理的函数，包括与石球即boss相碰撞
     */
    private void CollisionWithPorp() {
        if (isCollideWithPorp()) { //与1/8道具碰撞播放声音

            playFXSound(1);
            currentPromptType = (byte) ((System.currentTimeMillis() % 3) + 2);
            propTime = (1000 / TIME_PER_FRAME) * 8;
            promptTime = 10;
        } else if (isCollideWithSmallMap()) {
            playFXSound(1);
            propTime = (1000 / TIME_PER_FRAME) * 10;//10秒钟显示小地图时间
            promptTime = 10;
            currentPromptType = 6;
        } else if (isCollideWithFullMap()) {
            playFXSound(1);
            propTime = (1000 / TIME_PER_FRAME) * 10;//10秒钟显示大地图时间
            promptTime = 10; //开始提示
            currentPromptType =
                    5;
        } else if (isCollideWithCoin()) { //如果与金币相接触，分数加100
            playFXSound(2);
            score += 100;
            promptTime =
                    10;
            currentPromptType =
                    7;
        } else if (isCollideWithBoss()) {
            playFXSound(3);
            lifeValue--;
            posX =
                    650;
            posZ =
                    650;
            cameraAngle =
                    0;
            promptTime =
                    10;
            currentPromptType =
                    1;
            if (currentPhase == 2)//在第二阶段，一旦与boss碰撞，你将直接失败！
            {
                lifeValue = 0;
            }

            if (lifeValue == 0) {
                currentGameState = 2; //游戏失败
            }

        } else if (isCollideWithPart()) { //如果与1/8道具碰撞
            playFXSound(0);
            promptTime = 10;
            currentGetPartNum++;

            if (isMultiGame) {
                score += 200;
            }

            if (currentGetPartNum == 8) {
                currentPhase = 2;
                musicControl.musicChangeRate(200000);
                firstPhaseTime =
                        gameTimeLength;
                gameTimeLength = SECOND_TIME_LENGTH;
            }

        }
    }

    private boolean isCollideWithFullMap() {
        for (int i = 0; i < 3; i++) {
            if (propType[i] != 0 && Math.abs(posZ - propPosZ[i]) < 8 && Math.abs(posX - propPosX[i]) < 8) {
                propType[i] = 0;
                propID = (byte) i;
                return true;
            }

        }
        return false;
    }

    private boolean isCollideWithPorp() {
        for (int i = 6; i <
                9; i++) {
            if (propType[i] != 0 && (Math.abs(posZ - propPosZ[i]) < 8) && (Math.abs(posX - propPosX[i]) < 8)) {
                propType[i] = 0;
                propID = (byte) i;
                return true;
            }

        }
        return false;
    }

    private boolean isCollideWithSmallMap() {
        for (int i = 3; i < 6; i++) {
            if (propType[i] != 0 && Math.abs(posZ - propPosZ[i]) < 15 && Math.abs(posX - propPosX[i]) < 15) {
                propType[i] = 0;
                propID = (byte) i;
                return true;
            }

        }
        return false;
    }

    private boolean isCollideWithCoin() {
        for (int i = 0; i < coinPosX.length; i++) {
            if (coinPosX[i] != 0 && Math.abs(posZ - coinPosZ[i]) < 10 && Math.abs(posX - coinPosX[i]) < 10) {
                coinPosX[i] = 0;
                propID = (byte) (i + 10);
                return true;
            }
        }
        return false;
    }

    private boolean isCollideWithPart() {
        for (int i = 0; i <
                partPosX.length; i++) {
            if (partPosX[i] != 0 && Math.abs(posZ - partPosZ[i]) < 30 && Math.abs(posX - partPosX[i]) < 30) {
                partPosX[i] = 0;
                currentPromptType =
                        (byte) (i + 9);
                propID =
                        (byte) (i + 20);
                return true;
            }

        }
        return false;
    }
    private Image sfImage = null; //显示成功或失败的图片successorfailed。

    private void showSuccessOrFailed(Graphics g, int type) {
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
            if (!isMultiGame) {
                g.drawImage(sfImage, this.getWidth() >> 1, this.getHeight() >> 1, Graphics.VCENTER | Graphics.HCENTER);
                g.drawString("是", 0, 200, Graphics.TOP | Graphics.LEFT);
                g.drawString("否", 150, 200, Graphics.TOP | Graphics.LEFT);
            } else {
                g.drawString("游戏失败！", (this.getWidth() >> 1) - 24, this.getHeight() >> 1, Graphics.TOP | Graphics.LEFT);
                g.drawString("退出", 140, 200, Graphics.TOP | Graphics.LEFT);
            }

        }
        if (type > 0) {
            g.drawImage(sfImage, this.getWidth() / 2, 50, Graphics.TOP | Graphics.HCENTER);
            if (!isMultiGame) {
                g.drawString("寻找遗物剩余时间:" + firstPhaseTime, 30, 90, Graphics.TOP | Graphics.LEFT);
                g.drawString("返回原点剩余时间：" + gameTimeLength, 30, 110, Graphics.TOP | Graphics.LEFT);
                g.drawString("得分：" + score, 30, 130, Graphics.TOP | Graphics.LEFT);
                g.drawString("综合评定：" + getResult(), 30, 150, Graphics.TOP | Graphics.LEFT);
                g.drawString("继续游戏", 0, 200, Graphics.TOP | Graphics.LEFT);
            } else {
                g.drawString("我的得分：" + score, 30, 130, Graphics.TOP | Graphics.LEFT);
                g.drawString("队友得分：" + sScore, 30, 150, Graphics.TOP | Graphics.LEFT);
                g.drawString("退出", 140, 200, Graphics.TOP | Graphics.LEFT);
            }

        }
    }
    /**
     * 显示提示，其使用promptTime来作为提示显示的时间，用type来指定是什么样的提示。
     * 为了减少定义的变量，下面定义了type类型。
     * 1：与boss相碰，回到原点的提示
     * 2：获得加速道具的提示
     * 3：获得减速道具的提示
     * 4：获得逆向道具的提示
     * 5：获得大地图的提示
     * 6：获得小地图的提示
     * 7：获得金币加分数的提示
     * 9：头盔
     * 10、书简
     * 11、佩剑
     * 12、弓弩
     * 13、铠甲
     * 15、军靴
     * 14、配饰
     * 16、长矛
     * 20、服务器蓝牙连接显示
     * 30-37:当友方吃到某个道具在本机上的提示：分别对应头盔-》长矛
     * @param g 画笔
     * @param type 类型，如上
     */
    private String[] promptString = {"加速道具", "减速道具", "逆向道具",
        "大地图提示", "小地图提示", "金币", "头盔",
        "书简", "佩剑", "弓弩", "铠甲", "配饰", "军靴", "长矛"
    };

    private void showPrompt(Graphics g, int type) {
        if (promptTime > 0) {
            int x = 30;
            int y = 180;
            g.setColor(0x00ffff);
            if (type == 1) {
                g.drawString("魔石送至起始!", x, y, Graphics.TOP | Graphics.LEFT);
                g.drawString("还有" + lifeValue + "次机会！", x, y + 20, Graphics.TOP | Graphics.LEFT);

            } else if (type >= 2 && type < 8) {
                g.drawString("获得" + promptString[type - 2] + "!", x, y, Graphics.TOP | Graphics.LEFT);
            } else if (type >= 9 && type <= 16) {
                g.drawString("恭喜！获得" + promptString[type - 3] + "!", x, y, Graphics.TOP | Graphics.LEFT);
            } else if (type == 20) {
                g.drawString("玩家加入游戏，初始化游戏！", x, y, Graphics.TOP | Graphics.LEFT);
            } else if (type >= 30 && type <= 37) {
                g.drawString("友方玩家获得" + promptString[type % 10 + 6] + "!", x, y, Graphics.TOP | Graphics.LEFT);
            }
            promptTime--;
        }

    }

    public void gameStart() {
        currentGameState = 0;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * 游戏暂停函数，当用户选择暂停时
     */
    public void gamePause() {
        currentGameState = 1;
    }

    public void gameStop() {
        this.stopped = true;
        if (currentMusicState == true) {
            turnOnOrOffMusic();
        }
        if (isMultiGame) {
            mainMidlet.cancelBTService();
        }

        musicControl = null;
        if (currentGameState == 3) {
            DataStore d = new DataStore();
            d.writeScoreRecord(mainMidlet.playerName, this.getResult());
        }
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
            musicControl.changeMusic("/res/music1.mid");
            musicControl.musicStart();
            musicControl.changeMusicVolume(mainMidlet.musicVolume);
            currentMusicState = true;
            return currentMusicState;
        }
    }

    public boolean getCurrentMusicState() {
        return currentMusicState;
    }

    public int getResult() {
        return firstPhaseTime * 10 + gameTimeLength * 20 + score;
    }

    public int getCameraAngle() {
        return cameraAngle;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosZ() {
        return posZ;
    }

    public int getLifeValue() {
        return lifeValue;
    }

    public int getPropID() {
        return propID;
    }

    public int[][] getMazeMap() {
        return mazeMap;
    }

    public void setMazeMap(int[][] map) {
        synchronized (mazeMap) {
            this.mazeMap = map;
        }
    }

    public void setFriendState(int x, int y, byte life, byte sPropID, int angle) {
        this.sAngle = (short) angle;
        this.sPosX = (short) x;
        this.sPosZ = (short) y;
        this.sLife = life;
        this.sPropID = sPropID;
    }

    private void multiGameLogicProcess() {
        //下面的代码是处理道具的消失逻辑
        if (sLastPropID != sPropID) {
            sLastPropID = sPropID;
            if (sPropID / 10 == 0) {
                this.propPosX[sPropID % 10] = 0;
            } else if (sPropID / 10 == 1) {
                this.coinPosX[sPropID % 10] = 0;
                sScore +=
                        100;
            } else if (sPropID / 10 == 2) {
                this.partPosX[sPropID % 10] = 0;
                playFXSound(0);
                this.currentGetPartNum++;
                sScore += 200;
                this.currentPromptType = (byte) (30 + sPropID % 10);
                promptTime =
                        10;
                if (currentGetPartNum == 8) {
                    currentPhase = 2;
                    firstPhaseTime =
                            gameTimeLength;
                    gameTimeLength =
                            SECOND_TIME_LENGTH;
                }

            }
        }
        //下面的逻辑用于让镜头切换到对方的镜头下

        if (currentGameState == 2) {//失败状态
            cameraAngle = sAngle;
            posZ = sPosZ;
            posX = sPosX;
        }

        if (!connected) {
            sLife = 0;
        }

        if (sLife == 0) {
            sCurrentGameState = 2;
        }
//当对方胜利后，我们也胜利了！
        if (currentPhase == 2 && Math.abs(650 - sPosX) < 40 && Math.abs(650 - sPosZ) < 40 && connected) {
            sCurrentGameState = 3;
            if (currentGameState == 0 && connected) {
                currentGameState = 3;
            }
        }
    }

    public void setConnected(boolean i) {
        this.connected = i;
    }

    public boolean isFailed() {
        return currentGameState == 2;
    }

    public boolean isSuccess() {
        return currentGameState == 3;
    }
    private float[] SIN_VALUE = {0.0f, 0.309f, 0.588f, 0.809f, 0.951f, 1.0f};

    private float sinOf(int degree) {
        if (degree >= 0 && degree <= 90) {
            return SIN_VALUE[degree / 18];
        } else if (degree > 90 && degree <= 180) {
            return SIN_VALUE[(180 - degree) / 18];
        } else if (degree > -180 && degree <= -90) {
            return -SIN_VALUE[(degree + 180) / 18];
        } else if (degree < 0 && degree > -90) {
            return -SIN_VALUE[(-degree) / 18];
        }
        return 0;
    }

    private float cosOf(int degree) {
        if (degree + 90 > 180) {
            return sinOf((degree - 270));
        } else {
            return sinOf(degree + 90);
        }
    }

    private void playFXSound(int type) {
        if (currentMusicState == true && fxControl != null) {
            if (type == 0) {//part
                fxControl.changeMusic("/res/part.wav");
            } else if (type == 1) {//prop
                fxControl.changeMusic("/res/flash.wav");
            } else if (type == 2) {//coin
                fxControl.changeMusic("/res/coin.wav");
            } else if (type == 3) {
                fxControl.changeMusic("/res/fail.wav");
            }
            fxControl.musicStart();
        }
    }
}
