package res;





import java.io.IOException;
import java.io.InputStream;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.TempoControl;
import javax.microedition.media.control.VolumeControl;

/**
 *
 * @author Administrator
 */
public class MusicControl {

    /**
     * InputStream流读取数据
     * p为创建的播放对象
     * currentPos为音乐的当前播放时间点
     * currentTempo为音乐的当前节拍数
     * currentLevel为音乐的当前等级 共有5个等级。从0到4逐渐增大，0为无声，4为最大
     */
    static final long SECS_TO_MICROSECS = 1000000L;
    private InputStream is;
    private Player p;
    private long currentPos;
    private int currentTempo;
    private int currentLevel;
    private boolean isLoop;

    public MusicControl(boolean isLoop) {
        this.isLoop = isLoop;
    }

    /**
     * 改变音乐,str为音乐的路径 
     */
    public void changeMusic(String str) {
        try {
            is = getClass().getResourceAsStream(str);
            if (str.endsWith("mid")) {
                p = Manager.createPlayer(is, "audio/midi");
            } else if (str.endsWith("wav")) {
                p = Manager.createPlayer(is, "audio/X-wav");
                
            }
            if (isLoop) {
                p.setLoopCount(-1);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 播放音乐,从头开始
     */
    public void musicStart() {
        try {
            p.start();
            
            p.setMediaTime(0 * SECS_TO_MICROSECS);
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 音乐停止播放
     */
    public void musicStop() {
        try {
            if (p != null) {
                p.stop();
                p.close();
                //(p.getState());
            }


        } catch (Exception ex) {
//            ex.printStackTrace();
        }
    }

    /**
     * 获取当前音乐播放时间点
     */
    public void musicGetPos() {
        currentPos = p.getMediaTime();
    }

    /**
     * 音乐暂停播放
     */
    public void musicPause() {
        try {
            p.stop();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 在选择调用musicPause后，调用该函数可以继续播放音乐
     */
    public void musicContinue() {
        try {
            p.start();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获得当前播放音乐节拍
     */
    public void musicGetRate() {
        TempoControl tc = (TempoControl) p.getControl("TempoControl");
        currentTempo = tc.getTempo();
    }

    /**
     * 恢复原播放音乐节拍
     */
    public void musicReductRate() {
        TempoControl tc = (TempoControl) p.getControl("TempoControl");
        tc.setTempo(currentTempo);
    }

    /**
     * 改变音乐的播放速度
     * @param rate音乐的速度，单位是拍/分
     */
    public void musicChangeRate(int rate) {
        TempoControl tc = (TempoControl) p.getControl("TempoControl");
        tc.setTempo(rate);
    }

    /**
     * 获得当前播放音乐音量
     */
    public void musicGetVolume() {
        VolumeControl vc = (VolumeControl) p.getControl("VolumeControl");
        if (vc != null) {
            currentLevel = vc.getLevel() / 25;
        }
    }

    /**
     * 恢复原播放音乐音量
     */
    public void musicReductVolume() {
        VolumeControl vc = (VolumeControl) p.getControl("VolumeControl");
        if (vc != null) {
            if (currentLevel == 0) {
                vc.setLevel(0);
            } else if (currentLevel == 1) {
                vc.setLevel(25);
            } else if (currentLevel == 2) {
                vc.setLevel(50);
            } else if (currentLevel == 3) {
                vc.setLevel(75);
            } else if (currentLevel == 4) {
                vc.setLevel(100);
            }
        }
    }

    /**
     * 改变音乐播放的音量
     * @param level 音乐的等级 共有5个等级。从0到4逐渐增大，0为无声，4为最大
     */
    public void changeMusicVolume(int level) {
        VolumeControl vc = (VolumeControl) p.getControl("VolumeControl");
        if (vc != null) {
            if (level == 0) {
                vc.setLevel(0);
            } else if (level == 1) {
                vc.setLevel(25);
            } else if (level == 2) {
                vc.setLevel(50);
            } else if (level == 3) {
                vc.setLevel(75);
            } else if (level == 4) {
                vc.setLevel(100);
            }
        }
    }
}

