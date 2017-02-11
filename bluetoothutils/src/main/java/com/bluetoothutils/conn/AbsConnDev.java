package com.bluetoothutils.conn;

import android.bluetooth.BluetoothDevice;

/**
 * Created by sunxiaoyu on 2017/2/10.
 */

public abstract class AbsConnDev implements IConnDev{

    protected ConnListener connListener;
    protected BluetoothDevice bluetoothDevice;
    protected CONNSTATE connstate = CONNSTATE.BREAK ;  //标示连接状态

    protected enum CONNSTATE{
        BREAK,      //连接断开
        CONNING,    //连接中
        CONN        //连接成功
    }

    public void setConnListener(ConnListener connListener) {
        this.connListener = connListener;
    }

    @Override
    public void breakConn() {
        if(connListener != null){
            if(connstate == CONNSTATE.CONN){
                connListener.connBreak();
            }else if(connstate == CONNSTATE.CONNING){
                connListener.connCancel();
            }
        }
        connstate = CONNSTATE.BREAK;
    }

    public void read(String rece) {
        if (connListener != null){
            connListener.receData(rece);
        }
    }

    protected boolean deviceIsSame(BluetoothDevice newBluetoothDevice){
        return (bluetoothDevice != null && newBluetoothDevice != null && newBluetoothDevice.getAddress() == bluetoothDevice.getAddress());
    }


}
