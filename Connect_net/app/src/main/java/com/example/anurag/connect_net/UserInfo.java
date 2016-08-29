package com.example.anurag.connect_net;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

/**
 * Created by kapil on 18/6/16.
 */
public class UserInfo {
    private SharedPreferences sp;
    private SharedPreferences.Editor EPref;
    private Context context;
    private boolean calledFromFragment=false;
    public UserInfo(Context context)
    {
        this.context=context;
     sp=   context. getSharedPreferences(Constants.SHARED_PREFERENCE_LOGIN_FILE, 0);

        EPref=sp.edit();
    }

    public String getuserame()
    {return sp.getString("name","");}
    public String getusermail()
    {return sp.getString("email","");}
    public boolean isuserRegistered()
    {return sp.getBoolean("registeredUser",false);}
}
