package com.cyut.toolbox.toolbox;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cyut.toolbox.toolbox.connection.Backgorundwork;
import com.cyut.toolbox.toolbox.model.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {
    private static Boolean isExit = false;
    private static Boolean hasTask = false;
    private static final String ACTIVITY_TAG ="Logwrite";
    public static final String KEY = "STATUS";
    private String ServerUrl="http://35.194.171.235";
    private FirebaseAuth mAuth;
    public Button Login;
    private EditText username,password;
    String email;
    String pwd,uid;
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
        mAuth = FirebaseAuth.getInstance();

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            Toast.makeText(LoginActivity.this,"您的版本太低，請更新你的Android系統",Toast.LENGTH_SHORT).show();
            finish();
        }

       // Qiscus.init(getApplication(), "toolbox-mzj9nz7n85jfv");


        Login=(Button)findViewById(R.id.Login);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username=findViewById(R.id.email);
                email =  username.getText().toString().trim();
                password=findViewById(R.id.password);
                pwd = password.getText().toString().trim();
                createAccount(email,pwd);
                Log.d("AUTH", email + "/" + pwd);
                if (pwd.length() > 5) {
                    LoadUser(email);

                    String type = "login";
                    Login.setEnabled(false);
                    Backgorundwork backgorundwork = new Backgorundwork(LoginActivity.this);
                    backgorundwork.execute(type,email,pwd);
                    //傳至後台處理

                } else {
                    nullAlertDialog(getResources().getString(R.string.pwd_short_title), getResources().getString(R.string.pwd_short), "OK");
                }
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences sharedPreferences = getSharedPreferences(KEY, MODE_PRIVATE);
        Boolean FirstLogin=sharedPreferences.getBoolean("Status",false);
        Log.d("First Login?",FirstLogin.toString());
        if (FirstLogin && user!=null){
            Intent toLoadView=new Intent(LoginActivity.this,LoadingView.class);
            LoginActivity.this.startActivity(toLoadView);
            finish();
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

    //忘記密碼
    public void bt_forgotpsw(View view) {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this , forgotPassword.class);
        startActivity(intent);

    }
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);


        // [START create_user_with_email]
        mAuth.signInWithEmailAndPassword(email+"@gm.cyut.edu.tw", password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "loginUserWithEmail:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "loginUserWithEmail:failure", task.getException());

                        }

                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }
    //拿取uid
    public void LoadUser(final String mail){
        String url =ServerUrl+"/app/loaduser.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response:",response);
                        try {
                            byte[] u = response.getBytes(
                                    "UTF-8");
                            response = new String(u, "UTF-8");
                            Log.d(ContentValues.TAG, "Response " + response);
                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();
                            List<Item> posts = new ArrayList<Item>();
                            if (!response.contains("Undefined")) {
                                posts = Arrays.asList(mGson.fromJson(response, Item[].class));
                                List<Item> itemList=posts;
                                uid=itemList.get(0).getUid();
                                SharedPreferences sharedPreferences = getSharedPreferences(KEY , MODE_PRIVATE);
                                sharedPreferences.edit().putString("uid" , uid).apply();
                            }
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
                params.put("mail",mail+"@gm.cyut.edu.tw");

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }
}
