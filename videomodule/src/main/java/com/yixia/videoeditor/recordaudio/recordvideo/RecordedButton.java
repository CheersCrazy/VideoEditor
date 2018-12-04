package com.yixia.videoeditor.recordaudio.recordvideo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yixia.videoeditor.R;

/**
 * Created by zhaoshuang on 17/2/8.
 */

public class RecordedButton extends View {

    private int measuredWidth = -1;
    private Paint paint;
    private int colorGray;
    private float radius1;
    private float radius2;
    private float zoom = 0.8f;//初始化缩放比例
    private int dp5;
    private Paint paintProgress;
    private int colorBlue;
    /**
     * 当前进度 以角度为单位
     */
    private float girthPro;
    private RectF oval;
    private int max;
    private OnGestureListener onGestureListener;
    private int animTime = 150;
    private float downX;
    private float downY;
    /**
     * button是否处于打开状态
     */
    private boolean isOpenMode = true;

    private Paint paintSplit;
    private Paint paintDelete;
    private ValueAnimator buttonAnim;
    private float progress;

    private float rawX = -1;
    private float rawY = -1;

    public RecordedButton(Context context) {
        super(context);
        init();
    }

    public RecordedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecordedButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        dp5 = 5;
        colorGray = getResources().getColor(R.color.bg_main);
        colorBlue = getResources().getColor(R.color.blue);

        paint = new Paint();
        paint.setAntiAlias(true);

        paintProgress = new Paint();
        paintProgress.setAntiAlias(true);
        paintProgress.setColor(colorBlue);
        paintProgress.setStrokeWidth(dp5);
        paintProgress.setStyle(Paint.Style.STROKE);

        paintSplit = new Paint();
        paintSplit.setAntiAlias(true);
        paintSplit.setColor(Color.WHITE);
        paintSplit.setStrokeWidth(dp5);
        paintSplit.setStyle(Paint.Style.STROKE);

        paintDelete = new Paint();
        paintDelete.setAntiAlias(true);
        paintDelete.setColor(Color.RED);
        paintDelete.setStrokeWidth(dp5);
        paintDelete.setStyle(Paint.Style.STROKE);

        //设置绘制大小
        oval = new RectF();
    }


    public float getCurrentPro() {
        return progress;
    }

    public interface OnGestureListener {
        void onLongClick();

        void onOver();
    }

    public void setOnGestureListener(OnGestureListener onGestureListener) {
        this.onGestureListener = onGestureListener;
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (onGestureListener != null) {
                isOpenMode = true;
                onGestureListener.onLongClick();
            }
        }
    };


    private boolean cleanResponse;//清除所有响应

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                myHandler.sendEmptyMessageDelayed(0, animTime);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!cleanResponse) {
                    if (!myHandler.hasMessages(0)) {
                        if (isOpenMode) {
                            if (onGestureListener != null) onGestureListener.onOver();
                            closeButton();
                        }
                    } else {
                        myHandler.removeMessages(0);
                    }
                }

                cleanResponse = false;

                break;
        }
        return true;
    }


    public void closeButton() {
        if (isOpenMode) {
            isOpenMode = false;
        }
    }


    public void setMax(int max) {
        this.max = max;
    }

    /**
     * 设置进度
     */
    public void setProgress(float progress) {

        this.progress = progress;
        float ratio = progress / max;
        girthPro = 365 * ratio;
        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (measuredWidth == -1) {
            measuredWidth = getMeasuredWidth();

            radius1 = measuredWidth * zoom / 2;
            radius2 = measuredWidth * zoom / 2 - dp5;

            oval.left = dp5 / 2;
            oval.top = dp5 / 2;
            oval.right = measuredWidth - dp5 / 2;
            oval.bottom = measuredWidth - dp5 / 2;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (rawX == -1) {
            rawX = getX();
            rawY = getY();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //绘制外圈
        paint.setColor(colorGray);
        canvas.drawCircle(measuredWidth / 2, measuredWidth / 2, radius1, paint);
        //绘制内圈
        paint.setColor(Color.WHITE);
        canvas.drawCircle(measuredWidth / 2, measuredWidth / 2, radius2, paint);
        //绘制进度
        canvas.drawArc(oval, 270, girthPro, false, paintProgress);

    }
}