package com.cyut.toolbox.toolbox;

/**
 * Created by Haru on 2018/1/7.
 */

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyut.toolbox.toolbox.Fragment.InforFragment;
import com.cyut.toolbox.toolbox.Fragment.MainFragment;

import static android.content.ContentValues.TAG;

public class RecyclerViewHoldersQanda extends RecyclerView.ViewHolder{
    public ImageView q_headpic,a_headpic,q_message;
    public TextView q_nickname,a_nickname;
    public TextView q_ans,a_ans;
    public TextView time,a_time;


    public RecyclerViewHoldersQanda(View itemView) {
        super(itemView);
        q_ans=(TextView)itemView.findViewById(R.id.q_ans);
        q_headpic=(ImageView)itemView.findViewById(R.id.q_headpic);
        q_nickname=(TextView)itemView.findViewById(R.id.q_nickname);
        time=(TextView)itemView.findViewById(R.id.q_time);
        a_ans=(TextView)itemView.findViewById(R.id.a_ans);
        a_headpic=(ImageView)itemView.findViewById(R.id.a_headpic);
        a_nickname=(TextView)itemView.findViewById(R.id.a_nickname);
        a_time=(TextView)itemView.findViewById(R.id.a_time);
        q_message=itemView.findViewById(R.id.ans_this);
    }



}