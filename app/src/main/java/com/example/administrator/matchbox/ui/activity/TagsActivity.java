package com.example.administrator.matchbox.ui.activity;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.CursorTagsAdapter;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.bean.TopicBean;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.interfaces.ITopic;
import com.example.administrator.matchbox.model.TopicModel;
import com.example.administrator.matchbox.utils.BoxUtils;
import com.example.administrator.matchbox.utils.SPHelper;
import com.example.administrator.matchbox.weiget.CursorDialog;
import com.moxun.tagcloudlib.view.TagCloudView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/6.
 */

public class TagsActivity extends BaseActivity implements IBeanCallback<ArrayList<TopicBean>>, TagCloudView.OnTagClickListener {
    @BindView(R.id.tcv_tags)
    TagCloudView tcvTags;

    ITopic iTopic = new TopicModel();
    private List<TopicBean> topicList;

    List<TopicBean> result = new ArrayList<>();


    @Override
    protected void initView() {
        //获取服务器的标签
        iTopic.getHotTopicList(this);
        tcvTags.setOnTagClickListener(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_tags;
    }

    //RxJava
    //next.com.fau

    @OnClick(R.id.tv_finish)
    public void onClick() {
        if (result.size() >= 3) {
            //开始关注
            final Dialog dialog = BoxUtils.showProgressDialog(this);
            dialog.show();
            TopicModel model = new TopicModel();
            model.addTopicList(result, new IBeanCallback<Integer>() {
                @Override
                public void Success(Integer integer) {
                    dialog.dismiss();
                    if (integer == 0) {
                        showToast("关注成功");
                        updateFirstLogin();
                        finish();
                    } else {
                        if (integer == result.size()) {
                            showToast("关注失败");
                        } else {
                            showToast(integer + "个关注失败");
                            updateFirstLogin();
                            finish();
                        }
                    }
                }

                public void updateFirstLogin() {
                    SPHelper sp = new SPHelper(getBaseContext(), "user");
                    sp.save(new SPHelper.ContentValue("firstLogin", false));
                }

                @Override
                public void onError(String msg) {
                    dialog.dismiss();
                }
            });
        } else {
            //弹出对话框
            showToastDialog();
        }
    }

    CursorDialog dialog;

    private void showToastDialog() {
        dialog = new CursorDialog.Builder(this).notFloating()
                .setLayout(R.layout.dialog_toast)
                .setViewClick(R.id.tv_submit, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }).builder();
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        showToast("至少关注三个话题!");
    }

    @Override
    public void Success(ArrayList<TopicBean> topicBeen) {
        this.topicList = topicBeen;
        CursorTagsAdapter adapter = new CursorTagsAdapter(topicList);
        tcvTags.setAdapter(adapter);
    }

    @Override
    public void onError(String msg) {
        showToast(msg);
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, int position) {
        view.setSelected(!view.isSelected());
        if (view.isSelected()) {
            //加入集合
            result.add(topicList.get(position));
        } else {
            //移除集合
            result.remove(topicList.get(position));
        }
    }
}
