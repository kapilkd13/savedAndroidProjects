package com.example.anurag.connect_net;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TopicContent extends AppCompatActivity implements View.OnClickListener {
    LinearLayout linearMain;
    Button refresh;
    Button more;
    Context context;
    String category;
    String lId;
    String fId;
    TextView category_name;

    private String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_content);

        category = getIntent().getExtras().getString("category");
        topic = getIntent().getExtras().getString("topic");
        getSupportActionBar().setTitle(category+"->"+topic);

        new PhpRequest(this,3).execute("content",category, topic,"http://www.insigniathefest.com/kalam/show_learn_topic_content.php");

        refresh = (Button)findViewById(R.id.button1);
        more = (Button)findViewById(R.id.button2);
        //category_name=(TextView)findViewById(R.id.category_name);
        //category_name.setText(category);

        linearMain = (LinearLayout)findViewById(R.id.linearMain);
        refresh.setOnClickListener(this);
        more.setOnClickListener(this);


        context=this;

        onResume();
    }

    @Override
    public void onResume() {
        super.onResume();
        linearMain.removeAllViewsInLayout();

        Log.d("act",this.toString());

        writeFragFromFile();
    }

    private void writeFragFromFile() {
        String data= InternalStorage.read(context,Constants.INTERNAL_STORAGE_LEARN_TOPIC_CONTENT_FILE+category+topic);
        if(!data.isEmpty()) {
            try {

                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String noImg = jsonObject.optString("noImg").toString();
                    String noVideo = jsonObject.optString("noVideo").toString();


                    String category = jsonObject.optString("category").toString();
                    String title = jsonObject.optString("topic").toString();
                    String text = jsonObject.optString("text").toString();

                    //Log.d("post","post");

                    Log.d("post", "1");
                    if (noImg.isEmpty()) noImg = "0";
                    if (noVideo.isEmpty()) noVideo = "0";
                    int img = Integer.parseInt(noImg);
                    int video = Integer.parseInt(noVideo);
                    addLayout1(1, title, category, "", text, img, video);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //addLayout1(data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1://refresh
                final SharedPreferences sp;
                sp = getSharedPreferences(Constants.SHARED_PREFERENCE_FILE, 0);
                final Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    int i=0;
                    public void run() {
                        String connection=sp.getString(Constants.SHARED_PREFERENCE_CONNECTION_KEY,"no");

                        if(INTERNET.isInternetOn(context)){
                            if(connection.equals("yes")){
                                new PhpRequest(context,3).execute("content",category, topic,"http://www.insigniathefest.com/kalam/show_learn_topic_content.php");
                                linearMain.removeAllViewsInLayout();
                                writeFragFromFile();
                                Toast.makeText(context, " Done ", Toast.LENGTH_LONG).show();

                            }else {
                                new PhpRequest(context,3).execute("content",category, topic,"http://www.insigniathefest.com/kalam/show_learn_topic_content.php");
                                linearMain.removeAllViewsInLayout();
                                writeFragFromFile();
                                Toast.makeText(context, " Not Connected to internet ", Toast.LENGTH_LONG).show();

                            }
                        }else{

                            Toast.makeText(context, " Not Connected  ", Toast.LENGTH_LONG).show();

                        }

                    }
                };
                handler.postDelayed(runnable, 2000); //for initial delay..


                break;
            case R.id.button2://more

                startActivity(new Intent("android.intent.action.Main"));
                break;

            default:
                break;
        }
    }


    public void addLayout1(int id,String title, String category, String timestamp, String text, int noImg, int noVideo){

        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setId(R.id.layout);

        NewPostFragment1 f = new NewPostFragment1();
        f.addDetails(id,title, category, timestamp, text,noImg,noVideo);
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(ll.getId() , f, "id");
        transaction.commit();

        linearMain.addView(ll);
    }

}
