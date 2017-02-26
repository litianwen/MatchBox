package com.example.administrator.matchbox.weiget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.interfaces.IGetString;
import com.example.administrator.matchbox.utils.LogUtils;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Administrator on 2016/11/24.
 */

public class IndexBar extends LinearLayout implements View.OnClickListener {
    //排序
    Set<String> set = new TreeSet<>();

    public IndexBar(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    public IndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    //开始设置数据
    public void setIndex(List<? extends IGetString> list) {
        this.removeAllViews();
        set.clear();
        for (IGetString iGetString : list) {
            if (iGetString != null) {
                String str = iGetString.getString();
                LogUtils.e(str);
                if (!TextUtils.isEmpty(str)) {
                    set.add(str.substring(0, 1).toUpperCase());
                }
            }
        }
        //开始绘制
        TextView xing = new TextView(getContext());
        xing.setText("☆");
        xing.setGravity(Gravity.CENTER);
        xing.setPadding(5, 5, 5, 5);
        xing.setTextColor(getResources().getColor(R.color.textColor));
        xing.setTag("☆");
        this.addView(xing);
        xing.setOnClickListener(this);
        for (String s : set) {
            TextView tv = new TextView(getContext());
            tv.setText(s);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(2, 2, 2, 2);
            tv.setTextSize(12);
            tv.setTextColor(getResources().getColor(R.color.textColor));
            tv.setOnClickListener(this);
            tv.setTag(s);
            this.addView(tv);
        }
    }

    @Override
    public void onClick(View v) {
        if (onIndexClickListener != null) {
            String tag = v.getTag().toString();
            onIndexClickListener.onIndexBarClick(tag);
        }
    }


    public void setOnIndexClickListener(OnIndexClickListener onIndexClickListener) {
        this.onIndexClickListener = onIndexClickListener;
    }

    OnIndexClickListener onIndexClickListener;

    public interface OnIndexClickListener {
        void onIndexBarClick(String letter);
    }

}
