package com.bluetoothutils.search;

import android.bluetooth.BluetoothDevice;

/**
 * 搜索蓝牙监听
 * Created by sunxiaoyu on 2017/2/9.
 */
public interface SearchListener {

    /**
     * 开始搜索
     */
    void startSearch();

    /**
     * 停止搜索
     */
    void stopSearch();

    /**
     * 取消搜索
     */
    void cancelSearch();

    /**
     * 发现设备
     * @param device
     */
    void findDevice(BluetoothDevice device);

}
