package com.cyut.toolbox.toolbox;

/**
 * Created by Haru on 2018/1/7.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecyclerViewHolders extends RecyclerView.ViewHolder{
    public ImageView CategoryImage;
    public TextView Title;
    public TextView Area;
    public TextView Status;
    public RelativeLayout bg;
    public RecyclerViewHolders(View itemView) {
        super(itemView);
        CategoryImage = (ImageView)itemView.findViewById(R.id.CategoryImage);
        Title = (TextView)itemView.findViewById(R.id.titleTxt);
        Area = (TextView)itemView.findViewById(R.id.AreaTxt);
        Status=(TextView)itemView.findViewById(R.id.StatusTxt);
        bg=itemView.findViewById(R.id.bg);
    }

}