package stage1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import mainMenu.MainMidlet;

/**
 *
 * @author Administrator
 *     上面的定义可以看到我们是如何传递数据的：
 *1、双方都存活状态，可能传递的数据包括
 *如果收到的是8个字节：表示发送的是位置
 *如果收到的是9个字节：表示友方的生命减了
 *如果受到的是10个字节：表示友方获得了某种道具
 *如果受到的是14个字节：表示对方的角度
 *2、死亡状态
 *在死亡状态下，传送一个位，表示死亡！
 *一方死亡后，要将死亡后的镜头切至友方的镜头显示，那么此时应显示的内容多了个角度，角度的定义是
 *整型，所以死亡方受到的数据共14个字节
 *3、失去连接：
 *当失去连接后，画面告诉用户已失去连接，要求用户选择退
 *4、失败状态或成功：
 *双方都失败或成功后，要断开网络连接，显示胜利或失败界面！
 *  
 * 客户端吃掉的道具ID，道具的ID定义如下:
 * -1：没有吃到任何道具
 * 0-8:大地图、小地图和随机道具
 * 10-15:硬币道具
 * 20-27:八件遗物

 */
public class MazeClient implements DiscoveryListener, Runnable {

    private LocalDevice localDevice;//本机蓝牙设备
    private DiscoveryAgent discovery;//发现代理
    private Vector devices = new Vector();//存储远程设备的向量
    private Vector services = new Vector();//存储服务记录的向量
    private UUID[] uuidSet; //UUID 集合，里面要包括要寻找服务的UUID
    private Thread thread;
    private StreamConnection conn;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
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
    private boolean isClosed;
    /**
     * 这个就是我们的服务器的UUID
     */
    private static final UUID ECHO_SERVER_UUID = new UUID("F0E0D0C0B0A000908070605040302010", false);

    public MazeClient(MainMidlet mainMidlet) {
        uuidSet = new UUID[2];
        uuidSet[0] = new UUID(0x1101);
        uuidSet[1] = ECHO_SERVER_UUID;
        this.maze3D = null;
        this.mainMidlet = mainMidlet;
        initData();
    }

    private void initData() {
        sPosZ = 650;
        sPosX = 650;
        sLife = 3;
        sPropID = -1;
        sAngle = 0;
        currentState = INIT_STATE;
        isClosed = false;
    }

    public void start() {
        if (!isClosed) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass arg1) {
        if (devices.indexOf(btDevice) == -1) {
            devices.addElement(btDevice);
        }
    }

    public void servicesDiscovered(int arg0, ServiceRecord[] sr) {
        for (int i = 0; i < sr.length; i++) {
            services.addElement(sr[i]);
        }
    }

    public void serviceSearchCompleted(int arg0, int arg1) {
        synchronized (this) {
            notify();
        }
    }

    public void inquiryCompleted(int arg0) {
        synchronized (this) {
            notify();
        }
    }

    /**
     * 初始化蓝牙设备
     * @return 真：表明蓝牙设备已经初始化完毕，假表明无法初始化蓝牙设备
     */
    private boolean initBT() {
        try {
            mainMidlet.showCurrentState("正在初始化蓝牙设备......");
            //("初始化中");
            localDevice = LocalDevice.getLocalDevice();
            discovery = localDevice.getDiscoveryAgent();
        //("初始化完毕");
        } catch (BluetoothStateException ex) {
            return false;
        }
        return true;
    }

    private synchronized boolean searchDevice() {
        try {
            mainMidlet.showCurrentState("查找周围蓝牙设备......");
            discovery.startInquiry(DiscoveryAgent.GIAC, this);
            try {
                wait();//等待搜索完成将其唤醒
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                return false;
            }
        } catch (Exception ex) {
            mainMidlet.showCurrentState("无法查找到周围蓝牙设备！");
            return false;
        }
        return true;
    }

    private synchronized boolean searchService() {
        RemoteDevice rd;
        mainMidlet.showCurrentState("查找游戏服务......");
        if (devices.size() != 0 && devices.elementAt(0) != null) {
            rd = (RemoteDevice) devices.elementAt(0);
            //(rd.toString());
            try {
                discovery.searchServices(null, uuidSet, rd, this);
            } catch (Exception ex) {
                mainMidlet.showCurrentState("无法查找到游戏服务！");
                return false;
            }
            try {
                wait();//等待搜索服务完毕
            } catch (Exception ex) {
                mainMidlet.showCurrentState("无法查找到游戏服务！");
                return false;
            }
            //("搜索完毕，在服务列表中共有" + services.size() + "个服务记录");
            if (services.size() > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void openConnection() {
        if (services.size() > 0) {
            try {
                mainMidlet.showCurrentState("正在打开游戏连接......");
                ServiceRecord sr = (ServiceRecord) services.elementAt(0);
                String url = sr.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                conn = (StreamConnection) Connector.open(url);
                dataOutputStream = conn.openDataOutputStream();
                dataInputStream = conn.openDataInputStream();

            } catch (IOException ex) {
                ex.printStackTrace();
                dataOutputStream = null;
                dataInputStream = null;
            }
        }
    }

    private void closeConnection() {
        try {
            dataInputStream.close(); //关闭dos
            dataOutputStream.close();
            conn.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void init() throws IOException {
        if (initBT()) {
            if (searchDevice()) {
                if (searchService()) {
                    openConnection();

                    mainMidlet.showCurrentState("正在初始化游戏数据......");
                    if (dataOutputStream != null && dataInputStream != null) {
                        if (currentState == INIT_STATE) {
                            dataOutputStream.writeChar('M');//表示客户端要地图信息
                            byte[] byteMap = new byte[Maze.MAZE_HEIGHT * Maze.MAZE_WIDTH * 2];//读取地图的字节数组
                            int i = dataInputStream.read(byteMap);
                            if (i != Maze.MAZE_HEIGHT * Maze.MAZE_WIDTH * 2) {
                                return;
                            }
                            sMap = getMapFromByteArray(byteMap);
                            currentState = RUN_STATE;
                            mainMidlet.showCurrentState("正在开启游戏......");
                        }
                    }
                }
            }
        }

    }

    public synchronized void run() {
        try {
            while (maze3D != null && !isClosed) {
                if (currentState == RUN_STATE) {
                    dataOutputStream.write(encodeInfo());
                    byte[] b = new byte[20];
                    int length = dataInputStream.read(b);
                    if (length >= 0) {
                        decodeInfo(b, length);
                    } else {
                        maze3D.setConnected(false);
                        throw new Exception("失去连接");
                    }
                }
                Thread.sleep(150);
            }
        } finally {
            //("失去连接");
            maze3D.setConnected(false);
            closeConnection();
            return;
        }
    }

    public void cancelSearch() {
        synchronized (this) {
            notify();
        }
        isClosed = true;

    }
    //
    private int[][] getMapFromByteArray(byte[] array) {
        try {
            ByteArrayInputStream bstream = new ByteArrayInputStream(array);
            DataInputStream istream = new DataInputStream(bstream);
            int[][] map = new int[Maze.MAZE_WIDTH][Maze.MAZE_HEIGHT];
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    map[i][j] = istream.readShort();
                }
            }
            istream.close();
            bstream.close();
            return map;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    private byte[] parseMapToByteArray(int[][] array) throws IOException {
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

    private byte[] encodeInfo() throws IOException {
        ByteArrayOutputStream bstream = new ByteArrayOutputStream(10);
        DataOutputStream ostream = new DataOutputStream(bstream);
        if (maze3D.getLifeValue() != 0) {
            ostream.writeInt(maze3D.getPosX());//写入位置 8
            ostream.writeInt(maze3D.getPosZ());
            ostream.writeByte(maze3D.getLifeValue());//写入生命值 1
            ostream.writeByte(maze3D.getPropID());//写入道具 1
            //("客户端写入的数据：" + maze3D.getPosX() +
//                    " " + maze3D.getPosZ() + " " + maze3D.getLifeValue() + maze3D.getPropID());
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

    private void decodeInfo(byte[] b, int length) throws IOException {
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

    public int[][] getSMap() {
        return sMap;
    }

    public void setMaze3D(Maze3DCanvas maze3D) {
        this.maze3D = maze3D;
    }

    public void cancelService() {
        isClosed = true;
    }
}
