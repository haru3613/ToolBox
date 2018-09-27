package com.cyut.toolbox.toolbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Haru on 2017/5/10.
 */

public class LoadingView extends Activity {
    private static final int SPLASH_SHOW_TIME=2000;
    String mail;
    AnimationDrawable rocketAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);



        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.loading);

        ImageView rocketImage = findViewById(R.id.animate_logo);
        rocketImage.setBackgroundResource(R.drawable.animate);
        rocketAnimation = (AnimationDrawable) rocketImage.getBackground();

        new BackgroundSplashTask().execute();

    }

    private class BackgroundSplashTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                rocketAnimation.start();
                Thread.sleep(SPLASH_SHOW_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

            //如果未連線的話，mNetworkInfo會等於null
            if(mNetworkInfo != null)
            {
                Intent i = new Intent(LoadingView.this, MainActivity.class);
                startActivity(i);
                finish();
            }else{
                MaterialDialog.Builder dialog = new MaterialDialog.Builder(LoadingView.this);
                dialog.title("沒有連上網路");
                dialog.content("請檢查連線狀態");
                dialog.positiveText("確定");
                dialog.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                       finish();
                    }
                });

                dialog.show();
            }

        }
    }
}
