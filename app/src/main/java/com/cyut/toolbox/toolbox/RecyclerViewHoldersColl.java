package com.cyut.toolbox.toolbox;

/**
 * Created by Haru on 2018/1/7.
 */

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerViewHoldersColl extends RecyclerView.ViewHolder{
    public ImageView CategoryImage,send,ans;
    public TextView Title;
    public TextView Area;
    public TextView Status;
    public TextView message;
    public TextView Money,time,content,time_title;
    public ConstraintLayout bg;
    public RecyclerViewHoldersColl(View itemView) {
        super(itemView);

        CategoryImage = (ImageView)itemView.findViewById(R.id.a_headpic);
        send = (ImageView)itemView.findViewById(R.id.card_send);
        ans = (ImageView)itemView.findViewById(R.id.card_ans);
        Title = (TextView)itemView.findViewById(R.id.a_nickname);
        content = (TextView)itemView.findViewById(R.id.card_content);
        Area = (TextView)itemView.findViewById(R.id.a_ans);
        Status=(TextView)itemView.findViewById(R.id.a_time);
        time_title=(TextView)itemView.findViewById(R.id.card_time);
        message=(TextView)itemView.findViewById(R.id.card_message);
        time=(TextView)itemView.findViewById(R.id.card_until);
        Money=itemView.findViewById(R.id.moneyTxt);
        bg=itemView.findViewById(R.id.bg);


    }


}