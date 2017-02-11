package com.bluetoothutils.conn;

import android.bluetooth.BluetoothDevice;

/**
 * Created by sunxiaoyu on 2017/2/9.
 */

public interface IConnDev {

    void setConnListener(ConnListener connListener);
    void conn(BluetoothDevice bluetoothDevice);
    void breakConn();
    boolean write(String str);
    void read(String rece);

}
