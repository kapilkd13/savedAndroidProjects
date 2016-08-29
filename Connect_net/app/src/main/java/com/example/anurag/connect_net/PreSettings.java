package com.example.anurag.connect_net;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by kapil on 18/6/16.
 */
public class PreSettings {

    private SharedPreferences sp;
    private SharedPreferences.Editor EPref;

    public PreSettings(Context context)
    { Log.e("entered pre setings","true");
        //setting sharedpreference
        sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_LOGIN_FILE, 0);
        EPref = sp.edit();
if(context==null)
    Log.v("found prob"," heer ir is");
        //initializing imp variables in internal memory
        if(!sp.contains("registeredUser"))
        EPref.putBoolean("registeredUser", false);
        if(!sp.contains("name"))
        EPref.putString("name", "");
        if(!sp.contains("email"))
        EPref.putString("email","");
        Log.e("llook at file location",context.getFilesDir().getAbsolutePath() +Constants.PATH_DELIMETER+Constants.TIMELINE);

        //creating flder in internal storage
        File internaldirTimeline = new File(context.getFilesDir().getAbsolutePath() +Constants.PATH_DELIMETER+Constants.TIMELINE);
        if (!internaldirTimeline.exists()) {
            Log.e(" created timelinee","true");
            internaldirTimeline.mkdir();
            if(internaldirTimeline.exists())
                Log.v("internaldirtimrlinr","it exists");
        }


        File internaldirPoll = new File(context.getFilesDir().getAbsolutePath() +Constants.PATH_DELIMETER+Constants.POLL);
        if (!internaldirPoll.exists()) {
            internaldirPoll.mkdir();
        }
        File internaldirStudy = new File(context.getFilesDir().getAbsolutePath() +Constants.PATH_DELIMETER+Constants.LEARN);
        if (!internaldirStudy.exists()) {
            internaldirStudy.mkdir();
        }
        File internaldirFAQ = new File(context.getFilesDir().getAbsolutePath() +Constants.PATH_DELIMETER+Constants.FAQ);
        if (!internaldirFAQ.exists()) {
            internaldirFAQ.getParentFile().mkdirs();
            internaldirFAQ.mkdir();
            if(internaldirFAQ.exists())
                Log.v("internaldir","it exists");
        }
      String a=  Environment.getExternalStorageState();
        Log.e("storage state is",a);
       // here create panchayat/pictures and videos folder if doesn't exist'
        if((Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))&&(!Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED))) {
            Log.e("sd card mounted","true");
            String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();

            File timeline = new File(SDCardRoot +Constants.PATH_DELIMETER+Constants.TIMELINE);
            // File dir=new File()
            if (!timeline.exists()) {
                timeline.getParentFile().mkdirs();
                timeline.mkdir();
                Log.e("sd card mounted", "true");
            }
            File poll = new File(SDCardRoot +Constants.PATH_DELIMETER+Constants.POLL);
            // File dir=new File()
            if (!poll.exists()) {
                poll.getParentFile().mkdirs();
                poll.mkdir();
            }
            File study = new File(SDCardRoot +Constants.PATH_DELIMETER+Constants.LEARN);
            // File dir=new File()
            if (!study.exists()) {
                study.getParentFile().mkdirs();
                study.mkdir();
            }
            File Faq = new File(SDCardRoot +Constants.PATH_DELIMETER+Constants.FAQ);
            // File dir=new File()
            if (!Faq.exists()) {
                Faq.getParentFile().mkdirs();
                Faq.mkdir();
            }
            Log.e("lpanchayat created","true");
            EPref.putBoolean("externalStorage",true);
        }
        else {
            EPref.putBoolean("externalStorage", false);
            Log.e("else created", "true");
        }
    }
}
