package com.example.anurag.connect_net;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sarfraz on 08-06-2016.
 */
public class PollQuestionFragmentVideo extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.poll_ques_frag_type_takevideo, container, false);
        return view;
    }
}
