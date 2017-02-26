package com.example.administrator.matchbox.weiget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by Administrator on 2016/12/11.
 */

public class ScrollExpandableListView extends ExpandableListView {

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST));
    }

    public ScrollExpandableListView(Context context) {
        super(context);
    }

    public ScrollExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
