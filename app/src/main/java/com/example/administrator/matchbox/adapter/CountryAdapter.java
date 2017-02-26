package com.example.administrator.matchbox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.matchbox.bean.CountriesBean;
import com.example.administrator.matchbox.bean.SortAdapter;
import com.example.administrator.matchbox.utils.LogUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/11/24.
 */

public class CountryAdapter extends SortAdapter<CountriesBean> {
    List<CountriesBean> mList;

    public CountryAdapter(Context context, @NonNull List<CountriesBean> list) {
        super(context, list);
        mList = list;
        LogUtils.e(mList.toString());
    }

    @Override
    public View getView(int position) {
        TextView layout = (TextView) View.inflate(mContext, android.R.layout.simple_list_item_1, null);
        layout.setText(mList.get(position).getCountry() + mList.get(position).getArea());
        return layout;
    }


    public CountriesBean getBean(int position) {
        return mList.get(position);
    }
}
