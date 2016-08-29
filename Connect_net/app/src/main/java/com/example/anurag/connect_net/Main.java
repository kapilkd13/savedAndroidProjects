package com.example.anurag.connect_net;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main extends AppCompatActivity implements View.OnClickListener {
TextView msg,jsonCheck,timelineView;
    SharedPreferences sp;
    SharedPreferences.Editor EPref;
    Context  context;
    Button refresh,updateTimeline,showTimeline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        netChecker();
    }
    void init(){
        setContentView(R.layout.main);
        msg = (TextView) findViewById(R.id.connection);
        timelineView = (TextView) findViewById(R.id.timelineView);
        jsonCheck = (TextView) findViewById(R.id.jsonCheck);
        refresh=(Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(this);
        updateTimeline=(Button) findViewById(R.id.updateTimeline);
        updateTimeline.setOnClickListener(this);
        showTimeline=(Button) findViewById(R.id.showTimeline);
        showTimeline.setOnClickListener(this);

        sp = getSharedPreferences(Constants.SHARED_PREFERENCE_FILE, 0);
        EPref = sp.edit();

        EPref.putString(Constants.SHARED_PREFERENCE_CONNECTION_KEY, "no");
        EPref.commit();

        //for net connection use both may be in a loop
        context = this;
        new PhpRequest(sp, EPref ,1).execute("connection", "http://www.oopadai.com/connection.php");
        new PhpRequest(sp, EPref, jsonCheck,1).execute("jsoncheck", "http://www.insigniathefest.com/kapil/json.php");


    }

  void netChecker(){
      final Handler handler = new Handler();
      Runnable runnable = new Runnable() {
          int i=0;
          public void run() {
              String connection=sp.getString(Constants.SHARED_PREFERENCE_CONNECTION_KEY,"no");

              if(INTERNET.isInternetOn(context)){
                  if(connection.equals("yes")){
                      msg.setText("conneted");
                  }else {
                      // Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
                      msg.setText("connected but no internet access");
                  }
              }else{

                  msg.setText(" not connected");
              }

          }
      };
      handler.postDelayed(runnable, 1000); //for initial delay..
  }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refresh:
                netChecker();
                break;
            case R.id.updateTimeline:
                new PhpRequest(this,1).execute("timeline", "http://www.insigniathefest.com/kalam/get_latest_posts.php");
                break;
            case R.id.showTimeline:
                String data=new InternalStorage().read(this,Constants.INTERNAL_STORAGE_FAQ_FILE);
                timelineView.setText(data);
                break;
        }
    }
}



// new CheckInternet(context,sp,EPref,msg);
     /*Thread check= new Thread(){
         public void run() {
             while (true) {
                 try {
                     sleep(1000);
                     String connection = sp.getString(Constants.SHARED_PREFERENCE_CONNECTION_KEY, "no");

                     if (INTERNET.isInternetOn(context)) {
                         // if (connection .equals( "yes" ) ) {
                         if (connection.equals("yes")) {
                             //  Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
                             msg.setText("conneted");

                         } else {
                             // Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
                             msg.setText("connected but no internet access");
                         }
                     } else {

                         msg.setText(" not connected");
                     }


                     new PhpRequest(sp, EPref, 1).execute("connection", "http://www.oopadai.com/connection.php");

                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }

             }
         }

     };

        check.run();*/

      /*  final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i=0;
            public void run() {
                String connection=sp.getString(Constants.SHARED_PREFERENCE_CONNECTION_KEY,"no");

                if(INTERNET.isInternetOn(context)){
                    // if (connection .equals( "yes" ) ) {
                    if(connection.equals("yes")){
                        //  Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
                        msg.setText("conneted");

                    }else {
                        // Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
                        msg.setText("connected but no internet access");
                    }
                }else{

                    msg.setText(" not connected");
                }


                new PhpRequest(sp,EPref, 1).execute("connection","http://www.oopadai.com/connection.php");


                handler.postDelayed(this, 1000);  //for interval...
            }
        };
        handler.postDelayed(runnable, 1000); //for initial delay..



    }*/
