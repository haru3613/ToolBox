package com.cyut.toolbox.toolbox;

/**
 * Created by Haru on 2018/1/7.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class RecyclerViewHoldersMyReport extends RecyclerView.ViewHolder{
    public ImageView CategoryImage, finished,tomessage,view_rating;
    public TextView Title;
    public TextView Area;
    public TextView Status,progress_title,progress,rating_title;
    public TextView message,tool_title,tool;
    public TextView Money,time,content,time_title;
    public RatingBar ratingBar;
    public RecyclerViewHoldersMyReport(View itemView) {
        super(itemView);
        CategoryImage = (ImageView)itemView.findViewById(R.id.a_headpic);
        finished = (ImageView)itemView.findViewById(R.id.card_finish);
        Title = (TextView)itemView.findViewById(R.id.a_nickname);
        content = (TextView)itemView.findViewById(R.id.card_content);
        Area = (TextView)itemView.findViewById(R.id.a_ans);
        Status=(TextView)itemView.findViewById(R.id.a_time);
        time_title=(TextView)itemView.findViewById(R.id.card_time);
        message=(TextView)itemView.findViewById(R.id.card_message);
        time=(TextView)itemView.findViewById(R.id.card_until);
        Money=itemView.findViewById(R.id.moneyTxt);
        tomessage=itemView.findViewById(R.id.card_tomessage);
        progress=itemView.findViewById(R.id.card_progress);
        rating_title=itemView.findViewById(R.id.card_rating_title);
        progress_title=itemView.findViewById(R.id.card_progress_title);
        tool=itemView.findViewById(R.id.card_tool);
        tool_title=itemView.findViewById(R.id.card_tool_title);
        ratingBar=itemView.findViewById(R.id.card_rating);
        view_rating=itemView.findViewById(R.id.view_rating);
    }


}