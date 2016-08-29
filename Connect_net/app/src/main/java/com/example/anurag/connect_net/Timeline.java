package com.example.anurag.connect_net;

/**
 * Created by sarfraz on 27-05-2016.
 */
import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Timeline extends Fragment implements View.OnClickListener {
    LinearLayout linearMain;
   /* Button refresh;*/
 /*   Button more;*/

    View view;
    Loading l;
    SwipeRefreshLayout swipeLayout;
    ScrollView scrollView;
Button downloadVideo,playVideo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.timeline_fragment, container, false);
        setHasOptionsMenu(true);
        Log.v("oncreateview","called");
     /*   refresh = (Button) view.findViewById(R.id.button1);
        refresh.setOnClickListener(this);*/
      /*  more = (Button) view.findViewById(R.id.button2);
        more.setOnClickListener(this);*/
        linearMain = (LinearLayout) view.findViewById(R.id.linearMain);



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
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY();
                if (scrollY == 0) swipeLayout.setEnabled(true);
                else swipeLayout.setEnabled(false);

            }
        });



        //swipelayout ends

        onResume();
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.smoothScrollTo(0, 0);
        return view;
    }

    @Override
    public void onResume() {
        Log.v("onResume","called");
        super.onResume();
        linearMain.removeAllViewsInLayout();

        Log.d("act", getActivity().toString());

        writeFragFromFile();
       scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.smoothScrollTo(0, 0);
    }

    @Override
    public void onClick(View v) {
        Log.v("onclick", "called");
        switch (v.getId()) {
         /*   case R.id.button1://refresh
refreshAction();
                break;*/
            case R.id.imgrefresh:
                refreshAction();

                break;

            default:
                break;
        }
    }

    private void refreshAction() {

        scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.smoothScrollTo(0, 0);
        Log.v("refreshaction","called");
        l = new Loading(view.getContext());
        l.startProgressCircle();
        final SharedPreferences sp;
        sp = getContext().getSharedPreferences(Constants.SHARED_PREFERENCE_FILE, 0);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {
                String connection = sp.getString(Constants.SHARED_PREFERENCE_CONNECTION_KEY, "no");

                if (INTERNET.isInternetOn(getContext())) {
             //       if(new UserInfo(getContext()).isuserRegistered())
                    //right now user is not registered
                    {
                    if (connection.equals("yes")) {

                        new PhpRequest(getContext(),Timeline.this,1, 1).execute("timeline", "http://www.insigniathefest.com/kalam/get_latest_posts.php");
                        linearMain.removeAllViewsInLayout();
                        writeFragFromFile();
                        Toast.makeText(getContext(), " Done ", Toast.LENGTH_LONG).show();
                        l.stopProgressCircle();

                    } else {

                        new PhpRequest(getContext(),Timeline.this,1, 1).execute("timeline", "http://www.insigniathefest.com/kalam/get_latest_posts.php");

                        linearMain.removeAllViewsInLayout();

                        Toast.makeText(getContext(), " Connecting ", Toast.LENGTH_LONG).show();

                    }}
                  /*  else
                    {
                        new UserRegisteration(Timeline.this,1).execute();
                    l.stopProgressCircle();
                    }*/
                } else {

                    Toast.makeText(getContext(), " Not Connected  ", Toast.LENGTH_LONG).show();
                    l.stopProgressCircle();

                }

            }
        };
        handler.postDelayed(runnable, 2000); //for initial delay..

    }

    public  void afterUserRegCall()
    {
        refreshAction();

    }



    public void afterphpCall()
    {  writeFragFromFile();
        l.stopProgressCircle();
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.smoothScrollTo(0, 0);
    }
@Override
public void onCreateOptionsMenu(Menu menu,MenuInflater inflatermenu)
{


}

@Override
public boolean onOptionsItemSelected(MenuItem item)
{Log.v("refresh","called onitemselected");
  switch (item.getItemId())
  {

      case R.id.imgrefresh:
          if(MainActivity.currentTab=="Timeline")
          {  Log.v("refresh", "called onitemselected");
              scrollView.fullScroll(ScrollView.FOCUS_UP);//scrolling to top after refresh
              scrollView.smoothScrollTo(0, 0);
         refreshAction();

          return true;}
          else
              return false;


  }
    return false;
}

    private void writeFragFromFile() {
        //resetting fragment
        linearMain.removeAllViewsInLayout();

        Log.v("writefrag from file","called");
        String data = InternalStorage.read(getContext(), Constants.INTERNAL_STORAGE_TIMELINE_FILE);
        if (!data.isEmpty()) {
            try {

                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String isPoll = jsonObject.optString("isPoll").toString();
                    String noOption = jsonObject.optString("noOption").toString();
                    String noImg = jsonObject.optString("noImg").toString();
                    String noVideo = jsonObject.optString("noVideo").toString();

                    String title = jsonObject.optString("title").toString();
                    String category = jsonObject.optString("category").toString();
                    String timestamp = jsonObject.optString("timestamp").toString();
                    String text = jsonObject.optString("text").toString();
                    String Id = jsonObject.optString("id").toString();
                    //Log.d("post","post");
                    int type = 10;
                    //Log.d("ispoll",isPoll);
                    //Log.d("noImg",noImg);
                    if (isPoll.isEmpty()) isPoll = "0";
                    if (Integer.parseInt(isPoll) == 1) {
                        //Log.d("noImg",noImg);
                        if (noOption.equals("0")) {
                            type = 3;
                        } else {
                            type = 2;
                        }
                    } else {
                        // Log.d("noImg",noImg);
                        type = 1;
                    }
                    Log.d("post_type", " " + type);
                    switch (type) {
                        case 1:
                            Log.d("post", "1");
                            if (noImg.isEmpty()) noImg = "0";
                            if (noVideo.isEmpty()) noVideo = "0";
                            int img = Integer.parseInt(noImg);
                            int video = Integer.parseInt(noVideo);

                            if (Id.isEmpty()) Id = "0";
                            int id = Integer.parseInt(Id);
                            addLayout1(id, title, category, timestamp, text, img, video);
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                    }
                    //addLayout1(text);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //addLayout1(data);
        }
        else{refreshAction();}
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.smoothScrollTo(0, 0);
    }


    public void addLayout1(int id, String title, String category, String timestamp, String text, int noImg, int noVideo) {

        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setId(R.id.layout);

        NewPostFragment1 f = new NewPostFragment1();
        f.addDetails(id, title, category, timestamp, text, noImg, noVideo);
        android.support.v4.app.FragmentManager manager = getChildFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(ll.getId(), f, "id");
        transaction.commit();

        linearMain.addView(ll);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.smoothScrollTo(0, 0);
    }





}

