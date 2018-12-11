package com.yixia.videoeditor.manager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.yixia.videoeditor.resourcerecord.recordvoice.IPlayCallBack;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.yixia.videoeditor.utils.NumberFormatUtils;

/**
 * 基于MediaPlay播放音频文件
 */
public class MediaPlayManager {

    public static final String TAG = "MediaPlayManager";

    private MediaPlayer mediaPlayer;

    private static MediaPlayManager mInstance;


    private List<IPlayCallBack> callBacks = new ArrayList<>();


    /**
     * 获取单例引用
     *
     * @return
     */
    public static MediaPlayManager getInstance() {
        if (mInstance == null) {
            synchronized (MediaPlayManager.class) {
                if (mInstance == null) {
                    mInstance = new MediaPlayManager();
                }
            }
        }
        return mInstance;
    }


    public MediaPlayManager() {
        mediaPlayer = new MediaPlayer();
    }

    public String getLengthFromPath(String path) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.reset();
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.prepare();
            String time = NumberFormatUtils.getDoubleOne((double) mediaPlayer.getDuration() / 1000);
            return time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public void getLengthFromUrl(String url, final OnGetAudioTimeListener listener, final int position) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    String time = (double) mp.getDuration() / 1000 + "s";
                    listener.OnGetAudioTimeListener(time, position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playUrl(Context context, String url, final IPlayCallBack iPlayCallBack) {
        if (context == null) return;
        callBacks.clear();
        if (!callBacks.contains(iPlayCallBack)) {
            callBacks.add(iPlayCallBack);
        }
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            Uri uri = Uri.parse(url);
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    for (int i = 0; i < callBacks.size(); i++) {
                        callBacks.get(i).onBegin();
                    }
                }
            });
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    for (int i = 0; i < callBacks.size(); i++) {
                        callBacks.get(i).onComplete();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playByUrl(Context context, String url, final IPlayCallBack iPlayCallBack) {
        if (context == null) return;
        callBacks.clear();
        if (!callBacks.contains(iPlayCallBack)) {
            callBacks.add(iPlayCallBack);
        }

        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(context, "audio/mpeg"); //  userAgent -> audio/mpeg  不能为空
        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource(); //创建一个媒体连接源
        MediaSource mediaSource1 = new ExtractorMediaSource.Factory(defaultDataSourceFactory).createMediaSource(Uri.parse(url)); //创建一个播放数据源
        concatenatingMediaSource.addMediaSource(mediaSource1);
        player.setPlayWhenReady(true);

        for (int i = 0; i < callBacks.size(); i++) {
            callBacks.get(i).onBegin();
        }
        player.addListener(new Player.EventListener() {

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    for (int i = 0; i < callBacks.size(); i++) {
                        callBacks.get(i).onComplete();
                    }
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                for (int i = 0; i < callBacks.size(); i++) {
                    callBacks.get(i).onComplete();
                }
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
        player.prepare(concatenatingMediaSource);
        for (int i = 0; i < callBacks.size(); i++) {
            callBacks.get(i).onBegin();
        }
    }


    public void play(String path, final IPlayCallBack iPlayCallBack) {

        if (!callBacks.contains(iPlayCallBack)) {
            callBacks.add(iPlayCallBack);
        }
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.reset();
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    for (int i = 0; i < callBacks.size(); i++) {
                        callBacks.get(i).onComplete();
                    }
                }
            });
            for (int i = 0; i < callBacks.size(); i++) {
                callBacks.get(i).onBegin();
            }

            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            for (int i = 0; i < callBacks.size(); i++) {
                callBacks.get(i).onStop();
            }
        }
    }


    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }


    public interface OnGetAudioTimeListener {
        void OnGetAudioTimeListener(String timeLength, int position);
    }


}
