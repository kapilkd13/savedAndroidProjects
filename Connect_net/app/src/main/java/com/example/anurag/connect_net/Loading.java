package com.example.anurag.connect_net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

/**
 * Created by Anurag on 02-06-2016.
 */
public class Loading {
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    Context context;
private String message;
    public Loading(Context context){
        this.context = context;
    }
public void setText(String message)
{this.message=message;}
    public void startProgressCircle(){
        progressBar = new ProgressDialog(context);
        progressBar.setCancelable(true);
        if(message!=null)
            progressBar.setMessage(message);
        else
        progressBar.setMessage("Loading data...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);

        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;

        new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus == 0 ) {

                    try {
                        Thread.sleep(1000);
                    }

                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progressBarbHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });
                }

                if (progressBarStatus == 1) {
                    /*try {
                        Thread.sleep(2000);
                    }

                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                    progressBar.dismiss();
                }
            }
        }).start();
    }
    public void stopProgressCircle(){
        progressBarStatus = 1;
    }
}

