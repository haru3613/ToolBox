package com.cyut.toolbox.toolbox;

/**
 * Created by Haru on 2018/1/7.
 */

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class RecyclerViewHoldersMyPost extends RecyclerView.ViewHolder{
    public ImageView CategoryImage, finished,unfinish,tomessage,view_rating,report;
    public TextView Title;
    public TextView Area;
    public TextView Status,progress_title,progress;
    public TextView message,tool_title,tool;
    public TextView Money,time,content,time_title,rating_title;
    public RecyclerView mypost_ryv;
    public RatingBar ratingBar;
    public LinearLayout linearLayout;
    public RecyclerViewHoldersMyPost(View itemView) {
        super(itemView);
        CategoryImage = (ImageView)itemView.findViewById(R.id.a_headpic);
        view_rating=itemView.findViewById(R.id.view_rating);
        finished = (ImageView)itemView.findViewById(R.id.card_finish);
        unfinish = (ImageView)itemView.findViewById(R.id.card_unfinish);
        Title = (TextView)itemView.findViewById(R.id.a_nickname);
        content = (TextView)itemView.findViewById(R.id.card_content);
        Area = (TextView)itemView.findViewById(R.id.a_ans);
        Status=(TextView)itemView.findViewById(R.id.a_time);
        time_title=(TextView)itemView.findViewById(R.id.card_time);
        rating_title=itemView.findViewById(R.id.card_rating_title);
        message=(TextView)itemView.findViewById(R.id.card_message);
        time=(TextView)itemView.findViewById(R.id.card_until);
        Money=itemView.findViewById(R.id.moneyTxt);
        tomessage=itemView.findViewById(R.id.card_tomessage);
        progress=itemView.findViewById(R.id.card_progress);
        progress_title=itemView.findViewById(R.id.card_progress_title);
        tool=itemView.findViewById(R.id.card_tool);
        tool_title=itemView.findViewById(R.id.card_tool_title);
        mypost_ryv=itemView.findViewById(R.id.recycler_mypost);
        ratingBar=itemView.findViewById(R.id.card_rating);
        report=itemView.findViewById(R.id.report_icon);
        linearLayout=itemView.findViewById(R.id.report_case);
    }


}