package mainMenu;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import stage1.AnimationCanvas;
import stage1.Maze3DCanvas;
import stage1.MazeClient;
import stage1.MazeServer;
import stage1.WaitCanvas;
import stage2.FlyShotCanvas;
import stage2.FlyShotScene;
import stage2.FlyShotPausedMenu;
import stage3.Stage3AnimationCanvas;
import stage3.HorseRace;
import stage3.RaceClient;
import stage3.RaceServer;

/**
 * @author 版权：梁春晓 2008年7月20日
 */
public class MainMidlet extends MIDlet {

    public boolean needMusic = true;     //音乐的开和关
    public byte musicVolume = 3;
    public String playerName = "王晶";
    private Thread thread;
    private Display display = Display.getDisplay(this);//    public HighScore highScore;
    public MainMenuCanvas mainMenu;
    private Maze3DCanvas mazeCanvas;
    private WaitCanvas waitCanvas;
    private MazeClient client;
    private MazeServer server;
    private HorseRace horseRace;
    private RaceClient raceClient;
    private RaceServer raceServer;
    private FlyShotCanvas stage2Game;

    public MainMidlet() {
        DataStore dataStore = new DataStore();
        musicVolume = (byte) dataStore.readMusicVolume();

        playerName = dataStore.readNameRecord();
        if (musicVolume == 0) {
            needMusic = false;
        }


    }

    public void startApp() {
        mainMenuShow(0);
//        stage3AnimationShow(6);
//        stage3GameShow();
//        gameSuccessShow();
//      firstStageGameShow();
//        stage2GameShow(false);
//        firstStageGameShow();
    }

    public void pauseApp() {
        this.notifyPaused();
    }

    public void destroyApp(boolean unconditional) {
        System.gc();
        this.notifyDestroyed();
    }

    public void mainMenuShow(int index) {
        this.client = null;
        this.server = null;
        this.raceClient = null;
        this.raceServer = null;
        this.mazeCanvas = null;
        this.horseRace = null;
        this.stage2Game = null;
        this.waitCanvas = null;
        System.gc();
        if (mainMenu == null) {
            mainMenu = new MainMenuCanvas(this, index);
        } else {
            mainMenu.isStart = true;
            mainMenu.menuIndex = index;
            if (needMusic && mainMenu.musicControl == null) {
                mainMenu.turnOnOrOffMusic();
            }
            mainMenu.setFullScreenMode(true);
        }
        thread = new Thread(mainMenu);
        display.setCurrent(mainMenu);
        thread.start();
        System.gc();
        showRestMem();
    }

    public void mainCartoonShow() {                  //主题动画
        CartoonCanvas cartoon = new CartoonCanvas(this);
        cartoon.isStart = true;
        thread = new Thread(cartoon);
        display.setCurrent(cartoon);
        thread.start();
        System.gc();
        showRestMem();
    }

    public void highScoreShow() {
        DataStore dataStore = new DataStore();
        HighScore highScore = new HighScore(this, dataStore);
        highScore.isStart = true;
        highScore.isRender = true;
        thread = new Thread(highScore);
        display.setCurrent(highScore);
        thread.start();
        System.gc();
        showRestMem();
    }

    public void gameSetShow() {                    //游戏设置
        GameSet gameSet = new GameSet(this);
        display.setCurrent(gameSet);
        System.gc();
        showRestMem();
    }

    public void gameSuccessShow() {
        this.horseRace = null;
        System.gc();
        GameSuccess gamesucess = new GameSuccess(this);
        thread = new Thread(gamesucess);
        thread.start();
        display.setCurrent(gamesucess);
    }
    //显示剩余内存
    public void showRestMem() {
        long restMem = this.getRestMem();
        System.out.println("剩余的内存为：" + restMem + "KB");
    }

    public long getRestMem() {
        return java.lang.Runtime.getRuntime().freeMemory() / 1024;
    }

    /**
     * 开始第一关的动画
     * @param i 0表示第一个动画，4表示第一关结尾动画
     */
    public void fisrtStageAnimationShow(int i) {
        mazeCanvas = null;
        mainMenu = null;
        System.gc();
        AnimationCanvas stage1Animation = new AnimationCanvas(this, i);
        display.setCurrent(stage1Animation);
        thread = new Thread(stage1Animation);
        thread.start();
    }

    /**
     * 第一关游戏显示
     */
    public void firstStageGameShow() {
        //将前一部分的资源搜集回去
        mazeCanvas = null;
        mainMenu = null;
        System.gc();
        mazeCanvas = new Maze3DCanvas(this, false);
        display.setCurrent(mazeCanvas);
        mazeCanvas.gameStart();
    }

    public void btServerMazeGameShow() {
        mazeCanvas = null;
        mainMenu = null;
        waitCanvas = null;
        System.gc();
        server = new MazeServer(mazeCanvas, this);
        if (server.initBT()) {
            mazeCanvas = new Maze3DCanvas(this, true);
            display.setCurrent(mazeCanvas);
            mazeCanvas.gameStart();
            server.setMaze3D(mazeCanvas);
            server.start();
        }
    }

    public void btClientMazeGameShow() {
        mazeCanvas = null;
        mainMenu = null;
        waitCanvas = null;
        client = new MazeClient(this);
        try {
            client.init();
        } catch (Exception ex) {
            //("无法建立连接！");
            mainMenuShow(2);
        }
        if (client != null && client.getSMap() != null) {
            mazeCanvas = new Maze3DCanvas(this, client.getSMap(), true);
            display.setCurrent(mazeCanvas);
            mazeCanvas.gameStart();
            client.setMaze3D(mazeCanvas);
            client.start();
        } else {
            showCurrentState("无法初始化游戏！");
        }
    }

    public void showCurrentState(String str) {
        if (waitCanvas == null) {
            waitCanvas = new WaitCanvas(this);
            if (client != null) {
                waitCanvas.setClient(client);
            }
            if (server != null) {
                waitCanvas.setServer(server);
            }
        }
        waitCanvas.setString(str);
        if (!(display.getCurrent() instanceof WaitCanvas)) {
            display.setCurrent(waitCanvas);
        }
    }

    public void cancelBTService() {
        if (client != null) {
            //("选择了取消BT服务");
            client.cancelService();
        }
        if (server != null) {
            server.cancelService();
        }
        if (raceClient != null) {
            raceClient.cancelService();
        }
        if (raceServer != null) {
            raceServer.cancelService();
        }
    }

    /**
     * 
     * @param index 0:游戏开始前的动画，1见到石头怪的动画，2与boss对战前3结束前
     */
    public void stage2AnimationShow(int index) {
        mainMenu = null;

        System.gc();
        FlyShotScene stage2Animation = new FlyShotScene(this, index);
        display.setCurrent(stage2Animation);
        stage2Animation.startScene();
    }

    public void stage2GameShow(boolean restart) {
        try {
            if (restart || stage2Game == null) {
                stage2Game = null;
                System.gc();
                stage2Game = new FlyShotCanvas(this);
            }
            stage2Game.startGame();
            display.setCurrent(stage2Game);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void stage2PausedMenuShow() {
        FlyShotPausedMenu stage2PausedMenu = new FlyShotPausedMenu(this, stage2Game);
        thread = new Thread(stage2PausedMenu);
        thread.start();
        display.setCurrent(stage2PausedMenu);
    }

    public void stage3GameShow() {
        mainMenu = null;
        horseRace = null;
        System.gc();
        try {
            horseRace = new HorseRace(this, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(horseRace).start();
        Display.getDisplay(this).setCurrent(horseRace);
    }

    public void btServerHorseRaceGameShow() {
        mainMenu = null;
        System.gc();
        raceServer = new RaceServer(horseRace, this);
        if (raceServer.initBT()) {
            System.gc();
            try {
                horseRace = new HorseRace(this, true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            display.setCurrent(horseRace);
            horseRace.gameStart();
            raceServer.setHorseRace(horseRace);
            raceServer.start();
        }
    }

    public void btClientHorseRaceGameShow() {
        mainMenu = null;
        System.gc();
        raceClient = new RaceClient(this);
        try {
            raceClient.init();
        } catch (IOException ex) {
            //("无法建立连接！");
            mainMenuShow(2);
        }
        if (raceClient != null && raceClient.getSignal() == true) {
            try {
                horseRace = new HorseRace(this, true);
                horseRace.setSignal();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            display.setCurrent(horseRace);
            horseRace.gameStart();
            raceClient.setHorseRace(horseRace);
            raceClient.start();
        } else {
            showCurrentState("无法初始化游戏！");
        }
    }

    /**
     * 第三关动画显示
     * @param index 动画的序列。0为过场马车动画和与苏秦对话，4为刺客与女孩对话，6为
     */
    public void stage3AnimationShow(int index) {
        horseRace = null;
        mainMenu = null;
        System.gc();
        Stage3AnimationCanvas stage3Animation = new Stage3AnimationCanvas(this, index);
        display.setCurrent(stage3Animation);
        stage3Animation.startScene();
    }
    public static int x = 0;
    public static final String str = "第六届齐鲁软件设计大赛参赛作品  中国石油大学NewWorld团队    ";

    public static void drawLogo(Graphics g, int pX, int pY, int beilv) {
        if (x / beilv >= str.length() - 7) {
            x = 0;
        }
        g.setColor(0xffffff);
        g.drawString(str.substring(x / beilv, (x / beilv) + 7), pX, pY, Graphics.TOP | Graphics.LEFT);
        x += 1;
    }
}
