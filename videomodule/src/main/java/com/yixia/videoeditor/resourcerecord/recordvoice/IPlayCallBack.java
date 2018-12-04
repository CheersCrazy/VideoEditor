package com.yixia.videoeditor.resourcerecord.recordvoice;

/**
 * Created by zxwei on 2017/11/29.
 */

public interface IPlayCallBack {
    /**
     * 开始播放
     */
    void onBegin();

    /**
     * 正常播放完成
     */
    void onComplete();


    /**
     * 正常播放完成
     */
    void onStop();

}
