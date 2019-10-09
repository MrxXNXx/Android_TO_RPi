package com.mrz.raspberrypiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class BltActivity extends AppCompatActivity {
    ArrayList ba=new ArrayList();
    ListView listView;
    String a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blt);
        BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
        if(blueadapter==null)
        {
            Toast.makeText(BltActivity.this,"本机不支持蓝牙",Toast.LENGTH_SHORT);
        }
        if (!blueadapter.isEnabled())
        {
            blueadapter.enable();
        }
        //查找已经配对
        Set<BluetoothDevice> devices = blueadapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            ba.add(device.getName() + " : " + device.getAddress());
        }
        //查找并添加完毕
        //添加到listview
        listView= findViewById(R.id.list_lv);//在视图中找到ListView
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ba);//新建并配置ArrayAapeter
        listView.setAdapter(adapter);
        //listview添加完毕
        //侦听事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                a=(String) ba.get(i);
                String mac=a.substring((a.indexOf(" : ")+3));
                Intent intent=new Intent(BltActivity.this,BltconActivity.class);
                intent.putExtra("mac",mac);
                startActivity(intent);
            }
        });

    }
}
