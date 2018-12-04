package com.yixia.videoeditor.resourcerecord.recordvoice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.yixia.videoeditor.R;
import com.yixia.videoeditor.resourcerecord.handler.Dispatch;
import com.yixia.videoeditor.resourcerecord.utils.FileUtil;


/**
 * drawableLeft与文本一起居中显示
 */
@SuppressLint("AppCompatCustomView")
public class RecordTextView extends TextView {
    private static final String TAG = "DrawableCenterTextView";
    private static final float PRESS_DELAY_TIME = (float) 1;
    private float mTime;
    public Activity mActivity;

    private int mCurrentState = OnStateEvent.STATE_DEFAULT; // 当前的状态


    private OnRecordSucceessListener listener;
    private PopRecordRemind recordRemindPop;

    public interface OnRecordSucceessListener {
        void onRecordSucceess(String filePath, String fileTimes);
    }

    public void setOnRecordSucceessListener(Activity activity, OnRecordSucceessListener listener) {
        this.listener = listener;
        this.mActivity = activity;
    }


    public RecordTextView(Context context, AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
    }

    public RecordTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecordTextView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    private Runnable timeRunnable = new Runnable() {
        public void run() {
            try {
                mTime += 0.1f;
                if (mTime >= PRESS_DELAY_TIME) {
                    Dispatch.getInstance().removeRunnable(this);
                } else {
                    Dispatch.getInstance().postDelayed(this, 100);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 屏幕的触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.d("MotionEvent", "ACTION_DOWN: " + mCurrentState);
                Dispatch.getInstance().postDelayed(timeRunnable, 100);
                changeState(OnStateEvent.STATE_RECORDING);
                this.setSelected(true);
                break;
            case MotionEvent.ACTION_UP:
                MediaRecorderManager.getInstance().stop();
                if (mTime >= PRESS_DELAY_TIME) {
                    changeState(OnStateEvent.STATE_DEFAULT);
                } else if (mTime < PRESS_DELAY_TIME) {
                    changeState(OnStateEvent.STATE_RECORD_TIME_SHORT);
                    Dispatch.getInstance().removeRunnable(timeRunnable);
                }
                this.setSelected(false);
                mTime = 0;
                break;
        }
        return super.onTouchEvent(event);
    }


    /**
     * 改变
     */
    private void changeState(int state) {
        if (mCurrentState != state) {
            mCurrentState = state;
            if (mActivity == null) return;
            if (state == RecordTextView.OnStateEvent.STATE_DEFAULT) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissRecordRemindPop();
                        String recordAudioPath = MediaRecorderManager.getInstance().getLastRecordPath();
                        String timeText = MediaPlayManager.getInstance().getLengthFromPath(recordAudioPath) + "s";
                        if (listener != null) {
                            listener.onRecordSucceess(recordAudioPath, timeText);
                        }
                    }
                });
            } else if (state == RecordTextView.OnStateEvent.STATE_RECORDING) {
                showRecordRemindPop();
                MediaRecorderManager.getInstance().start(FileUtil.createFile(System.currentTimeMillis() + ".mp3"));
            } else if (state == RecordTextView.OnStateEvent.STATE_RECORD_TIME_SHORT) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissRecordRemindPop();
                        Toast.makeText(mActivity,"录音时长太短，请长按录音",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }



    public void showRecordRemindPop() {
        if (mActivity == null) return;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recordRemindPop = new PopRecordRemind(mActivity, R.layout.pop_record_remind);
                recordRemindPop.showCenter(mActivity, R.layout.pop_record_remind);
            }
        });
    }

    public void dismissRecordRemindPop() {
        if (mActivity == null) return;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (recordRemindPop != null) {
                    recordRemindPop.dismiss();
                }
            }
        });
    }


    public class OnStateEvent {
        public static final int STATE_DEFAULT = 0;
        public static final int STATE_RECORDING = 1;
        public static final int STATE_RECORD_TIME_SHORT = 2;
    }


}