package com.mrz.raspberrypiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;

public class LanActivity extends AppCompatActivity {
    EditText ipt;
    EditText portt;
    Button reset;
    Button conpi;
    Socket socket;
    Handler mhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lan);
        ipt=findViewById(R.id.text_ip);
        portt=findViewById(R.id.text_port);
        reset=findViewById(R.id.reset_text);
        conpi=findViewById(R.id.ConnectPi);
        mhandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        Toast.makeText(LanActivity.this,"网络连接失败，请检查网络设置",Toast.LENGTH_SHORT).show();
                }
            }
        };
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipt.setText("");
                portt.setText("");
            }
        });
        conpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(){
                        @Override
                    public void run() {
                        super.run();
                        try {
                            String ip;
                            int porte;
                            ip=new String(ipt.getText().toString());
                            porte=new Integer(Integer.parseInt(portt.getText().toString()));
                            socket=new Socket(ip,porte);
                            socket.close();
                            Intent intent=new Intent(LanActivity.this,PiconActivity.class);
                            intent.putExtra("ip",ip);
                            intent.putExtra("port",porte);

                            startActivity(intent);
                        } catch (IOException e) {
                            Message message=new Message();
                            message.what=1;
                            mhandler.sendMessage(message);
                        }
                    }
                }.start();
            }
        });
    }
}
