package com.yixia.videoeditor;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseVideoActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.add_Image).setOnClickListener(this);
        findViewById(R.id.showImage).setOnClickListener(this);
        findViewById(R.id.GetVideos).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_Image:
                showSelectDialog(false, requestCode);
                break;
            case R.id.showImage:
                showSelectImages(images, 0);
                break;
            case R.id.GetVideos:
                showSelectDialog(true, requestCode);
                break;
        }
    }
}
