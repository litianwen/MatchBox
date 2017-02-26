package com.example.administrator.matchbox.ui.fragment.tab.message;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.BaseFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

/**
 * Created by Administrator on 2016/12/14.
 */

public class PrivateFragmentOld extends BaseFragment {

    @Override
    protected void initView() {
        EaseConversationListFragment fragment = new EaseConversationListFragment();
        getChildFragmentManager().beginTransaction().add(R.id.fl_content, fragment).commit();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_chat;
    }
}
