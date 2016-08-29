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
import android.view.ViewTreeObserver;
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

public class Polls extends Fragment implements View.OnClickListener{
static int a=0;
    Loading l;
    private View view;
    RelativeLayout linearMain;

    private JSONArray jsonArray;
    private ListView list;
    SwipeRefreshLayout swipeLayout;
    ScrollView scrollView;
    ImageButton imgbutton;
    static android.support.v4.app.FragmentManager manager;
static boolean isLatestIndexFile=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // String classes[]={"MainActivity","Password","Email","CameraWalllpaper","Graphics","GraphicsSurface"};

        setHasOptionsMenu(true);
        manager = getChildFragmentManager();
        view = inflater.inflate(R.layout.poll_fragment, container,false);
        linearMain = (RelativeLayout)view.findViewById(R.id.ll);

        l= new Loading(view.getContext());
        //Prepare Array List for name, iconID, and buttonID
        imgbutton=(ImageButton)view.findViewById(R.id.imgrefresh);
        scrollView = (ScrollView) view.findViewById(R.id.scroll);

        //swipe refresh layout implementation
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

        //fetch file
        if(savedInstanceState!=null) {

            callPresentListLayout();
        }
        else{
            startFetchingFile();
        }



        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY();
                if (scrollY == 0) swipeLayout.setEnabled(true);
                else swipeLayout.setEnabled(false);

            }
        });
     //   display();

        //returning views
        return view;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {Log.v("refresh","called onitemselected");
        switch (item.getItemId())
        {

            case R.id.imgrefresh:
                if(MainActivity.currentTab == "Polls") {  Log.v("refresh","called onitemselected");
                  //  refreshAction();
                    return true;}


        }
        return false;
    }

    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.imgrefresh:
             //   refreshAction();
                break;

            case R.id.button2://more

                startActivity(new Intent("android.intent.action.Mains"));
                break;

            default:
                break;
        }
    }

private void refreshAction()
{   startFetchingFile();

}
    public  void afterUserRegCall()
    {
//

    }


    public void afterphpCall()
    {
        l.stopProgressCircle();}

    //called when saved instance is nott null this provides view to present frag
    private  void  callPresentListLayout()
    {   LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setId(R.id.poll_lecture_id);
        linearMain.addView(ll);
    }




    //folloeing methods implement the index file lookup for polls this index file containss the latest 5 polls .

    private void startFetchingFile() {
     //   if (!(new DownloadedFileAddress().checkFilePresence("LectureDetails.json", Constants.LEARN, folderId, getContext()))) {
            if (INTERNET.isInternetOn(getContext())) {
                ArrayList<String> dataList = new ArrayList();
                dataList.add(new UserInfo(getContext()).getusermail());
                Log.v("poll", "it is fetching here");
                (new DownloadPollsIndexFile(getContext(), this)).execute(dataList);
                //readIndexFile();
            } else {  readPollsFile();

                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_LONG);
            }


    }
//fetch individual poll file from server or from local mem
    private void startFetchingIndividualFile(int folderId) {
          if (!(new DownloadedFileAddress().checkFilePresence("LectureDetails.json", Constants.POLL, folderId, getContext()))) {
              if (INTERNET.isInternetOn(getContext())) {
                  ArrayList<String> dataList = new ArrayList();
                  dataList.add(new UserInfo(getContext()).getusermail());
                  dataList.add(Integer.toString(folderId));
                  (new DownloadPollsLectureFile(getContext(), this)).execute(dataList);
                  //readIndexFile();
              } else {
                  addPollFragment(folderId);

                  Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_LONG);
              }
          }else
          { addPollFragment(folderId);}

    }

    //reads polls index file this has to latest and updatd
    private void readPollsFile() {
        if(new DownloadedFileAddress().checkFilePresence("PollsIndex.json", Constants.POLL, getContext())) {
            File file = new File(new DownloadedFileAddress().getDownloadedFileAddress("PollsIndex.json", Constants.POLL, getContext()));
            try {
                Log.v("poll","it is here");
                if (file.exists()) {
                    FileInputStream filereader = new FileInputStream(file);
                    byte[] buffer = new byte[filereader.available()];
                    filereader.read(buffer);
                    filereader.close();
                    String jsonString = new String(buffer, "UTF-8");
                    if (jsonString != null)
                        //send string read from file for json decoding
                        prepareListData( jsonString);
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

    private void prepareListData( String jsonString) {

        try {


            JSONArray PollArray = new JSONArray(jsonString);

            for(int i=0;i<PollArray.length();i++) {

                JSONObject PollObject=PollArray.getJSONObject(i);
                startFetchingIndividualFile(PollObject.getInt("pollId"));
              //  addPollFragment(PollObject.getInt("pollId"));

            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }

    private  void  addPollFragment(int pollId) {

    Log.v("backstack", "enrerd in call learning");
    LinearLayout ll = new LinearLayout(getContext());
    ll.setOrientation(LinearLayout.VERTICAL);
    ll.setId(R.id.poll_lecture_id);

    android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
    //use this id to push this fragment to background
    PollLectureFragment f = new PollLectureFragment();
    f.addDetails(pollId, getContext());
    transaction.add(ll.getId(), f, "poll_lecture_id");
    // transaction.addToBackStack("asdf");
    transaction.commit();
   // manager.executePendingTransactions();
    linearMain.addView(ll);
    if (manager.getBackStackEntryCount() > 0) {
        Log.v("backstack", "has value");

    }
}

    public void afterphpCallfromLecture(int folderId) {//display();
        addPollFragment(folderId);
        //    l.stopProgressCircle();}


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

       // outState.putBoolean("showinglecture", showinglecture);
    }

    public void afterphpCallfromIndex() {
        readPollsFile();
    }
}

