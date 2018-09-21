package com.cyut.toolbox.toolbox;

/**
 * Created by Haru on 2018/1/7.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerViewMsgListHolders extends RecyclerView.ViewHolder{
    public ImageView imag;
    public TextView Name;


    public RecyclerViewMsgListHolders(View itemView) {
        super(itemView);
        imag = (ImageView)itemView.findViewById(R.id.list_image);
        Name = (TextView)itemView.findViewById(R.id.list_name);
    }

}