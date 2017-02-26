package com.example.administrator.matchbox.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.ListItemAdapter;
import com.example.administrator.matchbox.utils.EmUserUtils;
import com.example.administrator.matchbox.utils.TimeUtils;
import com.example.administrator.matchbox.weiget.CircleImageView;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/14.
 */

public class ConversationAdapter extends ListItemAdapter<EMConversation> {

    public ConversationAdapter(Context context, List<EMConversation> list) {
        super(context, list);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_conversation, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        EMConversation emConversation = getItem(position);
        EmUserUtils.getInstance().getUser(new EmUserUtils.UserCallback() {
            @Override
            public void getUser(EaseUser easeUser) {
                if (TextUtils.isEmpty(easeUser.getAvatar())) {
                    holder.ivHeadpic.setImageResource(R.mipmap.icon_register_avatar_default);
                } else
                    Glide.with(getContext()).load(easeUser.getAvatar()).into(holder.ivHeadpic);
                if (TextUtils.isEmpty(easeUser.getNickname()))
                    holder.tvUsername.setText("匿名");
                else
                    holder.tvUsername.setText(easeUser.getNickname());
            }
        }, emConversation.getUserName());
        holder.tvTime.setText(TimeUtils.getStringTime(emConversation.getLastMessage().getMsgTime()));
        EMMessage message = emConversation.getLastMessage();
//        if (message.isDelivered())
//            holder.tvContent.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.chat_icon_sending, 0, 0, 0);
//        else if (message.isAcked())
//            holder.tvContent.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.common_icon_warning, 0, 0, 0);
//        else
//            holder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        switch (message.getType()) {
            case TXT:
                EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                holder.tvContent.setText(body.getMessage());
                break;
            case IMAGE:
                holder.tvContent.setText("[图片]");
                break;
            case LOCATION:
                holder.tvContent.setText("[地理位置]");
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_headpic)
        CircleImageView ivHeadpic;
        @BindView(R.id.tv_username)
        TextView tvUsername;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
