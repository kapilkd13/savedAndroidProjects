package com.example.anurag.connect_net;

/**
 * Created by Anurag on 10-06-2016.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PollWindow extends AppCompatActivity {

    android.support.v4.app.FragmentManager manager;
    PollQuestionFragmentCheckbox checkboxFragment;
    PollQuestionFragmentSingleAnswer singleAnswerFragment;
    PollQuestionFragmentText textTypeFragment;
    PollQuestionFragmentYesNo yesNoTypeFragment;
    PollQuestionFragmentPhoto photoTypeFragment;
    PollQuestionFragmentVideo videoTypeFragment;

    private static final String CHECKBOX_TYPE = "checkboxtype";
    private static final String SINGLE_ANSWER_TYPE = "singleanswertype";
    private static final String YES_NO_TYPE = "yesnotype";
    private static final String TEXT_TYPE = "texttype";
    private static final String PHOTO_TYPE = "phototype";
    private static final String VIDEO_TYPE = "videotype";
    private String details;
    private String pollId,category,title,timestamp,noQuestion,noPerson;
    private int NPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manager = getSupportFragmentManager();

        details = getIntent().getExtras().getString("details");

        display();

    }

    private void display() {

        String data = details;
        if (!data.isEmpty()) {
            try {
                Log.d("display", "in data");

                JSONObject jsonObject = new JSONObject(details);
                pollId = jsonObject.optString("id").toString();
                category = jsonObject.optString("category").toString();
                title = jsonObject.optString("title").toString();
                timestamp = jsonObject.optString("timestamp").toString();
                noQuestion = jsonObject.optString("noQuestion").toString();
                noPerson = jsonObject.optString("noPerson").toString();
                String questions = jsonObject.optString("questions").toString();
                try{
                NPerson=Integer.parseInt(noPerson);
                }catch (Exception e){
                    NPerson=1;
                }
                int id=0;
                for(int j=1;j<=NPerson;j++) {
                    JSONArray questionArray = new JSONArray(questions);
                    for (int i = 0; i < questionArray.length(); i++) {
                        JSONObject question = questionArray.getJSONObject(i);


                        String qid = question.optString("id");
                        String Q = question.optString("question");
                        String requirePhoto = question.optString("requirePhoto");
                        String requireVideo = question.optString("requireVideo");
                        String noOption = question.optString("noOption");
                        String option1 = question.optString("option1");
                        String option2 = question.optString("option2");
                        String option3 = question.optString("option3");
                        String option4 = question.optString("option4");
                        String requireText = question.optString("requireText");

                        int qId = Integer.parseInt(qid);


                        id++;
                        if (requireText.equals("yes")) {
                            addQuestionTextArea(id, Q);
                            if (requirePhoto.equals("yes")) addQuestionPhoto(id, "");
                            if (requireVideo.equals("yes")) addQuestionVideo(id, "");
                        } else {
                            ArrayList<String> s = new ArrayList<String>();
                            s.add(Q);
                            switch (noOption) {
                                case "1":
                                    s.add(option1);
                                    break;
                                case "2":
                                    s.add(option1);
                                    s.add(option2);
                                    break;
                                case "3":
                                    s.add(option1);
                                    s.add(option2);
                                    s.add(option3);
                                    break;
                                case "4":
                                    s.add(option1);
                                    s.add(option2);
                                    s.add(option3);
                                    s.add(option4);
                                    break;

                            }
                            addQuestionSingleAnswer(id, s);
                            if (requirePhoto.equals("yes")) addQuestionPhoto(id, "");
                            if (requireVideo.equals("yes")) addQuestionVideo(id, "");
                        }

                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void updateResultsForVideo(String fragmentTag, String s, String type){
        Log.d("Tag", fragmentTag + " " + s + " " + type);

        Fragment f = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        View view = f.getView();
        TextView tvQeus = (TextView)view.findViewById(R.id.questionText);
        Button cameraButton = (Button)view.findViewById(R.id.camera);
        Button videoButton = (Button)view.findViewById(R.id.gallery);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open camera
                Log.d("ag","clicked for camera video");
            }
        });
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open gallery
                Log.d("ag","clicked for gallery video");
            }
        });


        if(type.equals("q")){
            tvQeus.setText(s);
        }

        Log.d("type", type);

    }
    public void updateResultsForPhoto(String fragmentTag, String s, String type){
        Log.d("Tag", fragmentTag + " " + s + " " + type);

        Fragment f = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        View view = f.getView();
        TextView tvQeus = (TextView)view.findViewById(R.id.questionText);
        Button cameraButton = (Button)view.findViewById(R.id.camera);
        Button videoButton = (Button)view.findViewById(R.id.gallery);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open camera
                Log.d("ag","clicked for camera photo");
            }
        });
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open gallery
                Log.d("ag","clicked for gallery photo");
            }
        });

        if(type.equals("q")){
            tvQeus.setText(s);
        }
    }

    public void addQuestionVideo(int id, String question){

        // we will get these contents from server

        String ques = question; //"Take video of your farm?";

        ArrayList<String> options = new ArrayList<String >();
        options.add(VIDEO_TYPE);                 //type of question
        options.add(id + "video");                 //tag of fragment
        options.add(ques);                          //question


        videoTypeFragment = new PollQuestionFragmentVideo();

        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, videoTypeFragment, id + "video");
        Log.d("added frag tag", id + "video");
        transaction.commit();

        DoSomethingTask task = new DoSomethingTask();
        task.execute(options);


    }
    public void addQuestionPhoto(int id, String question){

        String ques = question; //"Take photo of the tallest tree in your village?";

        ArrayList<String> options = new ArrayList<String >();
        options.add(PHOTO_TYPE);                 //type of question
        options.add(id+"photo");                 //tag of fragment
        options.add(ques);                          //question


        photoTypeFragment = new PollQuestionFragmentPhoto();

        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, photoTypeFragment, id + "photo");
        Log.d("added frag tag", id+"photo");
        transaction.commit();

        DoSomethingTask task = new DoSomethingTask();
        task.execute(options);

    }



    public void addQuestionYesNo(int id, String question){
        String ques = question; //"Are you a Land Owner?";

        ArrayList<String> options = new ArrayList<String >();
        options.add(YES_NO_TYPE);                 //type of question
        options.add(id+"yes_no");                 //tag of fragment
        options.add(ques);                          //question


        yesNoTypeFragment = new PollQuestionFragmentYesNo();

        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, yesNoTypeFragment, id + "yes_no");
        Log.d("added frag tag", id+"yes_no");
        transaction.commit();

        DoSomethingTask task = new DoSomethingTask();
        task.execute(options);

    }



    public void addQuestionTextArea(int id, String question){
        String ques = question; //"What is the name of your village Mukhiya?";

        ArrayList<String> options = new ArrayList<String >();
        options.add(TEXT_TYPE);                 //type of question
        options.add(id + "texttypeanswer");                       //tag of fragment
        options.add(ques);                          //question


        textTypeFragment = new PollQuestionFragmentText();

        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, textTypeFragment, id+"texttypeanswer");
        Log.d("added frag tag", id + "texttypeanswer");
        transaction.commit();

        DoSomethingTask task = new DoSomethingTask();

        task.execute(options);

    }


    public void addQuestionSingleAnswer(int id, ArrayList<String> list){

        ArrayList<String> options = new ArrayList<String >();
        options.add(SINGLE_ANSWER_TYPE);                 //type of question
        options.add(id + "singleAnswer");                       //tag of fragment

        for(int j=0;j<list.size();j++){
            options.add(list.get(j));
        }

        singleAnswerFragment = new PollQuestionFragmentSingleAnswer();

        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, singleAnswerFragment, id+"singleAnswer");
        Log.d("added frag tag", id+"singleAnswer");
        transaction.commit();

        DoSomethingTask task = new DoSomethingTask();
        task.execute(options);


    }


    public void addQuestionCheckbox(int id, ArrayList<String> list){
        String ques = "Which are the most popular professions in your village?";

        ArrayList<String> options = new ArrayList<String >();
        options.add(CHECKBOX_TYPE);                 //type of question
        options.add(id+"checkboxFragment");                       //tag of fragment

        for(int j=0;j<list.size();j++){
            options.add(list.get(j));
        }

        checkboxFragment = new PollQuestionFragmentCheckbox();

        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, checkboxFragment, id+"checkboxFragment");
        Log.d("added frag tag", id+"checkboxFragment" );
        transaction.commit();

        DoSomethingTask task = new DoSomethingTask();
        task.execute(options);


    }

    public void updateResultsForCheckbox(String fragmentTag, String s, String type){
        Log.d("Tag", fragmentTag + " " + s + " " + type);

        Fragment f = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        View view = f.getView();
        LinearLayout ll = (LinearLayout)view.findViewById(R.id.container);
        TextView tvQeus = (TextView)view.findViewById(R.id.questionText);

        if(type.equals("q")){
            tvQeus.setText(s);
        }else if(type.equals("a")){
            Log.d("type",type);
            CheckBox cb = new CheckBox(this);
            cb.setText(s);
            ll.addView(cb);
        }
    }
    public void updateResultsForSingleAnswer(String fragmentTag, String s, String type){
        Log.d("Tag", fragmentTag+" "+s+" "+type);

        Fragment f = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        View view = f.getView();
        //LinearLayout ll = (LinearLayout)view.findViewById(R.id.container);
        TextView tvQeus = (TextView)view.findViewById(R.id.questionText);
        RadioGroup rg = (RadioGroup)view.findViewById(R.id.radioGroupView);

        if(type.equals("q")){
            tvQeus.setText(s);
        }else if(type.equals("a")){
            Log.d("type", type);
            RadioButton rb = new RadioButton(this);
            rg.addView(rb);
        }
    }

    public void updateResultsTextType(String fragmentTag, String s, String type){
        Log.d("Tag", fragmentTag + " " + s + " " + type);

        Fragment f = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        View view = f.getView();
        LinearLayout ll = (LinearLayout)view.findViewById(R.id.container);
        TextView tvQeus = (TextView)view.findViewById(R.id.questionText);

        if(type.equals("q")){
            tvQeus.setText(s);
        }
        EditText box = new EditText(this);
        box.setHint("Your Answer");
        ll.addView(box);
    }
    public void updateResultsForYesNoType(String fragmentTag, String s, String type){
        Log.d("Tag", fragmentTag + " " + s + " " + type);

        Fragment f = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        View view = f.getView();
        LinearLayout ll = (LinearLayout)view.findViewById(R.id.container);
        TextView tvQeus = (TextView)view.findViewById(R.id.questionText);

        if(type.equals("q")){
            tvQeus.setText(s);
        }

        Log.d("type", type);
        RadioButton rb1 = new RadioButton(this);
        rb1.setText("Yes");

        RadioButton rb2 = new RadioButton(this);
        rb2.setText("No");

        RadioGroup rg = new RadioGroup(this);
        rg.addView(rb1);
        rg.addView(rb2);

        ll.addView(rg);
    }

    public class DoSomethingTask extends AsyncTask<ArrayList<String>, String, String> {

        private static final String TAG = "DoSomethingTask";



        @Override
        protected void onProgressUpdate(String... values) {                 //question type, tag, question/answer, q/a
            Log.d("in progress","yo");
            Log.v(TAG, "reporting back from the Random Number Task");
            if(values[0] == CHECKBOX_TYPE){
                updateResultsForCheckbox(values[1], values[2], values[3]);
            }
            if(values[0] == SINGLE_ANSWER_TYPE){
                updateResultsForSingleAnswer(values[1],values[2],values[3]);
            }
            if(values[0] == TEXT_TYPE){
                updateResultsTextType(values[1], values[2], values[3]);
            }
            if(values[0]==YES_NO_TYPE){
                updateResultsForYesNoType(values[1],values[2],values[3]);
            }
            if(values[0] == PHOTO_TYPE){
                updateResultsForPhoto(values[1], values[2], values[3]);
            }
            if(values[0] == VIDEO_TYPE){
                updateResultsForVideo(values[1], values[2], values[3]);
            }
            super.onProgressUpdate(values);
        }


        @Override
        protected String doInBackground(ArrayList<String>... params) {
            publishProgress(params[0].get(0), params[0].get(1), params[0].get(2), "q");           //question type, tag, question , q/a
            for(int i=3;i<params[0].size();i++){
                publishProgress(params[0].get(0),params[0].get(1), params[0].get(i), "a");        //question type, tag, answer  , q/a
            }

            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("done","yo");
        }
    }


}
