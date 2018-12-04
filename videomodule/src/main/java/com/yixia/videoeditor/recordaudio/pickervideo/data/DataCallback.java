package com.yixia.videoeditor.recordaudio.pickervideo.data;


import com.yixia.videoeditor.recordaudio.pickervideo.entity.Folder;

import java.util.ArrayList;


/**
 * Created by dmcBig on 2017/7/3.
 */

public interface DataCallback {


    void onData(ArrayList<Folder> list);

}
