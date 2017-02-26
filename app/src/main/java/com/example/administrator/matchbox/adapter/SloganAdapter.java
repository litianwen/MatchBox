package com.example.administrator.matchbox.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.matchbox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */

public class SloganAdapter extends PagerAdapter {

    List<ImageView> mList = new ArrayList<>();

    public SloganAdapter(Context context) {
        for (int i = 0; i < 5; i++) {
            ImageView iv = new ImageView(context);
            try {
                int id = R.mipmap.class.getField("tologin_banner" + (i + 1)).getInt(null);
                iv.setImageResource(id);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                mList.add(iv);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
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
