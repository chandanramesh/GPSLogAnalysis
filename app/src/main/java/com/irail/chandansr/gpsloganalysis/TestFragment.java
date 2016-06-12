package com.irail.chandansr.gpsloganalysis;

import android.app.Fragment;
import android.graphics.Color;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Iterator;

/**
 * Created by chandan.sr on 07-Jun-16.
 */

public class TestFragment extends Fragment {
    public final String TAG = "GPS Log Analyis";
    GpsStatusService mGpsStatusService;
    static View v;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"Fragment Created to adjust the parameters");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.test_setting_fragment,container,false);
        return v;
    }



}
