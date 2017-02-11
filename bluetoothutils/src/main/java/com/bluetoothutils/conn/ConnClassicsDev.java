package com.bluetoothutils.conn;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * Created by sunxiaoyu on 2017/2/9.
 */
public class ConnClassicsDev extends AbsConnDev{

    // 蓝牙连接后的输入输出流
    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    private BluetoothSocket bluetoothSocket;


    @Override
    public void conn(BluetoothDevice bluetoothDevice) {
        if(bluetoothDevice == null ||deviceIsSame(bluetoothDevice)){
            return ;
        }

        //1.  断开之前的蓝牙连接
        breakConn();

        //开启线程连接蓝牙
        new ConnThread(bluetoothDevice).start();

    }

    /**
     * 专门用于连接蓝牙的Thread
     */
    private class ConnThread extends Thread {

        private BluetoothDevice device;

        public ConnThread(BluetoothDevice device) {
            this.device = device;
        }

        @Override
        public void run() {
            super.run();

            //-------开始连接----------
            connstate = CONNSTATE.CONNING;
            if(connListener != null) {
                connListener.connStart();
            }

            try {
                //2：连接蓝牙
                Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                bluetoothSocket = (BluetoothSocket) m.invoke(device, Integer.valueOf(1));
                bluetoothSocket.connect();

                //3:获取蓝牙输入输出流
                inputStream = bluetoothSocket.getInputStream();
                outputStream = bluetoothSocket.getOutputStream();

                //4: 连接成功，将连接状态设置为true，监听输入输出流
                connstate = CONNSTATE.CONN;
                if(connListener != null)
                    connListener.connSuccess();

                receiveBluetoothMag();

            } catch (Exception e) {
                connstate = CONNSTATE.BREAK;
                if(connListener != null)
                    connListener.connFail(e.getMessage());
                e.printStackTrace();
                return;
            }
        }
    }

    /**
     * 监听输入流。蓝牙--》设备
     */
    public void receiveBluetoothMag() {
        try {
            while (connstate == CONNSTATE.CONN) {
                byte[] buffer = new byte[512];
                int size = inputStream.read(buffer);
                if (size > 0) {
                    read(DecodeClassicsUtils.byte2Hex(buffer, size));
                }
            }
        } catch (Exception e) {
            //连接断开
            if(connListener != null){
                connListener.connFail(e.getMessage());
            }
            e.printStackTrace();
        } finally {
            breakConn();
        }
    }

    @Override
    public boolean write(String str) {
        try {
            if (outputStream != null) {
                outputStream.write(DecodeClassicsUtils.hex2Byte(str));
                outputStream.flush();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void breakConn() {
        try {
            //断开连接时释放io流
            if (inputStream != null)
                inputStream.close();
            inputStream = null;

            if (outputStream != null)
                outputStream.close();
            outputStream = null;

            if (bluetoothSocket != null && bluetoothSocket.isConnected())
                bluetoothSocket.close();
            bluetoothSocket = null;
        } catch (Exception e) {
        }
        super.breakConn();
    }
}
