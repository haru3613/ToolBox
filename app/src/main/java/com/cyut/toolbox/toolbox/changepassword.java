package com.cyut.toolbox.toolbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cyut.toolbox.toolbox.connection.Backgorundwork;

public class changepassword extends AppCompatActivity {
    String u_email;
    String psw;
    String psw2;
    TextView view_err;
    String type = "changepsw";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        Bundle bundle = getIntent().getExtras();
        u_email = bundle.getString("u_email");
        view_err= findViewById(R.id.view_err);
        view_err.setText("");
    }

    public void resetpsw(View view) {
        psw = ((EditText) findViewById(R.id.psw)).getText().toString();
        psw2 = ((EditText) findViewById(R.id.psw2)).getText().toString();

        if(psw.length() > 5){
            if (psw.equals(psw2)) {
                Backgorundwork backgorundwork = new Backgorundwork(this);
                backgorundwork.execute(type,u_email,psw,psw2);
            }else{
                view_err.setText("檢查一下兩次輸入的密碼需相同唷");
            }
        }else{
            view_err.setText("密碼需要大於(含)6個字唷");

        }
    }
    }

