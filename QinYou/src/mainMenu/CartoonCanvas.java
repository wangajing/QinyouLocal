package mainMenu;

import java.io.IOException;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import res.MusicControl;

/**
 *
 * @author 版权：梁春晓  2008年7月19日
 */
public class CartoonCanvas extends GameCanvas implements Runnable {
    //屏幕的宽和高的常量
    public int CANVAS_WIDTH;
    public int CANVAS_HEIGHT;
    //每一帧所需要的时间，单位为毫秒
    public static int TIME_PER_FRAME = 100;
    //画笔
    private Graphics g = this.getGraphics();
    //游戏开始的标志
    //   private int offset_y=NumberRandom.nextInt(30);
    public boolean isStart = true;
    //////////////////////
    private Image imageWar = null;
    private Image imageWarFirst = null;
    private Image imageWarSecond = null;
    private Image imageWarDialog = null;
    ////////////////////////
    private Image imageGirl = null;
    private Image imageIntroduce = null;
    private Image imageName = null;
    private Image imageLoveName = null;
    private Image imageAge = null;
    private Image imageInterest = null;
    private Image imageFirstLeaf = null;
    private Image imageSecondLeaf = null;
    ////////////////////
    private Image imageSpaceTime = null;
    ////////////////////
    private Image imageGirlSaid = null;
    private Image imageGeneralSaid = null;  //将军
    private Image imageGirlDialog = null;
    private Image imageDialogBox = null;
    ////////////////////
    private final int wordPos_x = 25;
    private final int wordPos_y = 130;
    private int warImagePosFirst_x = 0;
    private int warImagePosSecond_x = 0;
    private int warImagePos_y = 25;
    //////////////////////
    private final static int GIRL_PICTURE_INDEX = 0;
    private final static int PLACE_PICTURE_INDEX = 1;
    private static int DIALOG1_PICTURE_INDEX = 2;
    private static int DIALOG2_PICTURE_INDEX = 3;
    private static int DIALOG3_PICTURE_INDEX = 4;
    private static int DIALOG4_PICTURE_INDEX = 5;
    public int WAR_PICTURE_INDEX = 10;
    public int pictureIndex = 0; //0 represents 
    private int curentWordY = 200;
    public MainMidlet midlet;
    private int grilFontHeight = 18;
    private int girlFontWidth = 73;

    //////////////////////////////
    private int girlNamePos_X = -20;
    private int girlNamePos_Y = -20;
    private int girlLoveNamePos_X = 176;
    private int girlLoveNamePos_Y = 140;
    private int girlAgePos_X = 10;
    private int girlAgePos_Y = 220;
    private int girlInterestPos_X = 0;
    private int girlInterestPos_Y = 190;
    private int firstLeafPos_X = 100;
    private int firstLeafPos_Y = -70;
    private int secondLeafPos_X = 150;
    private int secondLeafPos_Y = -40;
    private int grilCurrentWordPos_X = 200;
    private int grilCurrentWordPos_Y = 190;
    private MusicControl musicControl;
    private MusicControl fxControl;

    
    private int temp = 0;
    public CartoonCanvas(MainMidlet midlet) {
        super(false);   //@todo:在这里初始化您的其他戏数据
        this.midlet = midlet;
        try {
            //修改为true将会仅支持getKeyState的键盘状态查询
            //函数，而不再支持keyPressed等覆盖函数，这里为false以支持keyPressed函数
            this.setFullScreenMode(true); //设置全屏模式
            //@todo:在这里初始化您的其他戏数据
            if (midlet.needMusic) {
                musicControl = new MusicControl(true);
                fxControl = new MusicControl(false);
                fxControl.changeMusic("/res/s6.wav");
                musicControl.changeMusic("/res/cart.mid");
                musicControl.musicStart();
                musicControl.changeMusicVolume(midlet.musicVolume);
            }
            CANVAS_WIDTH = getWidth();
            CANVAS_HEIGHT = getHeight();
            init();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void init() throws IOException {
    }

    private void keyPressProcess() {
        int keyState = this.getKeyStates();
        if ((keyState & FIRE_PRESSED) != 0) {
            if (fxControl != null) {
                fxControl.musicStart();
            }
            if (pictureIndex == GIRL_PICTURE_INDEX) {
                pictureIndex = PLACE_PICTURE_INDEX;
            } else if (pictureIndex == PLACE_PICTURE_INDEX) {
                pictureIndex = DIALOG1_PICTURE_INDEX;
            } else if (pictureIndex == DIALOG1_PICTURE_INDEX) {
                pictureIndex = DIALOG2_PICTURE_INDEX;
            } else if (pictureIndex == DIALOG2_PICTURE_INDEX) {
                pictureIndex = DIALOG3_PICTURE_INDEX;
            } else if (pictureIndex == DIALOG3_PICTURE_INDEX) {
                pictureIndex = DIALOG4_PICTURE_INDEX;
            } else if (pictureIndex == DIALOG4_PICTURE_INDEX) {
                pictureIndex = WAR_PICTURE_INDEX;
            } else if (pictureIndex == WAR_PICTURE_INDEX) {
                this.isStart = false;
                  if (midlet.needMusic) {
                    this.musicControl.musicStop();
                    this.fxControl.musicStop();
                    musicControl = null;
                    fxControl = null;
                    System.gc();
                }
                midlet.mainMenuShow(7);
           }
        }
    }

     protected void keyPressed(int code) {
        if(code == -7) {
               isStart = false;
                if (midlet.needMusic) {
                    this.musicControl.musicStop();
                    this.fxControl.musicStop();
                    musicControl = null;
                    fxControl = null;
                    System.gc();
                }
                midlet.mainMenuShow(7);

                System.gc();
        }
    }
    
    public void logicProcess() {
        if (pictureIndex == WAR_PICTURE_INDEX) {
            
            if (curentWordY > 50) {
                curentWordY--;
            }
            if (warImagePosFirst_x != 344) {
                if (warImagePosFirst_x == 172) {
                    warImagePosSecond_x = 172;
                    if (warImagePosSecond_x != 344) {
                    } else {
                        warImagePosSecond_x = 172;
                    }
                }
                warImagePosFirst_x += 4;
                warImagePosSecond_x += 4;
            } else {
                warImagePosFirst_x = 0;
            }
        }
        if (pictureIndex == GIRL_PICTURE_INDEX) {
            if ((girlNamePos_X != 8) && (girlNamePos_Y != 120)) {
                girlNamePos_X += 2;
                girlNamePos_Y += 10;
            }
            if (girlLoveNamePos_X != 16) {
                girlLoveNamePos_X -= 10;
            }
            if (girlAgePos_Y != 170) {
                girlAgePos_Y -= 5;
            }
            if (girlInterestPos_X != 16) {
                girlInterestPos_X += 2;
            }
            if ((firstLeafPos_X != 0) && (firstLeafPos_Y != getHeight())) {
                firstLeafPos_X -= 5;
                firstLeafPos_Y += 5;
            } else {
                firstLeafPos_X = 100;
                firstLeafPos_Y = 0;
            }
            if (grilCurrentWordPos_X >= -600) {
                grilCurrentWordPos_X -= 4;
            } else {
                grilCurrentWordPos_X = getWidth();
                pictureIndex = PLACE_PICTURE_INDEX;
            }
        } 
        if(pictureIndex == PLACE_PICTURE_INDEX) {
              temp++;
             if(temp==5){
                 pictureIndex = DIALOG1_PICTURE_INDEX;
             }  
        } 
        if(pictureIndex ==DIALOG1_PICTURE_INDEX ) {
            temp++;
             if(temp==15){
                 pictureIndex = DIALOG2_PICTURE_INDEX;
             }  
        }
        if(pictureIndex ==DIALOG2_PICTURE_INDEX ) {
            temp++;
             if(temp==30){
                 pictureIndex = DIALOG3_PICTURE_INDEX;
             }  
        }
        if(pictureIndex ==DIALOG3_PICTURE_INDEX ) {
            temp++;
             if(temp==50){
                 pictureIndex = DIALOG4_PICTURE_INDEX;
             } 
        }
        if( pictureIndex == DIALOG4_PICTURE_INDEX) {
            temp++;
            if(temp == 70) {
                pictureIndex = WAR_PICTURE_INDEX;
            }
        }
    }

    public void render(Graphics g) {
        g.setColor(0x00000000);
        g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        g.setColor(0x0);//将画笔恢复为黑色
        //@todo:添加屏幕的绘制代码
        //测试代码：
        if (pictureIndex == GIRL_PICTURE_INDEX) {
            renderGril(g);
        } else if (pictureIndex == PLACE_PICTURE_INDEX) {
            renderDialog(g);
        } else if (pictureIndex == DIALOG1_PICTURE_INDEX) {
            renderGirlSaid1(g);
        } else if (pictureIndex == DIALOG2_PICTURE_INDEX) {
            renderGeneralSaid1(g);
        } else if (pictureIndex == DIALOG3_PICTURE_INDEX) {
            rendergirlSaid2(g);
        } else if (pictureIndex == DIALOG4_PICTURE_INDEX) {
            renderGeneralSaid2(g);
        } else if (pictureIndex == WAR_PICTURE_INDEX) {
            renderWarPicture(g);
        }
        System.gc();
        flushGraphics();
    }

    public void renderWarPicture(Graphics g) {
        if (imageWar == null) {
            try {
                imageWar = Image.createImage("/res/war.png");
                imageWarDialog = Image.createImage("/res/wardialog.png");
                imageWarFirst = Image.createImage(imageWar, 0, 0, 172, 100, Sprite.TRANS_NONE);
                imageWarSecond = Image.createImage(imageWar, 172, 0, 172, 100, Sprite.TRANS_NONE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        g.drawImage(imageWarDialog, 0, 140, Graphics.TOP | Graphics.LEFT);
    
        g.setColor(0x00);
        g.drawString(" 公元前228年,嬴政灭赵。", wordPos_x, curentWordY, Graphics.TOP | Graphics.LEFT);
        g.drawString("    获和氏璧，大悦，    ", wordPos_x, curentWordY + 15, Graphics.TOP | Graphics.LEFT);
        g.drawString("切分三块，交予三人保管，", wordPos_x, curentWordY + 30, Graphics.TOP | Graphics.LEFT);
        g.drawString("   传若凑齐和氏璧者，", wordPos_x, curentWordY + 45, Graphics.TOP | Graphics.LEFT);
        g.drawString("   便可自由穿梭时空！", wordPos_x, curentWordY + 60, Graphics.TOP | Graphics.LEFT);
        g.drawString("   若想获得和氏璧，", wordPos_x, curentWordY + 75, Graphics.TOP | Graphics.LEFT);
        g.drawString("须有机缘与智力才可拿到！", wordPos_x, curentWordY + 90, Graphics.TOP | Graphics.LEFT);
        g.drawString("      按FIRE键继续     ", wordPos_x, curentWordY + 130, Graphics.TOP | Graphics.LEFT);
        
        g.drawImage(imageWarFirst, warImagePosFirst_x - 172, warImagePos_y, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageWarSecond, warImagePosSecond_x - 344, warImagePos_y, Graphics.TOP | Graphics.LEFT);

        System.gc();
    }

    public void renderGril(Graphics g) {
        if (imageGirl == null) {
            try {
                imageGirl = Image.createImage("/res/girl.png");
                imageFirstLeaf = Image.createImage("/res/leaf.png");
                imageSecondLeaf = Image.createImage("/res/leaf.png");
                imageIntroduce = Image.createImage("/res/introduce.png");
                imageName = Image.createImage(imageIntroduce, 0, 0, girlFontWidth, grilFontHeight, Sprite.TRANS_NONE);
                imageLoveName = Image.createImage(imageIntroduce, 0, 18, girlFontWidth, grilFontHeight, Sprite.TRANS_NONE);
                imageAge = Image.createImage(imageIntroduce, 0, 36, girlFontWidth, grilFontHeight, Sprite.TRANS_NONE);
                imageInterest = Image.createImage(imageIntroduce, 0, 54, girlFontWidth, grilFontHeight, Sprite.TRANS_NONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        g.drawImage(imageGirl, 0, 0, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageName, girlNamePos_X, girlNamePos_Y - 20, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageLoveName, girlLoveNamePos_X, girlLoveNamePos_Y - 20, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageAge, girlAgePos_X, girlAgePos_Y - 20, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageInterest, girlInterestPos_X, girlInterestPos_Y - 20, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageFirstLeaf, firstLeafPos_X, firstLeafPos_Y - 20, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageSecondLeaf, secondLeafPos_X, secondLeafPos_Y - 20, Graphics.TOP | Graphics.LEFT);


        g.setColor(0x000000ff);
        g.drawString("雪儿是旅游爱好者，在参观秦陵时，不幸误入时空" +
                "回到了2000年前的秦朝，" +
                "幸运的是遇到了一位将军---", grilCurrentWordPos_X, grilCurrentWordPos_Y, Graphics.TOP | Graphics.LEFT);
        System.gc();
    }

    public void renderDialog(Graphics g) {
        try {
            imageGirlDialog = Image.createImage("/res/generalandgirl.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.drawImage(imageGirlDialog, 2, 8, Graphics.TOP | Graphics.LEFT);
        imageGirlDialog = null;
        System.gc();
    }

    public void renderGirlSaid1(Graphics g) {
        if (imageGirlDialog == null) {
            try {
                imageGirlDialog = Image.createImage("/res/generalandgirl.png");
                imageDialogBox = Image.createImage("/res/dialog1.png");
                imageGirlSaid = Image.createImage(imageDialogBox, 0, 0, 160, 80, Sprite.TRANS_NONE);
                imageGeneralSaid = Image.createImage(imageDialogBox, 160, 0, 160, 80, Sprite.TRANS_NONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        g.drawImage(imageGirlDialog, 2, 8, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageGirlSaid, 5, 130, Graphics.TOP | Graphics.LEFT);
        g.drawString("我怎么会在这里？", 20, 160, Graphics.TOP | Graphics.LEFT);
        g.drawString("你又怎么会这副打扮？", 20, 175, Graphics.TOP | Graphics.LEFT);
        System.gc();
    }

    public void renderGeneralSaid1(Graphics g) {
        if (imageGirlDialog == null) {
            try {
                imageGirlDialog = Image.createImage("/res/generalandgirl.png");
                imageDialogBox = Image.createImage("/res/dialog1.png");
                imageGirlSaid = Image.createImage(imageDialogBox, 0, 0, 160, 80, Sprite.TRANS_NONE);
                imageGeneralSaid = Image.createImage(imageDialogBox, 160, 0, 160, 80, Sprite.TRANS_NONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        g.drawImage(imageGirlDialog, 2, 8, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageGeneralSaid, 5, 130, Graphics.TOP | Graphics.LEFT);
        g.drawString("这里是秦国，你是何人？", 20, 160, Graphics.TOP | Graphics.LEFT);
        g.drawString("", 10, 175, Graphics.TOP | Graphics.LEFT);
    }

    public void rendergirlSaid2(Graphics g) {
        if (imageGirlDialog == null) {
            try {
                imageGirlDialog = Image.createImage("/res/generalandgirl.png");
                imageDialogBox = Image.createImage("/res/dialog1.png");
                imageGirlSaid = Image.createImage(imageDialogBox, 0, 0, 160, 80, Sprite.TRANS_NONE);
                imageGeneralSaid = Image.createImage(imageDialogBox, 160, 0, 160, 80, Sprite.TRANS_NONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        g.drawImage(imageGirlDialog, 2, 8, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageGirlSaid, 5, 130, Graphics.TOP | Graphics.LEFT);
        g.drawString("啊，我竟然回到了秦朝！", 20, 160, Graphics.TOP | Graphics.LEFT);
        g.drawString("我来自未来，想回去那", 20, 175, Graphics.TOP | Graphics.LEFT);
        g.drawString("怎样才能回到未来呢", 20, 190, Graphics.TOP | Graphics.LEFT);
    }

    public void renderGeneralSaid2(Graphics g) {
        if (imageGirlDialog == null) {
            try {
                imageGirlDialog = Image.createImage("/res/generalandgirl.png");
                imageDialogBox = Image.createImage("/res/dialog1.png");
                imageGirlSaid = Image.createImage(imageDialogBox, 0, 0, 140, 80, Sprite.TRANS_NONE);
                imageGeneralSaid = Image.createImage(imageDialogBox, 140, 0, 140, 80, Sprite.TRANS_NONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        g.drawImage(imageGirlDialog, 2, 8, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageGeneralSaid, 5, 130, Graphics.TOP | Graphics.LEFT);
        g.drawString("传说和氏璧有强大能量", 20, 160, Graphics.TOP | Graphics.LEFT);
        g.drawString("借助它可以自由穿梭时空", 20, 175, Graphics.TOP | Graphics.LEFT);
        g.drawString("若能获得它则可回到未来", 20, 190, Graphics.TOP | Graphics.LEFT);
    }

    public void run() {
        while (isStart) {
            try {
                long currentTime = System.currentTimeMillis();
                keyPressProcess();
                logicProcess();
                render(g);
                Thread.sleep(TIME_PER_FRAME);
            } catch (Exception ex) {
                ex.printStackTrace();
                //("线程运行时异常！");
            }
        }
    }
}
