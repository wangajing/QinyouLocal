package stage1;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import mainMenu.MainMidlet;

/**
 *
 * @author Administrator
 */
public class MazeServer implements Runnable {

    LocalDevice localDevice;
    StreamConnectionNotifier notifier;
    ServiceRecord record;
    private static final UUID ECHO_SERVER_UUID = new UUID("F0E0D0C0B0A000908070605040302010", false);
    private boolean isClosed = false;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    Thread thread;
    private Maze3DCanvas maze3D;    //下面是要获得到的数据
    private int sPosX;//客户端的位置x
    private int sPosZ;//客户端的位置z
    private byte sLife;//客户端的生命值
    private byte sPropID;//道具的ID
    private int sAngle;//角度
    private int[][] sMap;
    private byte currentState;//表明当前的传递状态
    public static final byte INIT_STATE = 1;//初始化状态
    public static final byte RUN_STATE = 2;//运行状态
    private MainMidlet mainMidlet;

    public MazeServer(Maze3DCanvas maze3D, MainMidlet mainMidlet) {
        this.maze3D = maze3D;
        this.mainMidlet = mainMidlet;
        initData();
    }

    public void initData() {
        sPosZ = 650;
        sPosX = 650;
        sLife = 3;
        sPropID = -1;
        sAngle = 0;
        currentState = INIT_STATE;
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public boolean initBT() {
        try {
            if (!isClosed) {
                mainMidlet.showCurrentState("初始化蓝牙设备");
                localDevice = LocalDevice.getLocalDevice();//获取蓝牙设备
                if (localDevice.setDiscoverable(DiscoveryAgent.GIAC)) {//设置蓝牙设备可见性
                    //("设置成功");
                } else {
                    //("设置失败！");
                    throw new Exception("无法设置蓝牙设备可见性！");
                }
                StringBuffer url = new StringBuffer("btspp://");//创建连接的URL
                url.append("localhost:");
                url.append(ECHO_SERVER_UUID.toString());
                url.append(";name=Echo Server");
                url.append(";authorize=false");
                mainMidlet.showCurrentState("创建游戏服务");
                notifier = (StreamConnectionNotifier) Connector.open(url.toString());//建立连接通知
                record = localDevice.getRecord(notifier);
            }


        } catch (Exception ex) {
            mainMidlet.showCurrentState("初始化游戏服务失败！请检查蓝牙设备是否能够正常工作！");
            return false;
        }
        return true;
    }

    public void run() {
        while (!isClosed) {
            StreamConnection conn = null;
            try {
                conn = notifier.acceptAndOpen();
            } catch (IOException e) {
                // wrong client or interruptedanyway
                continue;
            }
            processConnection(conn);
        }

    }

    private void processConnection(StreamConnection conn) {
        try {
            dataInputStream = conn.openDataInputStream();
            dataOutputStream = conn.openDataOutputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        while (!isClosed) {
            try {
                if (currentState == RUN_STATE) {
                    byte[] b = new byte[20];
                    int length = dataInputStream.read(b);
                    //(length);
                    if (length >= 0) {
                        decodeInfo(b, length);
                    } else {
                        maze3D.setConnected(false);
//                        //("错误！");
                        throw new Exception("连接出现错误！");
                    }
                    dataOutputStream.write(encodeInfo());

                } else if (currentState == INIT_STATE) {
                    if (dataInputStream.readChar() == 'M') {
                        dataOutputStream.write(parseMapToByteArray(maze3D.getMazeMap()));
                        currentState = RUN_STATE;
                        maze3D.initData();
                    } else {
                        return;
                    }
                }

            } catch (Exception ex) {
//                ex.printStackTrace();
                maze3D.setConnected(false);
                ex.printStackTrace();
                //("IO异常！");
                return;
            }
        }
    }

    public byte[] parseMapToByteArray(int[][] array) throws IOException {
        ByteArrayOutputStream bstream = new ByteArrayOutputStream(array.length * array[0].length * 2);
        DataOutputStream ostream = new DataOutputStream(bstream);
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                ostream.writeShort((short) array[i][j]);
            }
        }
        ostream.flush();
        ostream.close();
        //("发送的数组的长度：" + bstream.toByteArray().length);
        return bstream.toByteArray();
    }

    public void printData(byte[] b, int length) {
        try {

            ByteArrayInputStream bstream = new ByteArrayInputStream(b, 0, length);
            DataInputStream istream = new DataInputStream(bstream);
            //(istream.readUTF());
            istream.close();
            bstream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public byte[] encodeInfo() throws IOException {
        ByteArrayOutputStream bstream = new ByteArrayOutputStream(10);
        DataOutputStream ostream = new DataOutputStream(bstream);
        if (maze3D.getLifeValue() != 0) {
            ostream.writeInt(maze3D.getPosX());//写入位置 8
            ostream.writeInt(maze3D.getPosZ());
            ostream.writeByte(maze3D.getLifeValue());//写入生命值 1
            ostream.writeByte(maze3D.getPropID());//写入道具 1
            //("服务端写入的数据：" + maze3D.getPosX() + " " + maze3D.getPosZ() + " " + maze3D.getLifeValue() + maze3D.getPropID());
            if (sLife == 0) {
                ostream.writeInt(maze3D.getCameraAngle());
                //("服务器写入的数据：角度：" + maze3D.getCameraAngle());
            }
        } else if (maze3D.getLifeValue() == 0) {
            ostream.writeByte(0);
            //("服务器写入的数据：死亡：" + 0);
        }
        ostream.close();
        //("编码信息：" + bstream.toByteArray().length);
        return bstream.toByteArray();
    }

    public void decodeInfo(byte[] b, int length) throws IOException {
        ByteArrayInputStream bstream = new ByteArrayInputStream(b, 0, length);
        DataInputStream istream = new DataInputStream(bstream);
        if (length == 1) //一个字节，当然是挂掉啦
        {
            sLife = 0;
            //("读取数据：1个：对方挂掉啦");
        } else if (length == 10) //10个字节的话，分别是：位置x，z，生命life，道具id
        {
            sPosX = istream.readInt();
            sPosZ = istream.readInt();
            sLife = istream.readByte();
            sPropID = istream.readByte();
            //("读取数据：10个：" + sPosX + " " +
//                    "" + sPosZ + " " + sLife + " " + sPropID);
            maze3D.setFriendState(sPosX, sPosZ, sLife, sPropID, sAngle);
        } else if (length == 14) {
            sPosX = istream.readInt();
            sPosZ = istream.readInt();
            sLife = istream.readByte();
            sPropID = istream.readByte();
            sAngle = istream.readInt();
            //("读取数据：10个：" + sPosX + " " +
//                    "" + sPosZ + " " + sLife + " " + sPropID + " " + sAngle);

        } else {
            //("读取的数据个数：" + length + "无法解析！");
            return;
        }
        maze3D.setFriendState(sPosX, sPosZ, sLife, sPropID, sAngle);
    }

    public void cancelService() {
        this.isClosed = true;
    }

    public void setMaze3D(Maze3DCanvas maze3D) {
        this.maze3D = maze3D;
    }
}

