package com.example.smarthouse_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthouse_android.Model.DeviceModel;
import com.example.smarthouse_android.Network.APIService;
import com.example.smarthouse_android.Network.RetrofitInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Devices extends AppCompatActivity {

    BufferedReader bufferedReader;
    PrintWriter printWriter;
 static int temperature;
 static int humStatus;

 String speak;

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
        ImageView lampOff = (ImageView) findViewById(R.id.lampOff);
        ImageView  doorClosed = (ImageView) findViewById(R.id.doorClosed);
        ImageView   windowClosed = (ImageView) findViewById(R.id.windowClosed);
        ImageButton speechRec = findViewById(R.id.speech);

        speechRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySpeechRecognizer();
            }
        });
        getDeviceStatus();
        final Handler handler = new Handler();
        final int delay = 1000; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            public void run() {
              getDeviceStatus();
              handler.postDelayed(this, delay);
            }
        }, delay);

        lampSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (lampSwitch.isChecked()) {
                    APIService api = RetrofitInstance.getRetrofitInstance().create(APIService.class);
                    Call<DeviceModel> call = api.update("LightSwitch","LIGHT");

                    call.enqueue(new Callback<DeviceModel>() {
                        @Override
                        public void onResponse(Call<DeviceModel> call, Response<DeviceModel> response) {
                            System.out.println("************************LightSwitch" + response.code() + "**********************");
                        }

                        @Override
                        public void onFailure(Call<DeviceModel> call, Throwable t) {

                        }
                    });

                    lamp.setText("LIGHT");
                    sendMessage( lamp.getText().toString());
                    lampOff.setImageResource(R.drawable.lighton);
                } else {
                    APIService api = RetrofitInstance.getRetrofitInstance().create(APIService.class);
                    Call<DeviceModel> call = api.update("LightSwitch","DARK");

                    call.enqueue(new Callback<DeviceModel>() {
                        @Override
                        public void onResponse(Call<DeviceModel> call, Response<DeviceModel> response) {
                            System.out.println("************************LightSwitch" + response.code() + "**********************");
                        }

                        @Override
                        public void onFailure(Call<DeviceModel> call, Throwable t) {

                        }
                    });

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
                    APIService api = RetrofitInstance.getRetrofitInstance().create(APIService.class);
                    Call<DeviceModel> call = api.update("DoorSwitch","OPEN");

                    call.enqueue(new Callback<DeviceModel>() {
                        @Override
                        public void onResponse(Call<DeviceModel> call, Response<DeviceModel> response) {
                            System.out.println("************************DoorSwitch" + response.code() + "**********************");
                        }

                        @Override
                        public void onFailure(Call<DeviceModel> call, Throwable t) {

                        }
                    });
                    door.setText("OPEN");
                    sendMessage(door.getText().toString());
                    doorClosed.setImageResource(R.drawable.opendoor);
                }else {
                    APIService api = RetrofitInstance.getRetrofitInstance().create(APIService.class);
                    Call<DeviceModel> call = api.update("DoorSwitch","CLOSED");

                    call.enqueue(new Callback<DeviceModel>() {
                        @Override
                        public void onResponse(Call<DeviceModel> call, Response<DeviceModel> response) {
                            System.out.println("************************DoorSwitch" + response.code() + "**********************");
                        }

                        @Override
                        public void onFailure(Call<DeviceModel> call, Throwable t) {

                        }
                    });
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
                    APIService api = RetrofitInstance.getRetrofitInstance().create(APIService.class);
                    Call<DeviceModel> call = api.update("WindowSwitch","open");

                    call.enqueue(new Callback<DeviceModel>() {
                        @Override
                        public void onResponse(Call<DeviceModel> call, Response<DeviceModel> response) {
                            System.out.println("************************WindowSwitch " + response.code() + "**********************");
                        }

                        @Override
                        public void onFailure(Call<DeviceModel> call, Throwable t) {

                        }
                    });
                    window.setText("OPEN");
                    sendMessage("open");
                    windowClosed.setImageResource(R.drawable.openwindow);
                }
                else{
                    APIService api = RetrofitInstance.getRetrofitInstance().create(APIService.class);
                    Call<DeviceModel> call = api.update("WindowSwitch","shut");

                    call.enqueue(new Callback<DeviceModel>() {
                        @Override
                        public void onResponse(Call<DeviceModel> call, Response<DeviceModel> response) {
                            System.out.println("************************WindowSwitch" + response.code() + "**********************");
                        }

                        @Override
                        public void onFailure(Call<DeviceModel> call, Throwable t) {

                        }
                    });
                    window.setText("CLOSED");
                    sendMessage("shut");
                    windowClosed.setImageResource(R.drawable.closedwindow);

                }
            }
        });


    }
    private static final int SPEECH_REQUEST_CODE = 0;

    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch  lampSwitch = findViewById(R.id.lightSwitch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch   doorSwitch = findViewById(R.id.doorSwitch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch    windowSwitch = findViewById(R.id.windowSwitch);
        ImageButton speechRec = findViewById(R.id.speech);

        if (requestCode != SPEECH_REQUEST_CODE || resultCode != RESULT_OK) {
        }
        else {

            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            String spokenText = results.get(0);

            // Do something with spokenText
            if (spokenText.equalsIgnoreCase("Light on")) {
                lampSwitch.setChecked(true);
            }
            else if (spokenText.equalsIgnoreCase("Light off")) {
                lampSwitch.setChecked(false);
            }
            else if(spokenText.equalsIgnoreCase("Door open")){
                doorSwitch.setChecked(true);
            }else if(spokenText.equalsIgnoreCase("Door close")) {
                doorSwitch.setChecked(false);
            }
            else if(spokenText.equalsIgnoreCase("Window open")) {
                windowSwitch.setChecked(true);
            }
            else if(spokenText.equalsIgnoreCase("Window close")) {
                windowSwitch.setChecked(false);
            }else{
                Toast.makeText(Devices.this,"Pronounce the correct commands",Toast.LENGTH_LONG).show();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
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