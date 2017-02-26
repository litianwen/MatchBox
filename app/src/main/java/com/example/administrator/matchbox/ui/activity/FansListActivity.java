package com.example.administrator.matchbox.ui.activity;

import android.widget.FrameLayout;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.ui.fragment.contact.FansFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/15.
 */

public class FansListActivity extends BaseActivity {

    @BindView(R.id.fl_content)
    FrameLayout flContent;

    @Override
    protected void initView() {
        FansFragment fragment = new FansFragment();
        fragment.hideSerarch();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, fragment).commit();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_fanslist;
    }

}
