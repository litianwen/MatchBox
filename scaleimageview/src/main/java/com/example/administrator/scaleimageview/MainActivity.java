package com.example.administrator.scaleimageview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    ScaleImageView siv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        siv = (ScaleImageView) findViewById(R.id.siv);
        siv.setImageResource(R.mipmap.aa);
    }
}
