package mainMenu;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class HighScore extends GameCanvas implements Runnable {
    //屏幕的宽和高的常量
    public final int CANVAS_WIDTH = getWidth();
    public final int CANVAS_HEIGHT = getHeight();
    //每一帧所需要的时间，单位为毫秒
    public static int TIME_PER_FRAME = 100;
    //画笔
    private Graphics g = this.getGraphics();
    //游戏开始的标志
    public boolean isStart = true;    //@todo:这里添加游戏其他的变量
    private MainMidlet midlet = null;
    private Image ImageBack = null;
    private String[] name;
    private int[] score;
    private String[] record = null;
    private String[] recordScore = null;
    private String[] flag;
    private DataStore dataStore;
    private MainMenuCanvas menu;
    boolean isRender = true;
    private static final int scale = 20;
    private int size;
    int indexFlag1 = 1;

    public HighScore(MainMidlet mainMidlet, DataStore dataStore) {
        super(false);//修改为true将会仅支持getKeyState的键盘状态查询
        //函数，而不再支持keyPressed等覆盖函数，这里为false以支持keyPressed函数
        this.setFullScreenMode(true);//设置全屏模式
        //@todo:在这里初始化您的其他戏数据

        this.midlet = mainMidlet;
        this.dataStore = dataStore;
        size = dataStore.getScoreRecordNumber();
        name = new String[size / 2];
        score = new int[size / 2];
        record = new String[5];
        recordScore = new String[5];
        int[] index = new int[size / 2];
        flag = new String[5];

        dataStore.readScoreRecord(score, name);
        for (int i = 0; i < size / 2; i++) {
            index[i] = i;
        }
        int change = 0;
        for (int j = 0; j < size / 2 - 1; j++) {
            for (int k = j + 1; k < size / 2; k++) {
                if (score[j] < score[k]) {
                    change = score[k];
                    score[k] = score[j];
                    score[j] = change;
                    change = index[k];
                    index[k] = index[j];
                    index[j] = change;
                }
            }
        }
        for (int i = 0; i < size / 2; i++) {
            //       record[i] = name[index[i]] + "                " + score[i];
            record[i] = name[index[i]];
            // recordScore[i] = " "+score[i];
            recordScore[i] = " " + score[i];
        }
        for (int i = size / 2; i <= 4; i++) {
            //      record[i] = "                          " + "*****" + "                  " + "*****";
            record[i] = "*****" + "                  " + "*****";
        }
    }

    /**
     * 该函数用于初始化游戏数据，建议所有的初始化都在这里写
     */
    private void init() {
    }

    /**
     * 按键处理函数，用于按键后的逻辑处理
     */
    private void keyPressProcess() {
        int keyState = this.getKeyStates();
        if ((keyState & UP_PRESSED) != 0) {
            //@todo:当按方向键上时的逻辑处理
        } else if ((keyState & DOWN_PRESSED) != 0) {
            //@todo:当按方向键下时的逻辑处理
        } else if ((keyState & LEFT_PRESSED) != 0) {
            //@todo:当按方向键左时的逻辑处理
        } else if ((keyState & RIGHT_PRESSED) != 0) {
            //@todo:当按方向键右时的逻辑处理
        } else if ((keyState & FIRE_PRESSED) != 0) {
            //@todo:当按方向键下时的逻辑处理
        }
    }

    /**
     * 处理游戏逻辑
     */
    public void logicProcess() {
        //@todo:这里写下游戏的逻辑代码
    }

    /**
     * 整个屏幕的渲染函数
     */
    private void render(Graphics g) {
        g.setColor(0xffffff);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(0x0);//将画笔恢复为黑色
        renderScore(g);
        flushGraphics();//不要删除此代码 
    }

    private void renderScore(Graphics g) {
        System.gc();
        if (isRender) {
            g.setColor(0x000000);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(0x0);//将画笔恢复为黑色
            //@todo:添加屏幕的绘制代码
            //测试代码：
            try {
                ImageBack = Image.createImage("/res/hero.png");
            } catch (Exception e) {
                e.printStackTrace();
            }
            g.drawImage(ImageBack, 0, 0, Graphics.TOP | Graphics.LEFT);
            g.setColor(0x0);
            //     g.drawString("英       雄       榜", 88, 40, Graphics.TOP | Graphics.HCENTER);
            String title = " 姓    名" + "                " + " 分    数";
            g.drawString(title, getWidth() / 2, 65, Graphics.TOP | Graphics.HCENTER);
            int fontH = Font.getDefaultFont().getHeight();
            for (int i = 0; i < record.length; i++) {
                flag[i] = " *****" + "                  " + "*****";
                if (record[i] == null || recordScore[i] == null) {
                    g.drawString(flag[i], getWidth() / 2, 85 + i * scale, Graphics.TOP | Graphics.HCENTER);
                } else {
                    g.drawString(record[i], 37, 85 + i * scale, Graphics.LEFT | Graphics.TOP);
                    g.drawString(recordScore[i], 115, 85 + i * scale, Graphics.LEFT | Graphics.TOP);
                }
            }
            g.setColor(255, 0, 0);
            g.drawString("返回", 5, 210, Graphics.BASELINE | Graphics.LEFT);
            //      g.drawString("清空", 148, 210, Graphics.BASELINE | Graphics.LEFT);
            flushGraphics();//不要删除此代码
        }
        System.gc();
    }

//    private void renderClear(Graphics g) {
//        g.setColor(0xffffff);
//        g.fillRect(0, 0, getWidth(), getHeight());
//        g.setColor(0x0);//将画笔恢复为黑色
//        
//        try {
//            ImageBack = Image.createImage("/res/hero.png");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        g.drawImage(ImageBack, 20, 20, Graphics.TOP|Graphics.LEFT);
//        
//        g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
//        g.setColor(0, 0, 255);
//    //    g.drawString("英       雄       榜", 88, 40, Graphics.TOP | Graphics.HCENTER);
//        String title = "姓    名" + "                " + "  分    数";
//        g.drawString(title, getWidth() / 2, 65, Graphics.TOP | Graphics.HCENTER);
//        for (int i = 0; i < 5; i++) {
//            flag[i] = " *****" + "                  " + "*****";
//            g.drawString(flag[i], getWidth() / 2, 85 + i * scale, Graphics.TOP | Graphics.HCENTER);
//        }
//        g.setColor(255, 0, 0);
//        //("仓库的大小为" + size);
//        g.drawString("返回", 5, 210, Graphics.BASELINE | Graphics.LEFT);
//        g.drawString("清空", 148, 210, Graphics.BASELINE | Graphics.LEFT);
//
//        flushGraphics();//不要删除此代码
//
//    }
    public void keyPressed(int keyCode) {
        if (keyCode == -6) {
            //    this.isRender = true;
            this.isStart = false;
            midlet.mainMenuShow(1);
//        } else if (keyCode == -7) {
//            if(dataStore !=null) {
////            dataStore.deleteRecordStore();
////            midlet.HighScore();
//     //       this.isRender = false;
//    //        this.renderClear(g);
//            } else  {
//                return ;
//            }
        }
    }

    /**
     * 线程的run函数，这里完成游戏屏幕的循环
     */
    public void run() {
        while (isStart) {
            try {
                long currentTime = System.currentTimeMillis();
                keyPressProcess();
                logicProcess();
                renderScore(g);
                Thread.sleep(TIME_PER_FRAME);
            } catch (Exception ex) {
                ex.printStackTrace();
            //("线程运行时异常！");
            }
        }
    }
}
