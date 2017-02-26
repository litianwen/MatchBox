package com.example.administrator.matchbox.ui.fragment.tab;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.CursorPagerAdapter;
import com.example.administrator.matchbox.base.BaseFragment;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.model.UserModel;
import com.example.administrator.matchbox.ui.activity.PublishArticleActivity;
import com.example.administrator.matchbox.ui.activity.TagsActivity;
import com.example.administrator.matchbox.ui.fragment.tab.match.FriendFragment;
import com.example.administrator.matchbox.ui.fragment.tab.match.LoveFragment;
import com.example.administrator.matchbox.utils.LogUtils;
import com.example.administrator.matchbox.utils.SPHelper;
import com.example.administrator.matchbox.weiget.CursorDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/2.
 */

public class TabMatchFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private static final int REQUEST_ATT = 11;
    @BindView(R.id.rg_tab_match)
    RadioGroup rgTabMatch;
    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    FriendFragment friendFragment;
    LoveFragment loveFragment;

    @Override
    protected void initView() {
        SPHelper sp = new SPHelper(getContext(), "user");
        boolean isFirstLogin = sp.getBoolean("firstLogin");
        rgTabMatch.setOnCheckedChangeListener(this);
        initFragment();
        if (isFirstLogin) {
            rgTabMatch.check(R.id.rb_love);
            startActivityForResult(new Intent(getContext(), TagsActivity.class), REQUEST_ATT);
        } else {
            rgTabMatch.check(R.id.rb_friend);
        }

    }


    private void initFragment() {
        List<Fragment> list = new ArrayList<>();
        friendFragment = new FriendFragment();
        loveFragment = new LoveFragment();
        list.add(friendFragment);
        list.add(loveFragment);
        CursorPagerAdapter adapter = new CursorPagerAdapter(getChildFragmentManager(), list);
        vpContent.setAdapter(adapter);
        vpContent.addOnPageChangeListener(this);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_tab_match;
    }


    @OnClick({R.id.tv_publish, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_publish:
                showPublishDialog();
                break;
            case R.id.iv_search:
                break;
        }
    }

    Dialog dialog = null;

    private void showPublishDialog() {
        dialog = new CursorDialog.Builder(getContext())
                .notFloating()
                .setLayout(R.layout.dialog_publish)
                .setViewClick(R.id.tv_text, textClick)
                .setViewClick(R.id.tv_image, imgClick)
                .builder();
        LinearLayout llAnim = (LinearLayout) dialog.findViewById(R.id.ll_anim);
        RelativeLayout rlDismiss = (RelativeLayout) dialog.findViewById(R.id.rl_dismiss);

        rlDismiss.setOnTouchListener(dismissListener);
        dialog.show();
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.publish_tran_anim);
        llAnim.startAnimation(anim);

    }

    View.OnTouchListener dismissListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            dialog.dismiss();
            return false;
        }
    };

    View.OnClickListener textClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
            startActivity(new Intent(getContext(), PublishArticleActivity.class));
        }
    };
    View.OnClickListener imgClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //告诉下一个activity继续跳转到选择图片
            dialog.dismiss();
            startActivity(new Intent(getContext(), PublishArticleActivity.class).putExtra("isJump", true));
        }

    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e("-------界面返回");
        if (requestCode == REQUEST_ATT) {
            loveFragment.firstRequest();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_friend && vpContent.getCurrentItem() == 0 || checkedId == R.id.rb_love && vpContent.getCurrentItem() == 1)
            return;
        vpContent.setCurrentItem(checkedId == R.id.rb_friend ? 0 : 1);
        if (checkedId == R.id.rb_friend) {
            final SPHelper spHelper = new SPHelper(getContext(), "user");
            if (spHelper.getBoolean("attention")) {
                spHelper.save(new SPHelper.ContentValue("attention", false));
                //注册后的第一次进来，关注官方账号，刷新页面
                UserModel userModel = new UserModel();
                userModel.attUser(MyApp.getInstance().getUserBean().getUserId() + "", "219", new IBeanCallback() {
                    @Override
                    public void Success(Object o) {
                        //弹出对话框
                        showAttDialog();
                    }

                    @Override
                    public void onError(String msg) {
                        spHelper.save(new SPHelper.ContentValue("attention", true));
                        showToast(msg);
                    }
                });
            }
        }
    }

    CursorDialog attDialog;

    private void showAttDialog() {
        attDialog = new CursorDialog.Builder(getContext())
                .notFloating()
                .setLayout(R.layout.dialog_att)
                .setViewClick(R.id.tv_submit, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        attDialog.dismiss();
                        //调用Fragment重新加载数据
                        friendFragment.fristRequest();
                    }
                }).builder();
        ImageView iv = (ImageView) attDialog.findViewById(R.id.iv_icon);
        Glide.with(getActivity()).load(R.mipmap.icon)
                .into(iv);
        attDialog.show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        rgTabMatch.check(position == 0 ? R.id.rb_friend : R.id.rb_love);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
