package com.example.kapil.ivdownloader;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main2Activity extends AppCompatActivity {
    private ImageView iv;
    private  String filepath;
    private  static boolean downloadVideo=true;
    private static boolean downloadImage=false;
    private  VideoView myVideoView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        iv=(ImageView)findViewById(R.id.iv);
        myVideoView = (VideoView) findViewById(R.id.myvideoview);
        new Thread(new Runnable() {
            @Override
            public void run() {
                 filepath=getFilePath();


if(downloadImage) {

    iv.post(new Runnable() {
        @Override
        public void run() {
            if ((new File(filepath)).exists())
                iv.setImageBitmap(BitmapFactory.decodeFile((new File(filepath)).getAbsolutePath()));
        }
    });
}
                else if(downloadVideo) {
    myVideoView.post(new Runnable() {
        @Override
        public void run() {

            myVideoView.setVideoURI(Uri.parse(filepath));
            myVideoView.setMediaController(new MediaController(Main2Activity.this));
            myVideoView.requestFocus();
            myVideoView.start();
        }
    });
}
                    }
          //  Log.i("Local filename:", "" + filename);

        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }


    public String getFilePath()
    {String filepath="no file path ";
        try
        {
            URL url = new URL("http://download.wavetlan.com/SVV/Media/HTTP/H264/Talkinghead_Media/H264_test1_Talkinghead_mp4_480x360.mp4");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(false);
            urlConnection.connect();
            File SDCardRoot=new File("as");
            if(Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)) {
                Log.v("no error", "justv checking");
                Toast.makeText(getApplicationContext(),"very bad  ",Toast.LENGTH_LONG).show();
                 SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
            }
            else
            SDCardRoot=new File(getFilesDir().getAbsolutePath());
            String filename=null;
            if(downloadImage)
            filename="downloadedFile.png";
            else if(downloadVideo)
                filename="downloadedFile.mp4";
            Log.i("Local filename:", "" + filename);
            Log.i("Local filename:", "" + SDCardRoot);
            File dir = new File(SDCardRoot+"/panchayat");
           // File dir=new File()
            if(!dir.exists())
            {
                dir.mkdir();
            }

            File file = new File(dir,filename);
            // File dir=new File()
            if(file.createNewFile())
            {
                file.createNewFile();
            }
            int status = urlConnection.getResponseCode();
            Log.i("Local filename:", "" + status+"this is status");
            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ( (bufferLength = inputStream.read(buffer)) > 0 )
            {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                Log.i("Progress:","downloadedSize:"+downloadedSize+"totalSize:"+ totalSize) ;
            }
            fileOutput.close();
            if(downloadedSize==totalSize)
                filepath=file.getPath();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch(FileNotFoundException e)
        {e.printStackTrace();}
        catch (IOException e)
        {
            filepath=null;
            e.printStackTrace();
        }
        Log.i("filepath:"," "+filepath) ;
        return filepath;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
