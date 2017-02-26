package com.example.administrator.matchbox.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.GuideAdapter;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.bean.UserBean;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.model.UserModel;
import com.example.administrator.matchbox.utils.FileUtils;
import com.example.administrator.matchbox.utils.LogUtils;
import com.example.administrator.matchbox.utils.SPHelper;
import com.example.administrator.matchbox.weiget.CursorDialog;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements SurfaceHolder.Callback, Runnable, IBeanCallback<UserBean> {


    MediaPlayer mMediaPlayer;

    @BindView(R.id.sv_background)
    SurfaceView svBackground;

    @BindView(R.id.vp_carousel)
    ViewPager vpCarousel;

    @BindView(R.id.ll_points)
    LinearLayout llPoints;

    @Override
    protected void initView() {
        svBackground.getHolder().addCallback(this);
        shwoDialog();
        initViewPager();
        initPoint();
    }

    //是否自动登录
    private void isAutoLogin() {
        SPHelper sphelper = new SPHelper(this, "user");
        int id = sphelper.getInt("userId");
        if (id != -1) {
            //自动登录
            UserModel userModle = new UserModel();
            userModle.getUserInfo(id + "", this);
        }
    }

    int currPointIndex;

    private void initPoint() {
        for (int i = 0; i < 5; i++) {
            View view = new View(this);
            view.setBackgroundResource(R.drawable.guide_point);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.setMargins(10, 10, 10, 10);
            view.setLayoutParams(params);
            llPoints.addView(view);
        }
        //第一个默认选中
        llPoints.getChildAt(0).setSelected(true);
    }

    private void initViewPager() {
        //4
        int currPager = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % 5;
        vpCarousel.setAdapter(new GuideAdapter(this));
        vpCarousel.setCurrentItem(currPager);
        vpCarousel.addOnPageChangeListener(pagerChangedListener);
        //按住的时候 不能播
        //开始和结束
        startGuide();
    }

    ViewPager.OnPageChangeListener pagerChangedListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            llPoints.getChildAt(currPointIndex % 5).setSelected(false);
            llPoints.getChildAt(position % 5).setSelected(true);
            currPointIndex = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (!handler.hasMessages(1)) {
                    startGuide();
                }
            } else {
                stopGuide();
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            vpCarousel.setCurrentItem(vpCarousel.getCurrentItem() + 1);
            sendEmptyMessageDelayed(1, 2000);
        }
    };

    private void startGuide() {
        handler.sendEmptyMessageDelayed(1, 2000);
    }

    private void stopGuide() {
        handler.removeMessages(1);
    }


    private void shwoDialog() {
        //显示启动页
        ImageView iv = new ImageView(this);
        iv.setBackgroundResource(R.mipmap.welcomebg);
        CursorDialog dialog = new CursorDialog.Builder(this)
                .full()
                .setView(iv)
                .builder();
        dialog.task(this, null);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isAutoLogin();
            }
        });
        dialog.show(3000);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }


    @OnClick({R.id.tv_login, R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterPhoneActivity.class));
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //开始播放视频
        mMediaPlayer = MediaPlayer.create(this, R.raw.to_login_art);
        mMediaPlayer.setDisplay(holder);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    @Override
    public void run() {
        //加载数据
        List<File> files = FileUtils.getAllImageFile();
        MyApp.getInstance().setImgList(files);
    }

    @Override
    public void Success(UserBean userBean) {
        MyApp.getInstance().setUserBean(userBean);
        startActivity(new Intent(this, HomeActivity.class));
    }

    @Override
    public void onError(String msg) {
        LogUtils.e(msg);
    }
}
