package com.yixia.videoeditor.manager

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import cn.jzvd.JZUserAction
import cn.jzvd.JZUserActionStandard
import cn.jzvd.JZVideoPlayer
import cn.jzvd.JZVideoPlayerStandard
import com.yixia.videoeditor.R


/**
 * Created by jkzhang on 2018/6/28
 */
class PlayVideoPop(protected var mContext: Context) : PopupWindow(mContext) {


    private var playjZView: JZVideoPlayerStandard? = null


    var view: View = LayoutInflater.from(mContext).inflate(R.layout.layout_video_play, null)

    init {
        contentView = view
        width = LinearLayout.LayoutParams.WRAP_CONTENT
        height = LinearLayout.LayoutParams.WRAP_CONTENT
        isOutsideTouchable = true
        isFocusable = true
        isTouchable = true
        playjZView = view.findViewById(R.id.videoplayer)
        setBackgroundDrawable(ColorDrawable(0))
    }


    fun setUrl(url: String): PlayVideoPop {
        playjZView?.setUp(url, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "")
        JZVideoPlayer.setJzUserAction(MyUserActionStandard())
        return this
    }


    /**
     * 这只是给埋点统计用户数据用的，不能写和播放相关的逻辑，监听事件请参考MyJZVideoPlayerStandard，复写函数取得相应事件
     */
    internal inner class MyUserActionStandard : JZUserActionStandard {

        override fun onEvent(type: Int, url: Any, screen: Int, vararg objects: Any) {
            if (type == JZUserAction.ON_AUTO_COMPLETE || type == JZUserAction.ON_CLICK_START_AUTO_COMPLETE) {
//                dismiss()
            }else if ( type == JZUserAction.ON_CLICK_START_ICON){
                playjZView?.dissmissControlView()
            }

        }
    }


    fun showCenter(context: Context, layoutId: Int): PlayVideoPop {
        if (!isShowing) {
            val rootView = LayoutInflater.from(context).inflate(layoutId, null)
            showAtLocation(rootView, Gravity.CENTER, 0, 0)
        }
        return this
    }


    override fun dismiss() {
        super.dismiss()
        JZVideoPlayer.releaseAllVideos()
    }




}
