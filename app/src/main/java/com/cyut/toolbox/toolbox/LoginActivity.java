package com.cyut.toolbox.toolbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.qiscus.sdk.Qiscus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.HttpException;

public class LoginActivity extends AppCompatActivity {
    private static Boolean isExit = false;
    private static Boolean hasTask = false;
    private static final String ACTIVITY_TAG ="Logwrite";
    public static final String KEY = "STATUS";
    String email;
    String pwd;
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
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Button Login=(Button)findViewById(R.id.Login);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        }
        );

        final Button SignUp=(Button)findViewById(R.id.SignUp);

        //spinner option
        Spinner spinner=(Spinner)findViewById(R.id.spinner_login);

        ArrayAdapter<CharSequence> loginList = ArrayAdapter.createFromResource(LoginActivity.this,
                R.array.login_option,
                R.layout.spinner_item);
        spinner.setAdapter(loginList);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ToSignUp = new Intent(LoginActivity.this , SignUpActivity.class);
                startActivity(ToSignUp);
            }
        });



        //判斷使用者是否第一次登入

        SharedPreferences sharedPreferences = getSharedPreferences(KEY, MODE_PRIVATE);
        Boolean FirstLogin=sharedPreferences.getBoolean("Status",false);
        Log.d("FirstLogin?",FirstLogin.toString());
        if (FirstLogin){
            Intent toLoadView=new Intent(LoginActivity.this,LoadingView.class);
            LoginActivity.this.startActivity(toLoadView);
            finish();
        }
    }



    //Login帳號偵錯
    public void Login() {
        email = ((EditText) findViewById(R.id.email)).getText().toString();
        pwd = ((EditText) findViewById(R.id.password)).getText().toString();
        Log.d("AUTH", email + "/" + pwd);
        if (pwd.length() > 5) {
                String type = "login";
                Backgorundwork backgorundwork = new Backgorundwork(this);
                backgorundwork.execute(type,email,pwd);
                //傳至後台處理

            } else {
                nullAlertDialog(getResources().getString(R.string.pwd_short_title), getResources().getString(R.string.pwd_short), "OK");
            }
    }


    //呼叫回傳值為null的AlertDialog
    public void nullAlertDialog(String T, String M, String PB) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(LoginActivity.this);
        builder.title(T);
        builder.content(M);
        builder.positiveText(PB);

        builder.show();

    }


    public void bt_forgotpsw(View view) {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this , forgotPassword.class);
        startActivity(intent);

    }
}
