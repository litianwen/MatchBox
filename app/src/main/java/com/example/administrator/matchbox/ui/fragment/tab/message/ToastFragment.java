package com.example.administrator.matchbox.ui.fragment.tab.message;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.BaseFragment;
import com.example.administrator.matchbox.bean.PushBean;
import com.example.administrator.matchbox.ui.activity.CommentListActivity;
import com.example.administrator.matchbox.ui.activity.FansListActivity;
import com.example.administrator.matchbox.ui.activity.TopListActivity;
import com.example.administrator.matchbox.utils.PushUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/15.
 */

public class ToastFragment extends BaseFragment {

    @BindView(R.id.tv_commentsize)
    TextView tvCommentsize;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.tv_topsize)
    TextView tvTopsize;
    @BindView(R.id.iv_arrow2)
    ImageView ivArrow2;
    @BindView(R.id.tv_fanssize)
    TextView tvFanssize;
    @BindView(R.id.iv_arrow3)
    ImageView ivArrow3;

    @Override
    protected void initView() {
        //获取第一个
    }

    @Override
    public void onResume() {
        super.onResume();
        int commentCount = PushUtils.getUnReadCountByType(PushBean.TYPE_COMMENT);
        int topCount = PushUtils.getUnReadCountByType(PushBean.TYPE_TOP);
        int fansCount = PushUtils.getUnReadCountByType(PushBean.TYPE_FANS);
        if (commentCount > 0) {
            ivArrow.setVisibility(View.INVISIBLE);
            tvCommentsize.setVisibility(View.VISIBLE);
            tvCommentsize.setText(commentCount + "");
        } else {
            ivArrow.setVisibility(View.VISIBLE);
            tvCommentsize.setVisibility(View.INVISIBLE);
        }

        if (topCount > 0) {
            ivArrow2.setVisibility(View.INVISIBLE);
            tvTopsize.setVisibility(View.VISIBLE);
            tvTopsize.setText(topCount + "");
        } else {
            ivArrow2.setVisibility(View.VISIBLE);
            tvTopsize.setVisibility(View.INVISIBLE);
        }

        if (fansCount > 0) {
            ivArrow3.setVisibility(View.INVISIBLE);
            tvFanssize.setVisibility(View.VISIBLE);
            tvFanssize.setText(fansCount + "");
        } else {
            ivArrow3.setVisibility(View.VISIBLE);
            tvFanssize.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_toast;
    }

    @OnClick({R.id.rl_top, R.id.rl_comment, R.id.rl_fans})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_top:
                PushUtils.setReadByType(PushBean.TYPE_TOP);
                startActivity(new Intent(getContext(), TopListActivity.class));
                break;
            case R.id.rl_comment:
                PushUtils.setReadByType(PushBean.TYPE_COMMENT);
                startActivity(new Intent(getContext(), CommentListActivity.class));
                break;
            case R.id.rl_fans:
                PushUtils.setReadByType(PushBean.TYPE_FANS);
                startActivity(new Intent(getContext(), FansListActivity.class));
                break;
        }
    }
}
