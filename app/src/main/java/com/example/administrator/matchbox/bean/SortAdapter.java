package com.example.administrator.matchbox.bean;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.interfaces.IGetString;
import com.example.administrator.matchbox.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/24.
 */

//只关心上面的标签，布局随意
public abstract class SortAdapter<T extends IGetString> extends BaseAdapter {

    protected final Context mContext;
    protected List<T> mList = new ArrayList<>();

    //String title = "";


    public SortAdapter(Context context, @NonNull List<T> list) {
        //先将数据排列号
        synchronized (this) {
            mList = list;
            sort();
        }
        mContext = context;
    }

    public void sort() {
        LogUtils.e(mList.toString());
        Collections.sort(mList, new Comparator<IGetString>() {
            @Override
            public int compare(IGetString o1, IGetString o2) {
                return o1.getString().compareToIgnoreCase(o2.getString());
            }
        });
        LogUtils.e(mList.toString());
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //父布局如何搭建
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_sort, null);
            holder = new ViewHolder(convertView);
            holder.flContent.addView(getView(position));//setTag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.flContent.removeAllViews();
            holder.flContent.addView(getView(position));//setTag
        }
        //当前条目的首字母
        String select = mList.get(position).getString().substring(0, 1).toUpperCase();
        //如果是第一条
        if (position == getFirstLetter(select)) {
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(select);
        } else {
            holder.tvTitle.setVisibility(View.GONE);
        }
        return convertView;
    }

    public abstract View getView(int position);

    static class ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.fl_content)
        FrameLayout flContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //首字母出现在第几条
    public int getFirstLetter(String letter) {
        for (int i = 0; i < mList.size(); i++) {
            String str = mList.get(i).getString().substring(0, 1).toUpperCase();
            if (str.equals(letter)) {
                return i;
            }
        }
        return -1;
    }


    //获取任意一个条目的索引名称
    public String getPositionLetter(int position) {
        return mList.get(position).getString().substring(0, 1).toUpperCase();
    }

}
