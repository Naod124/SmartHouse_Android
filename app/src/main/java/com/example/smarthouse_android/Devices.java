package com.example.smarthouse_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Devices extends AppCompatActivity {

    BufferedReader bufferedReader;
    PrintWriter printWriter;

 static int temperature;
 static int humStatus;
String lightStatus;
String doorStatus;
String windowStatus;


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
        getDeviceStatus();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Devices");

        FloatingActionButton add, edit;
        ExtendedFloatingActionButton setting;

        final Boolean[] isAllFabsVisible = new Boolean[1];

        add = findViewById(R.id.addDevice);
        setting = findViewById(R.id.setting);
        edit = findViewById(R.id.delete);



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

        getDeviceStatus();

        add.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);
        isAllFabsVisible[0] = false;
        // Set the Extended floating action button to
        // shrinked state initially
        setting.shrink();
        // We will make all the FABs and action name texts
        // visible only when Parent FAB button is clicked So
        // we have to handle the Parent FAB button first, by
        // using setOnClickListener you can see below
        setting.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible[0]) {
                            // when isAllFabsVisible becomes
                            // true make all the action name
                            // texts and FABs VISIBLE.
                            add.show();
                            edit.show();

                            // Now extend the parent FAB, as
                            // user clicks on the shrinked
                            // parent FAB
                            setting.extend();
                            // make the boolean variable true as
                            // we have set the sub FABs
                            // visibility to GONE
                            isAllFabsVisible[0] = true;
                        } else {
                            // when isAllFabsVisible becomes
                            // true make all the action name
                            // texts and FABs GONE.
                            add.hide();
                            edit.hide();

                            // Set the FAB to shrink after user
                            // closes all the sub FABs
                            setting.shrink();
                            // make the boolean variable false
                            // as we have set the sub FABs
                            // visibility to GONE
                            isAllFabsVisible[0] = false;
                        }
                    }
                });
        // below is the sample action to handle add person
        // FAB. Here it shows simple Toast msg. The Toast
        // will be shown only when they are visible and only
        // when user clicks on them
        edit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Devices.this, EditDevice.class));
                    }
                });
        // below is the sample action to handle add alarm
        // FAB. Here it shows simple Toast msg The Toast
        // will be shown only when they are visible and only
        // when user clicks on them
        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Devices.this, AddDevice.class));
                    }
                });

        final Handler handler = new Handler();
        final int delay = 1000; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            public void run() {
              getDeviceStatus();
              handler.postDelayed(this, delay);
            }
        }, delay);

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Devices");
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

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.devicesmain,menu);

        MenuItem btnn =  menu.findItem(R.id.bar_switch);
        MenuItem  lightMode =  menu.findItem(R.id.app_switch);

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
        Call<DeviceModel> call = api.getDevices();
        TextView humidity = (TextView) findViewById(R.id.humidityText);
        TextView  temp = (TextView) findViewById(R.id.tempText);
        new Thread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {

                try {
                    Response response = call.execute();

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