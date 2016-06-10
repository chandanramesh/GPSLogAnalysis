package com.irail.chandansr.gpsloganalysis;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LogProcessAcitivity extends AppCompatActivity {
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    ListView loglist;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_process_acitivity);
        loglist =(ListView)findViewById(R.id.loglists);
        if( ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE,1);
        }
        Bundle bundle = getIntent().getExtras();
        String path = bundle.getString("Path");
        Log.d("Chandan",path);
        File file = new File(path);
        //file.delete();
        //Log.d("Chandan",file.getAbsolutePath());
       // Log.d("Chandan",file.exists()+"");
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        try {
            FileReader fw = new FileReader(file);
          //  Log.d("Chandan",fw.ready()+"")
           BufferedReader bufferedReader = new BufferedReader(fw);
            while(bufferedReader.ready()){

               String line = bufferedReader.readLine();
                if(line.contains("LgeGpsLocationProvider")|| line.contains("GpsLocationProvider")|| line.contains("LocationManagerService")){
                    Log.d("Chandan",line);
                    if(line.contains("LocationManagerService: request") || line.contains("START NAVIGATION")||line.contains("setSUPLServerWithTLS")||line.contains("enableLocationTracking")
                            ||line.contains("TTFF")||line.contains("LgeGpsIndicator: STATE_FIX")|| line.contains("incoming location")||line.contains("request_to_modem()"))
                   arrayList.add(line.substring(32));

                }

                //Log.d("Chandan",I+"");
            }
        } catch (IOException e ) {
            e.printStackTrace();
        }

        loglist.setAdapter(arrayAdapter);
    }
}
