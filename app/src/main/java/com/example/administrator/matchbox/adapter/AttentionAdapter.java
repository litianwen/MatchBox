package com.example.administrator.matchbox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.bean.SortAdapter;
import com.example.administrator.matchbox.bean.UserBean;
import com.example.administrator.matchbox.utils.ServerInterface;
import com.example.administrator.matchbox.weiget.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/13.
 */

public class AttentionAdapter extends SortAdapter<UserBean> {

    public AttentionAdapter(Context context, @NonNull List<UserBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position) {
        View layout = View.inflate(mContext, R.layout.item_like, null);
        ViewHolder holder = new ViewHolder(layout);
        UserBean bean = getItem(position);
        holder.tvId.setText("ID: " + bean.getUserId());
        holder.tvUsername.setText(bean.getUserName());
        if (bean.getUrl() == null || bean.getUrl().equals("null") || bean.getUrl().equals("")) {
            holder.ivHeadpic.setImageResource(R.mipmap.icon_register_avatar_default);
        } else
            Glide.with(mContext).load(ServerInterface.getErrorImagePath(bean.getUrl())).into(holder.ivHeadpic);
        return layout;
    }

    static class ViewHolder {
        @BindView(R.id.iv_headpic)
        CircleImageView ivHeadpic;
        @BindView(R.id.tv_username)
        TextView tvUsername;
        @BindView(R.id.tv_id)
        TextView tvId;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
