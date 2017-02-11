package com.connbluetoothdevice;

import android.bluetooth.BluetoothDevice;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * 显示搜索到的蓝牙设备
 * Created by sunxiaoyu on 2017/1/17.
 */
public class BlueAdapter extends BaseAdapter {


    public List<BluetoothDevice> list;

    public void setList(List<BluetoothDevice> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView tv = new TextView(parent.getContext());
        tv.setPadding(50,50,50,50);

        BluetoothDevice device = getItem(position);
        tv.setText(device.getName() + "--" + device.getAddress());

        return tv;
    }
}
