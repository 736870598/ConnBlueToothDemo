package com.bluetoothutils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.bluetoothutils.conn.AbsConnDev;
import com.bluetoothutils.conn.ConnFactory;
import com.bluetoothutils.conn.ConnListener;
import com.bluetoothutils.search.SearchListener;
import com.bluetoothutils.search.SearchRequest;
import com.bluetoothutils.search.SearchBlueToothDev;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 统一管理蓝牙的类，BluetoothClient中实际操作该类
 * Created by sunxiaoyu on 2017/2/9.
 */
public class BlueManager implements IBlueManager {

    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    /**
     * 搜索蓝牙
     */
    private SearchBlueToothDev searchBlueToothDev;
    /**
     * 连接类
     */
    private AbsConnDev connDev;

    public BlueManager(Context context) {
        this.context = context;
    }

    /**
     * 判断程序设备蓝牙是否可用
     */
    @Override
    public boolean devCanUseBlueTooth() {
        if(bluetoothAdapter == null){
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return (bluetoothAdapter != null);
    }

    /**
     * 打开蓝牙
     */
    @Override
    public void openBlueTooth() {
        if(devCanUseBlueTooth() && !bluetoothAdapter.isEnabled())
            bluetoothAdapter.enable();
    }

    /**
     * 关闭蓝牙
     */
    @Override
    public void closeBlueTooth() {
        if(devCanUseBlueTooth() && bluetoothAdapter.isEnabled())
            bluetoothAdapter.disable();
    }

    /**
     * 开始扫描
     * @param searchRequest    搜索请求
     * @param searchListener   搜索监听
     */
    @Override
    public void startSearch(SearchRequest searchRequest, SearchListener searchListener) {
        if(devCanUseBlueTooth()){
            if(searchBlueToothDev == null){
                searchBlueToothDev = new SearchBlueToothDev(context, bluetoothAdapter, searchRequest, searchListener);
            }else{
                searchBlueToothDev.setSearchRequest(searchRequest);
                searchBlueToothDev.setListener(searchListener);
            }
            searchBlueToothDev.startSearch();
        }else{
            if(searchListener != null)
                searchListener.cancelSearch();
        }
    }

    /**
     * 停止搜索
     */
    @Override
    public void stopSearch() {
        if (searchBlueToothDev != null){
            searchBlueToothDev.stopBleSearch();
            searchBlueToothDev.stopClassicsSearch();
        }
    }

    /**
     * 连接ble蓝牙设备
     * @param device       设备
     * @param readUUID      readUUID
     * @param writeUUID     writeUUID
     * @param connListener  连接监听
     */
    @Override
    public void connBleDev(BluetoothDevice device, String readUUID, String writeUUID, ConnListener connListener) {
        if(devCanUseBlueTooth()){
            connDev = ConnFactory.getInstance().connBleDev(context, device, readUUID, writeUUID, connListener);
        }
    }

    /**
     * 连接传统蓝牙设备
     * @param device        设备
     * @param connListener  连接监听
     */
    @Override
    public void connClassicsDev(BluetoothDevice device, ConnListener connListener) {
        if(devCanUseBlueTooth()){
            connDev = ConnFactory.getInstance().connClassicsDev(device, connListener);
        }
    }

    /**
     * 连接设备，该方法自动判断要连接的设备的类型，如果是传统蓝牙设备，直接进行连接；
     * 如果是ble设备，通过BleDevEnum进行匹配连接
     * @param device        设备
     * @param connListener  连接监听
     */
    @Override
    public void connDev(BluetoothDevice device, ConnListener connListener) {
        if(devCanUseBlueTooth()){
            connDev = ConnFactory.getInstance().connDev(context, device, connListener);
        }
    }

    /**
     * 断开连接
     */
    @Override
    public void breakConn() {
        connDev.breakConn();
    }

    /**
     * 写入数据
     */
    @Override
    public void writeToDev(String msg) {
        if(devCanUseBlueTooth() && connDev != null)
            connDev.write(msg);
    }

    /**
     * 得到之前匹配过的蓝牙设备
     */
    @Override
    public List<BluetoothDevice> getMatchDevList() {
        if(devCanUseBlueTooth()){
            Set<BluetoothDevice> set = bluetoothAdapter.getBondedDevices();
            if(set != null && set.size() > 0){
                return new ArrayList<>(bluetoothAdapter.getBondedDevices());
            }
        }
        return null;
    }
}
