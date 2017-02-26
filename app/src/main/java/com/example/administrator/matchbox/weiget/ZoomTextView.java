package com.example.administrator.matchbox.weiget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.utils.LogUtils;

/**
 * Created by Administrator on 2016/12/7.
 */

public class ZoomTextView extends FrameLayout implements View.OnClickListener {

    TextView tvContent;
    TextView tvMore;


    //true ->展开  false ->收起
    boolean isZoom = false;

    public ZoomTextView(Context context) {
        super(context);
        init();
    }


    private void init() {
        View layout = View.inflate(getContext(), R.layout.view_zoom, null);
        this.addView(layout);
        tvContent = (TextView) layout.findViewById(R.id.tv_content);
        tvMore = (TextView) layout.findViewById(R.id.tv_more);
        tvMore.setOnClickListener(this);
        setupText();
    }

    public void setText(String text) {
        tvContent.setText(text);
        tvContent.post(new Runnable() {
            @Override
            public void run() {
                setupText();
            }
        });
    }

    private void setupText() {
        //判断行数
        if (tvContent.getLineCount() <= 10) {
            tvMore.setVisibility(View.GONE);
            LogUtils.e(tvContent.getLineCount());
            if (tvContent.getLineCount() != 1) {
                tvContent.setGravity(Gravity.LEFT);
            } else {
                tvContent.setGravity(Gravity.CENTER);
            }
        } else {
            tvMore.setVisibility(View.VISIBLE);
        }
    }


    public ZoomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void onClick(View v) {
        //点击暂开和收起
        isZoom = !isZoom;
        if (isZoom) {
            //开始暂开
            tvMore.setText("收起");
            ObjectAnimator.ofInt(tvContent, "lines", 10, tvContent.getLineCount()).setDuration(300).start();
        } else {
            tvMore.setText("展开");
            ObjectAnimator.ofInt(tvContent, "lines", tvContent.getLineCount(), 10).setDuration(300).start();
        }
    }
}
