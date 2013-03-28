package mainMenu;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.microedition.rms.RecordStore;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class DataStore {

    RecordStore recordStore;
    /** Creates a new instance of DataStore */
    RecordStore recordVolume;

    public DataStore() {

        try {
            recordStore = RecordStore.openRecordStore("name", true, RecordStore.AUTHMODE_PRIVATE, true);
            recordVolume = RecordStore.openRecordStore("MusicVolume", true, RecordStore.AUTHMODE_PRIVATE, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeUTF("无名");
            dos.writeInt(2);
            byte[] name = baos.toByteArray();
            byte[] musicvolume = baos.toByteArray();
            if (recordStore.getNumRecords() == 0) {
                recordStore.addRecord(name, 0, name.length);
                recordVolume.addRecord(musicvolume, 0, musicvolume.length);
            }
            dos.close();
            baos.close();
            recordStore.closeRecordStore();
            recordVolume.closeRecordStore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    如果要加入的用户信息的分数大于记录中保存的分数之一
    就可以将第一个分数比较小的记录删除
    将用户姓名和分数加到分数记录存储的后面
    如果该记录存储不存在 就生成新的记录存储
    如果可用空间不够 不写入直接返回
    如果写入成功返回ID号*/
    public int writeScoreRecord(String name, int score) {
        int id = -1;          //非法返回值
        int userscore;
        try {                //打开记录存储
            recordStore = RecordStore.openRecordStore("score", true, RecordStore.AUTHMODE_PRIVATE, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (recordStore.getNumRecords() == 10) {
                int index = 2;
                byte[] _score = recordStore.getRecord(2);
                ByteArrayInputStream bais = new ByteArrayInputStream(_score);
                DataInputStream inputStream = new DataInputStream(bais);
                userscore = inputStream.readInt();
                for (int i = 4; i <= recordStore.getNumRecords(); i += 2) {
                    int sc;
                    bais.reset();
                    _score = recordStore.getRecord(i);
                    bais = new ByteArrayInputStream(_score);
                    inputStream = new DataInputStream(bais);
                    sc = inputStream.readInt();
                    if (sc < userscore) {
                        userscore = sc;
                        index = i;
                    }
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream outputStream = new DataOutputStream(baos);
                outputStream.writeUTF(name);
                byte[] playerName = baos.toByteArray();
                baos.reset();
                outputStream.writeInt(score);
                byte[] playerScore = baos.toByteArray();
                recordStore.setRecord(index - 1, playerName, 0, playerName.length);
                recordStore.setRecord(index, playerScore, 0, playerScore.length);
                inputStream.close();
                bais.close();
                recordStore.closeRecordStore();
            } else if (recordStore.getNumRecords() < 10) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream outputStream = new DataOutputStream(baos);
                outputStream.writeUTF(name);
                byte[] playerName = baos.toByteArray();
                baos.reset();
                outputStream.writeInt(score);
                byte[] playerScore = baos.toByteArray();
                if (recordStore.getSizeAvailable() < (playerName.length + playerScore.length)) {
                    outputStream.close();
                    baos.close();
                    recordStore.closeRecordStore();
                    return id;
                } else {
                    id = recordStore.addRecord(playerName, 0, playerName.length);
                    id = recordStore.addRecord(playerScore, 0, playerScore.length);
                    outputStream.close();
                    baos.close();
                    recordStore.closeRecordStore();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }


    /**
    读取记录存储中的分数和姓名 到参数中的姓名和分数数组中
    如果记录存储为空 不读取
     */
    public void readScoreRecord(int[] score, String[] name) {
        try {
            recordStore = RecordStore.openRecordStore("score", true, RecordStore.AUTHMODE_PRIVATE, true);
            int n = 0, s = 0;
            for (int i = 1; i <= recordStore.getNumRecords(); i++) {
                byte[] playerName = recordStore.getRecord(i);
                ByteArrayInputStream bais = new ByteArrayInputStream(playerName);
                DataInputStream inputStream = new DataInputStream(bais);
                name[n++] = inputStream.readUTF();
                i++;
                byte[] playerScore = recordStore.getRecord(i);
                bais = new ByteArrayInputStream(playerScore);
                inputStream = new DataInputStream(bais);
                score[s++] = inputStream.readInt();
                inputStream.close();
                bais.close();
            }
            recordStore.closeRecordStore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *是否能进高分榜
     */
    public boolean isHighScore(int score) {
        boolean ok = false;
        try {
            recordStore = RecordStore.openRecordStore("score", true, RecordStore.AUTHMODE_PRIVATE, true);
            // //("DataStore:NumRecord="+recordStore1.getNumRecords());
            if (recordStore.getNumRecords() < 10) ///////////////////
            {
                return true;
            }
            if (recordStore.getNumRecords() != 0) {
                ByteArrayInputStream bais;
                DataInputStream inputStream;
                for (int i = 2; i <= recordStore.getNumRecords(); i += 2) //////////
                {
                    byte[] _score = recordStore.getRecord(i);
                    bais = new ByteArrayInputStream(_score);
                    inputStream = new DataInputStream(bais);
                    int userscore = inputStream.readInt();
                    // //("DataStore:分数"+userscore);
                    if (score > userscore) {
                        ok = true;
                        break;
                    }
                    bais.reset();
                    inputStream.close();
                    bais.close();
                }
            }
            recordStore.closeRecordStore();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

  
//    得到score记录存储的数目*/
    public int getScoreRecordNumber() {
        int num = 0;
        try {
            recordStore = RecordStore.openRecordStore("score", true, RecordStore.AUTHMODE_PRIVATE, true);
            num = recordStore.getNumRecords();
            recordStore.closeRecordStore();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    public void deleteRecordStore() {
        try {
            recordStore = RecordStore.openRecordStore("score", true, RecordStore.AUTHMODE_PRIVATE, true);
            if (recordStore.getNumRecords() == 0) {
            } else {
                recordStore.deleteRecordStore("score");
                recordStore.closeRecordStore();
            }
        } catch  (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    写入玩家姓名*/
    public void writeNameRecord(String name) {
        try {
            recordStore = RecordStore.openRecordStore("name", true, RecordStore.AUTHMODE_PRIVATE, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeUTF(name);
            byte[] username = baos.toByteArray();
            recordStore.setRecord(1, username, 0, username.length);
            dos.close();
            baos.close();
            recordStore.closeRecordStore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeMusicVolume(int level) {
        try {
            recordVolume = RecordStore.openRecordStore("MusicVolume", true, RecordStore.AUTHMODE_PRIVATE, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(level);
            byte[] volumeLevael = baos.toByteArray();
            recordVolume.setRecord(1, volumeLevael, 0, volumeLevael.length);
            dos.close();
            baos.close();
            recordVolume.closeRecordStore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int readMusicVolume() {
        int volumeLevel = 2;
        try {
            recordVolume = RecordStore.openRecordStore("MusicVolume", true, RecordStore.AUTHMODE_PRIVATE, true);
            byte[] volumeLevael = recordVolume.getRecord(1);
            ByteArrayInputStream bais = new ByteArrayInputStream(volumeLevael);
            DataInputStream dis = new DataInputStream(bais);
            volumeLevel = dis.readInt();
            dis.close();
            bais.close();
            recordVolume.closeRecordStore();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return volumeLevel;
    }

    /**
    读取玩家姓名*/
    public String readNameRecord() {
        String str = "  ";
        try {
            recordStore = RecordStore.openRecordStore("name", true, RecordStore.AUTHMODE_PRIVATE, true);
            byte[] username = recordStore.getRecord(1);
            ByteArrayInputStream bais = new ByteArrayInputStream(username);
            DataInputStream dis = new DataInputStream(bais);
            str = dis.readUTF();
            dis.close();
            bais.close();
            recordStore.closeRecordStore();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
