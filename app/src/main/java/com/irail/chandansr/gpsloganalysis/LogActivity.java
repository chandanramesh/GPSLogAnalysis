package com.irail.chandansr.gpsloganalysis;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Process;

import java.io.IOException;

public class LogActivity extends AppCompatActivity {
    String TAG = "GPS Log Analyis";
    TextView logdisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        logdisplay = (TextView)findViewById(R.id.logbox);
        try{
              Process process= Runtime.getRuntime().exec("logcat");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder log = new StringBuilder();
            //String line ;
            Log.d("Buu",bufferedReader.ready()+"");
            while(bufferedReader.ready()){
                String line = bufferedReader.readLine();
               // String linecontent = line.toString();
                Log.d("Length",line.length()+"");
                //Log.d("Chandan" , line);
                //if(linecontent.contains("LocationManager")) {
                   // log.append(line);
               // }
            //  if(bufferedReader.)
                //logdisplay.setText(linecontent);
                Log.d("Chandan test" , line);
            }



        }
        catch (IOException e){
            Log.d(TAG ,"IO Exception in reading the LogCat Process");
        }
    }
}
