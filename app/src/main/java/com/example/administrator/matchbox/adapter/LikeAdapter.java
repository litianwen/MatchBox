package com.example.administrator.matchbox.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.ListItemAdapter;
import com.example.administrator.matchbox.bean.LikeBean;
import com.example.administrator.matchbox.utils.ServerInterface;
import com.example.administrator.matchbox.weiget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/12/8.
 */

public class LikeAdapter extends ListItemAdapter<LikeBean.ListBean> {

    public LikeAdapter(Context context, List<LikeBean.ListBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = new CircleImageView(mContext);
        ImageView iv = (ImageView) convertView;
        String url = getItem(position).getUserImg();
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(R.mipmap.icon_register_avatar_default);
        } else {
            Glide.with(getContext()).load(ServerInterface.getImagePath(url)).into(iv);
        }
        iv.setPadding(5, 5, 5, 5);
        return iv;
    }
}
