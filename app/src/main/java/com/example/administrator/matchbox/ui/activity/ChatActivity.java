package com.example.administrator.matchbox.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.bean.UserBean;
import com.example.administrator.matchbox.utils.EmUserUtils;
import com.example.administrator.matchbox.utils.ServerInterface;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;

/**
 * Created by Administrator on 2016/12/14.
 */

public class ChatActivity extends BaseActivity {

    UserBean userBean;

    @Override
    protected void initView() {
        userBean = (UserBean) getIntent().getSerializableExtra("userBean");
        //创建聊天的Fragment
        final EaseUser user = new EaseUser(userBean.getUserId() + "");
        if (!TextUtils.isEmpty(userBean.getUrl())) {
            user.setAvatar(ServerInterface.getErrorImagePath(userBean.getUrl()));
        }
        EmUserUtils.getInstance().addUser(userBean);
        EaseChatFragment fragment = new EaseChatFragment();
        //和谁聊天呢？
        Bundle bundle = new Bundle();
        bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        bundle.putString(EaseConstant.EXTRA_USER_ID, userBean.getUserId() + "");
        fragment.setArguments(bundle);
        //添加
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, fragment).commit();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_chat;
    }
}
