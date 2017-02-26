package com.example.administrator.scaleimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.widget.ImageView.ScaleType.MATRIX;

/**
 * Created by Administrator on 2016/11/28.
 */

public class ScaleImageView extends ImageView {
    Bitmap bmp;
    Paint paint;

    public static final int NONE = 1;
    public static final int DRAG = 2;
    public static final int SCALE = 3;

    private int type = NONE;

    //最大缩放
    //最小缩放
    public static final float MIN = 1f;

    public static final float MAX = 3f;

    //拖拽点
    PointF dragPoint;
    //缩放中心点
    PointF centerPoint;

    //当前缩放的长度
    float distance;


    Matrix matrix;
    //是否画线
    boolean isDrawLine;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isDrawLine = false;
            invalidate();
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isDrawLine) {
            for (int i = 1; i < 3; i++) {
                canvas.drawLine(0, getHeight() / 3 * i, getWidth(), getHeight() / 3 * i, paint);
                canvas.drawLine(getWidth() / 3 * i, 0, getWidth() / 3 * i, getHeight(), paint);
            }
        }
    }

    public ScaleImageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        //必须设置
        setScaleType(MATRIX);
        matrix = new Matrix(); // 平移，旋转，缩放，倾斜
        setImageMatrix(matrix);
        paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
    public void setImageBitmap(Bitmap bm) {
        setBackgroundColor(Color.rgb(0x31, 0x33, 0x35));
        int sceen = getResources().getDisplayMetrics().widthPixels;
        //图片到底有多大
        float width = bm.getWidth();
        float height = bm.getHeight();
        //如果图片的宽高比view小，那么需要放大
        if (width < sceen || height < sceen) {
            float min = width < height ? width : height;
            float rate = sceen / min;
            bm = ThumbnailUtils.extractThumbnail(bm, (int) (width * rate), (int) (height * rate));
        }
        this.bmp = bm;
        super.setImageBitmap(bm);
        matrix = new Matrix();
        setImageMatrix(matrix);
    }

    //在sd卡中的名称
    public String getImagePath(String name) {
        //在子线程中操作
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getWidth(), Bitmap.Config.ARGB_8888);
        //吧屏幕上的绘制进去
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bmp, matrix, null);
        canvas.save();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isDrawLine = true;
        if (handler.hasMessages(1))
            handler.removeMessages(1);
        handler.sendEmptyMessageDelayed(1, 2000);


        PointF pointf = new PointF(event.getX(), event.getY());
        //多指运算
        switch (event.getAction() & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //点上去 就显示出来
                type = DRAG;
                dragPoint = new PointF(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                type = SCALE;
                float x1 = event.getX();
                float x2 = event.getX(1);
                float y1 = event.getY();
                float y2 = event.getY(1);
                centerPoint = new PointF((x1 + x2) / 2, (y1 + y2) / 2);
                distance = getDistance(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //缩放复位
                scaleReset();
                break;
            case MotionEvent.ACTION_UP:
                //拖拽复位
                dragReset();
                break;
            case MotionEvent.ACTION_MOVE:
                //根据状态来判断是缩放还是拖拽
                if (type == DRAG) {
                    //当前的位置，移动的距离
                    matrix.postTranslate(pointf.x - dragPoint.x, pointf.y - dragPoint.y);
                    dragPoint = new PointF(event.getX(), event.getY());
                } else if (type == SCALE) {
                    float rate = getDistance(event) / distance;
                    matrix.postScale(rate, rate, centerPoint.x, centerPoint.y);
                }
                break;
        }
        setImageMatrix(matrix);
        invalidate();
        return true;
    }

    private void scaleReset() {
        //matrix 9个值
        float[] value = new float[9];
        matrix.getValues(value);
        if (value[0] < MIN)
            matrix.setScale(MIN, MIN, centerPoint.x
                    , centerPoint.y);
        else if (value[0] > MAX)
            matrix.setScale(MAX, MAX, centerPoint.x, centerPoint.y);
    }

    private void dragReset() {
        //需要获取图片在哪里
        RectF rectf = new RectF(0, 0, bmp.getWidth(), bmp.getHeight());
        matrix.mapRect(rectf);
        Log.e("TAG", "矩形的上" + rectf.top + "  矩形的下" + rectf.bottom + "  矩形的右" + rectf.right + "  矩形的左" + rectf.left);
        Log.e("TAG", "view的高度" + getHeight() + "  view的宽度" + getWidth());
        //水平方向
        float x = 0, y = 0;
        if (rectf.top > 0) {
            y = 0 - rectf.top;
        } else if (rectf.bottom < getHeight()) {
            y = getHeight() - rectf.bottom;//正 右和下
        }

        if (rectf.left > 0) {
            x = 0 - rectf.left;
        } else if (rectf.right < getWidth()) {
            x = getWidth() - rectf.right;
        }
        matrix.postTranslate(x, y);
    }


    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setImageBitmap(BitmapFactory.decodeResource(getResources(), resId));
    }


    public float getDistance(MotionEvent event) {
        float x1 = event.getX();
        float x2 = event.getX(1);
        float y1 = event.getY();
        float y2 = event.getY(1);
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

}
