package com.bluetoothutils.conn;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

/**
 * Created by sunxiaoyu on 2017/2/10.
 */

public class ConnFactory {

    private static ConnFactory instance;

    private ConnBleDev connBleDev;
    private ConnClassicsDev connClassicsDev;

    public static ConnFactory getInstance(){
        if(instance == null){
            instance = new ConnFactory();
        }
        return instance;
    }

    private ConnFactory(){}

    public AbsConnDev connBleDev(Context context, BluetoothDevice device, String readUUID, String writeUUID, ConnListener connListener) {
        if(connClassicsDev != null){
            connClassicsDev.breakConn();
        }
        if(connBleDev != null){
            connBleDev.breakConn();
        }
        if(connBleDev == null){
            connBleDev = new ConnBleDev(context, readUUID, writeUUID);
        }else{
            connBleDev.setWriteUUID(writeUUID);
            connBleDev.setReadUUID(readUUID);
        }
        connBleDev.setConnListener(connListener);
        connBleDev.conn(device);

        return connBleDev;
    }

    public AbsConnDev connClassicsDev(BluetoothDevice device, ConnListener connListener) {
        if(connBleDev != null){
            connBleDev.breakConn();
        }
        if(connClassicsDev != null){
            connClassicsDev.breakConn();
        }
        if(connClassicsDev == null){
            connClassicsDev = new ConnClassicsDev();
        }
        connClassicsDev.setConnListener(connListener);
        connClassicsDev.conn(device);

        return connClassicsDev;
    }

    public AbsConnDev connDev(Context context, BluetoothDevice device, ConnListener connListener) {
        if (device == null){
            return null;
        }
        switch (device.getType()){
            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                return connClassicsDev(device, connListener);
            case BluetoothDevice.DEVICE_TYPE_LE:
                String devName = device.getName();
                for (BleDevEnum bleDevEnum: BleDevEnum.values()) {
                    if (bleDevEnum.getName().equals(devName)){
                        return connBleDev(context, device, bleDevEnum.getReadUUID(), bleDevEnum.getWriteUUID(), connListener);
                    }
                }
        }
        return null;
    }
}
