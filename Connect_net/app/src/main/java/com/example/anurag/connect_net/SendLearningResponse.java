package com.example.anurag.connect_net;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * Created by kapil on 6/7/16.
 */
public class SendLearningResponse extends AsyncTask<ArrayList<String>, Integer, String> {
    private Context context;
    private Fragment fragment;
    private int id=2;//default
    private  boolean StatusOK=false;
    public SendLearningResponse(Fragment f)
    {this.context=context;
        this.fragment=f;}
    @Override
    protected void onPreExecute() {
        // setting progress bar to zero
        //      progressBar.setProgress(0);
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        //do nothing
    }

    @Override
    protected String doInBackground(ArrayList<String>... params) {
        return uploadFile(params[0]);
    }

    @SuppressWarnings("deprecation")
    private String uploadFile(ArrayList<String> param) {

        String responseString = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Constants.LEARNING_USER_RESPONSE_URL);
        try {
            com.example.anurag.connect_net.AndroidMultiPartEntity entity = new com.example.anurag.connect_net.AndroidMultiPartEntity(
                    new com.example.anurag.connect_net.AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
                        }
                    });




            // Adding file data to http body
//here you have to import id
            entity.addPart("userid", new StringBody(param.get(0)));
            entity.addPart("response", new StringBody(param.get(1)));
            Log.v("response", param.get(1));
            httppost.setEntity(entity);

            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();



//receiving response
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                StatusOK=true;
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
            Toast.makeText(context, "Please try again!!", Toast.LENGTH_LONG);
        }
        catch (IOException e) {
            responseString = e.toString();
        }


        return responseString;

    }

    @Override
    protected void onPostExecute(String result) {
        Log.e("on postExecute", "Response from server: " + result);


//here copy response to file
            if((StatusOK)&& ((LearningLectureFragment)fragment!=null)) {

                ((LearningLectureFragment)fragment).afterResponseSentCallback(true);
            }
            else
                ((LearningLectureFragment)fragment).afterResponseSentCallback(false);

        super.onPostExecute(result);
    }



}