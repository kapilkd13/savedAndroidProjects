package com.example.anurag.connect_net;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kapil on 5/7/16.
 */
public class MCQCheckboxFragment extends Fragment {
    View view;
    private String question,opt1,opt2,opt3,opt4,opt5,selectedCheckbox;
    TextView questionView;
    int questionId;
    CheckBox c1,c2,c3,c4,c5;
    private  int optionNum=0;
    ArrayList<String> optionArray;
    public  void addDetails(int questionId,String question,ArrayList<String> optionArray)
    {this.question=question;
        optionNum=optionArray.size();
        this.questionId=questionId;
        this.optionArray=optionArray;

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mcq_checkbox_fragment, container,false);

        //reassign values when saavedInstancestate is not null
        if(savedInstanceState!=null)
        {
            question=savedInstanceState.getString("question");
            questionId=savedInstanceState.getInt("questionId");
            optionArray=savedInstanceState.getStringArrayList("optionArray");
            selectedCheckbox=savedInstanceState.getString("selectedCheckbox");
            optionNum=savedInstanceState.getInt("optionNum");
        }
        Log.v("check","view called");
        initializeViews();
        populateViews();
        setAnswerCheckbox();
        return view;


    }

    private void initializeViews()
    {questionView=(TextView)view.findViewById(R.id.question);
        c1=(CheckBox)view.findViewById(R.id.c1);
        c2=(CheckBox)view.findViewById(R.id.c2);
        c3=(CheckBox)view.findViewById(R.id.c3);
        c4=(CheckBox)view.findViewById(R.id.c4);
        c5=(CheckBox)view.findViewById(R.id.c5);

    }
    private  void populateViews()
    {
        switch (optionNum)
        {
            case 5:opt5=optionArray.get(4);
                c5.setVisibility(View.VISIBLE);
            case 4:opt4=optionArray.get(3);
                c4.setVisibility(View.VISIBLE);
            case 3:opt3=optionArray.get(2);
                c3.setVisibility(View.VISIBLE);
            case 2:opt2=optionArray.get(1);
                c2.setVisibility(View.VISIBLE);
            case 1:opt1=optionArray.get(0);
                c1.setVisibility(View.VISIBLE);
            default:break;
        }

        c1.setText(opt1);
        c2.setText(opt2);
        c3.setText(opt3);
        c4.setText(opt4);
        c5.setText(opt5);
        questionView.setText(questionView.getText()+question);

    }

    private  void setAnswerCheckbox()
    {if(selectedCheckbox!=null){
        String[] answerarray=selectedCheckbox.split(",");
        for (String checkbox:answerarray) {
            switch(checkbox) {
                case "5":c5.setEnabled(true);
                    break;
                case "4":c4.setEnabled(true);
                    break;
                case "3":c3.setEnabled(true);
                    break;
                case "2":c2.setEnabled(true);
                    break;
                case "1":c1.setEnabled(true);
                    break;
                default:break;
            }

        }

    }
    }

    public  String getSelectedCheckbox()
    {//default means none selected
        String result="";
        if(c1.isChecked())
         result= result.equals("")?"1":result+",1";
          if(c2.isChecked())
              result= result.equals("")?"2":result+",2";
         if(c3.isChecked())
             result= result.equals("")?"3":result+",3";
         if(c4.isChecked())
             result= result.equals("")?"4":result+",4";
         if(c5.isChecked())
             result= result.equals("")?"5":result+",5";
        return result;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("question", question);
        outState.putInt("questionId", questionId);
        outState.putInt("optionNum",optionNum);
        outState.putStringArrayList("optionArray", optionArray);
        outState.putString("selectedCheckbox",getSelectedCheckbox());
    }
}


