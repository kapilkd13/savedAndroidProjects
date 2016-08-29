package com.example.kapil.ivuploader;

/**
 * Created by kapil on 28/5/16.
 */
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();

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
    private Uri fileUri; // file url to store image/video

    private Button btnCapturePicture, btnRecordVideo, btnUploadImage, btnUploadVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Changing action bar background color
        // These two lines are not needed
      //  getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.color.action_bar))));

        btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
        btnRecordVideo = (Button) findViewById(R.id.btnRecordVideo);
        btnUploadImage = (Button) findViewById(R.id.btnUploadImage);
        btnUploadVideo = (Button) findViewById(R.id.btnUploadVideo);
        /**
         * Capture image button click event
         */

        //share pref
preferencesetting=getPreferences(0);//keep privte
        sharedPreferences=preferencesetting.edit();

        askPermissions();



        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                askPermissions();
                captureImage();
            }
        });

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

        btnUploadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // record video
                askPermissions();
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

                i.setType("image/*");

                startActivityForResult(i, RESULT_LOAD_IMAGE_REQUEST_CODE);
            }
        });

        btnUploadVideo.setOnClickListener(new View.OnClickListener() {

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
     * Launching camera app to capture image
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Launching camera app to record video
     */
    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
Log.d("file uri", fileUri.getPath());
        // set video quality
        //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }



    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // successfully captured the image
                // launching upload activity
                launchUploadActivity(true);


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            }


            else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // video successfully recorded
                // launching upload activity
               launchUploadActivity(false);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        else if (requestCode == RESULT_LOAD_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                fileUri = data.getData();
                String path=getRealPathFromURI_API19(this.getApplicationContext(),fileUri);

                Log.d("data",path);
             fileUri= Uri.fromFile(new File(path));
                launchUploadActivity(true);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled image uploading", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to upload image", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        else if (requestCode == RESULT_LOAD_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                fileUri = data.getData();
                String path=getRealPathFromURI_API19(this.getApplicationContext(),fileUri);

                Log.d("data",path);
                fileUri= Uri.fromFile(new File(path));

                launchUploadActivity(false);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video uploading", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to upload video", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }

    private void launchUploadActivity(boolean isImage){
        Intent i = new Intent(MainActivity.this, UploadActivity.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("isImage", isImage);
        startActivity(i);
    }

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */

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




    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}