package com.example.smarthouse_android;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity  {



    public static final int SERVERPORT = 2400;
    public static final String SERVER_IP = "192.168.0.37";
   //public boolean valid = false;
   public String valid = "1234";

    public Button btn;
    public EditText text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btn = (Button) findViewById(R.id.button);
        text = (EditText) findViewById(R.id.pin);




       btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               String pass = text.getText().toString();
               if (pass.isEmpty()){
                   text.setError("Filed is empty");
               }
               else {
                   //sendMessage(pass);
                   loginUser(pass);
               }

           }
       });


    }

    public void loginUser(String pinCode){
        sendMessage(pinCode);
    }



    private void sendMessage(String toString) {
        text = (EditText) findViewById(R.id.pin);

        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Socket s = new Socket(SERVER_IP, SERVERPORT);



                    PrintWriter output = new PrintWriter(s.getOutputStream(),true);
                    output.println(toString);


                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    final String pass = input.readLine();
                  
                    if(pass.equalsIgnoreCase("Correct")){
                        startActivity(new Intent(MainActivity.this, Devices.class));
                    }
                    else {
                        text.setError("Wrong pin code");
                    }


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



