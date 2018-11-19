package com.cyut.toolbox.toolbox;

import android.Manifest;
import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;


import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cyut.toolbox.toolbox.Fragment.CollectionFragment;
import com.cyut.toolbox.toolbox.Fragment.InforFragment;
import com.cyut.toolbox.toolbox.Fragment.MainFragment;
import com.cyut.toolbox.toolbox.Fragment.PostFragment;
import com.cyut.toolbox.toolbox.Fragment.RatingFragment;
import com.cyut.toolbox.toolbox.Fragment.ReportFragment;
import com.cyut.toolbox.toolbox.Fragment.aboutFragment;
import com.cyut.toolbox.toolbox.connection.Backgorundwork;
import com.cyut.toolbox.toolbox.model.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;





import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    private String uid;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private  Toolbar toolbar;
    private static Boolean isExit = false;
    private static Boolean hasTask = false;
    public static final String KEY = "STATUS";
    Timer timerExit = new Timer();
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            isExit = false;
            hasTask = true;
        }
    };
    //按兩次Back退出app
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判斷是否按下Back
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 是否要退出
            if(!isExit ) {
                isExit = true; //記錄下一次要退出
                Toast.makeText(this, "再按一次Back退出APP"
                        , Toast.LENGTH_SHORT).show();
                // 如果超過兩秒則恢復預設值
                if(!hasTask) {
                    timerExit.schedule(task, 2000);
                }
            } else {
                finish(); // 離開程式
                System.exit(0);
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        verifyStoragePermissions(this);

        //處理更新檔
        UpdateManager Umanager = new UpdateManager(MainActivity.this);
        Umanager.checkUpdate();


        SharedPreferences sharedPreferences = getSharedPreferences(KEY, MODE_PRIVATE);
        uid=sharedPreferences.getString("uid",null);




        //set Nav information
        if (uid!=null){
            LoadUser(uid);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        NestedScrollView nsv = (NestedScrollView)findViewById(R.id.fragment_container);


        MainFragment fragment=new MainFragment();
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_container,fragment).commit();


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        String type = "update_user";
                        Backgorundwork backgorundwork = new Backgorundwork(MainActivity.this);
                        backgorundwork.execute(type,token,uid);
                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });



        //如果在特定Fragment FAB不顯示
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    fab.hide();
                } else {
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    if (currentFragment instanceof MainFragment) {
                        Log.d(TAG, "In Main");
                        fab.show();
                    }else if (currentFragment instanceof InforFragment){
                        Log.d(TAG, "In Infor");
                        fab.show();
                    }

                }
            }
        });

        /*Qiscus.getChatConfig()
                .setEnableFcmPushNotification(true)
                .setOnlyEnablePushNotificationOutsideChatRoom(true)
                .setStatusBarColor(R.color.colorNavHead)
                .setAppBarColor(R.color.primaryColor)
                .setTitleColor(R.color.primaryTextColor)
                .setRightBubbleTextColor(R.color.primaryTextColor);*/





        navigationView.setNavigationItemSelectedListener(this);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void hideViews() {
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(1));
        if (toolbar.getParent() instanceof AppBarLayout){
            ((AppBarLayout)toolbar.getParent()).setExpanded(false,true);
        }
    }

    public void showViews() {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(1));
        if (toolbar.getParent() instanceof AppBarLayout) {
            ((AppBarLayout) toolbar.getParent()).setExpanded(true, true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            MainFragment fragment=new MainFragment();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container,fragment).commit();
        } else if (id == R.id.nav_infor) {
            InforFragment fragment=new InforFragment();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container,fragment).commit();
        } else if (id == R.id.nav_collection) {
            CollectionFragment fragment=new CollectionFragment();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container,fragment).commit();
        } else if (id == R.id.nav_post) {
            PostFragment fragment = new PostFragment();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container,fragment).commit();
        } else if (id == R.id.nav_rating) {
            RatingFragment fragment = new RatingFragment();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container,fragment).commit();
        }  else if (id == R.id.nav_report) {
            ReportFragment fragment = new ReportFragment();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container,fragment).commit();
        } else if (id == R.id.nav_about) {
            aboutFragment fragment=new aboutFragment();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container,fragment).commit();
        } else if (id == R.id.nav_share) {
            shareTo("分享測試","https://drive.google.com/open?id=0B3BqbzgR0hXPMllJa0VVc1hPM2s","選擇要分享的軟體");
        }else if (id == R.id.nav_suggest) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/forms/WbSE9qmsQl654TLS2"));
            startActivity(browserIntent);
        } else if (id== R.id.nav_signout){
            SharedPreferences sharedPreferences = this.getSharedPreferences(KEY , MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("Status" , false).apply();
            Intent toLogin=new Intent(this,LoginActivity.class);
            startActivity(toLogin);
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareTo(String subject, String body, String chooserTitle) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(sharingIntent, chooserTitle));
    }

    public void LoadUser(final String uid){
        String url ="http://163.17.5.182/app/loaduser.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response:",response);
                        try {
                            byte[] u = response.getBytes(
                                    "UTF-8");
                            response = new String(u, "UTF-8");
                            Log.d(TAG, "Response " + response);
                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();
                            List<Item> posts = new ArrayList<Item>();
                            posts = Arrays.asList(mGson.fromJson(response, Item[].class));
                            List<Item> itemList=posts;

                            //讀取NAV資料
                            NavigationView navigationView = findViewById(R.id.nav_view);
                            View hView =navigationView.getHeaderView(0);
                            final TextView TextNickname=hView.findViewById(R.id.nick_name);
                            final TextView TextName=hView.findViewById(R.id.nav_name);
                            final TextView TextMail=hView.findViewById(R.id.mail);
                            final ImageView iv=hView.findViewById(R.id.hIV) ;
                            TextName.setText(itemList.get(0).getName());
                            TextNickname.setText(itemList.get(0).getNickname());
                            TextMail.setText(itemList.get(0).getMail());

                            Picasso.get().load("https://imgur.com/"+itemList.get(0).getImage()+".jpg").fit().centerInside().into(iv);

                            iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    InforFragment fragment=new InforFragment();
                                    FragmentManager manager=getSupportFragmentManager();
                                    manager.beginTransaction().replace(R.id.fragment_container,fragment).commit();
                                }
                            });

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(itemList.get(0).getName())
                                        .setPhotoUri(Uri.parse("https://imgur.com/"+itemList.get(0).getImage()+".jpg"))
                                        .build();
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User profile updated.");
                                                }
                                            }
                                        });
                            }




                            /*Qiscus.setUser(itemList.get(0).getMail(),itemList.get(0).getPwd())
                                    .withUsername(itemList.get(0).getName())
                                    .withAvatarUrl("https://imgur.com/"+itemList.get(0).getImage()+".jpg")
                                    .save()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(qiscusAccount  -> {
                                        Log.d(TAG,"Login Account:"+qiscusAccount);

                                    }, throwable -> {
                                        Log.d(TAG,"Qiscus Error:"+throwable);

                                    });*/


                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //do stuffs with response erroe
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("uid",uid);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
