package com.cyut.toolbox.toolbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class forgotPassword extends AppCompatActivity {
    String email;
    String identify;
    String type = "check_email_match_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Spinner spinner=(Spinner)findViewById(R.id.spinner_login);
        ArrayAdapter<CharSequence> loginList = ArrayAdapter.createFromResource(forgotPassword.this,
                R.array.login_option,
                R.layout.spinner_item);
        spinner.setAdapter(loginList);

    }

    public void onClick(View view) {
        email = ((EditText) findViewById(R.id.email)).getText().toString();
        identify = ((EditText) findViewById(R.id.identify)).getText().toString();
        Backgorundwork backgorundwork = new Backgorundwork(this);
        backgorundwork.execute(type,email,identify);
    }
}
