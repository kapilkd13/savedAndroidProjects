package com.example.anurag.connect_net;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kapil on 30/6/16.
 */
public  class genericList<E,F> implements Parcelable {
    public  E first;
    public F second;
    public genericList(E s,F id)
    {
        this.first=s;
        this.second=id;
    }

    protected genericList(Parcel in) {
    }

    public static final Creator<genericList> CREATOR = new Creator<genericList>() {
        @Override
        public genericList createFromParcel(Parcel in) {
            return new genericList(in);
        }

        @Override
        public genericList[] newArray(int size) {
            return new genericList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}