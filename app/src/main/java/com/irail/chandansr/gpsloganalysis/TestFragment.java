package com.irail.chandansr.gpsloganalysis;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chandan.sr on 07-Jun-16.
 */

public class TestFragment extends Fragment {
    public final String TAG = "GPS Log Analyis";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"Fragment Created to adjust the parameters");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.test_setting_fragment,container,false);
        return v;
    }

}
