package com.cyut.toolbox.toolbox;

/**
 * Created by Haru on 2018/1/7.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RecyclerViewCategoryHolders extends RecyclerView.ViewHolder{
    public ImageView imag;
    public TextView categoryText;
    public LinearLayout categoryBackground;

    public RecyclerViewCategoryHolders(View itemView) {
        super(itemView);
        imag = (ImageView)itemView.findViewById(R.id.View);
        categoryText=itemView.findViewById(R.id.cateText);
        categoryBackground=itemView.findViewById(R.id.categorybackground);
    }

}