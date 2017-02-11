package com.connbluetoothdevice;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bluetoothutils.BluetoothClient;
import com.bluetoothutils.conn.ConnListener;
import com.bluetoothutils.search.SearchListener;
import com.bluetoothutils.search.SearchRequest;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener{

    private BluetoothClient bluetoothClient;
    private BlueAdapter adapter;
    private List<BluetoothDevice> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        bluetoothClient = BluetoothClient.getInstense(this);

        list = bluetoothClient.getMatchDevList();
        if (list == null) {
            list = new ArrayList<>();
        }
    }

    private void initView(){
        findViewById(R.id.open_blue).setOnClickListener(this);
        findViewById(R.id.start_scan).setOnClickListener(this);
        findViewById(R.id.write_data).setOnClickListener(this);
        ListView listView = (ListView) findViewById(R.id.listview);
        adapter = new BlueAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                conn(adapter.getItem(position));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.write_data:
                bluetoothClient.writeToDev("2449434A43000C000000002B");
//                bluetoothClient.writeToDev("$CCICA,0,0*4B");
                break;
            case R.id.open_blue:
                if(bluetoothClient.devCanUseBlueTooth()){
                    bluetoothClient.openBlueTooth();
                }else{
                    Toast.makeText(MainActivity.this, "蓝牙用不起！！！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.start_scan:
                startSearch();
                break;
        }
    }


    /**
     * 搜索
     */
    private void startSearch(){

        SearchRequest searchRequest= new SearchRequest().setSearchBleDevTime(50000).setSearchClassicsDevTime(100000);
        bluetoothClient.search(searchRequest, new SearchListener() {
            @Override
            public void startSearch() {
                Toast.makeText(MainActivity.this, "startSearch", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void stopSearch() {
                Toast.makeText(MainActivity.this, "stopSearch", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cancelSearch() {
                Toast.makeText(MainActivity.this, "cancelSearch", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void findDevice(BluetoothDevice device) {

                if (!list.contains(device)) {
                    list.add(device);
                    adapter.setList(list);
                }
            }
        });
    }

    /**
     * 连接蓝牙设备
     * @param dev
     */
    private void conn(BluetoothDevice dev) {
        bluetoothClient.connDev(dev, new ConnListener() {
            @Override
            public void connStart() {
                Log.v("sxy", "connStart");
            }

            @Override
            public void connSuccess() {
                Log.v("sxy", "connSuccess");
            }

            @Override
            public void connFail(String error) {
                Log.v("sxy", "connFail--" + error);
            }

            @Override
            public void receData(String msg) {
                Log.v("sxyR:" , msg);
            }

            @Override
            public void writeData(String msg) {
                Log.v("sxyW:" , msg);
            }

            @Override
            public void connCancel() {
                Log.v("sxy", "connCancel");
            }

            @Override
            public void connBreak() {
                Log.v("sxy", "connBreak");
            }

        });
    }

}
