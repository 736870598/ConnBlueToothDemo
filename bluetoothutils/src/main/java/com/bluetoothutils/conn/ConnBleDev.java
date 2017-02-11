package com.bluetoothutils.conn;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by sunxiaoyu on 2017/2/9.
 */

public class ConnBleDev extends AbsConnDev{

    private Context context;
    private String readUUID;
    private String writeUUID;

    //连接的Gatt
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCallback mBluetoothGattCallBack;

    //读写模块的Characteristic
    private BluetoothGattCharacteristic writeCharacteristic;
    private BluetoothGattCharacteristic readCharacteristic;


    public ConnBleDev(Context context, String readUUID, String writeUUID) {
        this.context = context;
        this.readUUID = readUUID;
        this.writeUUID = writeUUID;
    }

    public void setReadUUID(String readUUID) {
        this.readUUID = readUUID;
    }

    public void setWriteUUID(String writeUUID) {
        this.writeUUID = writeUUID;
    }

    @Override
    public boolean write(String str) {
        if (writeCharacteristic != null && mBluetoothGatt != null) {
            writeCharacteristic.setValue(str + "\r\n");
            mBluetoothGatt.writeCharacteristic(writeCharacteristic);
            return true;
        }
        return false;
    }

    @Override
    public void conn(BluetoothDevice bluetoothDevice) {
        if(bluetoothDevice == null ||deviceIsSame(bluetoothDevice)){
            return ;
        }

        this.bluetoothDevice = bluetoothDevice;

        breakConn();

        connstate = CONNSTATE.CONNING;
        if(connListener != null)
            connListener.connStart();

        initBluetoothGattCallBack();
        mBluetoothGatt = bluetoothDevice.connectGatt(context, false, mBluetoothGattCallBack);

    }


    /**
     * 初始化BluetoothGattCallBack
     */
    private void initBluetoothGattCallBack() {
        if (mBluetoothGattCallBack != null) {
            return;
        }
        mBluetoothGattCallBack = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    mBluetoothGatt.discoverServices();
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    breakConn();
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                getVisitGatt(mBluetoothGatt.getServices());
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                read(new String(characteristic.getValue()));
            }
        };
    }

    /**
     * 断开连接
     */
    @Override
    public void breakConn() {
        readCharacteristic = null;
        writeCharacteristic = null;
        if (mBluetoothGatt != null) {
            try {
                mBluetoothGatt.disconnect();
                mBluetoothGatt.close();
            } catch (Exception e) {
            }
        }
        mBluetoothGatt = null;
        super.breakConn();
    }

    /**
     * 得到和北斗的通信模块
     * 这里可以比作到一个学校里去找同学，而通信模块所需的读写模块就可以看做俩个同学，
     * readUUid，writeUUid就是我们要找的俩个同学的身份证号码。
     * 把mBluetoothGatt比作学校，BluetoothGattService比作班级
     * BluetoothGattCharacteristic就可以看做同学。
     *
     * @param services
     */
    private void getVisitGatt(List<BluetoothGattService> services) {
        if (services == null || services.size() == 0) {
            return;
        }

        //遍历学校里每一个班级
        for (BluetoothGattService gattService : services) {

            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            //遍历班级里每一个同学
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {

                String uuid = gattCharacteristic.getUuid().toString();
                //得到同学的身份证号码进行比对
                if (uuid.equals(readUUID)) {
                    readCharacteristic = gattCharacteristic;
                    mBluetoothGatt.setCharacteristicNotification(readCharacteristic, true);
                }
                if (uuid.equals(writeUUID)) {
                    writeCharacteristic = gattCharacteristic;
                }

                //找到了就不在找了
                if (readCharacteristic != null && writeCharacteristic != null) {
                    connstate = CONNSTATE.CONN;
                    if (connListener != null)
                        connListener.connSuccess();
                    return;
                }
            }
        }
        connstate = CONNSTATE.BREAK;
        if (connListener != null)
            connListener.connFail("没有找到UUID对应的Characteristic");
    }


}
