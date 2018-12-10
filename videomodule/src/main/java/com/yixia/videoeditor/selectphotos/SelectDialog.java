package com.yixia.videoeditor.selectphotos;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yixia.videoeditor.R;

/**
 * 选择对话框
 */

public class SelectDialog extends Dialog implements View.OnClickListener {

    private Activity mActivity;
    private Button mMBtn_Cancel;
    private TextView mPhotoBtn;
    private TextView mCameraBtm;
    private OnSelectClickListener onSelectClickListener;


    public void setOnSelectClickListener(OnSelectClickListener onSelectClickListener) {
        this.onSelectClickListener = onSelectClickListener;
    }

    public SelectDialog(Activity activity, int theme) {
        super(activity, theme);
        mActivity = activity;
        setCanceledOnTouchOutside(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.view_dialog_video,
                null);
        setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        Window window = getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = LayoutParams.MATCH_PARENT;
        wl.height = LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        onWindowAttributesChanged(wl);

        initViews();
    }

    private void initViews() {
        mMBtn_Cancel = findViewById(R.id.mBtn_Cancel);
        mPhotoBtn = (TextView) findViewById(R.id.mBtn_photo);
        mCameraBtm = findViewById(R.id.mBtn_caneme);

        mMBtn_Cancel.setOnClickListener(this);
        mPhotoBtn.setOnClickListener(this);
        mCameraBtm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.mBtn_Cancel) {
            dismiss();
        } else if (id == R.id.mBtn_photo) {
            dismiss();
            if (onSelectClickListener != null) {
                onSelectClickListener.onClickPhoto();
            }
        } else if (id == R.id.mBtn_caneme) {
            dismiss();
            if (onSelectClickListener != null) {
                onSelectClickListener.onClickCamera();
            }
        }

    }


    public interface OnSelectClickListener {
        void onClickPhoto();

        void onClickCamera();
    }


}
