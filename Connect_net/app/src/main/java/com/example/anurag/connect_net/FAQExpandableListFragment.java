package com.example.anurag.connect_net;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kapil on 3/7/16.
 */
public class FAQExpandableListFragment  extends Fragment {
public  static boolean isLatestIndexFile=false;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String,List<genericList<String,Integer>>> listDataChild;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.faq_expandable_main, container,false);
        ///  init();
        Log.v("initialize", "ho gya h");
        //  setContentView(R.layout.expandable_main);

        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        startCreatingIndexFile();
        // preparing list data
        //  prepareListData();

      /*  listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild,this);

        // setting list adapter
        expListView.setAdapter(listAdapter);*/
        return  view;
    }

    private void startCreatingIndexFile()
    {
        if((INTERNET.isInternetOn(getContext()))&&(!isLatestIndexFile))
        {
            ArrayList<String> userDetails=new ArrayList();
            userDetails.add(new UserInfo(getContext()).getusermail());
            (new DownloadFAQIndexFile(getContext(), this)).execute(userDetails);
            //readIndexFile();
        }
        else if(isIndexFilePresent())
        {Log.v("file","present");
            //open file from folder
            readIndexFile();
            listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild,this);

            expListView.setAdapter(listAdapter);
        }

        else
        {
            Toast.makeText(getContext(),"Please check Internet connection and try again",Toast.LENGTH_LONG);
            //make toast to check your intenet connection and try again and turn loading circle off
        }

    }

    //call back func by DownloadLearnIndexFile async task
    public void afterphpCall()
    {
        //  l.stopProgressCircle();
        if(isIndexFilePresent())
            readIndexFile();
        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild,this);

        expListView.setAdapter(listAdapter);
    }

    //reading Learning Index.json file
    private void readIndexFile()
    {File file=new File(new DownloadedFileAddress().getDownloadedFileAddress("FAQIndex.json", Constants.FAQ, getContext()));
        try {
            FileInputStream filereader=new FileInputStream(file);
            byte[] buffer=new byte[filereader.available()];
            filereader.read(buffer);
            filereader.close();
            String jsonString=new String(buffer,"UTF-8");
            if(jsonString!=null)
                prepareListData(jsonString);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(Exception e)
        {  e.printStackTrace();}

    }
    private boolean isIndexFilePresent()
    {
        return(new DownloadedFileAddress().checkFilePresence("FAQIndex.json", Constants.FAQ, getContext()));
    }
    /*
     * Preparing the list data
     */
    private void prepareListData(String jsonString) {
        Log.v("json String",jsonString);
        //here  read the master file and assign id to each option
        try {
//initialize
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<genericList<String,Integer>>>();

//trying to parse json file to extract array values
            JSONArray outer_array=new JSONArray(jsonString);
            for (int i=0;i<outer_array.length();i++) {

                JSONObject topicObject=(JSONObject)outer_array.get(i);
                //adding topic name to list header
                String topicName=topicObject.getString("topic_name");
                listDataHeader.add(topicName);

                //adding lectures to topic
                List< genericList<String,Integer>> lecturelist = new ArrayList<genericList<String,Integer>>();
                JSONArray lecture_array=topicObject.getJSONArray("lectures");
                for (int j=0;j<lecture_array.length();j++) {
                    JSONObject lectureObject=(JSONObject)lecture_array.get(j);
                    String lectureName=lectureObject.getString("LectureName");
                    int lecture_folder_id=lectureObject.getInt("LectureFolder");

                    lecturelist.add(new genericList<String, Integer>(lectureName,lecture_folder_id));
                }
                //adding lecture list to lecture data child
                listDataChild.put(topicName,lecturelist);

            }
       /*     JSONObject jb = new JSONObject(jsonString);
            JSONArray a=jb.getJSONArray("content_scripts");
            if(a!=null)
            {JSONObject j=(JSONObject)a.get(0);
                JSONArray arr=j.getJSONArray("matches");

                Log.v("preparelist",(String)arr.get(0));
            }

            String error = jb.getString("error");
            String message=jb.getString("message");
            Log.e("preparelist", "json unparsed: " + error+message);

            File file=new File(new DownloadedFileAddress().getNewFileAddress("LearningIndex.json",Constants.LEARN,getContext()));

        FileWriter writer=new FileWriter(file);
            writer.write(jsonString);
            writer.close();*/
        }  catch (JSONException e) {
            e.printStackTrace();
        }

/*
Log.v("here","i was here");
        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
       List< genericList<String,Integer>> top250 = new ArrayList<genericList<String,Integer>>();
        top250.add(new genericList<String, Integer>("sdfsdf",27));
        top250.add(new genericList<String, Integer>("The Godfather",27));
        top250.add(new genericList<String, Integer>("The Godfather: Part II", 27));
        top250.add(new genericList<String, Integer>("Pulp Fiction",27));
        top250.add(new genericList<String, Integer>("The Good, the Bad and the Ugly", 27));
        top250.add(new genericList<String, Integer>("The Dark Knight",27));
        top250.add(new genericList<String, Integer>("12 Angry Men", 27));

        List< genericList<String,Integer>> nowShowing = new ArrayList<genericList<String,Integer>>();
        nowShowing.add(new genericList<String, Integer>("The Conjuring", 29));
        nowShowing.add(new genericList<String, Integer>("Despicable Me 2", 29));
        nowShowing.add(new genericList<String, Integer>("Turbo", 29));
        nowShowing.add(new genericList<String, Integer>("Grown Ups 2", 29));
        nowShowing.add(new genericList<String, Integer>("Red 2", 29));
        nowShowing.add(new genericList<String, Integer>("The Wolverine", 29));

        List< genericList<String,Integer>> comingSoon = new ArrayList<genericList<String,Integer>>();
        comingSoon.add(new genericList<String, Integer>("2 Guns", 29));
        comingSoon.add(new genericList<String, Integer>("The Smurfs 2",29));
        comingSoon.add(new genericList<String, Integer>("The Spectacular Now", 29));
        comingSoon.add(new genericList<String, Integer>("The Canyons",29));
        comingSoon.add(new genericList<String, Integer>("Europa Report", 29));


        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);*/
    }
//gneric list to add list name along with folder id

}