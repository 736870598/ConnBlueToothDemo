package com.bluetoothutils.conn;

/**
 *
 * 蓝牙连接接口
 *
 * Created by sunxiaoyu on 2017/2/9.
 */
public interface ConnListener {

    /**
     * 开始连接
     */
    void connStart();

    /**
     * 连接成功
     */
    void connSuccess();

    /**
     * 连接失败
     * @param error 失败原因
     */
    void connFail(String error);

    /**
     * 接收数据（蓝牙设备-->程序）
     */
    void receData(String msg);

    /**
     * 写入数据，写入成功时调用该方法 （程序-->蓝牙设备）
     */
    void writeData(String msg);

    /**
     * 取消连接
     */
    void connCancel();

    /**
     * 连接断开
     */
    void connBreak();
}
