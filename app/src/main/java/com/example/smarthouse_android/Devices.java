package com.example.smarthouse_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.smarthouse_android.Model.DeviceModel;
import com.example.smarthouse_android.Network.APIService;
import com.example.smarthouse_android.Network.RetrofitInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import retrofit2.Call;
import retrofit2.Response;

public class Devices extends AppCompatActivity {

    BufferedReader bufferedReader;
    PrintWriter printWriter;
   /* TextView lamp;
    TextView door;
    TextView window;
    ImageView lampOn;
    ImageView lampOff;
    ImageView doorOpen;
    ImageView doorClosed;
    ImageView windowOpen;
    ImageView windowClosed;
    TextView humidity;
    TextView temp;
    */



 static int temperature;
 static int humStatus;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch  lampSwitch = findViewById(R.id.lightSwitch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch   doorSwitch = findViewById(R.id.doorSwitch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch    windowSwitch = findViewById(R.id.windowSwitch);


        TextView lamp = (TextView) findViewById(R.id.lampOnOff);
        TextView door = (TextView) findViewById(R.id.doorOnOFF);
        TextView humidity = (TextView) findViewById(R.id.humidityText);
        TextView  temp = (TextView) findViewById(R.id.tempText);
        TextView window = (TextView) findViewById(R.id.windowOnOfff);
        // lampOn = (ImageView) findViewById(R.id.lam);
        ImageView lampOff = (ImageView) findViewById(R.id.lampOff);
        //doorOpen = (ImageView) findViewById(R.id.dooropen1);
        ImageView  doorClosed = (ImageView) findViewById(R.id.doorClosed);
        //windowOpen = (ImageView) findViewById(R.id.windowopen1);
        ImageView   windowClosed = (ImageView) findViewById(R.id.windowClosed);

        Button button = findViewById(R.id.button2);

        getDeviceStatus();



        APIService api = RetrofitInstance.getRetrofitInstance().create(APIService.class);
        Call<DeviceModel> call= api.getDevices();


        lampSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (lampSwitch.isChecked()) {
                    lamp.setText("LIGHT");
                    sendMessage( lamp.getText().toString());
                    lampOff.setImageResource(R.drawable.lighton);
                } else {
                    lamp.setText("DARK");
                    sendMessage(lamp.getText().toString());
                    lampOff.setImageResource(R.drawable.lightoff);
                }
            }
        });

        doorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (doorSwitch.isChecked()) {
                    door.setText("OPEN");
                    sendMessage(door.getText().toString());
                    doorClosed.setImageResource(R.drawable.opendoor);
                }else {
                    door.setText("CLOSED");
                    sendMessage(door.getText().toString());
                    doorClosed.setImageResource(R.drawable.doorclosed);
                }
            }
        });

        windowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (windowSwitch.isChecked()){
                    window.setText("OPEN");
                    sendMessage("open");
                    windowClosed.setImageResource(R.drawable.openwindow);
                }
                else{
                    window.setText("CLOSED");
                    sendMessage("shut");
                    windowClosed.setImageResource(R.drawable.closedwindow);

                }
            }
        });


    }



    public void getDeviceStatus(){
        APIService api = RetrofitInstance.getRetrofitInstance().create(APIService.class);
        Call<DeviceModel> call = api.getDevices();
        TextView humidity = (TextView) findViewById(R.id.humidityText);
        TextView  temp = (TextView) findViewById(R.id.tempText);
        new Thread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                Response response = null;
                try {
                    response = call.execute();

                if (response.isSuccessful()) {
                    DeviceModel deviceModel = (DeviceModel) response.body();
                // temperature = deviceModel.getTemperature();
                  //  humStatus = deviceModel.getHumidity();
                    temp.setText(String.valueOf(deviceModel.getTemperature()) +  "°C");
                    humidity.setText(String.valueOf(deviceModel.getHumidity())+ "%");

                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    void sendMessage(final String input){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("194.47.46.148", 2400);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out.println(input);
                    final String answer = in.readLine();

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