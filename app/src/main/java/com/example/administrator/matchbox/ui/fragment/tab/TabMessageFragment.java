package com.example.administrator.matchbox.ui.fragment.tab;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.CursorPagerAdapter;
import com.example.administrator.matchbox.base.BaseFragment;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.model.UserModel;
import com.example.administrator.matchbox.ui.activity.ContactActivity;
import com.example.administrator.matchbox.ui.fragment.tab.message.PrivateFragment;
import com.example.administrator.matchbox.ui.fragment.tab.message.ToastFragment;
import com.example.administrator.matchbox.weiget.CursorDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/2.
 */

public class TabMessageFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.rg_tab_message)
    RadioGroup rgTabMessage;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    @BindView(R.id.iv_dots)
    ImageView ivDots;

    @Override
    protected void initView() {
        rgTabMessage.setOnCheckedChangeListener(this);
        rgTabMessage.check(R.id.rb_private);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new PrivateFragment());
        fragmentList.add(new ToastFragment());
        vpContent.setAdapter(new CursorPagerAdapter(getChildFragmentManager(), fragmentList));
        vpContent.addOnPageChangeListener(this);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_tab_message;
    }

    @OnClick({R.id.tv_add_friend, R.id.tv_contact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_friend:
                //添加好友  对话框，对方的id
                showAddDialog();
                break;
            case R.id.tv_contact:
                //跳转到联系人界面
                startActivity(new Intent(getContext(), ContactActivity.class));
                break;
        }
    }

    CursorDialog attDialog;

    private void showAddDialog() {
        View layout = View.inflate(getContext(), R.layout.dialog_att_friend, null);
        final EditText etContent = (EditText) layout.findViewById(R.id.et_content);
        attDialog = new CursorDialog.Builder(getContext())
                .notFloating()
                .setView(layout)
                .setViewClick(R.id.tv_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        attDialog.dismiss();
                    }
                }).setViewClick(R.id.tv_submit, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        attDialog.dismiss();
                        new UserModel().attUser(MyApp.getInstance().getUserBean().getUserId() + "", etContent.getText().toString(), new IBeanCallback() {
                            @Override
                            public void Success(Object o) {
                                showToast("关注成功");
                            }

                            @Override
                            public void onError(String msg) {
                                showToast(msg);
                            }
                        });
                    }
                }).builder();
        attDialog.show();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        vpContent.setCurrentItem(checkedId == R.id.rb_private ? 0 : 1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        rgTabMessage.check(position == 0 ? R.id.rb_private : R.id.rb_toast);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void showDots() {
        ivDots.setVisibility(View.VISIBLE);
    }
}
