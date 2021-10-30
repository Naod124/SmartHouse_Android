package com.example.smarthouse_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CompoundButton;
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
       // lampOff = (ImageView) findViewById(R.id.lampoff1);
        doorOpen = (ImageView) findViewById(R.id.dooropen1);
      //  doorClosed = (ImageView) findViewById(R.id.doorclosed1);
        windowOpen = (ImageView) findViewById(R.id.windowopen1);
       // windowClosed = (ImageView) findViewById(R.id.windowclosed1);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch  lampSwitch = findViewById(R.id.lampSwitch1);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch   doorSwitch = findViewById(R.id.doorSwitch1);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch    windowSwitch = findViewById(R.id.windowSwitch1);



        lampSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (lampSwitch.isChecked()) {
                    lamp.setText("LIGHT");




                    sendMessage( lamp.getText().toString());

                    // client should not be able to acces the database and update it for now. Server should handle it
                    // db.UpdateLampElement(lamptxt.getText().toString());

                    lampOn.setImageResource(R.drawable.lighton);
                } else {
                    lamp.setText("DARK");

                    sendMessage(lamp.getText().toString());
                    // db.UpdateLampElement(lamptxt.getText().toString());
                    lampOn.setImageResource(R.drawable.lightoff);
                }

            }
        });

        doorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (doorSwitch.isChecked()) {
                    door.setText("OPEN");
                    sendMessage(door.getText().toString());
                    doorOpen.setImageResource(R.drawable.opendoor);

                }else {
                    door.setText("CLOSED");
                    sendMessage(door.getText().toString());
                    doorOpen.setImageResource(R.drawable.doorclosed);

                }

            }
        });

        windowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (windowSwitch.isChecked()){
                    window.setText("OPEN");
                    sendMessage("open");
                    windowOpen.setImageResource(R.drawable.openwindow);
                }

                else{
                    window.setText("CLOSED");
                    sendMessage("shut");
                    windowOpen.setImageResource(R.drawable.closedwindow);

                }
            }
        });

/*
        lampSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lamp.getText().equals("ON")){
                    sendMessage("DARK", "lamp");
                    lampOn.setImageResource(R.drawable.lightoff);

                }else{
                    lamp.setText("OFF");
                    if (lamp.getText().equals("OFF")) {
                        sendMessage("LIGHT", "lamp");
                        lampOn.setImageResource(R.drawable.lighton);
                    }
                }
            }
        });


        doorSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (door.getText().equals("OPEN")){
                    sendMessage("CLOSED", "door");
                    doorOpen.setImageResource(R.drawable.doorclosed);
                }else{
                    door.setText("CLOSED");
                    if (door.getText().equals("CLOSED")) {
                        sendMessage("OPEN", "door");
                        doorOpen.setImageResource(R.drawable.opendoor);
                    }
                }
            }
        });

        windowSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (window.getText().equals("OPEN")){
                    sendMessage("shut");
                    windowOpen.setImageResource(R.drawable.closedwindow);

                }else{
                    window.setText("CLOSED");
                    if (window.getText().equals("CLOSED")) {
                        sendMessage("open");
                        windowOpen.setImageResource(R.drawable.openwindow);
                    }
                }
            }
        });

 */
    }



    void sendMessage(final String input){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("192.168.0.41", 9999);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out.println(input);
                    final String answer = in.readLine();
                   /* new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            if (choice.equalsIgnoreCase("lamp")){
                                lamp.setText(answer);
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
                            } }});   */

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