package com.example.anurag.connect_net;

/**
 * Created by sarfraz on 27-05-2016.
 */
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Faqs extends Fragment implements View.OnClickListener {

    RelativeLayout  linearMain;
    ExpandableListView myList;
    View view;

   /* private Button refresh;*/
    Loading l;
    SwipeRefreshLayout swipeLayout;
    ScrollView scrollView;
    ImageButton imgbutton;
    static android.support.v4.app.FragmentManager manager;
    static boolean showingfaqlecture = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.faq_fragment, container, false);
        setHasOptionsMenu(true);

        manager = getChildFragmentManager();



        linearMain = (RelativeLayout) view.findViewById(R.id.ll);
     /*   refresh = (Button) view.findViewById(R.id.refresh);
        refresh.setOnClickListener(this);*/


//todo here make changes on the term of learning file
//swipe refresh controls
        imgbutton=(ImageButton)view.findViewById(R.id.imgrefresh);
        scrollView = (ScrollView) view.findViewById(R.id.scroll);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.parseColor("#ed037c"), Color.parseColor("#c40053"), Color.parseColor("#ffffff"), Color.parseColor("#51af50"));
        swipeLayout.setRefreshing(false);
        swipeLayout.setEnabled(false);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(false);
                Log.d("Swipe", "Refreshing Number");
              //  refreshAction();
            }
        });
    /*    scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY();
                if (scrollY == 0) swipeLayout.setEnabled(true);
                else swipeLayout.setEnabled(false);

            }
        });*/

      //  displayList();
        //expandAll();

        if(savedInstanceState!=null) {

            callPresentListLayout();
        }
        else{
            addLayout1();
        }
        return view;
    }
    /*private void expandAll(){
        int count = listAdapter.getGroupCount();
        for(int i=0;i<count;i++){
            myList.expandGroup(i);
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {Log.v("refresh","called onitemselected");
        switch (item.getItemId())
        {

            case R.id.imgrefresh:
                if(MainActivity.currentTab=="FAQ")
                {  Log.v("refresh","called onitemselected");
                  //  refreshAction();
                    return true;}


        }
        return false;
    }

    public void onClick(View v) {
        switch (v.getId()) {
         /*   case R.id.refresh://refresh
              refreshAction();

                break;*/
            case R.id.imgrefresh:

                break;

            case R.id.button2://more

                startActivity(new Intent("android.intent.action.Mains"));
                break;

            default:
                break;
        }
    }

    //called when saved instance is nott null this provides view to present frag
    private  void  callPresentListLayout()
    {   LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setId(R.id.faq_expand_id);
        linearMain.addView(ll);
    }

//todo to implement new refresh function
    /* l = new Loading(view.getContext());
        l.startProgressCircle();
        final SharedPreferences sp;
        sp = getContext().getSharedPreferences(Constants.SHARED_PREFERENCE_FILE, 0);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {
                String connection = sp.getString(Constants.SHARED_PREFERENCE_CONNECTION_KEY, "no");

                if (INTERNET.isInternetOn(getContext())) {
                  //  if(new UserInfo(getContext()).isuserRegistered())
                    {
                        if (connection.equals("yes")) {

                            new PhpRequest(getContext(), Faqs.this, 4, 1).execute("faqs", "http://www.insigniathefest.com/kalam/show_faqs.php");
                            if ((linearMain).getChildCount() > 0)
                                (linearMain).removeAllViews();
                            //    displayList();
                            Toast.makeText(getContext(), " Done ", Toast.LENGTH_LONG).show();
                            //  l.stopProgressCircle();
                            //activity resolves after response from phprequest by calling afterphpCall()
                        } else {

                            new PhpRequest(getContext(), Faqs.this, 4, 1).execute("faqs", "http://www.insigniathefest.com/kalam/show_faqs.php");
                            if ((linearMain).getChildCount() > 0)
                                (linearMain).removeAllViews();
                            //    displayList();
                            Toast.makeText(getContext(), " Connecting ", Toast.LENGTH_LONG).show();
                            //  l.stopProgressCircle();
                            //activity resolves after response from phprequest
                        }
                    }
                    //uncomment it later after creating server side coding of login page
                  *//*  else {
                        new UserRegisteration(Faqs.this,4).execute();
                    }*//*
                } else {

                    Toast.makeText(getContext(), " Not Connected  ", Toast.LENGTH_LONG).show();
                    l.stopProgressCircle();

                }

            }
        };
        handler.postDelayed(runnable, 2000); //for initial delay..*/



    //create fragment here
    public void addLayout1() {

        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setId(R.id.faq_expand_id);
        FAQExpandableListFragment f = new FAQExpandableListFragment();
        //   f.addDetails(id, title, category, timestamp, text, noImg, noVideo);

        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        //use this id to push this fragment to background

        transaction.add(ll.getId(), f, "faq_expand_id");
        //    transaction.addToBackStack(null);
        transaction.commit();

        //usua
        manager.executePendingTransactions();
        linearMain.addView(ll);


        scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.smoothScrollTo(0, 0);
    }

    public void afterUserRegCall() {
      //  refreshAction();

    }




    //called on selecting a list item in expandablelist
    public void onLectureSelection(int id) {
        startFetchingFile(id);
    }

    //following are the methods for implementing Lecture view

    //file lookup method
    private void startFetchingFile(int folderId) {
        if (!(new DownloadedFileAddress().checkFilePresence("LectureDetails.json", Constants.FAQ, folderId, getContext()))) {
            if (INTERNET.isInternetOn(getContext())) {
                ArrayList<String> dataList = new ArrayList();
                dataList.add(new UserInfo(getContext()).getusermail());
                dataList.add(Integer.toString(folderId));
                (new DownloadFAQLectureFile(getContext(), this)).execute(dataList);
                //readIndexFile();
            } else {
                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_LONG);
            }
        } else
            readLectureFile(folderId);

    }

    private void readLectureFile(int folderId) {
        File file = new File(new DownloadedFileAddress().getDownloadedFileAddress("LectureDetails.json", Constants.FAQ, folderId, getContext()));
        try {if(file.exists()) {
            FileInputStream filereader = new FileInputStream(file);
            byte[] buffer = new byte[filereader.available()];
            filereader.read(buffer);
            filereader.close();
            String jsonString = new String(buffer, "UTF-8");
            if (jsonString != null)
                //send string read from file for json decoding
                prepareListData(folderId, jsonString);
        }else
        {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_LONG);
        }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void prepareListData(int folderId, String jsonString) {

        try {


            JSONObject lectureObject = new JSONObject(jsonString);
            //extracting deetails from json String
            String question = lectureObject.getString("question");
            String category = lectureObject.getString("category");
            String answer = lectureObject.getString("answer");
            String timestamp = lectureObject.getString("timestamp");
            int numImg = lectureObject.getInt("numImg");
            int numVideo = lectureObject.getInt("numVideo");


            callFAQQuestionFragment(folderId, question, category, timestamp, answer, numImg, numVideo);


        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }

    private void callFAQQuestionFragment(int id, String question, String category, String timestamp, String answer, int noImg, int noVideo) {
        Log.v("backstack", "enrerd in call learning");
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setId(R.id.faq_lecture_id);

        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        //use this id to push this fragment to background
        FAQLectureFragment f = new FAQLectureFragment();
        f.addDetails(id, question,category,timestamp,answer,noImg,noVideo);
        transaction.replace(R.id.faq_expand_id, f, "faq_lecture_id");
        transaction.addToBackStack("asdf");
        transaction.commit();
        manager.executePendingTransactions();
        showingfaqlecture = true;
        if (manager.getBackStackEntryCount() > 0) {
            Log.v("backstack", "has value");

        }

    }
    //to popup last fragment on pressing back click on Lecture view
    public static void popupLastFragment() {
        Log.v("backstack", "has no value");
        if (showingfaqlecture) {
            manager.popBackStack();
            showingfaqlecture = false;
        }
    }








    public void afterphpCall(int folderId) {//display();
        readLectureFile(folderId);
        //    l.stopProgressCircle();}

    }







}


