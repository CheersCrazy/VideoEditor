package com.yixia.videoeditor;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.yixia.videoeditor.pickerimage.ImagePicker;
import com.yixia.videoeditor.pickerimage.bean.ImageItem;
import com.yixia.videoeditor.pickerimage.loader.GlideImageLoader;
import com.yixia.videoeditor.pickerimage.ui.ImageGridActivity;
import com.yixia.videoeditor.pickerimage.ui.ImagePreviewDelActivity;
import com.yixia.videoeditor.recordaudio.pickervideo.PickerActivity;
import com.yixia.videoeditor.recordaudio.pickervideo.PickerConfig;
import com.yixia.videoeditor.recordaudio.recordvideo.RecordedActivity;
import com.yixia.videoeditor.selectphotos.SelectDialog;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public abstract class BaseVideoActivity extends Activity implements EasyPermissions.PermissionCallbacks
        , SelectDialog.OnSelectClickListener {

    public static final int REQUEST_CODE_SELECT = 100;
    int REQUEST_CODE_PREVIEW = 101;
    SelectDialog selectDialog;
    boolean selectVideo;
    public int requestCode;//图片requestcode
    private int maxImgCount = 3;
    protected Activity mContext;
    public ArrayList<ImageItem> images = new ArrayList<>();
    public String selectVideoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initImagePicker();
    }


    public void showSelectDialog(Boolean isFromVideo, int isRequestCode) {
        selectDialog = new SelectDialog(this, R.style.transparentFrameWindowStyle);
        selectDialog.setOnSelectClickListener(this);
        selectVideo = isFromVideo;
        requestCode = isRequestCode;
        if (!this.isFinishing()) {
            selectDialog.show();
        }
    }

    @Override
    public void onClickCamera() {
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            EasyPermissions.requestPermissions(this, "拍照需要摄像头", 1, Manifest.permission.CAMERA);
            return;
        }
        startCamera();
    }

    private void startCamera() {
        if (selectVideo) {
            startActivityForResult(new Intent(mContext, RecordedActivity.class), REQUEST_CODE_SELECT);
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ImageGridActivity.EXTRAS_IMAGES, images);
            ImagePicker.getInstance().setSelectLimit(maxImgCount);
            Intent intent = new Intent(this, ImageGridActivity.class);
            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CODE_SELECT);
        }
    }

    @Override
    public void onClickPhoto() {
        if (selectVideo) {
            startActivityForResult(new Intent(mContext, PickerActivity.class), PickerConfig.PICKER_VIDEO);
        } else {
            //打开选择,本次允许选择的数量
            ImagePicker.getInstance().setSelectLimit(maxImgCount);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ImageGridActivity.EXTRAS_IMAGES, images);
            Intent intent1 = new Intent(this, ImageGridActivity.class);
            intent1.putExtras(bundle);
            startActivityForResult(intent1, requestCode);
        }

    }

    /**
     * 展示images
     *
     * @param images
     * @param position
     */
    public void showSelectImages(ArrayList<ImageItem> images, int position) {
        if (images == null || images.size() <= 0) {
            Toast.makeText(this, "照片不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if (position > images.size()) {
            Toast.makeText(this, "照片显示位置不正确", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, images);
        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, 0);
        intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
        startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());//设置图片加载器
        imagePicker.setShowCamera(false);                      //显示拍照按钮
        imagePicker.setCrop(false);                          //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);               //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setFocusWidth(800);                     //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                   //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
            }
        } else if (resultCode == PickerConfig.RESULT_CODE && data != null) {
            selectVideoPath = data.getStringExtra(PickerConfig.EXTRA_RESULT);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (perms == null) return;
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder builder = new AppSettingsDialog.Builder(this);
            builder.setRationale("需要相机权限，请到设置中为应用程序打开所需权限");
            builder.setTitle("权限请求");
            builder.build().show();
        }
    }

    public void onPermissionsGranted(int requestCode, List<String> perms) {
        startCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
