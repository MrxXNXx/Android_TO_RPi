package com.mrz.raspberrypiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class PiconActivity extends AppCompatActivity {
    String ip;
    int Port;
    PrintStream out;
    Socket socket;
    Button up,down,left,right;
    Handler mhandler;
    static int pan=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picon);
        up=findViewById(R.id.send_up);
        down=findViewById(R.id.send_down);
        left=findViewById(R.id.send_left);
        right=findViewById(R.id.send_right);
        Intent intent = getIntent();
        ip = intent.getStringExtra("ip");
        Port = intent.getIntExtra("port", 0);
        mhandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 1:pan=1;
                }
            }
        };
        new Thread(){
            @Override
            public void run() {
                try {
                    socket=new Socket(ip,Port);
                    out=new PrintStream(socket.getOutputStream());
                    Message message=new Message();
                    message.what=1;
                    mhandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread()
        {
            @Override
            public void run() {
                while (true)
                {
                    if(pan==1)
                    {
                        if(up.isPressed()==true)
                        {
                            out.println("w");
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if(down.isPressed()==true)
                        {
                            out.println("s");
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if(left.isPressed()==true)
                        {
                            out.println("a");
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if(right.isPressed()==true)
                        {
                            out.println("d");
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
