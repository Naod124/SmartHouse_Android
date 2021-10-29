package com.example.smarthouse_android;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Devices extends AppCompatActivity {

    BufferedReader bufferedReader;
    PrintWriter printWriter;
    TextView lamp;
    TextView door;
    TextView window;
    ImageView lampOn;
    ImageView lampOff;
    ImageView doorOpen;
    ImageView doorClosed;
    ImageView windowOpen;
    ImageView windowClosed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        lamp = (TextView) findViewById(R.id.lampText1);
        door = (TextView) findViewById(R.id.doorText1);
        window = (TextView) findViewById(R.id.windowText1);
        lampOn = (ImageView) findViewById(R.id.lampon1);
        lampOff = (ImageView) findViewById(R.id.lampoff1);
        doorOpen = (ImageView) findViewById(R.id.dooropen1);
        doorClosed = (ImageView) findViewById(R.id.doorclosed1);
        windowOpen = (ImageView) findViewById(R.id.windowopen1);
        windowClosed = (ImageView) findViewById(R.id.windowclosed1);
        Switch lampSwitch = findViewById(R.id.lampSwitch1);
        Switch doorSwitch = findViewById(R.id.doorSwitch1);
        Switch windowSwitch = findViewById(R.id.windowSwitch1);
        lampOff.setVisibility(View.INVISIBLE);
        lampOn.setVisibility(View.VISIBLE);
        windowClosed.setVisibility(View.INVISIBLE);
        windowOpen.setVisibility(View.VISIBLE);
        doorClosed.setVisibility(View.INVISIBLE);
        doorOpen.setVisibility(View.VISIBLE);




        lampSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lamp.getText().equals("ON")){
                    sendMessage("DARK", "lamp");
                }else{
                    sendMessage("LIGHT", "lamp");
                }
            }
        });


        doorSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (door.getText().equals("OPEN")){
                    sendMessage("CLOSED", "door");
                }else{
                    sendMessage("OPEN", "door");
                }
            }
        });

        windowSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (window.getText().equals("OPEN")){
                    sendMessage("shut", "window");
                }else{
                    sendMessage("open", "window");
                }
            }
        });
    }



    void sendMessage(final String input, String choice){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("192.168.0.41", 9999);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out.println(input);
                    final String answer = in.readLine();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            if (choice.equalsIgnoreCase("lamp")){
                                lamp.setText(answer);
                                if (answer.equalsIgnoreCase("ON")){
                                    lampOff.setVisibility(View.INVISIBLE);
                                    lampOn.setVisibility(View.VISIBLE);
                                }
                                else {
                                    lampOn.setVisibility(View.INVISIBLE);
                                    lampOff.setVisibility(View.VISIBLE);
                                }
                            }else if (choice.equalsIgnoreCase("door")){
                                door.setText(answer);

                                if (answer.equalsIgnoreCase("OPEN")){
                                    doorClosed.setVisibility(View.INVISIBLE);
                                    doorOpen.setVisibility(View.VISIBLE);
                                }
                                else {
                                    doorOpen.setVisibility(View.INVISIBLE);
                                    doorClosed.setVisibility(View.VISIBLE);
                                }
                            }else if (choice.equalsIgnoreCase("window") ){
                                window.setText(answer);

                                if (answer.equalsIgnoreCase("OPEN")){
                                    windowClosed.setVisibility(View.INVISIBLE);
                                    windowOpen.setVisibility(View.VISIBLE);
                                }
                                else {
                                    windowOpen.setVisibility(View.INVISIBLE);
                                    windowClosed.setVisibility(View.VISIBLE);
                                }
                            } }});

                    if(bufferedReader != null)
                        bufferedReader.close();
                    if(printWriter != null)
                        printWriter.close();
                    socket.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }).start();
    }

}