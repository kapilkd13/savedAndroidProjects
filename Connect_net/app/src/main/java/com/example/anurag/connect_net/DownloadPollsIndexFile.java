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
 * Created by kapil on 7/7/16.
 */
public class DownloadPollsIndexFile extends AsyncTask<ArrayList<String>, Integer, String> {
    private Context context;
    private Fragment fragment;

    private  boolean StatusOK=false;
    public DownloadPollsIndexFile(Context context, Fragment f)
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
        HttpPost httppost = new HttpPost(Constants.POLL_INDEX_FILE_URL);

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


            httppost.setEntity(entity);

            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();


//here set unique id to response from server

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
        Log.e("on postExecute", "Response from poll index server: " + result);

        // showing the server response in an alert dialog
        try {
//here copy response to file
            if(StatusOK) {
                if (!(new DownloadedFileAddress().checkFilePresence("PollsIndex.json", Constants.POLL, context))) {//create new file
                    File file = new File(new DownloadedFileAddress().getNewFileAddress("PollsIndex.json", Constants.POLL, context));
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    }
                }


                File file = new File(new DownloadedFileAddress().getDownloadedFileAddress("PollsIndex.json", Constants.POLL, context));
                if (file.exists()) {
                    FileWriter writer = new FileWriter(file);
                    writer.write(result);
                    writer.close();
                } else {
                    Log.v("problem", "in retrieving file address");
                }
                ((Polls)fragment).afterphpCallfromIndex();
            }
            else {
                Log.v("problem", "in retrieving file address");
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
//upload image and video here





        super.onPostExecute(result);
    }



}