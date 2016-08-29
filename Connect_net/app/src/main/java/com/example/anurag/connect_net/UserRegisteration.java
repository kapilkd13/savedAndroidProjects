package com.example.anurag.connect_net;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

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
 * Created by kapil on 17/6/16.
 */
public class UserRegisteration  extends AsyncTask<Void, Integer, String> {
  private boolean invalidentry=false;
    private static final String TAG = MainActivity.class.getSimpleName();
 private long totalSize=0;
private Context context;
    private SharedPreferences sp;
    private SharedPreferences.Editor EPref;
    private String name,email,loginvia;
    private Activity activity;
    private android.support.v4.app.Fragment fragment;
    private int fragmentID,activityID;
    private boolean calledinFrag=false;

//call from fragment
    public UserRegisteration(android.support.v4.app.Fragment fragment,int fragmentID)
    {this.fragment=fragment;
        this.fragmentID=fragmentID;
    calledinFrag=true;}
//call from activity
    public UserRegisteration(Activity activity,int activityID)
    {this.activity=activity;
    this.activityID=activityID;}

    @Override
    protected void onPreExecute() {

if(calledinFrag)
{
  sp=  fragment.getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE_LOGIN_FILE, 0);
    EPref = sp.edit();

}
else {
    sp = activity.getSharedPreferences(Constants.SHARED_PREFERENCE_LOGIN_FILE, 0);
    EPref = sp.edit();

     }


 /*     l.setText("registering user...");
        l.startProgressCircle();*/
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
    protected String doInBackground(Void... params) {
        return uploadFile();
    }



    @SuppressWarnings("deprecation")
    private String uploadFile() {

        String responseString = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(com.example.anurag.connect_net.Config.USER_REGISTERATION_URL);

        try {
            com.example.anurag.connect_net.AndroidMultiPartEntity entity = new com.example.anurag.connect_net.AndroidMultiPartEntity(
                    new com.example.anurag.connect_net.AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {

                        }
                    });







            // Adding file data to http body
//here you have to import id
            email=sp.getString("email","");
            name=sp.getString("name","");
            loginvia=sp.getString("loginvia","");
            if((name.equals(""))||(email.equals("")))
            {invalidentry=true;
                return "invalid entry";
            }



                entity.addPart("username", new StringBody(name));
                entity.addPart("userid", new StringBody(email));
                entity.addPart("appsecret", new StringBody(Constants.APP_SECRET));
            entity.addPart("loginvia",new StringBody(loginvia));
                // Extra parameters if you want to pass to server


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
        } catch (IOException e) {
            responseString = e.toString();
        }

        return responseString;

    }

    @Override
    protected void onPostExecute(String result) {
        Log.e(TAG, "Response from server: " + result);
        String error="true";
        // showing the server response in an alert dialog

        try {


            JSONObject jb = new JSONObject(result);

             error=jb.getString("error");
            String message=jb.getString("message");
            Log.e(TAG, "json unparsed: " + error+message);

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        if(error=="false")
        {EPref.putBoolean("registeredUser",true);
            EPref.commit();

Log.v("registered the user","done");
        }

if(calledinFrag)
{
    if(fragmentID==1)
    {((Timeline)fragment).afterUserRegCall();}

    else if(fragmentID==2)
    {((Learning)fragment).afterUserRegCall();}

    else  if(fragmentID==3)
    {((Polls)fragment).afterUserRegCall();}

    else if(fragmentID==4)
    {((Faqs)fragment).afterUserRegCall();}
    else if(fragmentID==5)
    {((LoginFragment)fragment).afterUserRegCall();}
}
else
{ if(activityID==1)
{((Login)activity).afterUserRegCall();}

else if(activityID==2)
{((writeToUs)activity).afterUserRegCall();}



}



        super.onPostExecute(result);
    }



}