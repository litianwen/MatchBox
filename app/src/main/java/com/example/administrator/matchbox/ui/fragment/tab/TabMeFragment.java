package com.example.administrator.matchbox.ui.fragment.tab;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.BaseFragment;
import com.example.administrator.matchbox.bean.UserBean;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.ui.fragment.me.MeArticleListView;
import com.example.administrator.matchbox.ui.fragment.me.MeTopicListView;
import com.example.administrator.matchbox.utils.ServerInterface;
import com.example.administrator.matchbox.weiget.CursorScorllView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2016/12/2.
 */

public class TabMeFragment extends BaseFragment implements CursorScorllView.OnScrollYChangedListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_headpic)
    ImageView ivHeadpic;
    @BindView(R.id.iv_editinfo)
    ImageView ivEditinfo;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_sex_id)
    TextView tvSexId;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_attention)
    TextView tvAttention;
    @BindView(R.id.tv_fans)
    TextView tvFans;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.scroll_view)
    CursorScorllView scrollView;

    UserBean userBean;

    @BindView(R.id.rl_fake)
    RelativeLayout rlFake;
    @BindView(R.id.rg_tab_me)
    RadioGroup rgTabMe;
    @BindView(R.id.rl_tab)
    RelativeLayout rlTab;

    boolean isFirst = true;

    @Override
    protected void initView() {
        userBean = MyApp.getInstance().getUserBean();
        initData();
        initUser();
        scrollView.setOnScrollYChangedListener(this);
        tvTitle.setPadding(0, 1000, 0, 0);
    }

    private void initUser() {
        tvTitle.setText(userBean.getUserName());
        tvUsername.setText(userBean.getUserName());
        if (TextUtils.isEmpty(userBean.getUrl()))
            ivHeadpic.setImageResource(R.mipmap.icon_register_avatar_default);
        else
            Glide.with(getActivity()).load(ServerInterface.getImagePath(userBean.getUrl())).into(ivHeadpic);
        tvInfo.setText(userBean.getMyInfo());
        tvSexId.setText("ID: " + userBean.getUserId());
        tvSexId.setCompoundDrawablesWithIntrinsicBounds(
                userBean.getSex().equals("男") ? R.mipmap.icon_gender_male :
                        userBean.getSex().equals("女") ? R.mipmap.icon_gender_famale :
                                R.mipmap.profile_emotion_double
                , 0, 0, 0);
        tvAttention.setText(userBean.getMyActionCount() + "\n" + "关注");
        tvFans.setText(userBean.getFansCount() + "\n" + "粉丝");
        rgTabMe.post(new Runnable() {
            @Override
            public void run() {
                scroll(0);
            }
        });

    }

    private void initData() {
        vpContent.setAdapter(new MyAdapter());
        rgTabMe.setOnCheckedChangeListener(this);
        vpContent.addOnPageChangeListener(this);
        rgTabMe.check(R.id.rb_toast);
        rgTabMe.check(R.id.rb_topic);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        vpContent.setLayoutParams(params);
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fargment_tab_me;
    }

    @OnClick({R.id.iv_editinfo, R.id.tv_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_editinfo:
                break;
            case R.id.tv_info:
                break;
        }
    }

    @Override
    public void onScrollYChanged(int scrollY) {
        int paddingTop = tvUsername.getTop() - scrollY + tvTitle.getBottom();
        if (paddingTop > 0)
            tvTitle.setPadding(0, paddingTop, 0, 0);
        else {
            tvTitle.setPadding(0, 0, 0, 0);
        }
        scroll(scrollY);
    }

    public void scroll(int scrollY) {
        int max = Math.max(rlFake.getTop(), scrollY);
        rlTab.setTranslationY(max);
        if (isFirst) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, listviews.get(0).getHeight());
            vpContent.setLayoutParams(params);
            isFirst = false;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        vpContent.setCurrentItem(checkedId == R.id.rb_topic ? 0 : 1);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(final int position) {
        rgTabMe.check(position == 0 ? R.id.rb_topic : R.id.rb_article);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, listviews.get(position).getHeight());
        vpContent.setLayoutParams(params);
        isFirst = false;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    List<ListView> listviews;

    class MyAdapter extends PagerAdapter {

        public MyAdapter() {
            listviews = new ArrayList<>();
            listviews.add(new MeTopicListView(getContext()));
            listviews.add(new MeArticleListView(getContext()));
        }

        @Override
        public int getCount() {
            return listviews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(listviews.get(position));
            return listviews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(listviews.get(position));
        }
    }
}
