package com.example.administrator.matchbox.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.config.MyApp;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/23.
 */

public abstract class BaseActivity extends AppCompatActivity {
    {
        MyApp.getInstance().addActivity(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        ButterKnife.bind(this);
        //getData(); getIntent.get...
        initBack();
        initView();
    }




    protected void initBack() {
        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
        if (ivBack != null) {
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }


    protected abstract void initView();

    protected abstract int getLayoutRes();


    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public Activity getActivity() {
        return this;
    }

}
