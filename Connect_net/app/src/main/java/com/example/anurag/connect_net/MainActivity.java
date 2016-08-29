package com.example.anurag.connect_net;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    TextView userName;
    TextView userEmail;
    private  GoogleApiClient googleApiClient;
    private SharedPreferences sp;
    private ImageView imageView;
    private SharedPreferences.Editor EPref;
    TabLayout.Tab timeline,faq,learning,poll;
public static  String currentTab="Timeline";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API).build();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
       timeline=tabLayout.newTab().setText("Timeline");

        learning=tabLayout.newTab().setText("Learn");

        poll=tabLayout.newTab().setText("Poll");

        faq=tabLayout.newTab().setText("FAQ");

        tabLayout.addTab(timeline);
        tabLayout.addTab(learning);
        tabLayout.addTab(poll);
        tabLayout.addTab(faq);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
getSupportActionBar().setElevation(0);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
if(tab==timeline)
    currentTab="Timeline";
else if(tab==learning)
    currentTab="Learn";
else if(tab==poll)
    currentTab="Poll";
else if(tab==faq)
    currentTab="FAQ";
                else
    currentTab="Timeline";
                Log.v("currenttab",currentTab);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //code for icon on bottom right corner
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Snackbar.make(view, "We will use it for 'write us section'", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent=new Intent(MainActivity.this,writeToUs.class);
                startActivity(intent);
            }
        });
        //side menu things
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        userEmail = (TextView) headerView.findViewById(R.id.userEmail);
        userName = (TextView) headerView.findViewById(R.id.userName);
        imageView = (ImageView) headerView.findViewById(R.id.imageView);

        sp = getSharedPreferences(Constants.SHARED_PREFERENCE_LOGIN_FILE, 0);
        EPref = sp.edit();


        userName.setText(sp.getString("name", ""));
        userEmail.setText(sp.getString("email", ""));

        /*URL img_value = null;
        img_value = new URL("http://graph.facebook.com/"+id+"/picture?type=large");
        Bitmap mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
        userpicture.setImageBitmap(mIcon1);*/
        String imageLink = "";
        URL img_value;
        switch (sp.getString("type", "google")) {
            case "google":
                imageLink = sp.getString("profilePicUri", "");
                Log.d("imagelink",imageLink);
                if (!imageLink.equals("")) {
                    Picasso.with(this)
                            .load(imageLink)
                            .skipMemoryCache()
                            .into(imageView);
                }
                break;
            case "facebook":
                String id = sp.getString("id", "");
                try {
                    img_value = new URL("http://graph.facebook.com/" + id + "/picture?type=square");
                    imageLink = img_value.toString();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }

                 Profile   profile = Profile.getCurrentProfile();
                if(profile!=null) {
                    Picasso.with(this)
                            .load(profile.getProfilePictureUri(200, 200).toString())
                            .skipMemoryCache()
                            .into(imageView);
                }

                break;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.refreshicon,menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    { Log.v("refresh called ", "refresh called");
        return false;

       /* switch (item.getItemId())
        {
            case R.id.imgrefresh:


                break;
            default:
                Log.v("refresh not called ","refresh not called");

        }
        return true;*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if((currentTab=="Learn")&&(Learning.showinglecture))
        {
            Learning.popupLastFragment();
        }
        else if((currentTab=="FAQ")&&(Faqs.showingfaqlecture))
        {
            Faqs.popupLastFragment();
        }
        else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if(id==R.id.nav_logout){

            switch (sp.getString("type", "google")) {
                case "google":
                    Glogout();
                    break;
                case "facebook":
                    Flogout();
                    break;
            }
            MyApplication.getInstance(). clearApplicationData();
            EPref.clear().commit();
            Log.d("logout","logout");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void Flogout() {
        try {
            LoginManager.getInstance().logOut();
        }finally {
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

    }
    private void Glogout() {
     //   GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
       if(googleApiClient.isConnected())
       {
           Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                   new ResultCallback<Status>() {
                       @Override
                       public void onResult(Status status) {

                           startActivity(new Intent(getApplicationContext(), Login.class));

                       }
                   });

       }

      //  finish();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("connection ","connection error");
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d("connection ","connected");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
