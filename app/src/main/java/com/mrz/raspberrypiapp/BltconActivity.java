package com.mrz.raspberrypiapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import java.io.PrintStream;
import java.util.UUID;

public class BltconActivity extends AppCompatActivity {
    BluetoothSocket mSocket=null;
    BluetoothAdapter mBluetoothAdapter;
    PrintStream out;
    private Handler handler;
    Button bup,bdown,bleft,bright;
    int cmp=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bltcon);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Intent intent = getIntent();

        //下面为主要
        bup=findViewById(R.id.bsend_up);
        bdown=findViewById(R.id.bsend_down);
        bleft=findViewById(R.id.bsend_left);
        bright=findViewById(R.id.bsend_right);
        mBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
        String macAddr=intent.getStringExtra("mac");
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(macAddr);
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        try {
            mSocket = device.createRfcommSocketToServiceRecord(uuid);

        } catch (Exception e) {

            e.printStackTrace();
        }
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 1:cmp=1;
                }
            }
        };
        //下面这段有问题
        new Thread(){
            @Override
            public void run() {
                mBluetoothAdapter.cancelDiscovery();
                try {
                    mSocket.connect();
                    out =new PrintStream(mSocket.getOutputStream());
                    Message message=new Message();
                    message.what=1;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }}

        }.start();
        new Thread()
        {
            @Override
            public void run() {
                while (true)
                {
                    if(cmp==1)
                    {
                        if(bup.isPressed()==true)
                        {
                            out.print("w");
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                        if(bdown.isPressed()==true)
                        {
                            out.print("s");
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if(bleft.isPressed()==true)
                        {
                            out.print("a");
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if(bright.isPressed()==true)
                        {
                            out.print("d");
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }.start();
    }
}
