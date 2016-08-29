package com.example.anurag.connect_net;

import android.app.ProgressDialog;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class playVideo extends AppCompatActivity {
    String filename;
    String category;
    String url;
    int id;
    private static ProgressDialog progressDialog;
  //  String videourl = "http://insigniathefest.com/kapil/APP/AndroidFileUpload/uploads/vid2.mp4"; //It should be 3gp or mp4
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        filename = getIntent().getStringExtra("filename");
        category = getIntent().getStringExtra("category");
        url = getIntent().getStringExtra("url");
        id = getIntent().getIntExtra("id", -1);

        videoView = (VideoView) findViewById(R.id.video_View);

        progressDialog = ProgressDialog.show(playVideo.this, "", "Bufferingvideo...", true);
        progressDialog.setCancelable(true);
if(id!=-1) {
    if (!PresentLocally()) {
        PlayVideo(new DownloadedFileAddress().makeWebURL(id, filename, url));
    }
}
        else
{
    Log.v("given id does not exist","id  is -1");
}
    }

    private void PlayVideo(String address) {
        try {
            Log.v("given address",address);
           // Toast.makeText(getApplicationContext(),address,Toast.LENGTH_LONG);
            getWindow().setFormat(PixelFormat.TRANSLUCENT);
            MediaController mediaController = new MediaController(playVideo.this);
            mediaController.setAnchorView(videoView);

            Uri videouri = Uri.parse(address);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(videouri);
            videoView.requestFocus();
            videoView.seekTo(100);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    progressDialog.dismiss();
                    videoView.start();
                }
            });

        } catch (Exception e) {
            progressDialog.dismiss();
            System.out.println("Video Play Error :" + e.toString());
            finish();
        }

    }

    private Boolean PresentLocally() {
        String LocalAddress = new DownloadedFileAddress().getDownloadedFileAddress(filename, category, id, getApplicationContext());
        Toast.makeText(getApplicationContext(),LocalAddress,Toast.LENGTH_LONG);
      //  Log.v("present locally",LocalAddress);
        if (LocalAddress == null)
            return false;
        else {
            PlayVideo(LocalAddress);
            return true;
        }

    }



}
