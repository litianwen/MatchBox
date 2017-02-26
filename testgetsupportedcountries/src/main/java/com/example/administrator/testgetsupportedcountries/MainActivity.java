package com.example.administrator.testgetsupportedcountries;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import cn.smssdk.SMSSDK;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SMSSDK.initSDK(this, "14d9c2752c2d8", "1b7a16ca5de341efcc7ceac9e8541d3e");
    }

    public void get(View v) {
        StringBuffer str = new StringBuffer();
        for (Map.Entry<Character, ArrayList<String[]>> ent : SMSSDK
                .getGroupedCountryList().entrySet()) {
            ArrayList<String[]> cl = ent.getValue();
            for (String[] paire : cl) {
                str.append("国家 (" + paire[0] + ")---(" + "区号:" + paire[1] + ")\n");
                Log.e("sdada", "国家 (" + paire[0] + ")---(" + "区号:" + paire[1] + ")\n");
            }
        }
        Log.e("TAG", str.toString());
    }
}
