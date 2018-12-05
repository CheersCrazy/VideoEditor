package com.yixia.videoeditor;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseVideoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImages();
            }
        });


        findViewById(R.id.add_Image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImages();
            }
        });
    }
}
