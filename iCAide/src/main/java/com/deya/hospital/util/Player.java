package com.deya.hospital.util;

import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

import java.io.IOException;

public class Player implements OnBufferingUpdateListener,
        OnCompletionListener, MediaPlayer.OnPreparedListener {
    public MediaPlayer mediaPlayer;
    private AnimationDrawable falshdrawable;

    public Player(AnimationDrawable falshdrawable) {

        this.falshdrawable = falshdrawable;
        initMediaPlayer();

    }


    public void initMediaPlayer(){
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }
    }

    //*****************************************************

    public void play() {
        mediaPlayer.start();
        falshdrawable.stop();
    }

    public void playUrl(String videoUrl) {
        if(null==mediaPlayer){
            initMediaPlayer();
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.prepare();//prepare之后自动播放
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;

    }

    public void pause() {
        mediaPlayer.pause();

        falshdrawable.stop();
        falshdrawable.selectDrawable(2);
    }

    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        } else {
            return false;
        }
    }

    public void stop() {
        falshdrawable.stop();
        falshdrawable.selectDrawable(2);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    /**
     * 通过onPrepared播放
     */
    public void onPrepared(MediaPlayer arg0) {
        arg0.start();
        falshdrawable.start();
    }


    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
//        if(mediaPlayer.getDuration()==100){
//            mediaPlayer.stop();
//        }
        Log.e("player", mediaPlayer.getDuration() + "");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stop();
    }
}