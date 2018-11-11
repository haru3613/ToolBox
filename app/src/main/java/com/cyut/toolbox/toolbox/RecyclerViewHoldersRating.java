package com.cyut.toolbox.toolbox;

/**
 * Created by Haru on 2018/1/7.
 */

import android.media.Rating;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class RecyclerViewHoldersRating extends RecyclerView.ViewHolder{
    public ImageView imageView;
    public TextView title,content,date;
    public RatingBar ratingBar;

    public RecyclerViewHoldersRating(View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.item_rating_image);
        title=itemView.findViewById(R.id.item_rating_title);
        content=itemView.findViewById(R.id.item_rating_content);
        date=itemView.findViewById(R.id.item_rating_date);
        ratingBar=itemView.findViewById(R.id.item_rating_ratingbar);
    }



}