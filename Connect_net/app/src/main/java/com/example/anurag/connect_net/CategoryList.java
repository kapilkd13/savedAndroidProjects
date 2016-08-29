package com.example.anurag.connect_net;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CategoryList extends AppCompatActivity{
    private List<SubscribeItem> myItem = new ArrayList<SubscribeItem>();

    ArrayList<String> names = new ArrayList<String >();
    ArrayList<Integer> iconID = new ArrayList<Integer>();
    ArrayList<Integer> buttonID = new ArrayList<Integer>();

    ArrayList<Integer> checked = new ArrayList<Integer>();

    static String AlertMessage;

    Loading l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        l = new Loading(this);


        init();

        //Prepare Array List for name, iconID, and buttonID
        for(int i=0;i<6;i++){
            names.add("Farming "+i);
            iconID.add(R.mipmap.ic_launcher);
            buttonID.add(R.mipmap.ic_launcher);
        }
        populateCategoryList();
        populateListView();
        registerClickCallback();
    }

    private void registerClickCallback() {
        ListView list = (ListView)findViewById(R.id.listview);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                l.startProgressCircle();
                SubscribeItem clickedCar = myItem.get(position);


                subscribe(position);
                populateCategoryList();
                populateListView();

                l.stopProgressCircle();
            }
        });
    }
    private void subscribe(int position){
        int flag=0;
        for(int i=0;i<checked.size();i++){
            if(checked.get(i) == position){
                flag = 1;
                unsubscribe(position);
            }
        }
        if(flag==0){
            checked.add(position);
            AlertMessage = "You Subscribed for Category "+names.get(position);
            MyAlert myAlert = new MyAlert();
            myAlert.show(getFragmentManager(), "my alert");
        }
    }

    private void unsubscribe(int position){
        for(int i=0;i<checked.size();i++){
            if(checked.get(i) == position){
                checked.remove(i);
            }
        }
        AlertMessage = "You Unsubscribed for category "+names.get(position);
        MyAlert myAlert = new MyAlert();
        myAlert.show(getFragmentManager(), "my alert");
    }


    private void populateCategoryList() {
        myItem.clear();
        for(int i=0;i<6;i++){
            int flag=0;
            for(int j=0;j<checked.size(); j++){
                if(i==checked.get(j)){
                    Log.d("Found in checked list",i+"");
                    myItem.add(new SubscribeItem(names.get(i), iconID.get(i), R.drawable.tick1));
                    flag=1;
                }
            }
            if(flag==0){
                myItem.add(new SubscribeItem(names.get(i), iconID.get(i), buttonID.get(i)));
            }
            Log.d("button",myItem.get(i).getButtonID()+"");
        }
    }
    private void populateListView() {
        ArrayAdapter<SubscribeItem> adapter = new CategoryListAdapter();
        ListView list = (ListView)findViewById(R.id.listview);
        list.removeAllViewsInLayout();
        list.setAdapter(adapter);

    }

    private class CategoryListAdapter extends ArrayAdapter<SubscribeItem>{
        public CategoryListAdapter(){
            super(CategoryList.this, R.layout.subscribed_item_row, myItem);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.subscribed_item_row, parent, false);
            }

            //Find the category to work with
            SubscribeItem currentItem = myItem.get(position);

            //Fill the view
            ImageView icon = (ImageView)itemView.findViewById(R.id.icon);
            icon.setImageResource(currentItem.getIconID());

            TextView name = (TextView)itemView.findViewById(R.id.name);
            name.setText(currentItem.getName());

            ImageView button = (ImageView)itemView.findViewById(R.id.button);
            button.setImageResource(currentItem.getButtonID());

            return itemView;
        }
    }
    public static class MyAlert  extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // builder.setTitle("title");
            builder.setMessage(AlertMessage);

            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            Dialog dialog = builder.create();
            return dialog;
        }
    }


    public void init(){

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
Log.v("culprit","culprit");
        if(item.getItemId() == android.R.id.home){

            Intent intent = new Intent(this,MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);

            finish();

            return true;

        }
return false;

    }
}