package com.example.administrator.matchbox.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.CursorPagerAdapter;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.ui.fragment.contact.AttentionFragment;
import com.example.administrator.matchbox.ui.fragment.contact.FansFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/13.
 */

public class ContactActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.rg_tab_contact)
    RadioGroup rgTabContact;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    @Override
    protected void initView() {
        rgTabContact.setOnCheckedChangeListener(this);
        rgTabContact.check(R.id.rb_attention);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new AttentionFragment());
        fragmentList.add(new FansFragment());
        vpContent.setAdapter(new CursorPagerAdapter(getSupportFragmentManager(), fragmentList));
        vpContent.addOnPageChangeListener(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_contact;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        vpContent.setCurrentItem(checkedId == R.id.rb_attention ? 0 : 1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        rgTabContact.check(position == 0 ? R.id.rb_attention : R.id.rb_fans);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
