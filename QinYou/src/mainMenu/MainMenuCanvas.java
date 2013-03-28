package mainMenu;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import res.MusicControl;

/**
 *
 * @author 版权 梁春晓  2008年7月18日
 */
public class MainMenuCanvas extends GameCanvas implements Runnable {
    //屏幕的宽和高的常量
    public int CANVAS_WIDTH;
    public int CANVAS_HEIGHT;
    //每一帧所需要的时间，单位为毫秒
    public static int TIME_PER_FRAME = 100;
    //画笔
    private Graphics g = this.getGraphics();
    //游戏开始的标志
    public boolean isStart = true;
    private Image imageLogo = null;
    private Image imageBackGround = null;
    private Image imageAllFont = null;
    private Image imageFlag = null;
    private Image imageStart = null;
    private Image imageGameSet = null;
    private Image imageScore = null;
    private Image imageAbout = null;
    private Image imageHelp = null;
    private Image imageCancel = null;
    /////MainMenuImage
    private Image imageStoryMode = null;
    private Image imageNetMode = null;
    ////FirstMenuImage
    private Image imageMaze = null;
    private Image imageHorse = null;
    private Image imageFly = null;
    ////SecondMenuImage
    private Image imageClient = null;
    private Image imageServer = null;
    private Image imageReturn = null;
    /////////////////////
    private final int Fontwidth = 81;
    private final int Fontheight = 15;
    private final int fontPos_x = 50;
    private final int fontPos_y = 55;
    private final int temp_value = 25;
    private int offset_x = 18;
    public int offset_y = 55;
    //  private final int flagLength = 142;
    private final static int LOGO_MENU_INDEX = 0;
    public final static int START_ITEM_INDEX = 0;
    public final static int START_MENU_INDEX = 1;
    public final static int GAME_SET_MENU_INDEX = 2;
    public final static int SCORE_MENU_INDEX = 3;
    public final static int ABOUT_MENU_INDEX = 4;
    public final static int HELP_MENU_INDEX = 5;
    private final static int STORY_ITEM_INDEX = 0;
    private final static int STORY_MENU_INDEX = 6;
    private int MAZE__ITEM_INDEX = 0;
    public final static int MAZE_MENU_INDEX = 7;
    private final static int SERVER_MENU_INDEX = 8;
    private final static int SERVER_MAZE_MENU_INDEX = 9;
    private final static int CLIENT_MAZE_MENU_INDEX = 10;
    private final static int SERVER_ITEM_INDEX = 0;    //@todo:这里添加游戏其他的变量
    private int helpItemIndex = 0;
    public int menuItemIndex = 0;
    public int menuIndex = 0;//0 represents mainMenu  ,and 1 represents startMenu;
    public int currentFlagY = offset_y;
    public int colorFlagY = offset_y;
    private int keyDirection;
    public MainMidlet midlet;
    CartoonCanvas cartoon;
    Thread thread;
    public MusicControl musicControl;
    private MusicControl fxControl;
    private int whiteFlagY = 60;        //LOGO的坐标
    /////////////////////////////
    GameSet gameSet;             //游戏设置
    HighScore highScore;     //显示记分榜

    public MainMenuCanvas(MainMidlet midlet, int index) {
        super(false);//修改为true将会仅支持getKeyState的键盘状态查询
        //函数，而不再支持keyPressed等覆盖函数，这里为false以支持keyPressed函数
        this.midlet = midlet;
        this.menuIndex = index;

        //@todo:在这里初始化您的其他戏数据
        this.setFullScreenMode(true); //设置全屏模式
        try {
            //修改为true将会仅支持getKeyState的键盘状态查询
            //函数，而不再支持keyPressed等覆盖函数，这里为false以支持keyPressed函数
            //@todo:在这里初始化您的其他戏数据
            imageFlag = Image.createImage("/res/jiantou.png");
            imageBackGround = Image.createImage("/res/menu.png");
            CANVAS_WIDTH = getWidth();
            CANVAS_HEIGHT = getHeight();
            thread = new Thread(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (midlet.needMusic) {
            turnOnOrOffMusic();
        }
    }
    /**
     * 该函数用于初始化游戏数据，建议所有的初始化都在这里写
     */
    long lastTime = System.currentTimeMillis();

    /**
     * 按键处理函数，用于按键后的逻辑处理
     */
    private void keyPressProcess() {


        int keyState = this.getKeyStates();
        if (System.currentTimeMillis() - lastTime < 100) {
            return;
        } else {
            lastTime = System.currentTimeMillis();
        }
        if (menuIndex == 0) {
            return;
        }
        if ((keyState & UP_PRESSED) != 0) {
            //@todo:当按方向键上时的逻辑处理
            playFXSound(0);
            if (menuIndex == STORY_MENU_INDEX) {
                menuItemIndex--;
                if (menuItemIndex < 0) {
                    menuItemIndex = 2;
                }
            } else if (menuIndex == START_MENU_INDEX) {
                menuItemIndex--;
                if (menuItemIndex < 0) {
                    menuItemIndex = 5;
                }
            } else if (menuIndex == HELP_MENU_INDEX) {
                helpItemIndex--;
                if (helpItemIndex < 0) {
                    helpItemIndex = 0;
                }
            } else if (menuIndex == MAZE_MENU_INDEX) {

                menuItemIndex--;
                if (menuItemIndex < 0) {
                    menuItemIndex = 3;
                }
            } else if (menuIndex == SERVER_MENU_INDEX) {
                menuItemIndex--;
                if (menuItemIndex < 0) {
                    menuItemIndex = 2;
                }
            } else if (menuIndex == SERVER_MAZE_MENU_INDEX) {
                menuItemIndex--;
                if (menuItemIndex < 0) {
                    menuItemIndex = 2;
                }
            } else if (menuIndex == CLIENT_MAZE_MENU_INDEX) {
                menuItemIndex--;
                if (menuItemIndex < 0) {
                    menuItemIndex = 2;
                }
            }

            keyDirection = UP;
        } else if ((keyState & DOWN_PRESSED) != 0) {
            playFXSound(0);
            if (menuIndex == START_MENU_INDEX) {
                menuItemIndex++;
                if (menuItemIndex > 5) {
                    menuItemIndex = 0;
                }
            } else if (menuIndex == HELP_MENU_INDEX) {
                helpItemIndex++;
                if (helpItemIndex > 2) {
                    helpItemIndex = 0;
                }
            } else if (menuIndex == STORY_MENU_INDEX) {
                menuItemIndex++;
                if (menuItemIndex > 2) {
                    menuItemIndex = 0;
                }
            } else if (menuIndex == MAZE_MENU_INDEX) {
                menuItemIndex++;
                if (menuItemIndex > 3) {
                    menuItemIndex = 0;
                }
            } else if (menuIndex == SERVER_MENU_INDEX) {
                menuItemIndex++;
                if (menuItemIndex > 2) {
                    menuItemIndex = 0;
                }
            } else if (menuIndex == SERVER_MAZE_MENU_INDEX) {
                menuItemIndex++;
                if (menuItemIndex > 2) {
                    menuItemIndex = 0;
                }
            } else if (menuIndex == CLIENT_MAZE_MENU_INDEX) {
                menuItemIndex++;
                if (menuItemIndex > 2) {
                    menuItemIndex = 0;
                }
            }
            keyDirection = DOWN;
        } else if ((keyState & LEFT_PRESSED) != 0) {
            //@todo:当按方向键左时的逻辑处理
        } else if ((keyState & RIGHT_PRESSED) != 0) {
            //@todo:当按方向键右时的逻辑处理
        } else if ((keyState & FIRE_PRESSED) != 0) {
            playFXSound(1);
            //@todo:当按方向键下时的逻辑处理

            if (menuIndex == START_MENU_INDEX) {
                if (menuItemIndex == 0) {
                    menuIndex = STORY_MENU_INDEX;
                    currentFlagY = offset_y;
                }
                if (menuItemIndex == 1) {
                    this.isStart = false;
                    midlet.gameSetShow();
                }
                if (menuItemIndex == 2) {
                    this.isStart = false;
                    midlet.highScoreShow();
                    System.gc();
                }
                if (menuItemIndex == 3) {
                    menuIndex = HELP_MENU_INDEX;
                }
                if (menuItemIndex == 4) {
                    menuIndex = ABOUT_MENU_INDEX;
                }
                if (menuItemIndex == 5) {
                    this.musicStop();
                    System.gc();
                    midlet.notifyDestroyed();
                }
            } else if (menuIndex == STORY_MENU_INDEX) {
                if (menuItemIndex == 0) {
                    System.gc();
                    musicStop();
                    this.isStart = false;
                    midlet.mainCartoonShow();
                }
                if (menuItemIndex == 1) {
                    menuIndex = SERVER_MENU_INDEX;
                    menuItemIndex = 0;
                    currentFlagY = offset_y;
                }
                if (menuItemIndex == 2) {
//                    midlet.mainMenuShow(0);
                    menuIndex = START_MENU_INDEX;
                    menuItemIndex = 0;
                    currentFlagY = offset_y;
                    System.gc();
                }
            } else if (menuIndex == MAZE_MENU_INDEX) {
                if (menuItemIndex == 0) {
                    this.musicStop();
                    this.isStart = false;
                    midlet.fisrtStageAnimationShow(0);
                }
                if (menuItemIndex == 1) {
                    this.musicStop();
                    this.isStart = false;
                    midlet.stage2AnimationShow(0);
                }
                if (menuItemIndex == 2) {
                    this.musicStop();
                    this.isStart = false;
                    midlet.stage3AnimationShow(0);
//                    midlet.stage3GameShow();
                }
                if (menuItemIndex == 3) {
                    menuIndex = STORY_MENU_INDEX;
                    menuItemIndex = STORY_ITEM_INDEX;
                    currentFlagY = offset_y;
                }
            } else if (menuIndex == SERVER_MENU_INDEX) {
                if (menuItemIndex == 0) {
                    menuIndex = SERVER_MAZE_MENU_INDEX;
                    menuItemIndex = 0;
                    currentFlagY = offset_y;
                }
                if (menuItemIndex == 1) {
                    menuIndex = CLIENT_MAZE_MENU_INDEX;
                    menuItemIndex = 0;
                    currentFlagY = offset_y;

                }
                if (menuItemIndex == 2) {
                    menuIndex = STORY_MENU_INDEX;
                    menuItemIndex = STORY_ITEM_INDEX;
                    currentFlagY = offset_y;
                }
            } else if (menuIndex == SERVER_MAZE_MENU_INDEX) {
                if (menuItemIndex == 0) {

                    musicStop();
                    this.isStart = false;
                    System.gc();
                    midlet.btServerMazeGameShow();

                }
                if (menuItemIndex == 1) {
                    System.gc();
                    musicStop();
                    this.isStart = false;
                    midlet.btServerHorseRaceGameShow();
                }
                if (menuItemIndex == 2) {
                    menuIndex = SERVER_MENU_INDEX;
                    menuItemIndex = 0;
                    currentFlagY = offset_y;
                }
            } else if (menuIndex == CLIENT_MAZE_MENU_INDEX) {
                if (menuItemIndex == 0) {
                    System.gc();
                    musicStop();
                    this.isStart = false;
                    midlet.btClientMazeGameShow();
                }
                if (menuItemIndex == 1) {
                    System.gc();
                    musicStop();
                    this.isStart = false;
                    midlet.btClientHorseRaceGameShow();
                }
                if (menuItemIndex == 2) {
                    menuIndex = SERVER_MENU_INDEX;
                    menuItemIndex = 0;
                    currentFlagY = offset_y;
                }
            }
        }
    }

    /**
     * 处理游戏逻辑
     */
    public void logicProcess() throws IOException {
        //@todo:这里写下游戏的逻辑代码
        //     logicMusic();
        if (menuIndex == 0) {
            if (whiteFlagY != 240) {
                whiteFlagY += 4;
            } else {
                menuIndex = START_MENU_INDEX;
            }
        }
        if (menuIndex == START_MENU_INDEX) {
            if (keyDirection == DOWN) {
                if (menuItemIndex == 0) {
                    currentFlagY = 55;
                    colorFlagY = currentFlagY;
                }
                if (currentFlagY != offset_y + menuItemIndex * temp_value) {
                    currentFlagY += 5;
                } else {
                    colorFlagY = currentFlagY;
                }
            } else if (keyDirection == UP) {
                if (menuItemIndex == 5) {
                    currentFlagY = 185;
                    colorFlagY = 182;
                }
                if (currentFlagY != offset_y + menuItemIndex * temp_value) {
                    currentFlagY -= 5;
                } else {
                    colorFlagY = currentFlagY;
                }
            }
        }
        if (menuIndex == STORY_MENU_INDEX) {
            if (keyDirection == DOWN) {
                if (menuItemIndex == 0) {
                    currentFlagY = 55;
                    colorFlagY = currentFlagY;
                }
                if (currentFlagY != offset_y + 2 * menuItemIndex * temp_value) {
                    currentFlagY += 10;
                } else {
                    colorFlagY = currentFlagY;
                }
            } else if (keyDirection == UP) {
                if (menuItemIndex == 2) {
                    currentFlagY = 155;
                    colorFlagY = currentFlagY;
                }
                if (currentFlagY != offset_y + 2 * menuItemIndex * temp_value) {
                    currentFlagY -= 10;
                } else {
                    colorFlagY = currentFlagY;
                }
            }
        }
        if (menuIndex == MAZE_MENU_INDEX) {
            if (keyDirection == DOWN) {
                if (menuItemIndex == 0) {
                    currentFlagY = 55;
                    colorFlagY = currentFlagY;
                }
                if (currentFlagY != offset_y + menuItemIndex * temp_value) {
                    currentFlagY += 5;
                } else {
                    colorFlagY = currentFlagY;
                }
            } else if (keyDirection == UP) {
                if (menuItemIndex == 3) {
                    currentFlagY = 130;
                }
                if (currentFlagY != offset_y + menuItemIndex * temp_value) {
                    currentFlagY -= 5;
                } else {
                    colorFlagY = currentFlagY;
                }
            }
        }
        if (menuIndex == SERVER_MENU_INDEX) {
            if (keyDirection == DOWN) {
                if (menuItemIndex == 0) {
                    currentFlagY = 55;
                    colorFlagY = currentFlagY;
                }
                if (currentFlagY != offset_y + 2 * menuItemIndex * temp_value) {
                    currentFlagY += 10;
                } else {
                    colorFlagY = currentFlagY;
                }
            } else if (keyDirection == UP) {
                if (menuItemIndex == 2) {
                    currentFlagY = 155;
                    colorFlagY = currentFlagY;
                }
                if (currentFlagY != offset_y + 2 * menuItemIndex * temp_value) {
                    currentFlagY -= 10;
                } else {
                    colorFlagY = currentFlagY;
                }
            }
        }
        if (menuIndex == SERVER_MAZE_MENU_INDEX) {
            if (keyDirection == DOWN) {
                if (menuItemIndex == 0) {
                    currentFlagY = 55;
                    colorFlagY = currentFlagY;
                }
                if (currentFlagY != offset_y + 2 * menuItemIndex * temp_value) {
                    currentFlagY += 5;
                } else {
                    colorFlagY = currentFlagY;
                }
            } else if (keyDirection == UP) {
                if (menuItemIndex == 2) {
                    currentFlagY = 155;
                    colorFlagY = currentFlagY;
                }
                if (currentFlagY != offset_y + 2 * menuItemIndex * temp_value) {
                    currentFlagY -= 5;
                } else {
                    colorFlagY = currentFlagY;
                }
            }
        }
        if (menuIndex == CLIENT_MAZE_MENU_INDEX) {
            if (keyDirection == DOWN) {
                if (menuItemIndex == 0) {
                    currentFlagY = 55;
                    colorFlagY = currentFlagY;
                }
                if (currentFlagY != offset_y + 2 * menuItemIndex * temp_value) {
                    currentFlagY += 5;
                } else {
                    colorFlagY = currentFlagY;
                }
            } else if (keyDirection == UP) {
                if (menuItemIndex == 2) {
                    currentFlagY = 155;
                    colorFlagY = currentFlagY;
                }
                if (currentFlagY != offset_y + 2 * menuItemIndex * temp_value) {
                    currentFlagY -= 5;
                } else {
                    colorFlagY = currentFlagY;
                }
            }
        }
    }

    public void renderMainMenu(Graphics g) {
        try {
            if (imageStart == null) {
                imageAllFont = Image.createImage("/res/menuText.png");
                imageStart = Image.createImage(imageAllFont, 0, 0, Fontwidth, Fontheight, Sprite.TRANS_NONE);
                imageGameSet = Image.createImage(imageAllFont, 0, 75, Fontwidth, Fontheight, Sprite.TRANS_NONE);
                imageScore = Image.createImage(imageAllFont, 0, 225, Fontwidth, Fontheight, Sprite.TRANS_NONE);
                imageHelp = Image.createImage(imageAllFont, 0, 60, Fontwidth, Fontheight, Sprite.TRANS_NONE);
                imageAbout = Image.createImage(imageAllFont, 0, 30, Fontwidth, Fontheight, Sprite.TRANS_NONE);
                imageCancel = Image.createImage(imageAllFont, 0, 45, Fontwidth, Fontheight, Sprite.TRANS_NONE);
            }
            imageAllFont = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        g.drawImage(imageBackGround, 0, 0, Graphics.TOP | Graphics.LEFT);
        if (colorFlagY != 182) {
            g.setColor(0x000fff00);
            g.fillRect(54, colorFlagY - 10, 72, 13);
        } else {
            g.setColor(0xfff00000);
            g.fillRect(54, colorFlagY - 10, 72, 13);
        }
        g.drawImage(imageFlag, offset_x, currentFlagY - 10, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageStart, fontPos_x, fontPos_y - 10, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageGameSet, fontPos_x, fontPos_y - 10 + temp_value, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageScore, fontPos_x, fontPos_y - 10 + 2 * temp_value, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageHelp, fontPos_x, fontPos_y - 10 + 3 * temp_value, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageAbout, fontPos_x, fontPos_y - 10 + 4 * temp_value, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageCancel, fontPos_x, fontPos_y - 10 + 5 * temp_value, Graphics.TOP | Graphics.LEFT);
    }

    public void renderStoryMenu(Graphics g) {
        System.gc();
        if (imageStoryMode == null) {
            try {
                imageAllFont = Image.createImage("/res/menuText.png");
                imageStoryMode = Image.createImage(imageAllFont, 0, 90, Fontwidth, Fontheight, Sprite.TRANS_NONE);
                imageNetMode = Image.createImage(imageAllFont, 0, 105, Fontwidth, Fontheight, Sprite.TRANS_NONE);
                imageReturn = Image.createImage(imageAllFont, 0, 195, Fontwidth, Fontheight, Sprite.TRANS_NONE);
                imageAllFont = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        g.drawImage(imageBackGround, 0, 0, Graphics.TOP | Graphics.LEFT);
        if (colorFlagY != 155) {
            g.setColor(0xf00ff00f);
            g.fillRect(54, colorFlagY, 72, 13);
        } else {
            g.setColor(0xfff00000);
            g.fillRect(70, colorFlagY, 40, 13);
        }
        g.drawImage(imageFlag, offset_x, currentFlagY, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageStoryMode, fontPos_x, fontPos_y, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageNetMode, fontPos_x, fontPos_y + 2 * temp_value, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageReturn, fontPos_x, fontPos_y + 4 * temp_value, Graphics.TOP | Graphics.LEFT);
        System.gc();

    }

    public void renderMazeGameMenu(Graphics g) {
        if (imageMaze == null) {
            try {
                imageAllFont = Image.createImage("/res/menuText.png");
                imageMaze = Image.createImage(imageAllFont, 0, 150, Fontwidth, Fontheight, Sprite.TRANS_NONE);
                imageHorse = Image.createImage(imageAllFont, 0, 165, Fontwidth, Fontheight, Sprite.TRANS_NONE);
                imageFly = Image.createImage(imageAllFont, 0, 180, Fontwidth, Fontheight, Sprite.TRANS_NONE);
                imageReturn = Image.createImage(imageAllFont, 0, 195, Fontwidth, Fontheight, Sprite.TRANS_NONE);
                imageAllFont = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        g.drawImage(imageBackGround, 0, 0, Graphics.TOP | Graphics.LEFT);
        if (colorFlagY != 130) {
            g.setColor(0xf00ff00f);
            g.fillRect(54, colorFlagY, 72, 13);
        } else {
            g.setColor(0xfff00000);
            g.fillRect(70, colorFlagY, 40, 13);
        }
        g.drawImage(imageFlag, offset_x, currentFlagY, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageMaze, fontPos_x, fontPos_y, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageHorse, fontPos_x, fontPos_y + temp_value, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageFly, fontPos_x, fontPos_y + 2 * temp_value, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageReturn, fontPos_x, fontPos_y + 3 * temp_value, Graphics.TOP | Graphics.LEFT);
        System.gc();
    }

    public void renderServerMenu(Graphics g) {
        if (imageClient == null) {
            try {

                imageAllFont = Image.createImage("/res/menuText.png");
                imageClient = Image.createImage(imageAllFont, 0, 135, Fontwidth, Fontheight, Sprite.TRANS_NONE);
                imageServer = Image.createImage(imageAllFont, 0, 120, Fontwidth, Fontheight, Sprite.TRANS_NONE);
                imageReturn = Image.createImage(imageAllFont, 0, 195, Fontwidth, Fontheight, Sprite.TRANS_NONE);
                imageAllFont = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        g.drawImage(imageBackGround, 0, 0, Graphics.TOP | Graphics.LEFT);
        if (colorFlagY != 155) {
            g.setColor(0xf00ff00f);
            g.fillRect(60, colorFlagY, 56, 13);
        } else {
            g.setColor(0xfff00000);
            g.fillRect(70, colorFlagY, 40, 13);
        }
        g.drawImage(imageFlag, offset_x, currentFlagY, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageServer, fontPos_x, fontPos_y, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageClient, fontPos_x, fontPos_y + 2 * temp_value, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageReturn, fontPos_x, fontPos_y + 4 * temp_value, Graphics.TOP | Graphics.LEFT);
    }

    private void renderSeverMaze(Graphics g) throws IOException {
        g.setColor(0xffffff);
        g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        if (imageMaze == null) {
            imageAllFont = Image.createImage("/res/menuText.png");
            imageMaze = Image.createImage(imageAllFont, 0, 150, Fontwidth, Fontheight, Sprite.TRANS_NONE);
            imageFly = Image.createImage(imageAllFont, 0, 180, Fontwidth, Fontheight, Sprite.TRANS_NONE);
            imageReturn = Image.createImage(imageAllFont, 0, 195, Fontwidth, Fontheight, Sprite.TRANS_NONE);
            imageAllFont = null;
        }
        g.drawImage(imageBackGround, 0, 0, Graphics.TOP | Graphics.LEFT);
        if (colorFlagY != 155) {
            g.setColor(0xf00ff00f);
            g.fillRect(54, colorFlagY, 72, 13);
        } else {
            g.setColor(0xfff00000);
            g.fillRect(70, colorFlagY, 40, 13);
        }
        g.drawImage(imageFlag, offset_x, currentFlagY, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageMaze, fontPos_x, fontPos_y, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageFly, fontPos_x, fontPos_y + 2 * temp_value, Graphics.TOP | Graphics.LEFT);
        g.drawImage(imageReturn, fontPos_x, fontPos_y + 4 * temp_value, Graphics.TOP | Graphics.LEFT);
    }
    private Image copyrightImage;

    private void renderLogo(Graphics g) {
        g.setColor(0xffffff);
        g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        if (imageLogo == null) {
            try {
                imageLogo = Image.createImage("/res/logo.png");
                copyrightImage = Image.createImage("/res/copyright.png");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        g.drawImage(imageLogo, 10, 50, Graphics.TOP | Graphics.LEFT);
        g.setColor(0x444444);
        g.drawString("第六届齐鲁软件设计大赛", 88, 140, Graphics.TOP | Graphics.HCENTER);
        g.drawString("参赛作品", 88, 160, Graphics.TOP | Graphics.HCENTER);
        g.setColor(0xffffff);
        g.fillRect(0, whiteFlagY, 176, 200);
        g.drawImage(copyrightImage, this.getWidth() >> 1, 200, Graphics.HCENTER | Graphics.VCENTER);

    }

    private void renderHelp(Graphics g) {

        if (imageBackGround == null) {
            try {
                imageBackGround = Image.createImage("/res/menu.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        g.drawImage(imageBackGround, 0, 0, Graphics.TOP | Graphics.LEFT);
        byte y = 40;
        g.setColor(0x0000ff);
        if (helpItemIndex == 0) {
            g.drawString("故事模式", 50, y - 5, Graphics.TOP | Graphics.LEFT);
            g.drawString("八卦迷阵：在规定的500秒内", 10, y + 15, Graphics.TOP | Graphics.LEFT);
            g.drawString("找齐八件遗物，然后在60秒内", 10, y + 30, Graphics.TOP | Graphics.LEFT);
            g.drawString("返回原点", 10, y + 45, Graphics.TOP | Graphics.LEFT);
            g.drawString("神赐羽翼：打败每个关卡的", 10, y + 60, Graphics.TOP | Graphics.LEFT);
            g.drawString("BOSS即可过关。", 10, y + 75, Graphics.TOP | Graphics.LEFT);
            g.drawString("神赐骑术：躲避障碍物并利用", 10, y + 90, Graphics.TOP | Graphics.LEFT);
            g.drawString("道具，尽快追上强盗，并在规", 10, y + 105, Graphics.TOP | Graphics.LEFT);
            g.drawString("定时间内将其击毙。", 10, y + 120, Graphics.TOP | Graphics.LEFT);
        } else if (helpItemIndex == 1) {
            y = 25;
            g.drawString("多人模式", 50, y + 10, Graphics.TOP | Graphics.LEFT);
            g.drawString("    使用蓝牙，实现两人同时", 10, y + 30, Graphics.TOP | Graphics.LEFT);
            g.drawString("游戏.在八卦迷阵关卡中，游戏", 10, y + 45, Graphics.TOP | Graphics.LEFT);
            g.drawString("双方合作完成在规定时间内获", 10, y + 60, Graphics.TOP | Graphics.LEFT);
            g.drawString("得八件遗物。在神勇骑术关卡", 10, y + 75, Graphics.TOP | Graphics.LEFT);
            g.drawString("中，游戏双方竞技，谁先到达", 10, y + 90, Graphics.TOP | Graphics.LEFT);
            g.drawString("终点谁就获得胜利。游戏中应", 10, y + 105, Graphics.TOP | Graphics.LEFT);
            g.drawString("先打开服务器, 客户端加入游", 10, y + 120, Graphics.TOP | Graphics.LEFT);
            g.drawString("戏后方能进行。", 10, y + 135, Graphics.TOP | Graphics.LEFT);
        } else if (helpItemIndex == 2) {
            y = 10;
            g.drawString("按键帮助", 50, y + 25, Graphics.TOP | Graphics.LEFT);
            g.drawString("八卦迷阵：up键向前，down键", 10, y + 45, Graphics.TOP | Graphics.LEFT);
            g.drawString("向后，left键向左转，right键", 10, y + 60, Graphics.TOP | Graphics.LEFT);
            g.drawString("向右转。", 10, y + 75, Graphics.TOP | Graphics.LEFT);
            g.drawString("天赐羽翼：up键向前，down键", 10, y + 90, Graphics.TOP | Graphics.LEFT);
            g.drawString("向后，left键向左，right键", 10, y + 105, Graphics.TOP | Graphics.LEFT);
            g.drawString("向右。", 10, y + 120, Graphics.TOP | Graphics.LEFT);
            g.drawString("神勇骑术：up键跳，left键左", 10, y + 135, Graphics.TOP | Graphics.LEFT);
            g.drawString("移，right键右移，fire发射小", 10, y + 150, Graphics.TOP | Graphics.LEFT);
            g.drawString("刀。", 10, y + 165, Graphics.TOP | Graphics.LEFT);
        }
        g.setColor(0xff00000);
        g.drawString("返回", 5, 200, Graphics.TOP | Graphics.LEFT);
        g.setColor(0x0);
        try {
            Image image = Image.createImage("/res/upanddownflag.png");
            g.drawImage(image, 80, 195, Graphics.LEFT|Graphics.TOP);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        g.drawString("返回", 5, 200, Graphics.TOP | Graphics.LEFT);
        System.gc();
    }

    private void renderAbout(Graphics g) {

        if (imageBackGround == null) {
            try {
                imageBackGround = Image.createImage("/res/menu.png");

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        g.drawImage(imageBackGround, 0, 0, Graphics.TOP | Graphics.LEFT);
        g.setColor(0x0000ff);
        byte y = 60;
        g.drawString("中国石油大学NewWorld团队", 88, y, Graphics.TOP | Graphics.HCENTER);
        g.drawString("组长：王阿晶", 88, y + 20, Graphics.TOP | Graphics.HCENTER);
        g.drawString("程序：梁春晓", 88, y + 40, Graphics.TOP | Graphics.HCENTER);
        g.drawString("           申宝明", 88, y + 60, Graphics.TOP | Graphics.HCENTER);
        g.drawString("           李绍坤 ", 88, y + 80, Graphics.TOP | Graphics.HCENTER);
        g.drawString("美工：孙    杰", 88, y + 100, Graphics.TOP | Graphics.HCENTER);
        g.drawString("指导：李    咏", 88, y + 120, Graphics.TOP | Graphics.HCENTER);
        g.drawString("返回", 4, 200, Graphics.TOP | Graphics.LEFT);
        System.gc();
    }

    /**
     * 整个屏幕的渲染函数
     */
    private void render(Graphics g) throws IOException {
        g.setColor(0x000000);
        g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        g.setColor(0x0);//将画笔恢复为黑色
        //@todo:添加屏幕的绘制代码
        //测试代码：
        if (menuIndex == LOGO_MENU_INDEX) {
            renderLogo(g);
        } else if (menuIndex == START_MENU_INDEX) {
            renderMainMenu(g);
        } else if (menuIndex == HELP_MENU_INDEX) {
            renderHelp(g);
        } else if (menuIndex == ABOUT_MENU_INDEX) {
            renderAbout(g);
        } else if (menuIndex == STORY_MENU_INDEX) {
            renderStoryMenu(g);
        } else if (menuIndex == MAZE_MENU_INDEX) {
            renderMazeGameMenu(g);
        } else if (menuIndex == SERVER_MENU_INDEX) {
            renderServerMenu(g);
        } else if (menuIndex == SERVER_MAZE_MENU_INDEX) {
            renderSeverMaze(g);
        } else if (menuIndex == CLIENT_MAZE_MENU_INDEX) {
            renderSeverMaze(g);
        }
        flushGraphics();//不要删除此代码
        System.gc();
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
                Thread.sleep(TIME_PER_FRAME);
                System.gc();
            } catch (Exception ex) {
                ex.printStackTrace();
            //("线程运行时异常！");
            }
        }
    }

    public void keyPressed(int keycode) {
        if (keycode == -6) {
            playFXSound(1);
            if (menuIndex == HELP_MENU_INDEX) {
                menuIndex = START_MENU_INDEX;
            } else if (menuIndex == ABOUT_MENU_INDEX) {
                menuIndex = START_MENU_INDEX;
            }
        }
    }

    public void turnOnOrOffMusic() {
        if (midlet.needMusic == false) {
            musicControl.musicStop();
            musicControl = null;
            fxControl.musicStop();
            fxControl = null;
        } else {
            musicControl = new MusicControl(true);
            fxControl = new MusicControl(false);
            musicControl.changeMusic("/res/menubg.mid");
            musicControl.musicStart();
            musicControl.changeMusicVolume(midlet.musicVolume);
        }
    }

    public void changeMusicVolume(int i) {
        musicControl.changeMusicVolume(i);
    }

    private void playFXSound(int type) {
        if (midlet.needMusic == true && fxControl != null) {
            if (type == 0) {
                fxControl.changeMusic("/res/menuUpOrDown.wav");
            } else if (type == 1) {
                fxControl.changeMusic("/res/fire.wav");
            }
            fxControl.musicStart();
        }
    }

    private void musicStop() {
        if (fxControl != null) {
            fxControl.musicStop();
            fxControl = null;
        }
        if (musicControl != null) {
            musicControl.musicStop();
            musicControl = null;
        }
        System.gc();
    }
}

