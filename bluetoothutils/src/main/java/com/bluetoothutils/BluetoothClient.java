package com.bluetoothutils;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.bluetoothutils.conn.ConnListener;
import com.bluetoothutils.search.SearchListener;
import com.bluetoothutils.search.SearchRequest;

import java.util.List;

/**
 * 用户统一调用该类操作蓝牙相关
 * Created by sunxiaoyu on 2017/2/9.
 */
public class BluetoothClient {

    private static BluetoothClient instense;
    private BlueManager blueManager;
    private Context context;

    public static BluetoothClient getInstense(Context context){
        if(instense == null){
            instense = new BluetoothClient(context);
        }
        return instense;
    }

    private BluetoothClient(Context context){
        this.context = context.getApplicationContext();
        blueManager = new BlueManager(this.context);
    }


    public boolean devCanUseBlueTooth(){
        return blueManager.devCanUseBlueTooth();
    }

    public void openBlueTooth(){
        blueManager.openBlueTooth();
    }

    public void closeBlueTooth(){
        blueManager.closeBlueTooth();
    }

    public void search(SearchRequest searchRequest, SearchListener listener){
        blueManager.startSearch(searchRequest, listener);
    }

    public void connClassicsDev(BluetoothDevice device, ConnListener connListener){
        blueManager.stopSearch();
        blueManager.connClassicsDev(device, connListener);
    }

    public void connBleDev(BluetoothDevice device, String readUUID, String writeUUID, ConnListener connListener){
        blueManager.stopSearch();
        blueManager.connBleDev(device, readUUID, writeUUID, connListener);
    }

    public void connDev(BluetoothDevice device, ConnListener connListener){
        blueManager.stopSearch();
        blueManager.connDev(device, connListener);
    }

    public void breakConn(){
        blueManager.breakConn();
    }

    public void writeToDev(String msg){
        blueManager.writeToDev(msg);
    }

    public List<BluetoothDevice> getMatchDevList(){
        return blueManager.getMatchDevList();
    }


}
