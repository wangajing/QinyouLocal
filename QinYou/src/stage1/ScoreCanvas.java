package stage1;




import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *显示成绩界面中要显示如下的内容：
 * 1、游戏名字（每一个实例是一样）
 * 2、游戏关卡名字
 * 3、背景（一样）
 * 4、游戏成绩（不同的游戏成绩记录方式有所不同）
 * 但基本包括：
 * 1、项目 数据， 这样就可以满足不同的用户的需求了！
 * @author Administrator
 */
public class ScoreCanvas extends GameCanvas implements Runnable {
    //屏幕的宽和高的常量
    public final int CANVAS_WIDTH = 176;
    public final int CANVAS_HEIGHT = 220;
    //每一帧所需要的时间，单位为毫秒
    public static int TIME_PER_FRAME = 100;
    //画笔
    private Graphics g = this.getGraphics();
    //游戏开始的标志
    private boolean isStart = true;
    
    private Image backgroundImage;
    private Image gameNameImage;
    public ScoreCanvas() {
        super(false);//修改为true将会仅支持getKeyState的键盘状态查询
        //函数，而不再支持keyPressed等覆盖函数，这里为false以支持keyPressed函数
        
        this.setFullScreenMode(true);//设置全屏模式
        //@todo:在这里初始化您的其他戏数据
        
        init();
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
        g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        g.setColor(0x0);//将画笔恢复为黑色
       //@todo:添加屏幕的绘制代码
        //测试代码：
        g.drawString("hello,world", 50, 100, Graphics.TOP|Graphics.LEFT);
        
        
        flushGraphics();//不要删除此代码
        
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
                render(g);
                Thread.sleep(TIME_PER_FRAME - System.currentTimeMillis() + currentTime);
            } catch (Exception ex) {
                ex.printStackTrace();
                //("线程运行时异常！");
            }
        }
    }
}
