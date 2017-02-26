package com.example.administrator.matchbox.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.ListItemAdapter;
import com.example.administrator.matchbox.bean.UserBean;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.model.UserModel;
import com.example.administrator.matchbox.utils.ServerInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/14.
 */

public class FansAdapter extends ListItemAdapter<UserBean> {

    List<UserBean> atts;
    UserModel model = new UserModel();

    public FansAdapter(Context context, List<UserBean> list, List<UserBean> atts) {
        super(context, list);
        this.atts = atts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_toast, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        final UserBean userBean = getItem(position);
        if (TextUtils.isEmpty(userBean.getUrl())) {
            holder.ivHeadpic.setImageResource(R.mipmap.icon_register_avatar_default);
        } else {
            Glide.with(getContext()).load(ServerInterface.getErrorImagePath(userBean.getUrl())).into(holder.ivHeadpic);
        }
        holder.tvUsername.setText(TextUtils.isEmpty(userBean.getUserName()) ? "匿名" : userBean.getUserName());
        holder.tvId.setText("ID: " + userBean.getUserId());
        if (isEachFans(userBean.getUserId())) {
            holder.ivAdd.setImageResource(R.mipmap.person_follow_eachother);
            holder.ivAdd.setOnClickListener(null);
        } else {
            holder.ivAdd.setImageResource(R.mipmap.person_follow_night);
            holder.ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.attUser(MyApp.getInstance().getUserBean().getUserId() + "", userBean.getUserId() + "", new IBeanCallback() {
                        @Override
                        public void Success(Object o) {
                            holder.ivAdd.setImageResource(R.mipmap.person_follow_eachother);
                            holder.ivAdd.setOnClickListener(null);
                            atts.add(userBean);
                        }

                        @Override
                        public void onError(String msg) {
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        return convertView;
    }


    public boolean isEachFans(int userId) {
        for (UserBean att : atts) {
            if (att.getUserId() == userId)
                return true;
        }
        return false;
    }

    static class ViewHolder {
        @BindView(R.id.iv_headpic)
        ImageView ivHeadpic;
        @BindView(R.id.tv_username)
        TextView tvUsername;
        @BindView(R.id.tv_id)
        TextView tvId;
        @BindView(R.id.iv_add)
        ImageView ivAdd;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

