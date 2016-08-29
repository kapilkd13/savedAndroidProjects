package com.example.anurag.connect_net;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kapil on 4/7/16.
 */
public class MCQRadioFragment extends Fragment {
    View view;
private String question,opt1,opt2,opt3,opt4,opt5,selectedRadio;
    TextView questionView;
    RadioButton r1,r2,r3,r4,r5;
    private  int optionNum=0;
    ArrayList<String> optionArray;
    int questionId=0;
    public  void addDetails(int questionId,String question,ArrayList<String>optionArray)
    {this.question=question;
        this.questionId=questionId;
        optionNum=optionArray.size();
        this.optionArray=optionArray;



    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mcq_radio_fragment, container,false);

        if(savedInstanceState!=null)
        {
            question=savedInstanceState.getString("question");
            questionId=savedInstanceState.getInt("questionId");
            optionArray=savedInstanceState.getStringArrayList("optionArray");
            selectedRadio=savedInstanceState.getString("selectedRadio");
            optionNum=savedInstanceState.getInt("optionNum");
        }

         initializeViews();
         populateViews();
        setAnswerRadio();
        return view;


    }

    private void initializeViews()
    {questionView=(TextView)view.findViewById(R.id.question);
        r1=(RadioButton)view.findViewById(R.id.r1);
        r2=(RadioButton)view.findViewById(R.id.r2);
        r3=(RadioButton)view.findViewById(R.id.r3);
        r4=(RadioButton)view.findViewById(R.id.r4);
        r5=(RadioButton)view.findViewById(R.id.r5);

    }
    private  void populateViews()
    {  switch (optionNum)
    {
        case 5:opt5=optionArray.get(4);
            r5.setVisibility(View.VISIBLE);
        case 4:opt4=optionArray.get(3);
            r4.setVisibility(View.VISIBLE);
        case 3:opt3=optionArray.get(2);
            r3.setVisibility(View.VISIBLE);
        case 2:opt2=optionArray.get(1);
            r2.setVisibility(View.VISIBLE);
        case 1:opt1=optionArray.get(0);
            r1.setVisibility(View.VISIBLE);
        default:break;
    }
        r1.setText(opt1);
        r2.setText(opt2);
        r3.setText(opt3);
        r4.setText(opt4);
        r5.setText(opt5);
        questionView.setText(questionView.getText()+question);

    }
    private  void setAnswerRadio()
    {if(selectedRadio!=null){
        switch(selectedRadio)
        {
            case "5":r5.setEnabled(true);
                break;
            case "4":r4.setEnabled(true);
                break;
            case "3":r3.setEnabled(true);
                break;
            case "2":r2.setEnabled(true);
                break;
            case "1":r1.setEnabled(true);
                break;
            default:break;
        }
    }
    }
    public String getSelectedRadio()
    {
        if(r1.isChecked())
            return("1");
      else  if(r2.isChecked())
            return("2");
        else if(r3.isChecked())
            return("3");
        else if(r4.isChecked())
            return("4");
        else if(r5.isChecked())
            return("5");
        else
            return "";
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("question",question);
        outState.putInt("questionId",questionId);
        outState.putInt("optionNum",optionNum);
        outState.putStringArrayList("optionArray",optionArray);
        outState.putString("selectedRadio",getSelectedRadio());
    }
}



