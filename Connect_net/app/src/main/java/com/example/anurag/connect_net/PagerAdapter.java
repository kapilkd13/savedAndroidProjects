package com.example.anurag.connect_net;

/**
 * Created by sarfraz on 27-05-2016.
 */
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.ImageButton;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Context context;
    public PagerAdapter(Context context,FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Timeline tab1 = new Timeline();
                return tab1;
            case 1:

                Learning tab2 = new Learning();
                return tab2;
            case 2:
                Polls tab3 = new Polls();
                return tab3;
            case 3:
                Faqs tab4 = new Faqs();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
