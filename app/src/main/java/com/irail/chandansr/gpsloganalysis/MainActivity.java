package com.irail.chandansr.gpsloganalysis;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity  implements LocationListener{

    Button start,stop,deletedata,showlog,processlog;
    LocationManager locationManager;
    Location location;
    String TAG = "GPS Log Analyis";
    CheckBox checkgps,checknetwork,checkpassive;
    GpsStatus gpsStatus;
    Boolean mFirstfix = false;
    long mStartTime,mTTFF;
    TextView mTTFFText,mFixCount,mLatText,mLongText,mAccuracyText,mAltitudeText;
    int mFixNo =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        start = (Button)findViewById(R.id.start);
        stop = (Button)findViewById(R.id.stop);
        deletedata = (Button)findViewById(R.id.delete);
        showlog = (Button)findViewById(R.id.showlog);
        processlog = (Button)findViewById(R.id.processlog);
        checkgps =(CheckBox)findViewById(R.id.checkGps);
        checknetwork=(CheckBox)findViewById(R.id.checknetwork);
        checkpassive=(CheckBox)findViewById(R.id.checkpassive);
        mTTFFText =(TextView)findViewById(R.id.ttffvalue);
        mFixCount =(TextView)findViewById(R.id.fixcountvalue);
        mLatText =(TextView)findViewById(R.id.latdisplayvalue);
        mLongText =(TextView)findViewById(R.id.longdisplayvalue);
        mAccuracyText=(TextView)findViewById(R.id.accuracyvalue);
        mAltitudeText=(TextView)findViewById(R.id.altitudevalue);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGps();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopgps();
            }
        });
        showlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showlog();
            }
        });
        processlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLog();
            }
        });
        checkgps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Toast.makeText(getApplicationContext(),"GPS Provider Enabled",Toast.LENGTH_SHORT).show();
            }
        });
        checknetwork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Toast.makeText(getApplicationContext(),"Network Provider Enabled",Toast.LENGTH_SHORT).show();
            }
        });
        checkpassive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Toast.makeText(getApplicationContext(),"Passive Provider Enabled",Toast.LENGTH_SHORT).show();
            }
        });
        deletedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this,deletedata);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().toString().equals("Position")) {
                            Toast.makeText(getApplicationContext(), "Delete Position", Toast.LENGTH_SHORT).show();
                            Bundle extra = new Bundle();
                            extra.putBoolean("position",true);
                            locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER,"delete_aiding_data",extra);
                        }
                        else if(item.getTitle().toString().equals("Almanac")) {
                            Toast.makeText(getApplicationContext(), "Delete Almanac", Toast.LENGTH_SHORT).show();
                            Bundle extra = new Bundle();
                            extra.putBoolean("almanac",true);
                            extra.putBoolean("alamanc-GLO",true);
                            extra.putBoolean("alamanc-BDS",true);
                            locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER,"delete_aiding_data",extra);
                        }
                        else if(item.getTitle().toString().equals("Ephemeris")) {
                            Toast.makeText(getApplicationContext(), "Delete Ephemeris", Toast.LENGTH_SHORT).show();
                            Bundle extra = new Bundle();
                            extra.putBoolean("ephemeris",true);
                            extra.putBoolean("ephemeris-GLO",true);
                            extra.putBoolean("ephemeris-BDS",true);
                            locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER,"delete_aiding_data",extra);
                        }
                        else if(item.getTitle().toString().equals("Time")) {
                            Toast.makeText(getApplicationContext(), "Delete Time", Toast.LENGTH_SHORT).show();
                            Bundle extra = new Bundle();
                            extra.putBoolean("time",true);
                            extra.putBoolean("time-gps",true);
                            extra.putBoolean("time-glo",true);
                            extra.putBoolean("time-BDS",true);
                            locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER,"delete_aiding_data",extra);
                        }
                        else if(item.getTitle().toString().equals("All Parameters")) {
                            Toast.makeText(getApplicationContext(), "Delete ALL Parameters", Toast.LENGTH_SHORT).show();
                            locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER,"delete_aiding_data",null);
                        }
                        return  true;
                    }
                });
                popupMenu.show();
              //  new AlertDialog.Builder(getApplicationContext()).setTitle("Delete Mode").show();
                //locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER,"delete_aiding_data",null);
            }
        });
    }

    void startGps(){
        Log.d(TAG , " Checking for Permissin Granted");
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  }, 1);

        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG , "Permissin Granted");
            Log.d(TAG,checkgps.isChecked()+"");
            Log.d(TAG,checknetwork.isChecked()+"");
            Log.d(TAG,checkpassive.isChecked()+"");

            if(checkgps.isChecked()) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                mStartTime= SystemClock.elapsedRealtime();
            }
            else if (checknetwork.isChecked()) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }
            else if(checkpassive.isChecked()) {
                locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, this);
            }
            else{
                Toast.makeText(getApplicationContext(),"None of the Provider is Enabled",Toast.LENGTH_LONG).show();
                Log.d(TAG,"Request Location has not been fired");
            }
        }

    }


    @Override
    public void onLocationChanged(Location location) {
        mFixNo++;
        gpsStatus = locationManager.getGpsStatus(gpsStatus);
        if(!mFirstfix){
            mTTFF= SystemClock.elapsedRealtime()-mStartTime;
            mFirstfix = true;
            Log.d(TAG,mTTFF+"");
            mTTFFText.setText(mTTFF+"");
        }
        mLatText.setText(location.getLatitude()+"");
        mLongText.setText(location.getLongitude()+"");
        mAccuracyText.setText(location.getAccuracy()+"");
        mAltitudeText.setText(location.getAltitude()+"");
        mFixCount.setText(mFixNo+"");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    void stopgps(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
            mFirstfix = false;
        }
    }

    void showlog(){
        Log.d(TAG,"Launching new activity to display Logs");
        Intent intent = new Intent(this,LogActivity.class);
        startActivity(intent);
    }

    void processLog(){
        Log.d(TAG ,"Launching new activity to process Logs");
        Intent intent = new Intent(this,LogProcessAcitivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.actionbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.suplact:
                break;
        }
        return true;
    }
}
