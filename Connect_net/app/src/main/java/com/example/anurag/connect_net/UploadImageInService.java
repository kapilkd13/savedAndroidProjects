package com.example.anurag.connect_net;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kapil on 12/6/16.
 */
public class UploadImageInService extends Service {

    private static final String TAG = writeToUs.class.getSimpleName();
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private static int uniqueID=0,notificationID=0;
    long totalSize = 0;
    private ProgressBar progressBar;
public ArrayList<String> imagelist;
    private String filePath = null,previewfilepath=null;
    private static boolean uploadingFile=false;




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
     imagelist= intent.getStringArrayListExtra("data");
        new UploadFileToServer().execute(imagelist);
        Toast.makeText(this, "service started", Toast.LENGTH_SHORT).show();
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

    private class UploadFileToServer extends AsyncTask<ArrayList<String>, Integer, String> {


        private int count=0;
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            //      progressBar.setProgress(0);

            notificationID++;

            mNotifyManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(UploadImageInService.this);
            mBuilder.setContentTitle("Picture Upload")
                    .setContentText("upload in progress").setSmallIcon(R.mipmap.ic_launcher);

// Start a lengthy operation in a background thread




            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible

            // updating percentage value
            //    txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }
        @Override
        protected String doInBackground(ArrayList<String>... params) {
            return uploadFile(params[0]);
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(ArrayList<String> param) {

            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                Log.v("num progress", Long.toString(num));

                                int progress = (int) ((num / (float) totalSize) * 100);
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
                                        mBuilder.setContentText("upload complete")
                                                // Removes the progress bar
                                                .setProgress(0, 0, false);
                                        mNotifyManager.notify(notificationID, mBuilder.build());
                                    }
                                }
                            }
                        });

                    filePath = param.get(0);
                    File sourceFile = new File(filePath);
                    entity.addPart("file", new FileBody(sourceFile));




                // Adding file data to http body
//here you have to import id
                entity.addPart("userid",new StringBody(param.get(3)));
                entity.addPart("posttype", new StringBody(param.get(2)));
                // Extra parameters if you want to pass to server
                entity.addPart("website",
                        new StringBody("www.insigniathefest.com"));
                entity.addPart("email", new StringBody("kapilkd13@gmail.com"));


                entity.addPart("filetype", new StringBody(param.get(1)));


                    entity.addPart("uniqueid", new StringBody(param.get(4)));



                totalSize = entity.getContentLength();
                Log.v("totalsize", Long.toString(totalSize));
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();


//here set unique id to response from server

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response

                    responseString = EntityUtils.toString(r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from image service server: " + result);

            // showing the server response in an alert dialog
            try {


                JSONObject jb = new JSONObject(result);

                String error=jb.getString("error");
                String message=jb.getString("message");
                Log.e(TAG, "json unparsed: " + error+message);
                if(jb.has("uniqueid")) {
                    String uniqueid = jb.getString("uniqueid");
                    uniqueID=Integer.parseInt(uniqueid);
                    Log.e(TAG, "Response from server: " + uniqueid);
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }

stopSelf();

            super.onPostExecute(result);
        }



    }
}
