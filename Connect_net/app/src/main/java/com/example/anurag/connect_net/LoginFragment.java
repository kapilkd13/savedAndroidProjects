package com.example.anurag.connect_net;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LoginFragment extends Fragment {
    private CallbackManager callbackManager = null;
    private AccessTokenTracker mtracker = null;
    private ProfileTracker mprofileTracker = null;
    private SharedPreferences sp;
    private SharedPreferences.Editor EPref;

    public static final String PARCEL_KEY = "parcel_key";

    private LoginButton loginButton;
    private  TextView tv;
//    webView.getSettings().setDomStorageEnabled(true);

    public  void afterUserRegCall()
    {
        //    Toast.makeText(getApplicationContext(), " User registered!!! ", Toast.LENGTH_LONG).show();

    }

    FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            Log.v("Email=jjjjjjjjjjjj", "" );
           // EPref.putString("email",profile.get);
           // EPref.putString("name",profile.getName());


            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
//Log.d("profilepic",profile.getProfilePictureUri(200,200).toString());
            // Facebook Email address

            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        public String Name,FEmail,Id;

                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            Log.v("LoginActivity Response ", response.toString());
                            Log.d("LoginActivity Response ", response.toString());


                            try {
                                Name = object.getString("name");
                                Id=object.getString("id");
                                FEmail = object.getString("email");
                                Log.v("Email = ", " " + FEmail);
                                Log.d("Id = ", " " + Id);
                                Toast.makeText(getApplicationContext(), "Name " + Name, Toast.LENGTH_LONG).show();
                                EPref.putString("email", FEmail);
                                EPref.putString("loginvia","facebook");
                                EPref.putString("name",Name);
                                EPref.putString("fbid", Id);
                                EPref.putString("type", "facebook");
                                EPref.commit();
                               new UserRegisteration(LoginFragment.this,5).execute();
                           //     startActivity(new Intent("android.intent.action.MainActivity"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
            Log.v("here ook", "yes");
            startActivity(new Intent("android.intent.action.MainActivity"));
          //  startActivity(new Intent(getContext(), MainActivity.class));
        }



        @Override
        public void onCancel() {
            Log.v("oncancel","yes");
        }

        @Override
        public void onError(FacebookException error) {
            Log.v("onerror","yes");
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

        sp = getContext().getSharedPreferences(Constants.SHARED_PREFERENCE_LOGIN_FILE, 0);
        EPref = sp.edit();

       callbackManager = CallbackManager.Factory.create();

     //   FacebookSdk.sdkInitialize();
        mtracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                Log.v("AccessTokenTracker", "oldAccessToken=" + oldAccessToken + "||" + "CurrentAccessToken" + currentAccessToken);
            }
        };


        mprofileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                Log.v("Session Tracker", "oldProfile=" + oldProfile + "||" + "currentProfile" + currentProfile);
                homeFragment(currentProfile);

            }
        };


        mtracker.startTracking();
        mprofileTracker.startTracking();
    }


    private void homeFragment(Profile profile) {

        if (profile != null) {
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(PARCEL_KEY, profile);
            HomeFragment hf = new HomeFragment();
            hf.setArguments(mBundle);

            if(getActivity()!=null) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainContainer, new HomeFragment());
                fragmentTransaction.commit();
            }

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        return  view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        //loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_friends"));

        // If using in a fragment
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        mtracker.stopTracking();
        mprofileTracker.stopTracking();
    }


    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isLoggedIn()) {
            loginButton.setVisibility(View.INVISIBLE);
            Profile profile = Profile.getCurrentProfile();
            homeFragment(profile);
        }

    }
}
