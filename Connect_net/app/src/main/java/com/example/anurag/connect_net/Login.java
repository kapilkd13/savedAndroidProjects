package com.example.anurag.connect_net;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private SignInButton login;
    private LoginButton fbloginbutton;
    private TextView name;
    private GoogleApiClient googleApiClient;
    private static final int REQUEST_CODE = 100;
    private GoogleSignInOptions signInOptions;
    private Button logout;
    private SharedPreferences sp;
    private SharedPreferences.Editor EPref;
    private String email;
private  LinearLayout linearMain;

    private static ArrayList<String> neededPerms;
    Loading l;
    //add all permissions required at runtime for android 23 ND Above
    private static final  String[] perms= {"android.permission.READ_EXTERNAL_STORAGE","android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE","android.permission.RECORD_AUDIO"};
    private  static final   int permsRequestCode = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        l=new Loading(this);
        init();

        askPermissions();

        new PreSettings(getApplicationContext());
        email=sp.getString("email","");

        if((!email.equals(""))&&(sp.getBoolean("registeredUser",false))){
            startActivity(new Intent("android.intent.action.MainActivity"));
            l.stopProgressCircle();
            finish();
        }


      //  l.startProgressCircle();

        FacebookSdk.sdkInitialize(getApplicationContext());
        Log.v("reach","dfdf");
        setContentView(R.layout.login);

        linearMain=(LinearLayout)findViewById(R.id.loginMain);
        LinearLayout ll = new LinearLayout(getApplicationContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setId(R.id.layout);

        LoginFragment f = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(ll.getId(), f, "id");
        transaction.commit();

        linearMain.addView(ll);
        fbloginbutton=(LoginButton)findViewById(R.id.login_button);
     //   fbloginbutton.setFragment(new LoginFragment());
        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();

        login = (SignInButton)findViewById(R.id.login);
        login.setSize(SignInButton.SIZE_WIDE);
        login.setScopes(signInOptions.getScopeArray());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, REQUEST_CODE);
            }
        });



    }


    private boolean needRuntimePerm(){

        return(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1);

    }
    @TargetApi(23)
    private void askPermissions() {
        neededPerms=new ArrayList<>();
        if (needRuntimePerm())//save time
        {
            for (String perm : perms) {
                if (!hasPermission(perm) && (shouldWeAsk(perm))) {
                    neededPerms.add(perm);
                }
                if(!neededPerms.isEmpty())
                    requestPermissions(neededPerms.toArray(new String[neededPerms.size()]), permsRequestCode);

            }

        }
    }
    @TargetApi(23)
    private boolean hasPermission(String permission){

        if(needRuntimePerm()){

            return(checkSelfPermission(permission)== PackageManager.PERMISSION_GRANTED);

        }

        return true;

    }

    private boolean shouldWeAsk(String permission){

        return (sp.getBoolean(permission, true));

    }



    private void markAsAsked(String permission){

        EPref.putBoolean(permission, false);
        EPref.apply();

    }

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:
                for(int i=0;i<grantResults.length;i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                        EPref.putBoolean(permissions[i],false);

                }
                break;
        }
    }

    private void init() {
        sp = getSharedPreferences(Constants.SHARED_PREFERENCE_LOGIN_FILE, 0);
        EPref = sp.edit();


    }
    public  void afterUserRegCall()
    {
    //    Toast.makeText(getApplicationContext(), " User registered!!! ", Toast.LENGTH_LONG).show();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();

           // name.setText(account.getDisplayName());


            if(account!=null) {
                new PreSettings(getApplicationContext());
                init();
                EPref.putString("email", account.getEmail());
                EPref.putString("name", account.getDisplayName());
                EPref.putString("loginvia", "google");
                EPref.putString("type", "google");
                Uri x = account.getPhotoUrl();

                if (x != null)
                    EPref.putString("profilePicUri", x.toString());
                if(INTERNET.isInternetOn(getApplicationContext()))
new UserRegisteration(Login.this,1).execute();


                EPref.commit();
                email = sp.getString("email", "");
                if (!email.equals("")) {
                    startActivity(new Intent("android.intent.action.MainActivity"));
                    finish();
                }
                finish();
            }
                else {
                    Toast.makeText(getApplicationContext(), " Try again ", Toast.LENGTH_LONG).show();
                }


            }


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
       // finish();
    }
}
