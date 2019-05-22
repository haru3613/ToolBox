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

public class RecyclerViewHolders extends RecyclerView.ViewHolder{
    public ImageView CategoryImage,send,like,ans,view_rating,report;
    public TextView Title;
    public TextView Area;
    public TextView Status;
    public TextView message,rating_title;
    public TextView Money,time,content,time_title;
    public RatingBar ratingBar;
    public ConstraintLayout bg;
    public LinearLayout linearLayout;
    public RecyclerViewHolders(View itemView) {
        super(itemView);
        view_rating=itemView.findViewById(R.id.view_rating);
        CategoryImage = (ImageView)itemView.findViewById(R.id.a_headpic);
        send = (ImageView)itemView.findViewById(R.id.card_send);
        ans = (ImageView)itemView.findViewById(R.id.card_ans);
        like = (ImageView)itemView.findViewById(R.id.card_like);
        Title = (TextView)itemView.findViewById(R.id.a_nickname);
        rating_title=itemView.findViewById(R.id.card_rating_title);
        content = (TextView)itemView.findViewById(R.id.card_content);
        Area = (TextView)itemView.findViewById(R.id.a_ans);
        Status=(TextView)itemView.findViewById(R.id.a_time);
        time_title=(TextView)itemView.findViewById(R.id.card_time);
        message=(TextView)itemView.findViewById(R.id.card_message);
        time=(TextView)itemView.findViewById(R.id.card_until);
        Money=itemView.findViewById(R.id.moneyTxt);
        bg=itemView.findViewById(R.id.bg);
        ratingBar=itemView.findViewById(R.id.card_rating);
        report=itemView.findViewById(R.id.report_icon);
        linearLayout=itemView.findViewById(R.id.report_case);
    }


}