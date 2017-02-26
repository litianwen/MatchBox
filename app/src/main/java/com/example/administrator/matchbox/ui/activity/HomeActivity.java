package com.example.administrator.matchbox.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.bean.PushBean;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.ui.fragment.tab.TabMessageFragment;
import com.example.administrator.matchbox.utils.PushUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/2.
 */

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ll_tab)
    LinearLayout llTab;
    @BindView(R.id.iv_dots)
    ImageView ivDots;

    int currIndex = -1;

    //只要有ViewPager
    Fragment[] fragments = new Fragment[5];

    @Override
    protected void initView() {
        llTab.getChildAt(0).setSelected(true);
        for (int i = 0; i < llTab.getChildCount(); i++) {
            llTab.getChildAt(i).setOnClickListener(this);
        }
        showFragment(0);
        //判断
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter("newMessage"));
        if (PushUtils.getUnReadCountByType(PushBean.TYPE_ALL) > 0)
            ivDots.setVisibility(View.VISIBLE);
        else
            ivDots.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_home;
    }

    @Override
    public void onClick(View v) {
        Integer tag = Integer.parseInt(v.getTag().toString());
        //切换Fragment
        showFragment(tag);
    }

    private void showFragment(Integer tag) {
        if (currIndex == tag) {
            //在同一个界面进行点击
            //下拉刷新
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //判断当前页是否存在
        if (currIndex != -1) {
            ft.hide(fragments[currIndex]);
        }
        //不要else
        if (fragments[tag] == null) {
            createFragment(tag);
            ft.add(R.id.fl_content, fragments[tag]);
        } else {
            ft.show(fragments[tag]);
        }
        //第一次的时候
        if (currIndex != -1)
            llTab.getChildAt(currIndex).setSelected(false);
        llTab.getChildAt(tag).setSelected(true);
        currIndex = tag;
        ft.commit();
    }

    private String[] fragmentName = {"TabMatchFragment", "TabTopicFragment", "TabMessageFragment", "TabBoxFragment", "TabMeFragment"};

    private void createFragment(Integer tag) {
        //0-4
        String name = "com.example.administrator.matchbox.ui.fragment.tab." + fragmentName[tag];
        //
        try {
            Fragment fragment = (Fragment) Class.forName(name).newInstance();
            fragments[tag] = fragment;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    long startTime;

    //两次退出程序
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - startTime < 2000) {
            MyApp.getInstance().exit();
        } else {
            showToast("再按一次退出");
            startTime = System.currentTimeMillis();
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ivDots.setVisibility(View.VISIBLE);
            //如果fragment显示的是第三页
            if (currIndex == 2) {
                //去显示红点
                TabMessageFragment fragment = (TabMessageFragment) fragments[2];
                fragment.showDots();
            }
        }
    };
}
