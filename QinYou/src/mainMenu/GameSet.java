package mainMenu;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.TextField;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class GameSet extends Form implements CommandListener, ItemStateListener {

    private TextField textField;
    private ChoiceGroup choice;
    private Command cmdExit = new Command("确定", Command.OK, 1);
    private MainMidlet midlet;
    Gauge gauge;
    int currentVolume;

    public GameSet(MainMidlet midlet) {
        super("游戏设置");
        this.midlet = midlet;
        currentVolume = midlet.musicVolume;
        textField = new TextField("玩家姓名：", midlet.playerName, 4, TextField.ANY);
        gauge = new Gauge("音量调节", true, 4, currentVolume);
        choice = new ChoiceGroup("声音设置：", Choice.EXCLUSIVE);
        choice.append("开启", null);
        choice.append("关闭", null);
        this.append(textField);
        this.append(choice);
        if (midlet.needMusic == true) {
            this.append(gauge);
            gauge.setValue(currentVolume);
        }
        this.addCommand(cmdExit);
        this.setCommandListener(this);
        this.setItemStateListener(this);
        boolean c[] = new boolean[choice.size()];
        if (midlet.needMusic) {
            c[0] = true;
            c[1] = false;
        } else {
            c[0] = false;
            c[1] = true;
        }
        choice.setSelectedFlags(c);
    }

    public void commandAction(Command c, Displayable displayable) {
        if (c == cmdExit) {
            String playerName1 = this.textField.getString();
            midlet.playerName = playerName1;
            DataStore dataStore = new DataStore();
            dataStore.writeNameRecord(midlet.playerName);
            dataStore.writeMusicVolume(gauge.getValue());
            midlet.musicVolume = (byte) gauge.getValue();

            if (gauge.getValue() == 0) {
                midlet.needMusic = false;
            } else {
                if (choice.getSelectedIndex() == 0) {
                    midlet.needMusic = true;
                } else {
                    midlet.needMusic = false;
                }
            }
            //(midlet.needMusic);

            midlet.mainMenuShow(1);
            System.gc();
        }
    }

    public void itemStateChanged(Item item) {
        if (item == choice) {
            if (choice.getSelectedIndex() == 0) {
                midlet.needMusic = true;
                midlet.mainMenu.turnOnOrOffMusic();
            } else {
                midlet.needMusic = false;
                midlet.mainMenu.turnOnOrOffMusic();
            }
            if (midlet.needMusic == true) {
                if (this.size() == 2) {
                    this.append(gauge);
                }
            } else {
                this.delete(2);
            }
        } else if (item == gauge) {
            midlet.mainMenu.changeMusicVolume(gauge.getValue());
        }
    }
}
   