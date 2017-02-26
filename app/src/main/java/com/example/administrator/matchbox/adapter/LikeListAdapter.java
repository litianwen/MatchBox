package com.example.administrator.matchbox.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.ListItemAdapter;
import com.example.administrator.matchbox.bean.LikeBean;
import com.example.administrator.matchbox.utils.ServerInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/11.
 */

public class LikeListAdapter extends ListItemAdapter<LikeBean.ListBean> {

    public LikeListAdapter(Context context, List<LikeBean.ListBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_like
                    , null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        LikeBean.ListBean bean = getItem(position);
        holder.tvId.setText(bean.getUserId() + "");
        holder.tvUsername.setText(TextUtils.isEmpty(bean.getUserName()) ? "匿名" : bean.getUserName());
        if (TextUtils.isEmpty(bean.getUserImg())) {
            holder.ivHeadpic.setImageResource(R.mipmap.icon_register_avatar_default);
        } else {
            Glide.with(getContext()).load(ServerInterface.getImagePath(bean.getUserImg()))
                    .into(holder.ivHeadpic);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_headpic)
        ImageView ivHeadpic;
        @BindView(R.id.tv_username)
        TextView tvUsername;
        @BindView(R.id.tv_id)
        TextView tvId;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
