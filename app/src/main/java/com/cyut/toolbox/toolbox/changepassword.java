package com.cyut.toolbox.toolbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class changepassword extends AppCompatActivity {
    String u_email;
    String psw;
    String psw2;
    String type = "changepsw";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        Bundle bundle = getIntent().getExtras();
        u_email = bundle.getString("u_email");

    }

    public void resetpsw(View view) {
        psw = ((EditText) findViewById(R.id.psw)).getText().toString();
        psw2 = ((EditText) findViewById(R.id.psw2)).getText().toString();
        Backgorundwork backgorundwork = new Backgorundwork(this);
        backgorundwork.execute(type,u_email,psw,psw2);
    }
}
