package com.cyut.toolbox.toolbox;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qiscus.sdk.Qiscus;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {
    public List<ItemObject> itemList;
    private Context context;
    private String uid;
    private EditText sm_message;
    private TextView sm_time;
    private static MaterialDialog dialog;

    int c_end_hours = 0;
    int c_end_mins = 0;
    int time_check_status = 0;
    int end_Minute = 0;
    int end_Hours = 0;
    int end_Day = 0;
    int end_Way = 0;


    public RecyclerViewAdapter() {

    }

    public RecyclerViewAdapter(Context context, List<ItemObject> itemList,String uid) {
        this.itemList = itemList;
        this.context = context;
        this.uid=uid;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        final View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);

        return rcv;
    }
    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, final int position) {

        switch (itemList.get(position).getCategoryImage()) {
            case "日常":
                holder.CategoryImage.setImageResource(R.drawable.life);
                break;
            case "接送":
                holder.CategoryImage.setImageResource(R.drawable.pickup);
                break;
            case "外送":
                holder.CategoryImage.setImageResource(R.drawable.delivery);
                break;
            case "課業":
                holder.CategoryImage.setImageResource(R.drawable.homework);
                break;
            case "修繕":
                holder.CategoryImage.setImageResource(R.drawable.repair);
                break;
            case "除蟲":
                holder.CategoryImage.setImageResource(R.drawable.debug);
                break;
        }
        if (itemList.get(position).getTitle().length()>8){
            holder.Title.setText(itemList.get(position).getTitle().substring(0,8)+"...");
        }else{
            holder.Title.setText(itemList.get(position).getTitle());
        }

        String Address=itemList.get(position).getCity()+itemList.get(position).getTown()+itemList.get(position).getRoad();
        if (Address.length()>10){
            holder.Area.setText(Address.substring(0,10)+"...");
        }else{
            holder.Area.setText(Address);
        }

        holder.Status.setText(itemList.get(position).getStatus());


        if (itemList.get(position).getPid().equals(uid)){
            holder.bg.setBackgroundColor(Color.parseColor("#90c7cd"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: "+itemList.get(position).getTitle());
                //OPEN DETAIL
                customDialog(itemList.get(position).getCategoryImage(),itemList.get(position).getTitle(),(itemList.get(position).getCity()+itemList.get(position).getTown()+
                        itemList.get(position).getRoad())+" \nNT$ "+itemList.get(position).getMoney(),itemList.get(position).getDetail(),itemList.get(position).getTime(),itemList.get(position).getUntil(),
                        itemList.get(position).getRid(),itemList.get(position).getCid(),uid,itemList.get(position).getStatus());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                normalDialogEvent(uid,itemList.get(position).getCid());
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        return this.itemList.size();
    }


    private void customDialog(final String count, final String title, final String data, final String message,final String time,final String until,final String rid,final String cid,final String uid,final String status){
        boolean wrapInScrollView = true;

        final View item = LayoutInflater.from(context).inflate(R.layout.detaildialog, null);

        final MaterialDialog.Builder dialog =new MaterialDialog.Builder(context);
        dialog.customView(item,wrapInScrollView);
        dialog.backgroundColorRes(R.color.colorBackground);
        ImageView imageView=(ImageView)item.findViewById(R.id.dialog_image);
        ImageView send=(ImageView)item.findViewById(R.id.sendmessage);
        TextView tv_title=(TextView)item.findViewById(R.id.dialog_title);
        TextView tv_data=(TextView)item.findViewById(R.id.dialog_data);
        TextView tv_message=(TextView)item.findViewById(R.id.dialog_message);
        TextView tv_time=(TextView)item.findViewById(R.id.d_time);
        TextView tv_umtil=(TextView)item.findViewById(R.id.d_until);
        switch(count) {
            case "日常":
                imageView.setImageResource(R.drawable.life);
                break;
            case "接送":
                imageView.setImageResource(R.drawable.pickup);
                break;
            case "外送":
                imageView.setImageResource(R.drawable.delivery);
                break;
            case "課業":
                imageView.setImageResource(R.drawable.homework);
                break;
            case "修繕":
                imageView.setImageResource(R.drawable.repair);
                break;
            case "除蟲":
                imageView.setImageResource(R.drawable.debug);
                break;
        }
        String t;
        if (title.length()>20){
            t=title.substring(0,20)+"...";
        }
        else{
            t=title;
        }


        tv_title.setText(t);
        tv_data.setText(data);
        tv_message.setText(message);
        tv_time.setText("發案時間：\n"+time);
        tv_umtil.setText("至\n"+until+" 截止");


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uid.equals(rid)){
                    Toast.makeText(context,"你不能接自己的案子",Toast.LENGTH_SHORT).show();

                }else if(status.equals("待接案")){
                    SendMessage(uid,cid);
                }else{
                    Toast.makeText(context,"此案件已完成或在進行中",Toast.LENGTH_SHORT).show();
                }

            }
        });



        dialog.show();

    }


    public void normalDialogEvent(final String uid,final String cid) {
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(context);
        dialog.title("是否收藏此案件");
        dialog.positiveText("確定");
        dialog.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                String type = "collection";
                Backgorundwork backgorundwork = new Backgorundwork(context);
                backgorundwork.execute(type,cid,uid);
            }
        });

        dialog.show();
    }

    public void SendMessage(final String uid,final String cid) {
        boolean wrapInScrollView = true;

        dialog=new MaterialDialog.Builder(context)
                .title("我想接案")
                .positiveText("送出申請")
                .customView(R.layout.wantcase, wrapInScrollView)
                .backgroundColorRes(R.color.colorBackground)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        //TODO 檢查資料庫是否已有接案
                        if (sm_message.getText().toString().equals("")||sm_time.getText().toString().equals("申請時效")){
                            Toast.makeText(context,"請設定訊息及時間",Toast.LENGTH_SHORT).show();
                        }else{
                            String sm =sm_message.getText().toString();
                            Log.d(TAG, "onClick: "+sm);
                            String type = "sendmessage";
                            Backgorundwork backgorundwork = new Backgorundwork(context);
                            backgorundwork.execute(type,cid,uid,sm,Integer.toString(c_end_hours),Integer.toString(c_end_mins));
                        }
                    }
                })
                .build();


        View item = dialog.getCustomView();
        sm_message=(EditText)item.findViewById(R.id.to_message);
        Button button=(Button)item.findViewById(R.id.setting_time);
        sm_time=(TextView)item.findViewById(R.id.ut_time);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c_end_hours = 0;
                c_end_mins = 0;
                time_check_status = 0;
                end_Minute = 0;
                end_Hours = 0;
                end_Day = 0;
                end_Way = 0;


                sm_time.setText("正在設定時間");

                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                // Create a new instance of TimePickerDialog and return it
                new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        sm_time.setText("您設定的案件有效時間:" + hourOfDay + "個小時" + minute + "分");
                        c_end_hours = hourOfDay;
                        c_end_mins = minute;
                        Log.d("Tag", "get_time_end_hour=" + c_end_hours + "min=" + c_end_mins);
                    }
                }, hour, minute, true).show();


                time_check_status = 0;

            }
        });



        dialog.show();
    }



    public static void dissmissDialog() {
        if(dialog!=null){
            dialog.dismiss();
        }
    }


    //---------------------------------------send_data to php and database----------------------------------------------------------

    public String toTimeFormat(String y, String month, String day, String hour, String min, String sec){
        String formatetime=y+"-"+month+"-"+day+" "+hour+":"+min+":"+sec;
        return formatetime;
    }
    public String toTimeFormat1(int y, int month, int day, int hour, int min, int sec){
        String formatetime=y+"-"+month+"-"+day+" "+hour+":"+min+":"+sec;
        return formatetime;
    }

}