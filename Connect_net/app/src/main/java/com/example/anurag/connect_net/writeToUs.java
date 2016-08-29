package com.example.anurag.connect_net;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//remaining tasks
//importing user id to pass as entity
//saving instance on rotation
//look after socket exception and all other exception and tell user to try again
//also json decodong server respobnse in this case may caise prob
/**
 * created by kapil
 *
 *
 */
public class writeToUs extends AppCompatActivity {

    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String POST_TYPE_REVIEW="review";
    private static final String POST_TYPE_COMPLAIN="complain";
    //add all permissions required at runtime for android 23 ND Above
    private static final  String[] perms= {"android.permission.READ_EXTERNAL_STORAGE","android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE","android.permission.RECORD_AUDIO"};
    private  static final   int permsRequestCode = 200;


    //for shared preferences to save permission whether grantd or not
    private SharedPreferences preferencesetting;
    private SharedPreferences.Editor sharedPreferences;
    // Camera activity request codes
    private static int uniqueID=0;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static ArrayList<String> neededPerms;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int RESULT_LOAD_IMAGE_REQUEST_CODE = 300;
    private static final int RESULT_LOAD_VIDEO_REQUEST_CODE = 400;
    private static final int VIDEO_OPTIONS_REQUEST_CODE = 800;
    private static final int IMAGE_OPTIONS_REQUEST_CODE = 900;
    private static final int RESULT_VIDEO_FOUND_OK = 1000;
    private static final int RESULT_VIDEO_PROBLEM = 1200;
    private static final int RESULT_IMAGE_FOUND_OK = 1300;
    private static final int RESULT_IMAGE_PROBLEM = 1400;
    private Uri VideofileUri=null,ImagefileUri=null; // file url to store image/video
    private Button btnUploadimg, btnUploadvideo,send;
private Loading l;
    private ImageView imgPreview;
    private VideoView vidPreview;
    public static boolean imageset=false,videoset=false,isReview=false;
    private TextView Description;
    private RadioButton complain;
    private RadioButton review;
    long totalSize = 0;
    private ProgressBar progressBar;
    Context context;
    private String filePath = null,previewfilepath=null;
    private static boolean uploadingFile=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_to_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        l = new Loading(writeToUs.this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        context  = writeToUs.this;
        btnUploadimg = (Button) findViewById(R.id.upimage);
        btnUploadvideo = (Button) findViewById(R.id.upvideo);
        send = (Button) findViewById(R.id.send);
        complain=(RadioButton)findViewById(R.id.complainrb);
        review=(RadioButton)findViewById(R.id.reviewrb);
        Description=(TextView)findViewById(R.id.description);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        vidPreview = (VideoView) findViewById(R.id.videoPreview);
        preferencesetting=getPreferences(0);//keep privte
        sharedPreferences=preferencesetting.edit();

        if(savedInstanceState!=null)
            restoreState(savedInstanceState);
        askPermissions();

        //on upload video button click
        btnUploadvideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // record video
                askPermissions();
                //  recordVideo();

                Intent i = new Intent(writeToUs.this, com.example.anurag.connect_net.Videooptions.class);
                startActivityForResult(i, VIDEO_OPTIONS_REQUEST_CODE);
            }
        });

        //on upload image button click
        btnUploadimg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // record video
                askPermissions();
                //  recordVideo();
                Intent i=new Intent(writeToUs.this, com.example.anurag.connect_net.Imageoptions.class);
                startActivityForResult(i, IMAGE_OPTIONS_REQUEST_CODE);
            }
        });

        //on send button click
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                markPostType();
              handlesend();

                // callNotification();
                //here first upload description by encoding it to json file in response you will receive a
                //unique writetous no . use this no to send image and file . on server we will automatically allign it to
                //correct no by renaming it for uniqueness and leaving its address to the database
            }
        });

    }


    public void restoreState( Bundle outState)
    {if(outState.getBoolean("complain"))

        complain.setEnabled(true);
    else
        review.setEnabled(true);

        if(outState.getString("description")!=null)
            Description.setText(outState.getString("description"));
        if(outState.getBoolean("imageset"))
        {imageset=true;
            callPreviewMedia(outState.getString("ImagefileUriPath"),true);
            ImagefileUri=outState.getParcelable("ImagefileUri");
        }
        if(outState.getBoolean("videoset"))
        {videoset=true;
            callPreviewMedia(outState.getString("VideofileUriPath"),false);
            VideofileUri=outState.getParcelable("VideofileUri");
        }



    }
/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

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

    private void markPostType()
    {

        if(review.isChecked())
            isReview=true;
        else
            isReview=false;
    }

    //permission functions for android version 6

    private boolean needRuntimePerm(){

        return(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1);

    }
    @TargetApi(23)
    private void askPermissions() {
        neededPerms=new ArrayList<>();
        if (needRuntimePerm())//save time
        {
            for (String perm : perms) {
                if (!hasPermission(perm) && (shouldWeAsk(perm))) {
                    neededPerms.add(perm);
                }
                if(!neededPerms.isEmpty())
                    requestPermissions(neededPerms.toArray(new String[neededPerms.size()]), permsRequestCode);

            }

        }
    }
    @TargetApi(23)
    private boolean hasPermission(String permission){

        if(needRuntimePerm()){

            return(checkSelfPermission(permission)== PackageManager.PERMISSION_GRANTED);

        }

        return true;

    }

    private boolean shouldWeAsk(String permission){

        return (preferencesetting.getBoolean(permission, true));

    }



    private void markAsAsked(String permission){

        sharedPreferences.putBoolean(permission, false);
        sharedPreferences.apply();

    }

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:
                for(int i=0;i<grantResults.length;i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                        sharedPreferences.putBoolean(permissions[i],false);

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // if the result is capturing Image
        if (requestCode == IMAGE_OPTIONS_REQUEST_CODE) {
            if (resultCode == RESULT_IMAGE_FOUND_OK) {
                ImagefileUri=data.getParcelableExtra("value");
                checkAndDisable();
                // successfully captured the image
                // launching upload activity
                //   launchUploadActivity(true);


            } else if (resultCode == RESULT_IMAGE_PROBLEM) {
                String errorDescription=data.getStringExtra("value");
                // user cancelled Image capture
             /*   Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
               */
                Toast.makeText(getApplicationContext(),
                        errorDescription, Toast.LENGTH_SHORT)
                        .show();
            }


            else {
                // String errorDescription=data.getStringExtra("value");
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "you cancelled image uploading", Toast.LENGTH_SHORT)
                        .show();
            }

        }

        else if (requestCode == VIDEO_OPTIONS_REQUEST_CODE) {
            if (resultCode == RESULT_VIDEO_FOUND_OK) {
                VideofileUri=data.getParcelableExtra("value");

                checkAndDisable();
            } else if (resultCode == RESULT_VIDEO_PROBLEM) {
                String errorDescription=data.getStringExtra("value");
                Toast.makeText(getApplicationContext(),
                        errorDescription, Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record video
                //  String errorDescription=data.getStringExtra("value");
                Toast.makeText(getApplicationContext(),
                        "you cancelled video uploading", Toast.LENGTH_SHORT)
                        .show();
            }
        }



    }

    private  void checkAndDisable()
    {
        if(ImagefileUri!=null)
        {
            File f=new File(ImagefileUri.getPath());
            if(!f.exists())
            {
                Toast.makeText(getApplicationContext(),
                        "image path is not valid, choose another image", Toast.LENGTH_SHORT)
                        .show();
            }
            else{
                imageset=true;
                callPreviewMedia(ImagefileUri.getPath(), true);

            }

        }
        if(VideofileUri!=null)
        {
            File f=new File(VideofileUri.getPath());
            if(!f.exists())
            {
                Toast.makeText(getApplicationContext(),
                        "video path is not valid, choose another video", Toast.LENGTH_SHORT)
                        .show();
            }
            else if(((f.length())/1024)>15360)
            {
                Toast.makeText(getApplicationContext(),
                        "can't upload file greater than 15mb", Toast.LENGTH_SHORT)
                        .show();
             //   tooLargeVideo=true;
                VideofileUri=null;
            }
            else{
                videoset=true;
                callPreviewMedia(VideofileUri.getPath(), false);

            }

        }

    }

    public void callPreviewMedia(String prefilepath,boolean isImage)
    {
        previewfilepath=prefilepath;
        previewMedia(isImage);
        if(isImage)
            btnUploadimg.setText("Change img");
        else
            btnUploadvideo.setText("change video");

    }

    public void afterUserRegCall()
    {
        Log.v("write to us","trying again handlesend");
        handlesend();
    }


    private  void handlesend()
    {//before sending data check for registered user.
       if(!INTERNET.isInternetOn(getApplicationContext()))
       {  Toast.makeText(getApplicationContext(),"No Internet",Toast.LENGTH_SHORT).show();
       return;}


    //for testing
        l.setText("Uploading Data...");
        l.startProgressCircle();
        // uniqueID=100;
        //   uploadImageAndVideo();
        String description=Description.getText().toString();
        //getJSONencodedFile(description); //enter whether it is complain or review
//start circle wheel showing uploading

        ArrayList<String> filelist=new ArrayList<>();
        filelist.add(description);
        filelist.add("text");
        if(isReview)
            filelist.add(POST_TYPE_REVIEW);
        else
            filelist.add(POST_TYPE_COMPLAIN);
        filelist.add(getuserid());
        if((description!=null)&&(!description.isEmpty())) {
            uploadingFile=false;
            new UploadFileToServer().execute(filelist);
        }
        else
        {  Toast.makeText(getApplicationContext(),
                "give some description", Toast.LENGTH_SHORT)
                .show();
l.stopProgressCircle();
        }
    }


    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<ArrayList<String>, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            //      progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            //   progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            //  progressBar.setProgress(progress[0]);

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
            HttpPost httppost = new HttpPost(com.example.anurag.connect_net.Config.FILE_UPLOAD_URL);

            try {
                com.example.anurag.connect_net.AndroidMultiPartEntity entity = new com.example.anurag.connect_net.AndroidMultiPartEntity(
                        new com.example.anurag.connect_net.AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                if(totalSize==0)
                                    publishProgress( 0);
                                else
                                    publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                if(uploadingFile) {
                    filePath = param.get(0);
                    File sourceFile = new File(filePath);
                    entity.addPart("file", new FileBody(sourceFile));
                }
                else
                {String description=param.get(0);
                    entity.addPart("description", new StringBody(description));
                }


                entity.addPart("filetype", new StringBody(param.get(1)));
                // Adding file data to http body
                entity.addPart("posttype", new StringBody(param.get(2)));
//here you have to import id
                entity.addPart("userid", new StringBody(param.get(3)));

                // Extra parameters if you want to pass to server
                entity.addPart("website",
                        new StringBody("www.insigniathefest.com"));
                entity.addPart("email", new StringBody("kapilkd13@gmail.com"));




                if(!(param.get(1).equals("text")))
                {if(uniqueID!=0)
                    entity.addPart("uniqueid", new StringBody(param.get(4)));
                else
                    return("unique id is not set");
                }

                totalSize = entity.getContentLength();
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
            }
            catch (SocketTimeoutException e)
            {
                Log.v("socket is timed  out", "whaat next");
                responseString = e.toString();
               Toast.makeText(getApplicationContext(),"Please try again!!",Toast.LENGTH_LONG);
            }
            catch (IOException e) {
                responseString = e.toString();
            }


            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

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
//upload image and video here
            if(!uploadingFile) {
                uploadingFile=true;
                uploadImageAndVideo();
            }
            if(l!=null)
                l.stopProgressCircle();
            showAlert(result);



            super.onPostExecute(result);
        }



    }
    private void showAlert(String message) {

        if(!((Activity) context).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message).setTitle("Response from Servers")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do nothing
                        }
                    });
            AlertDialog alert = builder.create();


            alert.show();

        }
    }
    private void uploadImageAndVideo()
    {

        if((imageset)&&(uniqueID!=0))
        {ArrayList<String> imglist=new ArrayList<>();
            imglist.add(ImagefileUri.getPath());
            imglist.add("image");
            if(isReview)
                imglist.add(POST_TYPE_REVIEW);
            else
                imglist.add(POST_TYPE_COMPLAIN);
            imglist.add(getuserid());
            imglist.add(Integer.toString(uniqueID));
            //  new UploadFileToServer().execute(imglist);
            Intent i=new Intent(writeToUs.this, com.example.anurag.connect_net.UploadImageInService.class);
            i.putStringArrayListExtra("data",imglist);
            startService(i);

        }
        if(videoset)
        { ArrayList<String> videolist=new ArrayList<>();
            videolist.add(VideofileUri.getPath());
            videolist.add("video");
            if(isReview)
                videolist.add(POST_TYPE_REVIEW);
            else
                videolist.add(POST_TYPE_COMPLAIN);
            videolist.add(getuserid());
            videolist.add(Integer.toString(uniqueID));
            //do this in service
            //     new UploadFileToServer().execute(videolist);
            Intent i=new Intent(writeToUs.this, com.example.anurag.connect_net.UploadVideoInService.class);
            i.putStringArrayListExtra("data",videolist);
            startService(i);
        }


    }
    public String getuserid()
    {return new UserInfo(getApplicationContext()).getusermail();}


    /**
     * Displaying captured image/video on the screen
     * */
    private void previewMedia(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {
            imgPreview.setVisibility(View.VISIBLE);
            // vidPreview.setVisibility(View.GONE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            Bitmap bitmap = BitmapFactory.decodeFile(previewfilepath, options);
            try {
                ExifInterface exif = new ExifInterface(previewfilepath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                }
                else if (orientation == 3) {
                    matrix.postRotate(180);
                }
                else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); // rotating bitmap
            }
            catch (Exception e) {

            }
            //    ImageView img = (ImageView) findViewById(R.id.imgTakingPic);
            imgPreview.setImageBitmap(bitmap);
            // imgPreview.setImageBitmap(bitmap);
        } else {
            //  imgPreview.setVisibility(View.GONE);
            vidPreview.setVisibility(View.VISIBLE);
            //   vidPreview.setVideoPath(filePath);

            //  vidPreview.start();
            vidPreview.post(new Runnable() {
                @Override
                public void run() {
                    vidPreview.setVideoURI(Uri.parse("/storage/sdcard0/panchayat/Timeline/29/vid1.mp4"));
                    vidPreview.setMediaController(new MediaController(writeToUs.this));
                    vidPreview.requestFocus();
                    vidPreview.start();
                }
            });

            // start playing

        }
    }



    public void onSaveInstanceState(Bundle outState) {
        Log.d("I am called: ", "timeline onsavedInstance");
        super.onSaveInstanceState(outState);

        //   outState.putIntegerArrayList("kk", kk);
        //    outState.putChar("id", 'i');
        outState.putString("description", Description.getText().toString());
        outState.putBoolean("complain", review.isChecked() ? false : true);
        outState.putBoolean("imageset",imageset?true:false);
        outState.putBoolean("videoset", videoset ? true : false);
        if(VideofileUri!=null) {
            outState.putString("VideofileUriPath", VideofileUri.getPath());
            outState.putParcelable("VideofileUri", VideofileUri);
        }
        if(ImagefileUri!=null) {
            outState.putString("ImagefileUriPath", ImagefileUri.getPath());
            outState.putParcelable("ImagefileUri", ImagefileUri);
        }

    }
    private void callNotification()
    {final int num=0;
        Log.v("notidifcation","i m called");
        final   NotificationManager mNotifyManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder       mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Picture Download")
                .setContentText("Download in progress").setSmallIcon(R.mipmap.ic_launcher);
        new Thread(new Runnable() {
            @Override
            public void run() {int num=0;
                for(num=0;num<=100;num+=5) {
                    mBuilder.setProgress(100, num, false);
                    Log.v("notidifcation", "i m called");
                    mNotifyManager.notify(1, mBuilder.build());
                    try {
                        Thread.sleep(1000);

                    }
                    catch (Exception e)
                    {e.printStackTrace();}

                }
            }
        }).start();
        // updating progress bar value
        //  progressBar.setProgress(progress[0]);
        //   if(num==100)
        mBuilder.setContentText("Download complete")
                // Removes the progress bar
                .setProgress(0,0,false);
        //  mNotifyManager.notify(1, mBuilder.build());
    }

}



