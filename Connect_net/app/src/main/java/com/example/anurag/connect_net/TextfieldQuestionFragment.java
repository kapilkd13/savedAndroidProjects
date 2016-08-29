package com.example.anurag.connect_net;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kapil on 5/7/16.
 */
public class TextfieldQuestionFragment  extends Fragment {
    View view;
    private String question,answerString;

    int questionId;
    TextView questionView;
    EditText answer;
    private  int optionNum=0;
    public  void addDetails(int questionId,String question)
    {this.question=question;
this.questionId=questionId;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.textfield_question_fragment, container,false);

        if(savedInstanceState!=null)
        {
            question=savedInstanceState.getString("question");
            questionId=savedInstanceState.getInt("questionId");
            answerString=savedInstanceState.getString("answerString");

        }

        initializeViews();
        populateViews();
        return view;


    }

    private void initializeViews()
    {questionView=(TextView)view.findViewById(R.id.question);
       answer=(EditText)view.findViewById(R.id.answer);
    if(answerString!=null)
    answer.setText(answerString);
    }
    private  void populateViews()
    {

        questionView.setText(questionView.getText()+question);

    }

    public String getAnswer()
    {return(answer.getText().toString());}


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("question",question);
        outState.putInt("questionId",questionId);
        outState.putString("answer",answerString);
    }
}


