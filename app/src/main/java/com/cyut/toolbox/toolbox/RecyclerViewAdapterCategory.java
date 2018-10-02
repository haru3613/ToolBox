package com.cyut.toolbox.toolbox;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RecyclerViewAdapterCategory extends RecyclerView.Adapter<RecyclerViewCategoryHolders> {
    public List<String> itemList;
    private Context context;


    private static MaterialDialog dialog;



    public RecyclerViewAdapterCategory() {

    }

    public RecyclerViewAdapterCategory(Context context, ArrayList<String> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewCategoryHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        final View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category, null);
        RecyclerViewCategoryHolders rcv = new RecyclerViewCategoryHolders(layoutView);

        return rcv;
    }
    @Override
    public void onBindViewHolder(RecyclerViewCategoryHolders holder, final int position) {

        switch (itemList.get(position)) {
            case "日常":
                holder.imag.setImageResource(R.drawable.life);
                holder.categoryText.setText("日常");
                break;
            case "接送":
                holder.imag.setImageResource(R.drawable.pickup);
                holder.categoryText.setText("接送");
                break;
            case "外送":
                holder.imag.setImageResource(R.drawable.delivery);
                holder.categoryText.setText("外送");
                break;
            case "課業":
                holder.imag.setImageResource(R.drawable.homework);
                holder.categoryText.setText("課業");
                break;
            case "修繕":
                holder.imag.setImageResource(R.drawable.repair);
                holder.categoryText.setText("修繕");
                break;
            case "除蟲":
                holder.imag.setImageResource(R.drawable.debug);
                holder.categoryText.setText("除蟲");
                break;
        }


    }
    @Override
    public int getItemCount() {
        return this.itemList.size();
    }



}