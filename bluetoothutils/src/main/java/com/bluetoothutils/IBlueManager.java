package com.bluetoothutils;

import android.bluetooth.BluetoothDevice;

import com.bluetoothutils.conn.ConnListener;
import com.bluetoothutils.search.SearchListener;
import com.bluetoothutils.search.SearchRequest;

import java.util.List;

/**
 * Created by sunxiaoyu on 2017/2/9.
 */

public interface IBlueManager {

    boolean devCanUseBlueTooth();
    void openBlueTooth();
    void closeBlueTooth();
    void startSearch(SearchRequest searchRequest, SearchListener searchListener);
    void stopSearch();
    void connBleDev(BluetoothDevice device, String readUUID, String writeUUID, ConnListener connListener);
    void connClassicsDev(BluetoothDevice device, ConnListener connListener);
    void connDev(BluetoothDevice device, ConnListener connListener);
    void breakConn();
    void writeToDev(String msg);
    List<BluetoothDevice> getMatchDevList();


}
