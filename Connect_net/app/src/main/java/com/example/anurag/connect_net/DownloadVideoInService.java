package com.example.anurag.connect_net;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by kapil on 20/6/16.
 */
public class DownloadVideoInService extends Service {

    private static final String TAG = DownloadVideoInService.class.getSimpleName();

    private static int uniqueID=0,notificationID=0;
    long totalSize = 0;
    private ProgressBar progressBar;
    public ArrayList<String> videolist;
    private String filePath = null,previewfilepath=null;
    private static boolean uploadingFile=false;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        videolist= intent.getStringArrayListExtra("data");

        Toast.makeText(this, "service started", Toast.LENGTH_SHORT).show();
        new DownloadFileFromServer().execute(videolist);
        return START_NOT_STICKY;
        //    return super.onStartCommand(intent, flags, startId);

    }


    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }
    public void selfdestruction()
    {stopSelf();}

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }


    private class DownloadFileFromServer extends AsyncTask<ArrayList<String>, Integer, String> {
        NotificationManager mNotifyManager;
        NotificationCompat.Builder mBuilder;
        private int count = 0, status;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            //      progressBar.setProgress(0);
            Log.v("on preexecte", "online");
            notificationID++;

            mNotifyManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(DownloadVideoInService.this);
            mBuilder.setContentTitle("Video Downloaded")
                    .setContentText("Download in progress").setSmallIcon(R.mipmap.ic_launcher);

// Start a lengthy operation in a background thread


            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected String doInBackground(ArrayList<String>... params) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                return  downloadManagerfile(params[0]);     }
         else  return DownloadFile(params[0]);

        }

        private String downloadManagerfile(ArrayList<String> param) {
            //add category oncee all videos on server arranged by ...upload/category/postid/audio or video name
            String Outer_video_path=param.get(0);
            String Category;
            String Post_unique_id = param.get(1);
            String video_name = param.get(2);
            String storage_path = param.get(3);
            Log.v("path supplied to ",storage_path);
            File storage_file=new File(storage_path);

                try {
                /*    //creating download folder and file
                    if(!storage_file.exists())
                    {
                        storage_file.getParentFile().mkdirs();
                        storage_file.createNewFile();
                    }
*/
                    //creating web url of video
                    URL url = new URL(new DownloadedFileAddress().makeWebURL(Integer.parseInt(Post_unique_id), video_name, Outer_video_path));
Log.v("here","now");
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url.toString()));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
request.setDestinationUri(Uri.fromFile(storage_file));
                 //   request.setDestinationInExternalFilesDir(getApplicationContext(), new File(storage_path).getParent(), new File(storage_path).getName());
                    DownloadManager manager = (DownloadManager) getSystemService(getApplicationContext().DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }



        return "testing";
    }

        @SuppressWarnings("deprecation")
        private String DownloadFile(ArrayList<String> param) {
            Log.v("on download file","online");
           String Outer_video_path=param.get(0);
            //add category oncee all videos on server arranged by ...upload/category/postid/audio or video name
            String Category;
            String Post_unique_id=param.get(1);
            String video_name=param.get(2);
            String storage_path=param.get(3);
          try{

              Log.v("on download file",storage_path);

         //URL url = new URL(Outer_video_path+Constants.PATH_DELIMETER+Post_unique_id+Constants.PATH_DELIMETER+video_name);
              URL url = new URL(new DownloadedFileAddress().makeWebURL(Integer.parseInt(Post_unique_id), video_name, Outer_video_path));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
              urlConnection.setReadTimeout(10000);
              urlConnection.setConnectTimeout(150000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            File storage_file=new File(storage_path);
            if(!storage_file.exists())
            {
                storage_file.getParentFile().mkdirs();
                storage_file.createNewFile();
            }
             status = urlConnection.getResponseCode();
            Log.i("Local filename:", "" + status+"this is status");
            FileOutputStream fileOutput = new FileOutputStream(storage_file);
            InputStream inputStream = urlConnection.getInputStream();
            long totalSize = urlConnection.getContentLength();
            long downloadedSize = 0;
            byte[] buffer = new byte[2048];
            int bufferLength = 0;
            while ( (bufferLength = inputStream.read(buffer)) != -1 )
            {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                Log.i("Progress:","downloadedSize:"+downloadedSize+"totalSize:"+ totalSize) ;
                int progress = (int) ((downloadedSize / (float) totalSize) * 100);
                //   if(totalSize==0)
                //       publishProgress( 0);
                //   else
                //     publishProgress((int) ((num / (float) totalSize) * 100));
                if ((progress>count) || (progress== 100)) ;
                {count=progress;

                    mBuilder.setProgress(100, progress, false);
                    mBuilder.setContentText(progress + "%");
                    mNotifyManager.notify(notificationID, mBuilder.build());
                    // updating progress bar value
                    //  progressBar.setProgress(progress[0]);
                    if (progress == 100) {
                        mBuilder.setContentText("Download complete")
                                // Removes the progress bar
                                .setProgress(0, 0, false);
                        mNotifyManager.notify(notificationID, mBuilder.build());
                    }
                }
            }
              inputStream.close();
            fileOutput.close();
            if(downloadedSize==totalSize)
                filePath=storage_file.getPath();
        }
          catch (SocketTimeoutException e)
          {Log.v("socket is timed  out","whaat next");
          e.printStackTrace();}
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch(FileNotFoundException e)
        {e.printStackTrace();}
        catch (IOException e)
        {
            filePath=null;
            e.printStackTrace();
        }
        Log.i("filepath:"," "+filePath) ;



          return "success status "+status;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from video service server: " + result);

            // showing the server response in an alert dialog


            stopSelf();

            super.onPostExecute(result);
        }



    }
}
