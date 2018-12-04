package com.yixia.videoeditor.recordaudio.pickervideo;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.yixia.videoeditor.R;
import com.yixia.videoeditor.recordaudio.pickervideo.adapter.MediaGridAdapter;
import com.yixia.videoeditor.recordaudio.pickervideo.adapter.SpacingDecoration;
import com.yixia.videoeditor.recordaudio.pickervideo.data.DataCallback;
import com.yixia.videoeditor.recordaudio.pickervideo.data.VideoLoader;
import com.yixia.videoeditor.recordaudio.pickervideo.entity.Folder;
import com.yixia.videoeditor.recordaudio.pickervideo.entity.Media;

import java.util.ArrayList;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by dmcBig on 2017/6/9.
 */

public class PickerActivity extends AppCompatActivity implements DataCallback, View.OnClickListener {


    RecyclerView recyclerView;
    Button done;
    MediaGridAdapter gridAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_picker);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        findViewById(R.id.btn_back).setOnClickListener(this);
        done = (Button) findViewById(R.id.btn_ok);
        done.setOnClickListener(this);
        createAdapter();
        getMediaData();
    }


    void createAdapter() {
        //创建默认的线性LayoutManager
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, PickerConfig.GridSpanCount);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SpacingDecoration(PickerConfig.GridSpanCount, PickerConfig.GridSpace));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        ArrayList<Media> medias = new ArrayList<>();
        ArrayList<Media> select = new ArrayList<>();
        gridAdapter = new MediaGridAdapter(medias, this, select, PickerConfig.DEFAULT_SELECTED_MAX_COUNT, PickerConfig.DEFAULT_SELECTED_MAX_SIZE);
        recyclerView.setAdapter(gridAdapter);
    }


    @AfterPermissionGranted(119)
    void getMediaData() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            getLoaderManager().initLoader(PickerConfig.PICKER_VIDEO, null, new VideoLoader(this, this));
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.READ_EXTERNAL_STORAGE), 119, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onData(ArrayList<Folder> list) {
        setView(list);
    }

    void setView(ArrayList<Folder> list) {
        gridAdapter.updateAdapter(list.get(0).getMedias());
        setButtonText();
        gridAdapter.setOnItemClickListener(new MediaGridAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Media data, ArrayList<Media> selectMedias) {
                setButtonText();
            }
        });
    }

    void setButtonText() {
        done.setEnabled(gridAdapter.getSelectMedias().size()>0);
        done.setText(getString(R.string.done) + "(" + gridAdapter.getSelectMedias().size() + "/" + PickerConfig.DEFAULT_SELECTED_MAX_COUNT + ")");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            finish();
        } else if (id == R.id.btn_ok) {
            if (gridAdapter.getSelectMedias().size()>0){
                done(gridAdapter.getSelectMedias());
            }

        }
    }

    public void done(ArrayList<Media> selects) {
        Intent intent = new Intent();
        intent.putExtra(PickerConfig.EXTRA_RESULT, selects.get(0).path);
        setResult(PickerConfig.RESULT_CODE, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        Glide.get(this).clearMemory();
        super.onDestroy();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
