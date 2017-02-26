package com.example.administrator.matchbox.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.matchbox.base.ListItemAdapter;
import com.example.administrator.matchbox.bean.TopicBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/5.
 */

public class HistoryAdapter extends ListItemAdapter<TopicBean> {
    public HistoryAdapter(Context context, List<TopicBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(getContext(), android.R.layout.simple_list_item_1, null);
        TextView tv = (TextView) convertView;
        tv.setSingleLine();
        tv.setText(getItem(position).getName());
        tv.setBackgroundColor(Color.WHITE);
        return tv;
    }
}
