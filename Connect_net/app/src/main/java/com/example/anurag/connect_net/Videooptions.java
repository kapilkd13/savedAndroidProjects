package com.example.anurag.connect_net;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.anurag.connect_net.writeToUs;

import java.io.File;
import java.util.ArrayList;


/**
 * created by kapil
 *
 *
 */
public class Videooptions extends AppCompatActivity {
    // LogCat tag
    private static final String TAG = writeToUs.class.getSimpleName();

    //add all permissions required at runtime for android 23 ND Above
    private static final  String[] perms= {"android.permission.READ_EXTERNAL_STORAGE","android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE","android.permission.RECORD_AUDIO"};
    private  static final   int permsRequestCode = 200;


    //for shared preferences to save permission whether grantd or not
    private SharedPreferences preferencesetting;
    private SharedPreferences.Editor sharedPreferences;
    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static ArrayList<String> neededPerms;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int RESULT_LOAD_IMAGE_REQUEST_CODE = 300;
    private static final int RESULT_LOAD_VIDEO_REQUEST_CODE = 400;
    private static final int RESULT_VIDEO_FOUND_OK = 1000;
    private static final int RESULT_VIDEO_PROBLEM = 1200;
    private Uri VideofileUri; // file url to store image/video
    private Button btnRecordVideo, btnChooseVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videooptions);
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


        btnRecordVideo = (Button) findViewById(R.id.videorecord);
        btnChooseVideo = (Button) findViewById(R.id.videoexist);

        preferencesetting=getPreferences(0);//keep privte
        sharedPreferences=preferencesetting.edit();

        askPermissions();


        /**
         * Record video button click event
         */
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // record video
                askPermissions();
                recordVideo();
            }
        });


        btnChooseVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                askPermissions();
                // record video
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

                i.setType("video/*");

                startActivityForResult(i, RESULT_LOAD_VIDEO_REQUEST_CODE);
            }
        });


        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            //instead of finishing return error with activity intent result showing no camera error
            finish();

        }

    }


    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Launching camera app to record video
     */
    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        VideofileUri = new MediaFileURI().getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
        Log.d("file uri", VideofileUri.getPath());
        // set video quality
        //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, VideofileUri); // set the image file
        // name

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
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

            return(checkSelfPermission(permission)==PackageManager.PERMISSION_GRANTED);

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

//parsing returned file path to correct it to actual path
    @TargetApi(19)
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        if (uri.getHost().contains("com.android.providers.media")) {
            // Image pick from recent
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        } else {
            // image pick from gallery
            return  getRealPathFromURI_API11to18(context, uri);
        }

    }



    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }



    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //to send result back to the write to us main activity
        Intent resultintent=new Intent();
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // video successfully recorded
                // launching upload activity
           //     launchUploadActivity(false);
                resultintent.putExtra("value", VideofileUri);
                setResult(RESULT_VIDEO_FOUND_OK, resultintent);
                finish();
            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();
                resultintent.putExtra("value", "User cancelled video recording");
                setResult(RESULT_VIDEO_PROBLEM, resultintent);
                finish();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
                resultintent.putExtra("value", "Sorry! Failed to record video");
                setResult(RESULT_VIDEO_PROBLEM, resultintent);
                finish();
            }



        }



        else if (requestCode == RESULT_LOAD_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                VideofileUri = data.getData();
                String path=getRealPathFromURI_API19(this.getApplicationContext(),VideofileUri);

                Log.d("data",path);
                VideofileUri= Uri.fromFile(new File(path));
                resultintent.putExtra("value", VideofileUri);
                setResult(RESULT_VIDEO_FOUND_OK, resultintent);
                finish();
              //  launchUploadActivity(false);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video uploading", Toast.LENGTH_SHORT)
                        .show();

                resultintent.putExtra("value", "User cancelled video uploading");
                setResult(RESULT_VIDEO_PROBLEM, resultintent);
                finish();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to upload video", Toast.LENGTH_SHORT)
                        .show();
                resultintent.putExtra("value", "Sorry! Failed to upload video");
                setResult(RESULT_VIDEO_PROBLEM, resultintent);
                finish();
            }
        }

    }


}
