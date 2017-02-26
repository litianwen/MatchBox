package com.example.administrator.matchbox.weiget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/12/16.
 */

public class CursorScorllView extends ScrollView {
    public CursorScorllView(Context context) {
        super(context);
    }

    public CursorScorllView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setOnScrollYChangedListener(OnScrollYChangedListener onScrollYChangedListener) {
        this.onScrollYChangedListener = onScrollYChangedListener;
    }

    OnScrollYChangedListener onScrollYChangedListener;

    public interface OnScrollYChangedListener {
        public void onScrollYChanged(int scrollY);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollYChangedListener != null)
            onScrollYChangedListener.onScrollYChanged(t);
    }

}
