package com.cyut.toolbox.toolbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class UseState extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_state);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView UseS_Title=(TextView)findViewById(R.id.UseS);
        UseS_Title.setText(getResources().getString(R.string.user_statement_title));

        TextView UseStatement=(TextView)findViewById(R.id.U_text);
        UseStatement.setText(getResources().getString(R.string.user_statement));


    }

}
