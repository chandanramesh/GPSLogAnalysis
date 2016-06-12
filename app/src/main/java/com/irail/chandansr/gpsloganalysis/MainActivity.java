package com.irail.chandansr.gpsloganalysis;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity  implements LocationListener{

    Button processlog,mSelectlog;
    LocationManager locationManager;
    Location location;
    public final String TAG = "GPS Log Analyis";
    CheckBox checkgps,checknetwork,checkpassive;
    GpsStatus gpsStatus;
    Boolean mFirstfix = false,mIconChange =false;
    long mStartTime,mTTFF;
    TextView mSelectProvider,mTTFFText,mFixCount,mLatText,mLongText,mAccuracyText,mAltitudeText;
    ListView mLeftDrawer;
    int mFixNo =0;
    Uri mcontactUri;
    String mfilePath;
    String[] mDrawerContent ={"Graph View","Text View","Test Settings","SUPL Settings"};
    private static final int  Request_Code =123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
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
        mLeftDrawer =(ListView)findViewById(R.id.leftdrawer);
        mSelectProvider =(TextView)findViewById(R.id.textView2);
        mSelectlog =(Button)findViewById(R.id.selectlog);
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
        mLeftDrawer.setAdapter(new ArrayAdapter<String>(this ,android.R.layout.simple_list_item_1,mDrawerContent));
        mLeftDrawer.setOnItemClickListener(new ListView.OnItemClickListener() {;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLeftDrawer.setVisibility(View.GONE);
                if(position ==3){
                    mLeftDrawer.setVisibility(View.GONE);
                    FragmentManager fragmentManager = (FragmentManager)getFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentById(R.id.drawer_layout);
                    if (fragment == null) {
                        fragment = new TestFragment();
                        fragmentManager.beginTransaction()
                                .add(R.id.drawer_layout, fragment).addToBackStack("TestFrag")
                                .commit();

                    }
                }

                if(position ==1){
                    mLeftDrawer.setVisibility(View.GONE);
                    FragmentManager fragmentManager = (FragmentManager)getFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentById(R.id.drawer_layout);
                    if (fragment == null) {
                        fragment = new TextViewFragment();
                        fragmentManager.beginTransaction()
                                .add(R.id.drawer_layout, fragment).addToBackStack("TestFrag")
                                .commit();

                    }
                }
            }
        });
        mSelectlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                fileIntent.setType("*/*"); // intent type to filter application based on your requirement
                startActivityForResult(fileIntent, Request_Code);
            }
        });
    }

    void startGps(MenuItem item){
        Log.d(TAG , " Checking for Permissin Granted");
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  }, 1);

        }
       // Intent gpsintent = new Intent(this,GpsStatusService.class);
       // startService(gpsintent);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED) {

            if(checkgps.isChecked()) {
                item.setIcon(R.drawable.stop);
                mIconChange= true;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                mStartTime= SystemClock.elapsedRealtime();
            }
            else if (checknetwork.isChecked()) {
                item.setIcon(R.drawable.stop);
                mIconChange= true;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }
            else if(checkpassive.isChecked()) {
                item.setIcon(R.drawable.stop);
                mIconChange= true;
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
    void stopgps(MenuItem item){
       // Intent gpsintent = new Intent(this,GpsStatusService.class);
      //  stopService(gpsintent);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED) {
            item.setIcon(R.drawable.play);
            mIconChange= false;
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
        if(mfilePath==null)
        {
            Toast.makeText(getApplicationContext(),"Select File for Processing",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG ,"Launching new activity to process Logs");
        Intent intent = new Intent(this,LogProcessAcitivity.class);
        intent.putExtra("Path",mfilePath);
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
            case R.id.exitapp:
                Toast.makeText(getApplicationContext(),"EXITING",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.play_pause:
                if(!mIconChange) {
                    startGps(item);
                }
                else if(mIconChange){

                    stopgps(item);
                }
                break;
            case R.id.deleteaidingdata:
                deleteAidingData(item);
                break;
        }
        return true;
    }

    public void onBackPressed() {
        Log.d(TAG, "Back Button Presssed");
        if(getSupportFragmentManager().findFragmentByTag("TestFrag")!=null)
            getSupportFragmentManager().popBackStack("TestFrag",0);
        else
            super.onBackPressed();
    }

    public void deleteAidingData(MenuItem item){
        PopupMenu popupMenu = new PopupMenu(MainActivity.this,mSelectProvider);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().toString().equals("Position")) {
                    Toast.makeText(getApplicationContext(), "Delete Position", Toast.LENGTH_SHORT).show();
                    clearData();
                    Bundle extra = new Bundle();
                    extra.putBoolean("position",true);
                    locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER,"delete_aiding_data",extra);
                }
                else if(item.getTitle().toString().equals("Almanac")) {
                    Toast.makeText(getApplicationContext(), "Delete Almanac", Toast.LENGTH_SHORT).show();
                    clearData();
                    Bundle extra = new Bundle();
                    extra.putBoolean("almanac",true);
                    extra.putBoolean("alamanc-GLO",true);
                    extra.putBoolean("alamanc-BDS",true);
                    locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER,"delete_aiding_data",extra);
                }
                else if(item.getTitle().toString().equals("Ephemeris")) {
                    Toast.makeText(getApplicationContext(), "Delete Ephemeris", Toast.LENGTH_SHORT).show();
                    clearData();
                    Bundle extra = new Bundle();
                    extra.putBoolean("ephemeris",true);
                    extra.putBoolean("ephemeris-GLO",true);
                    extra.putBoolean("ephemeris-BDS",true);
                    locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER,"delete_aiding_data",extra);
                }
                else if(item.getTitle().toString().equals("Time")) {
                    Toast.makeText(getApplicationContext(), "Delete Time", Toast.LENGTH_SHORT).show();
                    clearData();
                    Bundle extra = new Bundle();
                    extra.putBoolean("time",true);
                    extra.putBoolean("time-gps",true);
                    extra.putBoolean("time-glo",true);
                    extra.putBoolean("time-BDS",true);
                    locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER,"delete_aiding_data",extra);
                }
                else if(item.getTitle().toString().equals("All Parameters")) {
                    Toast.makeText(getApplicationContext(), "Delete ALL Parameters", Toast.LENGTH_SHORT).show();
                    clearData();
                    locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER,"delete_aiding_data",null);
                }
                return  true;
            }
        });
        popupMenu.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Request_Code) {
            if (resultCode == RESULT_OK) {
                Log.d("Chandan", "File is Selected");
                mcontactUri = data.getData();
                mfilePath = mcontactUri.getPath();

            }
        }
    }

    public void clearData(){
        mTTFFText.setText("");
        mLongText.setText("");
        mLatText.setText("");
        mFixCount.setText("");
        mAccuracyText.setText("");
        mFixCount.setText("");
        mAltitudeText.setText("");
    }
}
