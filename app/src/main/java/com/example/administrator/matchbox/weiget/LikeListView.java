package com.example.administrator.matchbox.weiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.administrator.matchbox.R;

/**
 * Created by Administrator on 2016/12/8.
 */

public class LikeListView extends LinearLayout {

    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, 80);

    public LikeListView(Context context) {
        super(context);
    }

    public LikeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setAdapter(ListAdapter adapter) {
        if (adapter.getCount() == 0) {
            this.setVisibility(View.GONE);
            return;
        }
        this.setVisibility(View.VISIBLE);
        //移除所有的视图
        removeAllViews();
        //设置布局
        int width = getWidth(); //View的宽度
        int size = width / 80;
        addLikeImageView();
        size--;
        if (adapter.getCount() > size) {
            for (int i = 0; i < size - 1; i++) {
                View view = adapter.getView(i, null, null);
                view.setLayoutParams(params);
                this.addView(view);
            }
            //添加最后一部分
            TextView tv = new TextView(getContext());
//            GradientDrawable drawable = new GradientDrawable();
//            drawable.setShape(GradientDrawable.OVAL);
//            drawable.setStroke(1, Color.RED);
//            tv.setBackgroundDrawable(drawable);
            tv.setBackgroundResource(R.drawable.like_background);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            tv.setText(adapter.getCount() - size + 1 + "");
            tv.setLayoutParams(params);
            this.addView(tv);
        } else {
            for (int i = 0; i < adapter.getCount(); i++) {
                View view = adapter.getView(i, null, null);
                view.setLayoutParams(params);
                this.addView(view);
            }
        }
    }

    private void addLikeImageView() {
        ImageView iv = new ImageView(getContext());
        iv.setImageResource(R.mipmap.icon_post_like);
        iv.setLayoutParams(params);
        iv.setScaleType(ImageView.ScaleType.CENTER);
        this.addView(iv);
    }

}
