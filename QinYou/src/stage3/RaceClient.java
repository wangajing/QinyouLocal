package stage3;


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

public class RaceClient implements DiscoveryListener, Runnable {

    private LocalDevice localDevice;//本机蓝牙设备
    private DiscoveryAgent discovery;//发现代理
    private Vector devices = new Vector();//存储远程设备的向量
    private Vector services = new Vector();//存储服务记录的向量
    private UUID[] uuidSet; //UUID 集合，里面要包括要寻找服务的UUID
    private Thread thread;
    private StreamConnection conn;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private HorseRace HorseRace;    //下面是要获得到的数据
    
//    private byte life;
//    private byte lifetimes;
  //客户端的游戏状态
//    private byte speed;
//  private boolean isjumped;
//  private byte x;
    private byte currentState;//表明当前的传递状态
    public static final byte INIT_STATE = 1;//初始化状态
    public static final byte RUN_STATE = 2;//运行状态
    private MainMidlet mainMidlet;
    private boolean isClosed;
    private boolean synchronization;//是否同步
    /**
     * 这个就是我们的服务器的UUID
     */
    private static final UUID ECHO_SERVER_UUID = new UUID("F0E0D0C0B0A000908070605040302010", false);
    private int enemycurrentPosition;
    private byte enemylifetimes;
    private byte enemylife;
    private byte enemycurrentGameState;
    private int enemyX;
    private byte enemyjumpleft;

    public RaceClient(MainMidlet mainMidlet) {
        uuidSet = new UUID[2];
        uuidSet[0] = new UUID(0x1101);
        uuidSet[1] = ECHO_SERVER_UUID;
        this.HorseRace = null;
        this.mainMidlet = mainMidlet;
        initData();
    }

    private void initData() {

        enemycurrentPosition = 0;
        enemylifetimes = 2;
        enemylife = 100;
        enemycurrentGameState = 0;
        enemyX = 88;
        enemyjumpleft=10;
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

    private synchronized void searchDevice() {
        try {
            mainMidlet.showCurrentState("查找周围蓝牙设备......");
            discovery.startInquiry(DiscoveryAgent.GIAC, this);
            try {
                wait();//等待搜索完成将其唤醒
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            mainMidlet.showCurrentState("无法查找到周围蓝牙设备！");
            return;
        }
        return;
    }

    private synchronized void searchService() {
        RemoteDevice rd;
        mainMidlet.showCurrentState("查找游戏服务......");
        if (devices.size() != 0 && devices.elementAt(0) != null) {
            rd = (RemoteDevice) devices.elementAt(0);
            //(rd.toString());
            try {
                discovery.searchServices(null, uuidSet, rd, this);
            } catch (Exception ex) {
                mainMidlet.showCurrentState("无法查找到游戏服务！");
            }
            try {
                wait();//等待搜索服务完毕
            } catch (Exception ex) {
                mainMidlet.showCurrentState("无法查找到游戏服务！");
                return;
            }
            //("搜索完毕，在服务列表中共有" + services.size() + "个服务记录");
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
            searchDevice();
            searchService();
            openConnection();
            mainMidlet.showCurrentState("正在初始化游戏数据......");
            if (dataOutputStream != null && dataInputStream != null) {
                if (currentState == INIT_STATE) {
                    dataOutputStream.writeChar('S');//准备就绪
                    synchronization=true;
                    currentState = RUN_STATE;
                    mainMidlet.showCurrentState("正在开启游戏......");
                }
            }
        }
    }

    public synchronized void run() {
        try {
            while (HorseRace != null && !isClosed) {
                if (currentState == RUN_STATE) {
                    dataOutputStream.write(encodeInfo());
//                    //("客户端发送成功");
                    byte[] b = new byte[20];
                    int length = dataInputStream.read(b);
                    if (length >= 0) {
                        decodeInfo(b, length);
                    } else {
                        HorseRace.setConnected(false);
                        throw new Exception("失去连接1");
                    }
                }
                Thread.sleep(50);
            }
        } finally {
            //("失去连接2");
            HorseRace.setConnected(false);
            closeConnection();
            return;
        }
    }

    public void cancelSearch() {
        synchronized (this) {
            notify();
        }

    }
    private byte[] encodeInfo() throws IOException {
        ByteArrayOutputStream bstream = new ByteArrayOutputStream(8);
        DataOutputStream ostream = new DataOutputStream(bstream);
        if (HorseRace.getCurrentPosition() <= 13000 && (HorseRace.getLife() + HorseRace.getLifetimes()) != 0) {
//            ostream.writeByte(HorseRace.getCurrentGameState());
            ostream.writeInt(HorseRace.getCurrentPosition());
            ostream.writeByte(HorseRace.getLifetimes());
            ostream.writeByte(HorseRace.getLife());
           ostream.writeByte(HorseRace.getX());
            ostream.writeByte(HorseRace.getJump());
        } else if (HorseRace.getCurrentPosition() > 13000 || (HorseRace.getLife() + HorseRace.getLifetimes()) == 0) {
            ostream.writeByte(0);
           // cancelService();
        }

        ostream.close();
        return bstream.toByteArray();
    }

    private void decodeInfo(byte[] b, int length) throws IOException {
         ByteArrayInputStream bstream = new ByteArrayInputStream(b, 0, length);
        DataInputStream istream = new DataInputStream(bstream);
      if (length == 1) //一个字节，
      {
          enemylife=0;
      }
      else if(length==8)
      {  
          enemycurrentPosition=istream.readInt();
          enemylifetimes=istream.readByte();
          enemylife=istream.readByte();
          enemyX=istream.readByte();
          enemyjumpleft=istream.readByte();
      }

        HorseRace.setFriendState(enemycurrentPosition, enemylifetimes, enemylife, enemyX,enemyjumpleft);
    }

    public void setHorseRace(HorseRace HorseRace) {
        this.HorseRace = HorseRace;
    }

    public void cancelService() {
        isClosed = true;
    }
    public boolean getSignal()
    {
        return synchronization;
    }
}
