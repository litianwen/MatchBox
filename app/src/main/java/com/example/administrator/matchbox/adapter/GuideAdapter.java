package com.example.administrator.matchbox.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.matchbox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */

public class GuideAdapter extends PagerAdapter {

    List<View> mList = new ArrayList<>();

    public GuideAdapter(Context context) {
        String[] text1 = context.getResources().getStringArray(R.array.text1);
        String[] text2 = context.getResources().getStringArray(R.array.text2);
        for (int i = 0; i < 5; i++) {
            View layout = View.inflate(context, R.layout.pager_layout, null);
            TextView tv1 = (TextView) layout.findViewById(R.id.text1);
            TextView tv2 = (TextView) layout.findViewById(R.id.text2);
            tv1.setText(text1[i]);
            tv2.setText(text2[i]);
            mList.add(layout);
        }
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mList.get(position % 5));
        return mList.get(position % 5);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mList.get(position % 5));
    }
}
