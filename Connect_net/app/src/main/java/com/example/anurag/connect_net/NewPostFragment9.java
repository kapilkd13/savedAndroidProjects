package com.example.anurag.connect_net;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sarfraz on 28-05-2016.
 */
public class NewPostFragment9 extends Fragment  {
    TextView tv_title, tv_category, tv_timestamp, tv_text;
    String title,category,timestamp,text;
    View view;
    public void addDetails(String title,String category,String timestamp,String text){
        this.title=title;
        this.category = category;
        this.timestamp = timestamp;
        this.text = text;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_post_fragment9, container,false);
        init();

        tv_text.setText(text);
        tv_title.setText(title);
        tv_category.setText(Html.fromHtml(" from <u>"+category+"</u>"));
        tv_timestamp.setText(timestamp);


        tv_category.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Toast.makeText(getContext(), "you are in "+category, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), Category.class);
                intent.putExtra("category", category);
                startActivity(intent);
                Log.d("click",category);
            }
        });
        return view;
    }
    public void init(){
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_category = (TextView) view.findViewById(R.id.tv_category);
        tv_timestamp = (TextView) view.findViewById(R.id.tv_timestamp);
        tv_text = (TextView) view.findViewById(R.id.tv_text);
    }


}
