package com.cyut.toolbox.toolbox;

/**
 * Created by Haru on 2018/1/7.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerViewMsgDetailHolders extends RecyclerView.ViewHolder{
    public TextView ap_name;


    public RecyclerViewMsgDetailHolders(View itemView) {
        super(itemView);
        ap_name = (TextView)itemView.findViewById(R.id.ap_name);


    }

}