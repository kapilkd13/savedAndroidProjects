package com.example.anurag.connect_net;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Anurag on 27-05-2016.
 */
public class InternalStorage  {
   static   FileOutputStream FOS;
    static  FileInputStream FIS;

    public static   void write(Context context,String Filename,String data){
        byte[] b=data.getBytes();
        File f= new File(Filename);
        FOS=null;
        try {
            FOS=context.openFileOutput(Filename,Context.MODE_PRIVATE);
            FOS.write(b);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }finally {

        }


    }
    public static  String read(Context context,String Filename){
        FIS=null;
        String data="";

        try {
            FIS=context.openFileInput(Filename);
            byte[] dataArray= new byte[FIS.available()];
            data=FIS.toString();
            while (FIS.read(dataArray)!=-1){
                   data=new String(dataArray);
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }finally {

        }
        return  data;
    }
}
