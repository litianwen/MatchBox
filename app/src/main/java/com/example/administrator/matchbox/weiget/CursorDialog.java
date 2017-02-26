package com.example.administrator.matchbox.weiget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.example.administrator.matchbox.R;

/**
 * Created by Administrator on 2016/11/23.
 */

/**
 * 使用Builder模式来建立
 */
public class CursorDialog extends Dialog {

    private CursorDialog(Context context) {
        super(context);
    }

    private CursorDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    //加载一个异步任务
    public void task(final Runnable run, final Callback callback) {
        new Thread() {
            @Override
            public void run() {
                run.run();
                if (callback != null)
                    callback.onTaskFinish();
            }
        }.start();
    }

    public void show(final int time) {
        //延迟关闭
        super.show();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                dismiss();
            }
        };
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        }.start();
    }


    public interface Callback {
        void onTaskFinish();
    }


    public static class Builder {

        private final Context mContext;
        //参数
        //是否全屏 默认不全屏
        private boolean isFloating = true;//浮动 窗口化
        private String title;//flase
        private boolean isFull;

        private View layout;


        public Builder setLayout(int res) {
            layout = View.inflate(mContext, res, null);
            return this;
        }

        public Builder setViewClick(int id, View.OnClickListener listener) {
            View view = layout.findViewById(id);
            view.setOnClickListener(listener);
            return this;
        }

        public Builder setViewClick(int id, AdapterView.OnItemClickListener listener) {
            View view = layout.findViewById(id);
            if (view instanceof AdapterView) {
                AdapterView v = (AdapterView) view;
                v.setOnItemClickListener(listener);
            }
            return this;
        }


        public Builder setView(View v) {
            layout = v;
            return this;
        }

        public Builder notFloating() {
            isFloating = false;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder full() {
            isFull = true;
            isFloating = false;
            return this;
        }


        public Builder(Context context) {
            this.mContext = context;
        }


        public CursorDialog builder() {
            CursorDialog dialog;
            if (isFloating) {
                //浮动的
                if (TextUtils.isEmpty(title)) {
                    dialog = new CursorDialog(mContext, R.style.CursorDialogThemeNotTitle);
                } else {
                    dialog = new CursorDialog(mContext);
                }
            } else {
                if (TextUtils.isEmpty(title)) {
                    if (isFull) {
                        //全屏
                        dialog = new CursorDialog(mContext, R.style.CursorDialogNotTitleFullTheme);
                    } else {
                        //非全屏
                        dialog = new CursorDialog(mContext, R.style.CursorDialogNotFloatNotTitileTheme);
                    }
                } else {
                    if (isFull) {
                        //全屏
                        dialog = new CursorDialog(mContext, R.style.CursorDialogFullTheme);
                    } else {
                        //非全屏
                        dialog = new CursorDialog(mContext, R.style.CursorDialogNotFloatTheme);
                    }
                }
            }
            if (layout == null)
                throw new IllegalArgumentException("view不能为空");
            dialog.setContentView(layout);
            return dialog;
        }

    }
}