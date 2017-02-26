package com.example.administrator.matchbox.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.bean.TopicBean;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.model.ArticleModel;
import com.example.administrator.matchbox.utils.BoxUtils;
import com.example.administrator.matchbox.weiget.CursorDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/5.
 */

public class PublishArticleActivity extends BaseActivity implements TextWatcher {

    private static final int REQUSET_IMAGE = 1000;
    private static final int REQUSET_TOPIC = 1001;

    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.tv_topic_name)
    TextView tvTopicName;
    @BindView(R.id.iv_select_topic)
    ImageView ivSelectTopic;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.iv_content)
    ImageView ivContent;
    @BindView(R.id.iv_clear_content)
    ImageView ivClearContent;
    boolean isJump;
    @BindView(R.id.tv_add_image)
    TextView tvAddImage;
    @BindView(R.id.rl_select_topic)
    RelativeLayout rlSelectTopic;

    @BindView(R.id.tv_size)
    TextView tv_size;

    @BindView(R.id.tv_update)
    TextView tv_update;

    //最终调用发布接口时的地址
    String path;
    //最终调用接口发布的话题
    TopicBean topic;

    @Override
    protected void initView() {
        isJump = getIntent().getBooleanExtra("isJump", false);
        if (isJump) {
            //点击的是图片
            startActivityForResult(new Intent(this, SelectorImageActivity.class), REQUSET_IMAGE);
        }
        etContent.addTextChangedListener(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_publish_article;
    }


    @OnClick({R.id.tv_back, R.id.tv_publish, R.id.iv_select_topic, R.id.iv_content, R.id.iv_clear_content})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_publish:
                //判断
                if (topic == null) {
                    showSelectTopicDialog();
                    return;
                }
                ArticleModel model = new ArticleModel();
                File file = null;
                if (path != null)
                    file = new File(path);
                publish(model, file);
                break;
            case R.id.iv_select_topic:
                startActivityForResult(new Intent(getBaseContext(), SelectTopicActivity.class), REQUSET_TOPIC);
                break;

            case R.id.iv_content:
                startActivityForResult(new Intent(this, SelectorImageActivity.class), REQUSET_IMAGE);
                break;
            case R.id.iv_clear_content:
                showDeleteDialog();
                break;
        }
    }

    private void publish(ArticleModel model, File file) {
        final Dialog dialog = BoxUtils.showProgressDialog(this);
        dialog.show();
        model.publishArticle(file, topic.getTopicId(), etContent.getText().toString(), new IBeanCallback() {
            @Override
            public void Success(Object o) {
                showToast("发布成功");
                dialog.dismiss();
                //如果没有关闭
                finish();
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
                dialog.dismiss();
            }
        });
    }

    CursorDialog selectDialog;

    private void showSelectTopicDialog() {
        selectDialog = new CursorDialog.Builder(this).setLayout(R.layout.dialog_select_topic)
                .notFloating()
                .setViewClick(R.id.tv_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectDialog.dismiss();
                    }
                }).setViewClick(R.id.tv_submit, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectDialog.dismiss();
                        startActivityForResult(new Intent(getActivity(), SelectTopicActivity.class), REQUSET_TOPIC);
                    }
                }).builder();
        selectDialog.show();
    }

    CursorDialog deleteDialog;

    private void showDeleteDialog() {
        deleteDialog = new CursorDialog.Builder(this).setLayout(R.layout.dialog_delete)
                .notFloating()
                .setViewClick(R.id.tv_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDialog.dismiss();
                    }
                })
                .setViewClick(R.id.tv_submit, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDialog.dismiss();
                        ivContent.setImageResource(R.mipmap.icon_post_addphoto);
                        path = null;
                        ivClearContent.setVisibility(View.INVISIBLE);
                        tvAddImage.setVisibility(View.VISIBLE);
                        //清空了图片,判断有没有文字
                        if (etContent.getText().toString().length() == 0) {
                            tvPublish.setEnabled(false);
                        }
                    }
                })
                .builder();
        deleteDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUSET_IMAGE) {
            if (resultCode == RESULT_OK) {
                //图片来了
                path = data.getStringExtra("path");
                Glide.with(this).load(path).into(ivContent);
                ivClearContent.setVisibility(View.VISIBLE);
                tvAddImage.setVisibility(View.INVISIBLE);
                if (etContent.getText().toString().length() < 256)
                    tvPublish.setEnabled(true);
            } else {
                if (isJump)
                    finish();
            }
        } else if (requestCode == REQUSET_TOPIC) {
            if (resultCode == RESULT_OK) {
                ivSelectTopic.setVisibility(View.GONE);
                tvTopicName.setVisibility(View.VISIBLE);
                tv_update.setVisibility(View.VISIBLE);
                topic = (TopicBean) data.getSerializableExtra("topic");
                tvTopicName.setText(topic.getName());
                rlSelectTopic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(getBaseContext(), SelectTopicActivity.class), REQUSET_TOPIC);
                    }
                });
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0) {
            if (path == null) {
                tvPublish.setEnabled(false);
            } else {
                tvPublish.setEnabled(true);
            }
        } else if (s.length() < 256) {
            tvPublish.setEnabled(true);
        } else {
            tvPublish.setEnabled(false);
        }
        tv_size.setText(s.length() + "/255");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
