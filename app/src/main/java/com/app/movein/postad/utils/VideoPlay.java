package com.app.movein.postad.utils;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.app.movein.R;

public class VideoPlay extends Activity {

    String TAG = "com.ebookfrenzy.videoplayer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postvideoplay);
        savedInstanceState = getIntent().getExtras();
        String mVideoFilePath = savedInstanceState.getString("VideoPath");

        final VideoView videoView =
                (VideoView) findViewById(R.id.postvideoplayer);

        videoView.setVideoPath(mVideoFilePath);

        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setOnPreparedListener(new
                                                MediaPlayer.OnPreparedListener() {
                                                    @Override
                                                    public void onPrepared(MediaPlayer mp) {
                                                        Log.i(TAG, "Duration = " +
                                                                videoView.getDuration());
                                                    }
                                                });

        videoView.start();


    }
}