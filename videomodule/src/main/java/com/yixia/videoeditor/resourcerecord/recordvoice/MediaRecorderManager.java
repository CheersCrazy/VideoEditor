package com.yixia.videoeditor.resourcerecord.recordvoice;

import android.media.MediaRecorder;

/**
 * Created by jkzhang on 2018/6/26
 */
public class MediaRecorderManager {
    public static final String TAG = "Recorder";
    private MediaRecorder mRecorder;
    private String lastRecordPath;
    private static MediaRecorderManager mInstance;

    public MediaRecorderManager() {

    }

    /**
     * 获取单例引用
     *
     * @return
     */
    public static MediaRecorderManager getInstance() {
        if (mInstance == null) {
            synchronized (MediaRecorderManager.class) {
                if (mInstance == null) {
                    mInstance = new MediaRecorderManager();
                }
            }
        }
        return mInstance;
    }


    public String getLastRecordPath() {
        return lastRecordPath;
    }


    /**
     * 开始录制
     *
     * @param filePath
     */
    public void start(String filePath) {
        try {
            if(mRecorder==null) {
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置音频采集方式
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);//设置音频输出格式
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);//设置音频编码方式
            }
            this.lastRecordPath = filePath;
            mRecorder.setOutputFile(filePath);//设置录音文件输出路径
            mRecorder.prepare();
            mRecorder.start();
        } catch (Exception e) {
        }
    }

    /**
     * 释放录音资源
     */
    public void stop() {
        try {
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.release();
            }
        } catch (IllegalStateException e) {

        } catch (RuntimeException e) {

        } catch (Exception e) {

        }
        mRecorder = null;
    }


}
