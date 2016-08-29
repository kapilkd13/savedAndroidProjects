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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Learning extends Fragment implements View.OnClickListener {


    ArrayList<String> names = new ArrayList<String>();
    ArrayList<Integer> iconID = new ArrayList<Integer>();
    Loading l;
    static boolean showinglecture = false;
    private View view;
    RelativeLayout linearMain;
    /*  private Button refresh;*/
    private JSONArray jsonArray;
    private ListView list;
    SwipeRefreshLayout swipeLayout;
    ScrollView scrollView;
    ImageButton imgbutton;

    static android.support.v4.app.FragmentManager manager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // String classes[]={"MainActivity","Password","Email","CameraWalllpaper","Graphics","GraphicsSurface"};
        jsonArray = null;
        setHasOptionsMenu(true);
        //     new PhpRequest(getContext(), 1).execute("learn_category", "http://www.insigniathefest.com/kalam/show_learn_category.php");
//creating manager

        manager = getChildFragmentManager();

        view = inflater.inflate(R.layout.learning_fragment, container, false);
        linearMain = (RelativeLayout) view.findViewById(R.id.ll);
      /*  refresh = (Button) view.findViewById(R.id.refresh);
        refresh.setOnClickListener(this);*/

        //show loading circle
        l = new Loading(view.getContext());


        imgbutton = (ImageButton) view.findViewById(R.id.imgrefresh);
        scrollView = (ScrollView) view.findViewById(R.id.scroll);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.parseColor("#ed037c"), Color.parseColor("#c40053"), Color.parseColor("#ffffff"), Color.parseColor("#51af50"));
        swipeLayout.setRefreshing(false);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(false);
                Log.d("Swipe", "Refreshing Number");
                refreshAction();
            }
        });
        swipeLayout.setEnabled(false);
       /* linearMain.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                int scrollY = linearMain.getScrollY();
                if (scrollY == 0) swipeLayout.setEnabled(true);
                else swipeLayout.setEnabled(false);

            }
        });*/

        //swipelayout ends
        //  display();

        //create a method to update  the mster learning file in json format which has all thee details like  category subcategory and the folder no
        if(savedInstanceState!=null) {

                callPresentListLayout();
        }
        else{
        addLayout1();
        }

        return view;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v("refresh", "called onitemselected");
        switch (item.getItemId()) {

            case R.id.imgrefresh:
                if (MainActivity.currentTab == "Learn") {
                    Log.v("refresh", "called onitemselected");
                    refreshAction();
                    return true;
                }


        }
        return false;
    }

    public void onClick(View v) {
        switch (v.getId()) {
         /*   case R.id.refresh://refresh

                refreshAction();
                break;*/

            case R.id.imgrefresh:
                refreshAction();
                break;

            case R.id.button2://more

                startActivity(new Intent("android.intent.action.Mains"));
                break;

            default:
                break;
        }
    }

//todo work on refresh action what to do on refresh click
    private void refreshAction() {   // l = new Loading(view.getContext());

     /*   l.startProgressCircle();
        final SharedPreferences sp;
        sp = getContext().getSharedPreferences(Constants.SHARED_PREFERENCE_FILE, 0);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {
                String connection = sp.getString(Constants.SHARED_PREFERENCE_CONNECTION_KEY, "no");

                if (INTERNET.isInternetOn(getContext())) {
                    //   if(new UserInfo(getContext()).isuserRegistered())
                    {
                        if (connection.equals("yes")) {

                            new PhpRequest(getContext(), Learning.this, 2, 1).execute("learn_category", "http://www.insigniathefest.com/kalam/show_learn_category.php");
                            if (names.size() > 0) {
                                names.clear();
                                myItem.clear();
                            }
                            // (list).removeAllViews();
                            // display();
                            Toast.makeText(getContext(), " Done ", Toast.LENGTH_LONG).show();
                            //    l.stopProgressCircle();
                            //activity resolves/shows after getting response from phprequest

                        } else {

                            new PhpRequest(getContext(), Learning.this, 2, 1).execute("learn_category", "http://www.insigniathefest.com/kalam/show_learn_category.php");
                            if (names.size() > 0) {
                                names.clear();
                                myItem.clear();
                            }
                            // (list).removeAllViews();
                            //  display();
                            Toast.makeText(getContext(), " Connecting ", Toast.LENGTH_LONG).show();
                            //   l.stopProgressCircle();
                            //activity resolves/shows after getting response from phprequest
                        }
                    }*//*else{
                        new UserRegisteration(Learning.this,2).execute();
                    }*//*
                } else {

                    Toast.makeText(getContext(), " Not Connected  ", Toast.LENGTH_LONG).show();
                    l.stopProgressCircle();

                }

            }
        };
        handler.postDelayed(runnable, 2000); //for initial delay..*/
    }

    //create fragment here
    public void addLayout1() {

        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setId(R.id.learn_expand_id);
        ExpandableListFragment f = new ExpandableListFragment();
        //   f.addDetails(id, title, category, timestamp, text, noImg, noVideo);


        //use this id to push this fragment to background

    android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
    transaction.add(ll.getId(), f, "learn_expand_id");
    transaction.commit();
    manager.executePendingTransactions();

        linearMain.addView(ll);

        scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.smoothScrollTo(0, 0);
    }
//called when saved instance is nott null this provides view to present frag
    private  void  callPresentListLayout()
    {   LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setId(R.id.learn_expand_id);
        linearMain.addView(ll);
    }





    public void afterUserRegCall() {
        refreshAction();

    }




    //called on selecting a list item in expandablelist
    public void onLectureSelection(int id) {
        startFetchingFile(id);
    }

    //to popup last fragment on pressing back click on Lecture view
    public static void popupLastFragment() {
        Log.v("backstack", "has no value");
        if (showinglecture) {
            manager.popBackStack();
            showinglecture = false;
        }
    }

    private void display() {
      /*  String data = InternalStorage.read(getContext(), Constants.INTERNAL_STORAGE_LEARN_CATEGORY_FILE);
        if (!data.isEmpty()) {
            try {
                Log.d("display", "in data");
                jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String category = jsonObject.optString("category").toString();
                    Log.d("cat", category);
                    names.add(category);
                    iconID.add(R.mipmap.ic_launcher);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        populateCategoryList();
        populateListView();
        registerClickCallback();*/
    }

    //following are the methods for implementing Lecture view

    //file lookup method
    private void startFetchingFile(int folderId) {
        if (!(new DownloadedFileAddress().checkFilePresence("LectureDetails.json", Constants.LEARN, folderId, getContext()))) {
            if (INTERNET.isInternetOn(getContext())) {
                ArrayList<String> dataList = new ArrayList();
                dataList.add(new UserInfo(getContext()).getusermail());
                dataList.add(Integer.toString(folderId));
                (new DownloadLectureFile(getContext(), this)).execute(dataList);
                //readIndexFile();
            } else {
                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_LONG);
            }
        } else
            readLectureFile(folderId);

    }

    private void readLectureFile(int folderId) {
        if(new DownloadedFileAddress().checkFilePresence("LectureDetails.json", Constants.LEARN, folderId, getContext())) {
            File file = new File(new DownloadedFileAddress().getDownloadedFileAddress("LectureDetails.json", Constants.LEARN, folderId, getContext()));
            try {
                if (file.exists()) {
                    FileInputStream filereader = new FileInputStream(file);
                    byte[] buffer = new byte[filereader.available()];
                    filereader.read(buffer);
                    filereader.close();
                    String jsonString = new String(buffer, "UTF-8");
                    if (jsonString != null)
                        //send string read from file for json decoding
                        prepareListData(folderId, jsonString);
                } else {
                    Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_LONG);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void prepareListData(int folderId, String jsonString) {

        try {


            JSONObject lectureObject = new JSONObject(jsonString);
            //extracting deetails from json String
            String topic = lectureObject.getString("topic");
            String category = lectureObject.getString("category");
            String text = lectureObject.getString("text");
            int numImg = lectureObject.getInt("numImg");
            int numVideo = lectureObject.getInt("numVideo");


            callLearningFragment(folderId, topic, category, "timestamp", text, numImg, numVideo);


        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }

    private void callLearningFragment(int id, String title, String category, String timestamp, String text, int noImg, int noVideo) {
        Log.v("backstack", "enrerd in call learning");
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setId(R.id.learn_lecture_id);

        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        //use this id to push this fragment to background
        LearningLectureFragment f = new LearningLectureFragment();
        f.addDetails(id, title,category,timestamp,text,noImg,noVideo,getContext());
        transaction.replace(R.id.learn_expand_id, f, "learn_lecture_id");
        transaction.addToBackStack("asdf");
        transaction.commit();
        manager.executePendingTransactions();
        showinglecture = true;
        if (manager.getBackStackEntryCount() > 0) {
            Log.v("backstack", "has value");

        }

    }


    public void afterphpCall(int folderId) {//display();
        readLectureFile(folderId);
        //    l.stopProgressCircle();}


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("showinglecture",showinglecture);
    }
}