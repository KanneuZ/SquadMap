package com.example.squadmaps;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.yandex.mapkit.geometry.Point;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Client {
    private Socket mSocket = null;
    private String mHost = null;
    private int mPort = 0;
    public static final String LOG_TAG = "SOCKET";

    private final Semaphore SEMAPHORE = new Semaphore(0);
    private final ArrayList<byte[]> outQ = new ArrayList<>();
    private final byte[] inQ = new byte[65536];

    private int inDataSize = 0;
    private Thread readThr = null;
    private Thread writeThr = null;
    private int a;
    private boolean isRegistered = false;

    private final int PKT_POOL_TEST = 0;
    private final int PKT_USER_REG = 1;
    private final int PKT_USER_LOGIN = 2;
    private final int PKT_USER_GROUPINFO = 3;
    private final int PKT_GROUP_CREATE = 4;
    private final int PKT_GROUP_JOIN = 5;
    private final int PKT_USER_COORDS = 6;
    private final int PKT_MARKER_CREATE = 8;

    ArrayList<GroupInfo> grArray = new ArrayList<>();
    ArrayList<MarkersInfo> mkr = new ArrayList<>();
    public static MutableLiveData<Boolean> isReg = new MutableLiveData<>();
    public static MutableLiveData<ArrayList<GroupInfo>> groupArray = new MutableLiveData<>();
    public static MutableLiveData<UserInfo> userPoint = new MutableLiveData<>();
    public static MutableLiveData<ArrayList<MarkersInfo>> marker = new MutableLiveData<>();

    public Client() {}

    public Client (final String host, final int port) {
        this.mHost = host;
        this.mPort = port;
        inDataSize = 0;
        isReg.postValue(false);
        groupArray.postValue(null);
    }

    public void openConnection() throws Exception  {
        closeConnection();
        readThr = new Thread(new ReadServerThread());
        readThr.start();
    }

    public void closeConnection() {
        if (mSocket != null && !mSocket.isClosed()) {
            try {
                mSocket.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Ошибка при закрытии сокета :" + e.getMessage());
            } finally {
                mSocket = null;
            }
        }

        mSocket = null;
    }

    void sendPacket(int type, byte[] body, int size) {
        if (mSocket == null || mSocket.isClosed()) {
            Log.d(LOG_TAG, "Ошибка отправки данных. Сокет не создан или закрыт");
            return;
        }

        byte[] data = new byte[size+3];
        System.arraycopy(body, 0, data, 3, size);
        size += 3;

        data[0] = (byte) type;
        data[1] = (byte) (size&0xFF);
        data[2] = (byte) ((size >> 8)&0xFF);
        Log.i(LOG_TAG, "sendPacket: "+data.length+" "+size);

        synchronized (outQ) {
            outQ.add(data);
        }

        SEMAPHORE.release();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        closeConnection();
    }

    private boolean parsePacket(int type, int size) {
        byte[] body = new byte[size];
        System.arraycopy(inQ, 3, body, 0, size);

        switch (type) {
            case 0: return PKTTestAns(body);
            case 1: return PKTRegAns(body);
            case 2: return PKTLoginAns(body);
            case 3: return PKTGroupInfoAns(body);
            case 4: return PKTGroupCreateAns(body);
            case 5: return PKTGroupJoinAns(body);
            case 6: return PKTUserCordAns(body);
            case 9: return PKTMarkerAddAns(body);
        }

        return false;
    }

    /* ==================== PKT ans ==================== */

    public boolean PKTTestAns(byte[] body) {
        a = 0;
        String ansStr = readString(body, a);
        return true;
    }

    // type size data
    public boolean PKTRegAns(byte[] body) {
        int ansType = body[0];
        a = 1;

        String ansStr = readString(body, a);

        switch (ansType) {
            case 0:
                isRegistered = true;
                isReg.postValue(true);
                return true;
            case 1:
            case 2:
                isRegistered = false;
                return true;
        }

        return true;
    }

    public boolean PKTLoginAns(byte[] body) {
        int ansType = body[0];
        a = 1;

        String ansStr = readString(body, a);

        switch (ansType) {
            case 0:
                isRegistered = true;
                isReg.postValue(true);
                return true;
            case 1:
            case 2:
                isRegistered = false;
                return true;
        }

        return true;
    }

    public boolean PKTGroupCreateAns(byte[] body) {
        int ansType = body[0];
        a = 1;

        String ansStr = readString(body, a);
        switch (ansType) {
            case 0:
                Log.d(LOG_TAG, "Группа успешно создана!");
                return true;
            case 1:
            case 2:
                return true;
        }

        return true;
    }

    public boolean PKTGroupInfoAns(byte[] body) {
        String grName, memberName;
        int mode, grId, memberId, size;

        GroupInfo group = new GroupInfo();
        ArrayList<UserInfo> childs = new ArrayList<>();

        a = 0;
        grId = (body[a]&0xFF) + ((body[a+1]&0xFF)<<8) + ((body[a+2]&0xFF)<<16) + ((body[a+3]&0xFF)<<32);

        a += 7;
        grName = readString(body, a);
        Log.d(LOG_TAG, "PKTGroupInfoAns: "+grId+" "+grName);

        group.setName(grName);
        group.setGrId(grId);


        while (a < body.length-1) {
            memberId = bytesToInt(body, a);

            mode = (body[a+4]&0xFF) + ((body[a+5]&0xFF)<<8);
            size = body[a+6]&0xFF;

            a += 7;
            memberName = readString(body, a);
            Log.d(LOG_TAG, "PKTGroupInfoAns: "+memberId+" "+memberName);

            UserInfo ci = new UserInfo();

            if ((mode & (1 << 2)) != 0) memberName = "* "+memberName;

            ci.setName(memberName);
            childs.add(ci);
        }

        group.setProductList(childs);
        grArray.add(group);
        groupArray.postValue(grArray);
        return true;
    }

    public boolean PKTGroupJoinAns(byte[] body) {
        int ansType = body[0];
        a = 1;

        String ansStr = readString(body, a);
        Log.d(LOG_TAG, "PKTLoginAns: "+ansType+" "+ansStr);
        switch (ansType) {
            case 0:
                Log.d(LOG_TAG, "Вы вошли в группу!");
                return true;
            case 1:
            case 2:
                return true;
        }

        return true;
    }

    public boolean PKTUserCordAns(byte[] body) {
        float longitude, latitude;
        int id, offset;

        offset = 0;
        id = bytesToInt(body, offset);
        offset += 4;
        latitude = bytesToFloat(body, offset);
        offset += 4;
        longitude = bytesToFloat(body, offset);

//        Log.d(LOG_TAG, "PKTUserCordAns: id = "+id+" cord "+longitude+" "+latitude);
        userPoint.postValue(new UserInfo(id, latitude, longitude));
        return true;
    }

    public boolean PKTMarkerAddAns(byte[] body) {
        float longitude, latitude;
        int markerId, type, color;
        String description;
        int offset = 0;
        a = 0;

        mkr.clear();
        while (offset < body.length-1) {
            markerId = bytesToInt(body, offset);
            offset += 4;
            type = body[offset++]&0xFF;
            color = body[offset++]&0xFF;

            description = readString(body, offset);
            offset = a;
            latitude = bytesToFloat(body, offset);
            offset += 4;
            longitude = bytesToFloat(body, offset);
            offset += 4;

            mkr.add(new MarkersInfo(markerId, type, new Point(latitude, longitude)));
            Log.i(LOG_TAG, "PKTMarkerAddAns: "+type+" "+color+" "+description+" "+latitude+" "+longitude);
        }

        marker.postValue(mkr);
        return true;
    }

    /* ==================== PKT qwr ==================== */

    public void PKTReg(String login, String password, String name) {
        byte[] body = (login+"\0"+password+"\0"+name).getBytes(StandardCharsets.UTF_8);
        sendPacket(PKT_USER_REG, body, body.length);
    }

    public void PKTLogin(String login, String password) {
        byte[] body = (login+"\0"+password).getBytes(StandardCharsets.UTF_8);
        sendPacket(PKT_USER_LOGIN, body, body.length);
    }

    public void PKTGroupCreate(String name, String password) {
        byte[] body;
        if (password == null) body = name.getBytes(StandardCharsets.UTF_8);
        else body = (name+"\0"+password).getBytes(StandardCharsets.UTF_8);
        sendPacket(PKT_GROUP_CREATE, body, body.length);
    }

    public void PKTGroupJoin(String name, String password) {
        byte[] body;
        if (password == null) body = name.getBytes(StandardCharsets.UTF_8);
        else body = (name+"\0"+password).getBytes(StandardCharsets.UTF_8);
        sendPacket(PKT_GROUP_JOIN, body, body.length);
    }

    public void PKTUserCord(Point position) {
        float longitude, latitude;
        byte[] body = new byte[8];
        int offset = 0;

        latitude = (float) position.getLatitude();
        longitude = (float) position.getLongitude();

        offset = intToBytes(Float.floatToIntBits(latitude), body, offset);
        intToBytes(Float.floatToIntBits(longitude), body, offset);

        sendPacket(PKT_USER_COORDS, body, body.length);
    }

    public void PKTMarkerCreate(int type, int color, String description, Point point) {
        float longitude, latitude;
        byte[] descr = (description+"\0").getBytes(StandardCharsets.UTF_8);
        byte[] body = new byte[descr.length+10];
        int offset = 0;

        latitude = (float) point.getLatitude();
        longitude = (float) point.getLongitude();

        body[0] = (byte) type;
        body[1] = (byte) color;
        System.arraycopy(descr, 0, body, 2, descr.length);
        offset = 2 + descr.length;

        Log.d(LOG_TAG, "PKTMarkerCreate: "+offset);

        offset = intToBytes(Float.floatToIntBits(latitude), body, offset);
        intToBytes(Float.floatToIntBits(longitude), body, offset);


        sendPacket(PKT_MARKER_CREATE, body, body.length);
    }

    /* ==================== extra func ==================== */

    private int longToBytes(long lnum, int n, byte[] bytes, int startIndex) {
        for (int i = 0; i < 8; i++) bytes[startIndex + i] = (byte) ((lnum >> (i * 8)) & 0xFF);
        return startIndex + 8;
    }

    private int intToBytes(int num, byte[] bytes, int startIndex) {
        for (int i = 0; i < 4; i++) bytes[startIndex + i] = (byte) ((num >> (i * 8)) & 0xFF);
        return startIndex + 4;
    }

    private double bytesToDouble(byte[] bytes, int offset) {
        long l = ((bytes[offset+0] & 0xFFL) <<  0) |
                 ((bytes[offset+1] & 0xFFL) <<  8) |
                 ((bytes[offset+2] & 0xFFL) << 16) |
                 ((bytes[offset+3] & 0xFFL) << 24) |
                 ((bytes[offset+4] & 0xFFL) << 32) |
                 ((bytes[offset+5] & 0xFFL) << 40) |
                 ((bytes[offset+6] & 0xFFL) << 48) |
                 ((bytes[offset+7] & 0xFFL) << 56) ;

        return Double.longBitsToDouble(l);
    }

    private float bytesToFloat(byte[] bytes, int offset) {
        int i = ((bytes[offset+0] & 0xFF) <<  0) |
                ((bytes[offset+1] & 0xFF) <<  8) |
                ((bytes[offset+2] & 0xFF) << 16) |
                ((bytes[offset+3] & 0xFF) << 24);

        return Float.intBitsToFloat(i);
    }

    private int bytesToInt(byte[] bytes, int offset) {
        return (bytes[offset]&0xFF) + ((bytes[offset+1]&0xFF)<<8) + ((bytes[offset+2]&0xFF)<<16) + ((bytes[offset+3]&0xFF)<<32);
    }

    public String readString(byte[] body, int pos) {
        int i = pos;
        String str;

        if (pos == body.length) return null;

        while (pos < body.length-1 && (char)body[pos] != '\0') pos++;

        if (pos == body.length-1) str = new String(body, i, pos-i+1);
        else str = new String(body, i, pos-i);

        if ((char)body[pos] == 0) pos++;
        a = pos;

        return str;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    /* ==================== Threads ==================== */

    class WriteServerThread implements Runnable {
        public WriteServerThread() {}

        @Override
        public void run() {
            byte[] el;

            DatabaseHelper databaseHelper = new DatabaseHelper(MyApplication.getContext());
//            Log.d(LOG_TAG, "run: "+databaseHelper.getLogin()+" "+databaseHelper.getPassword());
            if (databaseHelper.getLogin() != null && databaseHelper.getPassword() != null) PKTLogin(databaseHelper.getLogin(), databaseHelper.getPassword());

            while (true) {
                try {
                    SEMAPHORE.acquire();
                    synchronized (outQ) {
                        if (outQ.isEmpty()) continue;

                        el = outQ.get(0);
                        outQ.remove(0);
                    }

                    mSocket.getOutputStream().write(el);
                    mSocket.getOutputStream().flush();
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    class ReadServerThread implements Runnable {
        public ReadServerThread() {}

        @Override
        public void run() {
            int r, type, size;


            do {
                try {
                    mSocket = new Socket(mHost, mPort);
                    if (mSocket.isConnected()) {
                        Log.d(LOG_TAG, "Соединение установлено");

                        writeThr = new Thread(new WriteServerThread());
                        writeThr.start();
                    }
                } catch (IOException e) {
                    Log.d(LOG_TAG, "Соединение НЕ установлено");
                }
            } while (!mSocket.isConnected());

            while (true) {
                try {
                    r = mSocket.getInputStream().read(inQ, inDataSize, inQ.length-inDataSize);

                    inDataSize += r;
                    while (inDataSize >= 3) {
                        type = inQ[0];
                        size = (inQ[1]&0xFF) + ((inQ[2]&0xFF)<<8);

                        Log.d(LOG_TAG, "ReadServerThread: type = "+type+" size = "+size+" inDataSize = "+inDataSize);
                        if (inDataSize < size) break;

                        parsePacket(type, size-3);
                        inDataSize -= size;
                        if (inDataSize != 0) System.arraycopy(inQ, size, inQ, 0, inDataSize);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
