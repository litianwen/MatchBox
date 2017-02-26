package com.example.administrator.matchbox.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.CursorPagerAdapter;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.bean.TopicBean;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.model.TopicModel;
import com.example.administrator.matchbox.ui.fragment.topic.TopicFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/12.
 */

public class TopicDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.cb_attention)
    CheckBox cbAttention;
    @BindView(R.id.rg_tab_topic)
    RadioGroup rgTabTopic;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    TopicModel model = new TopicModel();

    List<Fragment> fragmentArrayList = new ArrayList<>();

    TopicBean bean;

    @Override
    protected void initView() {
        initTitle();
        initLine();
        initViewPager();
        initRadioGroup();
        initAttention();
    }

    private void initAttention() {
        cbAttention.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                model.attTopic(MyApp.getInstance().getUserBean().getUserId(), bean.getTopicId(), isChecked, new IBeanCallback() {
                    @Override
                    public void Success(Object o) {
                        showToast(isChecked ? "关注成功" : "取消关注成功");
                    }

                    @Override
                    public void onError(String msg) {
                        showToast(msg);
                    }
                });
            }
        });
    }

    private void initTitle() {
        bean = (TopicBean) getIntent().getSerializableExtra("topic");
        tvTitle.setText(bean.getName());
        //拿不到是否已经关注了该话题
    }

    private void initRadioGroup() {
        rgTabTopic.setOnCheckedChangeListener(this);
        rgTabTopic.check(R.id.rb_new);
    }

    private void initViewPager() {
        fragmentArrayList.add(TopicFragment.newInstance(TopicFragment.TYPE_NEW, bean.getTopicId()));
        fragmentArrayList.add(TopicFragment.newInstance(TopicFragment.TYPE_HOT, bean.getTopicId()));
        vpContent.setAdapter(new CursorPagerAdapter(getSupportFragmentManager(), fragmentArrayList));
        vpContent.addOnPageChangeListener(this);
    }

    private void initLine() {
        int width = getResources().getDisplayMetrics().widthPixels / 2;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        viewLine.setLayoutParams(params);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_topic_detail;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewLine.getLayoutParams();
        int width = getResources().getDisplayMetrics().widthPixels / 2;
        int leftMargin = (int) ((float) width * (position + positionOffset));
        params.setMargins(leftMargin, 0, 0, 0);
        viewLine.setLayoutParams(params);
    }

    @Override
    public void onPageSelected(int position) {
        rgTabTopic.check(position == 0 ? R.id.rb_new : R.id.rb_hot);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        vpContent.setCurrentItem(checkedId == R.id.rb_new ? 0 : 1);
    }
}
