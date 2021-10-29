package com.example.smarthouse_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    public static final int SERVERPORT = 2400;
    public static final String SERVER_IP = "194.47.40.186";

    public Switch lampSwitch;
    public TextView lamptxt;
    public ImageView lightON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        lampSwitch = (Switch) findViewById(R.id.lampSwitch);
        lightON = (ImageView) findViewById(R.id.lightON);
        lamptxt = (TextView) findViewById(R.id.lamptxt);




        lampSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (lampSwitch.isChecked()) {
                    lamptxt.setText("LIGHT");



                    sendMessage(lamptxt.getText().toString());

                    lightON.setImageResource(R.drawable.lighton);
                } else {
                    lamptxt.setText("DARK");

                    sendMessage(lamptxt.getText().toString());
                    lightON.setImageResource(R.drawable.lightoff);
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lampSwitch:
                sendMessage(lampSwitch.getText().toString());
        }
    }


    private void sendMessage(String toString) {

        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Socket s = new Socket(SERVER_IP, SERVERPORT);



                    PrintWriter output = new PrintWriter(s.getOutputStream(),true);
                    output.println(toString);


                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    final String st = input.readLine();
                    System.out.println("From server" + st);



                    output.close();

                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        });
        thread.start();
    }
}



