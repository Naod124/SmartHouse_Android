package com.example.smarthouse_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;

import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthouse_android.Model.DeviceModel;
import com.example.smarthouse_android.Model.Door;
import com.example.smarthouse_android.Model.Light;
import com.example.smarthouse_android.Model.TempHumi;
import com.example.smarthouse_android.Model.Window;
import com.example.smarthouse_android.Network.APIService;
import com.example.smarthouse_android.Network.RetrofitInstance;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
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
Light[] lightStatus;
Door[] doorStatus;
Window[] windowStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_Light);
        if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);

        }else {
            setTheme(R.style.Theme_Light);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        getDeviceStatus();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Devices");

        FloatingActionButton add, edit;
        ExtendedFloatingActionButton setting;

        final Boolean[] isAllFabsVisible = new Boolean[1];




        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch  lampSwitch = findViewById(R.id.lightSwitch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch  lampSwitch1 = findViewById(R.id.lightSwitch2);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch   doorSwitch = findViewById(R.id.doorSwitch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch    windowSwitch = findViewById(R.id.windowSwitch);

        TextView lamp = (TextView) findViewById(R.id.lampOnOff);
        TextView lamp1 = (TextView) findViewById(R.id.lampOnOff);
        TextView door = (TextView) findViewById(R.id.doorOnOFF);
        TextView humidity = (TextView) findViewById(R.id.humidityText);
        TextView  temp = (TextView) findViewById(R.id.tempText);
        TextView window = (TextView) findViewById(R.id.windowOnOfff);
        ImageView lampOff = (ImageView) findViewById(R.id.lampOff);
        ImageView lampOff1 = (ImageView) findViewById(R.id.lampOff2);
        ImageView  doorClosed = (ImageView) findViewById(R.id.doorClosed);
        ImageView   windowClosed = (ImageView) findViewById(R.id.windowClosed);
        Button alarmButton = (Button) findViewById(R.id.fireOff);
        //ImageButton poweroff = (ImageButton) findViewById(R.id.powerOff);

        //alarmButton.setBackground(null);
        //poweroff.setBackground(null);

        ImageButton imageButton = new ImageButton(this);
        imageButton.setBackground(null);

        getDeviceStatus();

        File lightFile1 = new File("statusLight1.txt");
        File lightFile2 = new File("statusLight2.txt");
        File doorFile = new File("statusDoor.txt");
        File windowFile = new File("statusWindow.txt");

        String oldContent = "";

        BufferedReader reader = null;

        final FileWriter[] writer = {null};



        final Handler handler = new Handler();
        final int delay = 1000; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            public void run() {
              getDeviceStatus();
              handler.postDelayed(this, delay);
            }
        }, delay);


        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIService api = RetrofitInstance.getRetrofitInstance().create(APIService.class);
                Call<DeviceModel> call = api.update("lights","3","false");

                call.enqueue(new Callback<DeviceModel>() {
                    @Override
                    public void onResponse(Call<DeviceModel> call, Response<DeviceModel> response) {
                        System.out.println("************************LightSwitch" + response.code() + "**********************");
                    }

                    @Override
                    public void onFailure(Call<DeviceModel> call, Throwable t) {

                    }
                });

            }
        });

        lampSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (lampSwitch.isChecked()) {
                    APIService api = RetrofitInstance.getRetrofitInstance().create(APIService.class);
                    Call<DeviceModel> call = api.update("lights","1","true");

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
                    String newContent = oldContent.replaceAll("0", "1");

                    //Rewriting the input text file with newContent

                    try {
                        writer[0] = new FileWriter(lightFile1);
                        writer[0].write(newContent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    APIService api = RetrofitInstance.getRetrofitInstance().create(APIService.class);
                    Call<DeviceModel> call = api.update("lights","1","false");

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
                    String newContent = oldContent.replaceAll("1", "0");

                    //Rewriting the input text file with newContent

                    try {
                        writer[0] = new FileWriter(lightFile1);
                        writer[0].write(newContent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        lampSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (lampSwitch.isChecked()) {
                    APIService api = RetrofitInstance.getRetrofitInstance().create(APIService.class);
                    Call<DeviceModel> call = api.update("lights","2","true");

                    call.enqueue(new Callback<DeviceModel>() {
                        @Override
                        public void onResponse(Call<DeviceModel> call, Response<DeviceModel> response) {
                            System.out.println("************************LightSwitch" + response.code() + "**********************");
                        }

                        @Override
                        public void onFailure(Call<DeviceModel> call, Throwable t) {

                        }
                    });

                    lamp1.setText("LIGHT");
                    sendMessage( lamp.getText().toString());
                    lampOff1.setImageResource(R.drawable.lighton);
                    String newContent = oldContent.replaceAll("0", "1");

                    //Rewriting the input text file with newContent

                    try {
                        writer[0] = new FileWriter(lightFile2);
                        writer[0].write(newContent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    APIService api = RetrofitInstance.getRetrofitInstance().create(APIService.class);
                    Call<DeviceModel> call = api.update("lights","2","false");

                    call.enqueue(new Callback<DeviceModel>() {
                        @Override
                        public void onResponse(Call<DeviceModel> call, Response<DeviceModel> response) {
                            System.out.println("************************LightSwitch" + response.code() + "**********************");
                        }

                        @Override
                        public void onFailure(Call<DeviceModel> call, Throwable t) {

                        }
                    });

                    lamp1.setText("DARK");
                    sendMessage(lamp.getText().toString());
                    lampOff1.setImageResource(R.drawable.lightoff);
                    String newContent = oldContent.replaceAll("1", "0");

                    //Rewriting the input text file with newContent

                    try {
                        writer[0] = new FileWriter(lightFile2);
                        writer[0].write(newContent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        doorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (doorSwitch.isChecked()) {
                    APIService api = RetrofitInstance.getRetrofitInstance().create(APIService.class);
                    Call<DeviceModel> call = api.update("doors","1","true");

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
                    String newContent = oldContent.replaceAll("0", "1");

                    //Rewriting the input text file with newContent

                    try {
                        writer[0] = new FileWriter(doorFile);
                        writer[0].write(newContent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    APIService api = RetrofitInstance.getRetrofitInstance().create(APIService.class);
                    Call<DeviceModel> call = api.update("doors","1","false");

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
                    String newContent = oldContent.replaceAll("1", "0");

                    //Rewriting the input text file with newContent

                    try {
                        writer[0] = new FileWriter(doorFile);
                        writer[0].write(newContent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        windowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (windowSwitch.isChecked()){
                    APIService api = RetrofitInstance.getRetrofitInstance().create(APIService.class);
                    Call<DeviceModel> call = api.update("windows","1","true");

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
                    String newContent = oldContent.replaceAll("0", "1");

                    //Rewriting the input text file with newContent

                    try {
                        writer[0] = new FileWriter(windowFile);
                        writer[0].write(newContent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    APIService api = RetrofitInstance.getRetrofitInstance().create(APIService.class);
                    Call<DeviceModel> call = api.update("windows","1","false");

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
                    String newContent = oldContent.replaceAll("1", "0");

                    //Rewriting the input text file with newContent

                    try {
                        writer[0] = new FileWriter(windowFile);
                        writer[0].write(newContent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.devicesmain,menu);

        MenuItem btnn =  menu.findItem(R.id.bar_switch);
        MenuItem  lightMode =  menu.findItem(R.id.app_switch);
        MenuItem  power =  menu.findItem(R.id.shutoff);

        btnn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                return false;
            }
        });

        lightMode.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                return false;
            }
        });

        power.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(Devices.this,MainActivity.class));

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.voice_say){

            displaySpeechRecognizer();
            Toast.makeText(this, "voice clicked", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
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
        //ImageButton speechRec = findViewById(R.id.voice_say);

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
        Call<DeviceModel> call2 = api.getDevice("humidity");
        Call<DeviceModel> call1 = api.getDevice("temperature");
        Call<DeviceModel> call = api.getDevices();
        /*Call getHumi = api.getHumi();
        Call getTemp = api.getTemp();

         */
        TextView humidity = (TextView) findViewById(R.id.humidityText);
        TextView  temp = (TextView) findViewById(R.id.tempText);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch  lampSwitch = findViewById(R.id.lightSwitch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch   doorSwitch = findViewById(R.id.doorSwitch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch    windowSwitch = findViewById(R.id.windowSwitch);
        TextView window = (TextView) findViewById(R.id.windowOnOfff);
        ImageView lampOff = (ImageView) findViewById(R.id.lampOff);
        ImageView  doorClosed = (ImageView) findViewById(R.id.doorClosed);
        ImageView   windowClosed = (ImageView) findViewById(R.id.windowClosed);
        TextView lamp = (TextView) findViewById(R.id.lampOnOff);
        TextView door = (TextView) findViewById(R.id.doorOnOFF);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                   // Response response = call.execute();
                    Response response = call.execute();

                   // Response response1 = getTemp.execute();
                    if (response.isSuccessful()  ) {
                        DeviceModel deviceModel = (DeviceModel) response.body();
                        //DeviceModel deviceModel1 = (DeviceModel) response1.body();
                        temp.setText((deviceModel.getTemperature()) +  "°C");
                        humidity.setText((deviceModel.getHumidity())+ "%");
                        doorStatus = deviceModel.getDoorSwitch();
                        windowStatus = deviceModel.getWindowSwitch();
                        lightStatus = deviceModel.getLightSwitch();




                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
       /* new Thread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {

                try {
                    Response response = call.execute();

                    if (response.isSuccessful()) {
                        DeviceModel deviceModel = (DeviceModel) response.body();
                        temp.setText(String.valueOf(deviceModel.getTemperature()) +  "°C");
                        humidity.setText(String.valueOf(deviceModel.getHumidity())+ "%");
                        doorStatus = deviceModel.getDoorSwitch();
                        windowStatus = deviceModel.getWindowSwitch();
                        lightStatus = deviceModel.getLightSwitch();

                        if (doorStatus.equalsIgnoreCase("OPEN")){
                            doorSwitch.setChecked(true);
                        }
                        if (windowStatus.equalsIgnoreCase("open")){
                            windowSwitch.setChecked(true);
                        }
                        if (lightStatus.equalsIgnoreCase("LIGHT")){
                            lampSwitch.setChecked(true);
                        }



                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        */
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

     /*   DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Devices");
        DatabaseReference ref2 = ref1.child("State");
        DatabaseReference lightRef = ref2.child("LightSwitch");
        DatabaseReference windowRef = ref2.child("WindowSwitch");
        DatabaseReference doorRef = ref2.child("DoorSwitch");

        lightRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lightStatus = snapshot.getValue(String.class);
                if (lightStatus != null && lightStatus.equalsIgnoreCase("LIGHT")){
                    lampSwitch.setChecked(true);
                }
                else {
                    assert lightStatus != null;
                    if(lightStatus.equalsIgnoreCase("DARK")){
                        lampSwitch.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        windowRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                windowStatus = snapshot.getValue(String.class);
                if(windowStatus != null && windowStatus.equalsIgnoreCase("open")){
                    windowSwitch.setChecked(true);
                }
                else {
                    assert windowStatus != null;
                    if(windowStatus.equalsIgnoreCase("shut")){
                        windowSwitch.setChecked(false);

                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        doorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doorStatus =snapshot.getValue(String.class);
                if (doorStatus != null) {
                    if(doorStatus.equalsIgnoreCase("OPEN")){
                        doorSwitch.setChecked(true);
                    }
                    else if(doorStatus.equalsIgnoreCase("CLOSED")){
                        doorSwitch.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

      */

 /*       if (lightStatus.equalsIgnoreCase("LIGHT")){
            lampSwitch.setChecked(true);
        }
        else if(lightStatus.equalsIgnoreCase("DARK")){
            lampSwitch.setChecked(false);
        }
        else if(doorStatus.equalsIgnoreCase("OPEN")){
            doorSwitch.setChecked(true);
        }
        else if(doorStatus.equalsIgnoreCase("CLOSED")){
            doorSwitch.setChecked(false);
        }
        else if(windowStatus.equalsIgnoreCase("open")){
            windowSwitch.setChecked(true);
        }
        else if(windowStatus.equalsIgnoreCase("shut")){
            windowSwitch.setChecked(true);

        }
*/
}