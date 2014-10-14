package com.example.jianyang.kamcordvideofeed;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by jianyang on 10/6/14.
 */
public class VideoPlayerActivity extends Activity {
    private String mVidAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoplayer);

        VideoView vidView = (VideoView)findViewById(R.id.myVideo);
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        String videoUrl = intent.getStringExtra(MainActivity.TAG_VIDEO_URL);

        if (videoUrl != null) {
            mVidAddress = videoUrl;
        }

        Uri vidUri =  Uri.parse(mVidAddress);
        vidView.setVideoURI(vidUri);
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);
        vidView.start();
    }
}
