package com.example.anurag.connect_net;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Topics extends AppCompatActivity implements View.OnClickListener {
    private List<Items> myItem = new ArrayList<Items>();

    ArrayList<String> names = new ArrayList<String >();
    ArrayList<Integer> iconID = new ArrayList<Integer>();
    Loading l;

    RelativeLayout linearMain;
    private Button refresh;
    private JSONArray jsonArray;
    private ListView list;
    private Context context;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        jsonArray=null;
        context = this;
        category = getIntent().getExtras().getString("category");
        getSupportActionBar().setTitle(category);
        new PhpRequest(this, 3).execute("topic",category,"", "http://www.insigniathefest.com/kalam/show_learn_topics.php");


        linearMain = (RelativeLayout)findViewById(R.id.ll);
        refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(this);

        l=new Loading(this);
        //Prepare Array List for name, iconID, and buttonID
        display();

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh://refresh
                l = new Loading(this);
                l.startProgressCircle();
                final SharedPreferences sp;
                sp = getSharedPreferences(Constants.SHARED_PREFERENCE_FILE, 0);
                final Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    int i = 0;

                    public void run() {
                        String connection = sp.getString(Constants.SHARED_PREFERENCE_CONNECTION_KEY, "no");

                        if (INTERNET.isInternetOn(context)) {
                            if (connection.equals("yes")) {

                                new PhpRequest(context, 3).execute("topic",category,"", "http://www.insigniathefest.com/kalam/show_learn_topics.php");
                                if(names.size() > 0){
                                    names.clear();
                                    myItem.clear();
                                }
                                // (list).removeAllViews();
                                display();
                                Toast.makeText(context, " Done ", Toast.LENGTH_LONG).show();
                                l.stopProgressCircle();

                            } else {

                                new PhpRequest(context, 3).execute("topic",category,"", "http://www.insigniathefest.com/kalam/show_learn_topics.php");
                                if(names.size() > 0){
                                    names.clear();
                                    myItem.clear();
                                }
                                // (list).removeAllViews();
                                display();
                                Toast.makeText(context, " Not Connected to internet ", Toast.LENGTH_LONG).show();
                                l.stopProgressCircle();
                            }
                        } else {

                            Toast.makeText(context, " Not Connected  ", Toast.LENGTH_LONG).show();
                            l.stopProgressCircle();

                        }

                    }
                };
                handler.postDelayed(runnable, 2000); //for initial delay..


                break;
            case R.id.button2://more

                startActivity(new Intent("android.intent.action.Mains"));
                break;

            default:
                break;
        }
    }


    private void display() {
        String data = InternalStorage.read(this, Constants.INTERNAL_STORAGE_LEARN_TOPIC_FILE+category);
        if (!data.isEmpty()) {
            try {
                Log.d("display", "in data");
                jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String topic = jsonObject.optString("topic").toString();
                    Log.d("cat", topic);
                    names.add(topic);
                    iconID.add(R.mipmap.ic_launcher);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        populateCategoryList();
        populateListView();
        registerClickCallback();
    }

    private void populateCategoryList() {
        if(jsonArray!=null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                myItem.add(new Items(names.get(i), iconID.get(i)));
            }
        }
    }

    private void populateListView() {
        ArrayAdapter<Items> adapter = new CategoryListAdapter();
        list = (ListView) findViewById(R.id.listview);
        list.setAdapter(adapter);
    }


    private void registerClickCallback() {
        list = (ListView)findViewById(R.id.listview);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /// l.startProgressCircle();
                Items clickedItem = myItem.get(position);
                String topic=names.get(position);
                Toast.makeText(context, "you are in "+category+"->"+topic, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, TopicContent.class);
                intent.putExtra("category", category);
                intent.putExtra("topic", topic);
               // l.stopProgressCircle();
                startActivity(intent);
                Log.d("click",category);


            }
        });
    }
    private class CategoryListAdapter extends ArrayAdapter<Items>{
        public CategoryListAdapter(){
            super(context, R.layout.learning_item_row, myItem);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.learning_item_row, parent, false);
            }

            //Find the category to work with
            Items currentItem = myItem.get(position);

            //Fill the view
            ImageView icon = (ImageView)itemView.findViewById(R.id.icon);
            icon.setImageResource(currentItem.getIconID());

            TextView name = (TextView)itemView.findViewById(R.id.name);
            name.setText(currentItem.getName());


            return itemView;
        }
    }
    class Items{
        String name;
        int iconID;
        public Items(String name, int iconID){
            this.name = name;
            this.iconID = iconID;
        }

        public int getIconID() {
            return iconID;
        }

        public void setIconID(int iconID) {
            this.iconID = iconID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}



