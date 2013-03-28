package stage3;


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
public class RaceServer implements Runnable {

    LocalDevice localDevice;
    StreamConnectionNotifier notifier;
    ServiceRecord record;
    private static final UUID ECHO_SERVER_UUID = new UUID("F0E0D0C0B0A000908070605040302010", false);
    private boolean isClosed = false;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    Thread thread;
    private HorseRace HorseRace;    //下面是要获得到的数据
   private int enemycurrentPosition;
  private byte enemylife;
  private byte enemyX;
//  private boolean isjumped;
//  private byte x;
   
    private byte currentState;//表明当前的传递状态
    public static final byte INIT_STATE = 1;//初始化状态
    public static final byte RUN_STATE = 2;//运行状态
    private MainMidlet mainMidlet;
    private byte enemylifetimes;
    private byte currentGameState;
    private byte enemycurrentGameState;
    private byte enemyjumpleft;

    public RaceServer(HorseRace HorseRace, MainMidlet mainMidlet) {
        this.HorseRace =HorseRace;
        this.mainMidlet = mainMidlet;
        initData();
    }

    public void initData() {
        enemycurrentPosition=0;
        currentGameState=0;
        enemylifetimes=2;
        enemylife=100;
        enemyX=88;
        enemyjumpleft=10;
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

    public void setHorseRace(HorseRace HorseRace) {
        this.HorseRace=HorseRace;
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
//                       //("服务端解码成功");
                    } else {
                       HorseRace.setConnected(false);
                        throw new Exception("连接出现错误！");
                    }
//                    //("编码的内容是"+encodeInfo());
                    dataOutputStream.write(encodeInfo());
//                    //("服务端编码成功");
                } else if (currentState == INIT_STATE) {
                    if (dataInputStream.readChar() == 'S') {
                        dataOutputStream.write('S');
//                        //("发出开始游戏符号");
                        currentState = RUN_STATE;
                        HorseRace.initFirstStage();
                        HorseRace.setSignal();
                    } else {
                        return;
                    }
                }
             
            } catch (Exception ex) {
//                ex.printStackTrace();
                HorseRace.setConnected(false);
                ex.printStackTrace();
                //("IO异常！");
                return;
            }
        }
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
        ByteArrayOutputStream bstream = new ByteArrayOutputStream(8);
        DataOutputStream ostream = new DataOutputStream(bstream);
        if (HorseRace.getCurrentPosition() <= 13000 && (HorseRace.getLife() + HorseRace.getLifetimes()) != 0) {
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
//        //("编码信息：" + bstream.toByteArray().length);
//        //(""+bstream.toByteArray());
        return bstream.toByteArray();
    }

    public void decodeInfo(byte[] b, int length) throws IOException {
        ByteArrayInputStream bstream = new ByteArrayInputStream(b, 0, length);
        DataInputStream istream = new DataInputStream(bstream);
      if (length == 1) //一个字节，
      {
          enemylife=0;
       //   cancelService();
          //("客户端失败服务器获胜");
      }
      else if(length==8)
      {  // enemycurrentGameState=istream.readByte();
          enemycurrentPosition=istream.readInt();
          enemylifetimes=istream.readByte();
          enemylife=istream.readByte();
           enemyX=istream.readByte();
           enemyjumpleft=istream.readByte();
      }

         HorseRace.setFriendState( enemycurrentPosition,  enemylifetimes,enemylife,enemyX,enemyjumpleft);
    }

    public void cancelService() {
        this.isClosed = true;
    }

    public void HorseRace(HorseRace HorseRace) {
        this.HorseRace = HorseRace;
    }
}

