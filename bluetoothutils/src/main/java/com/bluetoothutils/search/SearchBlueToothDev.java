package com.bluetoothutils.search;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;

/**
 * Created by sunxiaoyu on 2017/2/9.
 */
public class SearchBlueToothDev {

    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    private SearchRequest searchRequest;
    private SearchListener listener;

    private BluetoothAdapter.LeScanCallback leScanCallback;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    stopClassicsSearch();
                    break;
                case 1:
                    stopBleSearch();
                    break;
                case 2:
                    if(listener != null)
                        listener.stopSearch();
                    break;
            }
            return false;
        }
    });

    public SearchBlueToothDev(Context context, BluetoothAdapter bluetoothAdapter, SearchRequest searchRequest, SearchListener listener) {
        this.context = context;
        this.bluetoothAdapter = bluetoothAdapter;
        this.searchRequest = searchRequest;
        this.listener = listener;
    }

    public void setSearchRequest(SearchRequest searchRequest) {
        this.searchRequest = searchRequest;
    }

    public void setListener(SearchListener listener) {
        this.listener = listener;
    }

    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public void stopClassicsSearch(){
        unSginBroadcastReceiver();
        bluetoothAdapter.cancelDiscovery();
    }

    public void stopBleSearch(){
        if(leScanCallback != null)
            bluetoothAdapter.stopLeScan(leScanCallback);
    }

    public synchronized void startSearch(){
        handler.removeMessages(2);
        if(listener != null){
            stopBleSearch();
            stopClassicsSearch();
            listener.startSearch();
        }

        if(searchRequest != null){
            if(searchRequest.getSearchClassicsDevTime() > 0){
                sginBroadcastReceiver();
                bluetoothAdapter.startDiscovery();
                handler.sendEmptyMessageDelayed(0, searchRequest.getSearchClassicsDevTime());
            }
            if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) && searchRequest.getSearchBleDevTime() > 0){
                initleScanCallback();
                bluetoothAdapter.startLeScan(leScanCallback);
                handler.sendEmptyMessageDelayed(1, searchRequest.getSearchBleDevTime());
            }
            long maxTime = Math.max(searchRequest.getSearchBleDevTime(), searchRequest.getSearchClassicsDevTime());
            handler.sendEmptyMessageDelayed(2, maxTime);
        }else{
            if(listener != null)
                listener.stopSearch();
        }
    }



    private void initleScanCallback(){
        if(leScanCallback == null){
            leScanCallback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    if (listener != null && device != null)
                        listener.findDevice(device);
                }
            };
        }
    }

    /**
     * 注册广播（监听现设备 连接断开 状态 ）
     */
    private void sginBroadcastReceiver(){
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        // 发现设备
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if(device != null)
                            listener.findDevice(device);
                    }
                }
            };
            intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        }
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * 注销广播
     */
    private void unSginBroadcastReceiver(){
        try {
            if (broadcastReceiver != null)
                context.unregisterReceiver(broadcastReceiver);
        }catch (Exception e){
        }
    }



}
