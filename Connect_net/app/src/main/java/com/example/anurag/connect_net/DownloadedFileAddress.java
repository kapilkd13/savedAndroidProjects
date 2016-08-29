package com.example.anurag.connect_net;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by kapil on 18/6/16.
 */
public class DownloadedFileAddress {
    private String filename, filetype, file_null_address;
private Context context;
    public DownloadedFileAddress(String filename, String filetype,Context context) {
        this.filename = filename;
this.context=context;
        this.filetype = filetype;

    }

    public  DownloadedFileAddress()
    {}


    public boolean checkFilePresence(String filename, String Category,Context context)
    {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            Log.v("sd when downloaded card",Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PATH_DELIMETER + Category  + Constants.PATH_DELIMETER + filename);
            Log.v("sd card","this file is present in sd card");
            if(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PATH_DELIMETER + Category  + Constants.PATH_DELIMETER + filename).exists())
                return true;

        }
        if(new File(context.getFilesDir().getAbsolutePath() + Constants.PATH_DELIMETER + Category  + Constants.PATH_DELIMETER + filename).exists())
        { Log.v("getdownloaded add",context.getFilesDir().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + filename);
            return true;
        }
        return  false;
    }


    public boolean checkFilePresence(String filename, String Category,int id,Context context)
    {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            Log.v("sd when downloaded card",Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + id + Constants.PATH_DELIMETER + filename);
            Log.v("sd card","this file is present in sd card");
            if(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + id + Constants.PATH_DELIMETER + filename).exists())
                return true;

        }
        if(new File(context.getFilesDir().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + id + Constants.PATH_DELIMETER + filename).exists())
        { Log.v("getdownloaded add",context.getFilesDir().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + id + Constants.PATH_DELIMETER + filename);
            return true;
        }
        return  false;
    }

    public String getDownloadedFileAddress(String filename, String Category,int id,Context context) {
        Log.v("sd card","this file is present in sd card");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            Log.v("sd when downloaded card",Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + id + Constants.PATH_DELIMETER + filename);
            Log.v("sd card","this file is present in sd card");
                if(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + id + Constants.PATH_DELIMETER + filename).exists())
                { Log.v("sd card","this file is present in sd card");
                    return (Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + id + Constants.PATH_DELIMETER + filename);
                }

            }


        Log.v("getdownloaded add",context.getFilesDir().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + id + Constants.PATH_DELIMETER + filename);


        if(new File(context.getFilesDir().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + id + Constants.PATH_DELIMETER + filename).exists())
                { Log.v("getdownloaded add",context.getFilesDir().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + id + Constants.PATH_DELIMETER + filename);
                    return (context.getFilesDir().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + id + Constants.PATH_DELIMETER + filename);

                }



        return file_null_address;
    }


    //for files shared among all folder no and directly present in category folder like LearnIndex.json

    public String getDownloadedFileAddress(String filename, String Category,Context context) {
        Log.v("sd card","this file is present in sd card");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            Log.v("sd when downloaded card",Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PATH_DELIMETER + Category  + Constants.PATH_DELIMETER + filename);
            Log.v("sd card","this file is present in sd card");
            if(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PATH_DELIMETER + Category +  Constants.PATH_DELIMETER + filename).exists())
            { Log.v("sd card","this file is present in sd card");
                return (Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PATH_DELIMETER + Category  + Constants.PATH_DELIMETER + filename);
            }

        }


        Log.v("getdownloaded add",context.getFilesDir().getAbsolutePath() + Constants.PATH_DELIMETER + Category  + Constants.PATH_DELIMETER + filename);


        if(new File(context.getFilesDir().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + filename).exists())
        { Log.v("getdownloaded add",context.getFilesDir().getAbsolutePath() + Constants.PATH_DELIMETER + Category  + Constants.PATH_DELIMETER + filename);
            return (context.getFilesDir().getAbsolutePath() + Constants.PATH_DELIMETER + Category  + Constants.PATH_DELIMETER + filename);

        }



        return file_null_address;
    }

//accept filetype img or video and
    public String getNewFileAddress(String filename, String Category,int id,Context context) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            Log.v("storage location",Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + id + Constants.PATH_DELIMETER + filename);

            Log.v("primary storage",context.getFilesDir().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + id + Constants.PATH_DELIMETER + filename);

            return (Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + id + Constants.PATH_DELIMETER + filename);


        }
        else {

            Log.v("primary storage",context.getFilesDir().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + id + Constants.PATH_DELIMETER + filename);

            return (context.getFilesDir().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + id + Constants.PATH_DELIMETER + filename);


        }

    }

    //for creating outer files
    public String getNewFileAddress(String filename, String Category,Context context) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            Log.v("storage location",Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PATH_DELIMETER + Category + Constants.PATH_DELIMETER + filename);

            Log.v("primary storage",context.getFilesDir().getAbsolutePath() + Constants.PATH_DELIMETER + Category  + Constants.PATH_DELIMETER + filename);

            return (Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PATH_DELIMETER + Category  + Constants.PATH_DELIMETER + filename);


        }
        else {

            Log.v("primary storage",context.getFilesDir().getAbsolutePath() + Constants.PATH_DELIMETER + Category  + Constants.PATH_DELIMETER + filename);

            return (context.getFilesDir().getAbsolutePath() + Constants.PATH_DELIMETER + Category  + Constants.PATH_DELIMETER + filename);


        }

    }


    public String makeWebURL(int id,String filename,String url)
    {
        return (url+Constants.PATH_DELIMETER+id+Constants.PATH_DELIMETER+filename);
    }
}
