package com.example.anurag.connect_net;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kapil on 7/7/16.
 */
public class PollLectureFragment  extends Fragment {
    TextView tv_title, tv_category, tv_timestamp, tv_text;
    ImageView img_1,img_2,img_3;
    VideoView video;
    String title,category,timestamp,text,FindInDevice;
    int noImg,noVideo,id;
    LinearLayout videoLayout,questionlayout;
    Button downloadVideo,playVideo,submitAnswer;
    View view;
    Context context;
    boolean isInstanceSaved=false;
    List< genericList<String,Integer>> questionlist ;
    android.support.v4.app.FragmentManager manager;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.poll_lecture_layout, container, false);
        manager = getChildFragmentManager();
        init();
        if(savedInstanceState!=null)
        {isInstanceSaved=true;
            Log.v("loading img2222", "ailaa");
            title=savedInstanceState.getString("title");
            text=savedInstanceState.getString("text");
            category=savedInstanceState.getString("category");
            timestamp=savedInstanceState.getString("timestamp");
            id=savedInstanceState.getInt("id");

        }

            //for creating question layout
            populateQuestionLayout(this.id);


        //set values to views
        setValues();


//submit func
        submitAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserResponse();
            }
        });





        Log.v("read lecture","inside read Lecture File");

        return view;
    }

    public void addDetails(int id,Context context){
        this.id=id;

        this.context=context;

    }
    public void init(){
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_category = (TextView) view.findViewById(R.id.tv_category);
        tv_timestamp = (TextView) view.findViewById(R.id.tv_timestamp);
        tv_text = (TextView) view.findViewById(R.id.tv_text);

        questionlayout=(LinearLayout)view.findViewById(R.id.question_layout);

        submitAnswer=(Button)view.findViewById(R.id.submit);
    }

    private  void  setValues()
    {
        tv_title.setText(title);
        tv_category.setText(category);
        tv_text.setText(text);
        tv_timestamp.setText(timestamp);

    }
    private void populateQuestionLayout(int id) {
        if ((new DownloadedFileAddress().checkFilePresence("LectureDetails.json", Constants.POLL, id, getContext()))) {
            readLectureFile(id);
        }
    }
//read individual Poll lecture file
    private void readLectureFile(int folderId) {
        Log.v("read lecture", "inside read Lecture File");
        File file = new File(new DownloadedFileAddress().getDownloadedFileAddress("LectureDetails.json", Constants.POLL, folderId, getContext()));
        try {if(file.exists()) {
            Log.v("read lecture","inside read Lecture File");
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
//this prepares json data from string
    private void prepareListData(int folderId, String jsonString) {
        LinearLayout questionTopLayout=(LinearLayout)view.findViewById(R.id.question_top_layout);
        questionTopLayout.setVisibility(View.VISIBLE);
        try {
            Log.v("read lecture",jsonString);
//creating json oblect
            JSONObject lectureObject = new JSONObject(jsonString);


            //extracting deetails from json object
            title=lectureObject.getString("topic");
            category=lectureObject.getString("category");
            timestamp=lectureObject.getString("timestamp");
            text=lectureObject.getString("text");

            //extracting question List
            JSONArray questionListArray=lectureObject.getJSONArray("questions_id");
            questionlist = new ArrayList<genericList<String,Integer>>();

            //traversing each question
            for (int j=0;j<questionListArray.length();j++) {
                JSONObject questionBox=(JSONObject)questionListArray.get(j);
                int questionId=questionBox.getInt("id");
                String question=questionBox.getString("question");
                String type=questionBox.getString("type");
                String numOptions=questionBox.getString("numOptions");

                //traversing each option of each question
                ArrayList<String> optionList=new ArrayList<>();
                for(int i=1;i<=Integer.parseInt( numOptions);i++) {String option="opt"+i;
                    optionList.add(questionBox.getString(option));}
                if(!  questionBox.isNull("correctOptions"))
                {   String correctOption=questionBox.getString("correctOptions");
                }
                //when taking user input save each fragment type and id from herer for sending response
                questionlist.add(new genericList<String, Integer>(type,questionId));
                Log.v("read lecture", "adding view");
                if("radio".equals(type))
                    inflateRadioQuestion(questionId, question, type, numOptions, optionList, Integer.toString(j+1));
                else if("checkbox".equals(type))
                    inflateCheckBoxQuestion(questionId, question, type, numOptions, optionList, Integer.toString(j+1));
                else if("textfield".equals(type))
                    inflateTextfieldQuestion(questionId, question, type,Integer.toString(j+1));
            }
            Log.v("read lecture", "adding view");
            questionlayout.setVisibility(View.VISIBLE);

            //  callLearningFragment(folderId, topic, category, "timestamp", text, numImg, numVideo);


        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }

    private void inflateTextfieldQuestion(int questionId, String question, String type,String TAG) {
        Log.v("read lecture", "adding  text view"+TAG);
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setId(R.id.textfield_question_id);
        if(!isInstanceSaved) {
            TextfieldQuestionFragment f = new TextfieldQuestionFragment();
            f.addDetails(questionId, question);

            android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
            //use this id to push this fragment to background

            transaction.add(ll.getId(), f, TAG);
            //    transaction.addToBackStack(null);
            transaction.commit();

            //usua
            manager.executePendingTransactions();
        }
        Log.v("read lecture", "adding view");
        questionlayout.addView(ll);

    }

    private void inflateCheckBoxQuestion(int questionId, String question, String type, String numOptions, ArrayList<String> optionList,String TAG) {
        Log.v("read lecture", "adding  check view" + TAG);
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setId(R.id.checkbox_question_id);
        if(!isInstanceSaved) {
            MCQCheckboxFragment f = new MCQCheckboxFragment();
            f.addDetails(questionId, question, optionList);

            android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
            //use this id to push this fragment to background

            transaction.add(ll.getId(), f, TAG);
            //    transaction.addToBackStack(null);
            transaction.commit();

            //usua
            manager.executePendingTransactions();}
        questionlayout.addView(ll);

    }

    private void inflateRadioQuestion(int questionId, String question, String type, String numOptions, ArrayList<String> optionList,String TAG) {
        Log.v("read lecture", "adding radio view"+TAG);
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setId(R.id.radio_question_id);
        if(!isInstanceSaved) {
            MCQRadioFragment f = new MCQRadioFragment();
            f.addDetails(questionId, question, optionList);

            android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
            //use this id to push this fragment to background

            transaction.add(ll.getId(), f, TAG);
            //    transaction.addToBackStack(null);
            transaction.commit();

            //usua
            manager.executePendingTransactions();
        }
        questionlayout.addView(ll);  }

    private int getScreenWidth()
    {
        return Resources.getSystem().getDisplayMetrics().widthPixels;

    }
    private String makeWebURL(int id,String filename)
    {
        return (Constants.SERVER_LEARN_DIRECTORY_PATH+Constants.PATH_DELIMETER+id+Constants.PATH_DELIMETER+filename);
    }




    private void createThumbnail()
    {Bitmap thumb;
        String localaddress=new DownloadedFileAddress().getDownloadedFileAddress(Constants.VIDEO_NAME, Constants.LEARN, id, getContext());
        if(localaddress!=null)
        {
            thumb= ThumbnailUtils.createVideoThumbnail(localaddress, MediaStore.Images.Thumbnails.MINI_KIND);
        }
        else
            thumb= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.videoplaceholder);

        video.setBackground(new BitmapDrawable(thumb));
    }
    //call back func by DownloadLearnIndexFile async task
    public void afterphpCall()
    {
        //todo
    }

    public  void getUserResponse()
    {int size=questionlist.size();
        JSONObject responseObject=new JSONObject();
        JSONArray answerArray=new JSONArray();
        try {
            responseObject.put("LectureId",id);

            for(int i=1;i<=size;i++)
            {

                JSONObject answer=new JSONObject();
                String userAnswer="";//default
                String type=questionlist.get(i-1).first;
                int questionid=questionlist.get(i-1).second;
                Fragment f=   manager.findFragmentByTag(Integer.toString(i));
                Log.v("type",type);
                if("radio".equals(type))
                    userAnswer=((MCQRadioFragment)f).getSelectedRadio();
                else if("checkbox".equals(type))
                    userAnswer=((MCQCheckboxFragment)f).getSelectedCheckbox();
                else if("textfield".equals(type))
                    userAnswer=((TextfieldQuestionFragment)f).getAnswer();

                answer.put("questionId",questionid);
                answer.put("type",type);

                answer.put("answer",userAnswer);

                answerArray.put(answer);
            }
            responseObject.put("answerArray",answerArray);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        String responseString=responseObject.toString();
        //for php json decoding which consider anything even json object as an array stupid!!
     /*   if(responseString.startsWith("{")&&responseString.endsWith("}"))
     responseString=   responseString.substring(1,responseString.length()-1);*/
        responseString="["+responseString+"]";
        Log.v("answer", "answer" + responseString);
        sendUserResponse(responseString);
    }
    //sending response using Async Task
    private void sendUserResponse(String response){
        ArrayList<String > responseArray=new ArrayList<>();
        responseArray.add(new UserInfo(getContext()).getusermail());
        responseArray.add(response);

        new SendPollResponse(this).execute(responseArray);

    }
    //AsyncTask Callback func
    public void afterResponseSentCallback(boolean isResponseSent) {
        if(isResponseSent)
            Log.v("callback","value sent");
            // Toast.makeText(,"Answer Submitted Successfully",Toast.LENGTH_LONG);
        else
            Log.v("callback","value not sent");
        //     Toast.makeText(getContext(),"check Internet connection and try again",Toast.LENGTH_LONG);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", title);
        outState.putString("timestamp",timestamp);
        outState.putString("text", text);
        outState.putString("category", category);

        outState.putInt("id",id);

    }
}
