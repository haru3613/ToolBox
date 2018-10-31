package com.cyut.toolbox.toolbox.adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyut.toolbox.toolbox.R;
import com.cyut.toolbox.toolbox.RecyclerViewHolders;
import com.cyut.toolbox.toolbox.RecyclerViewHoldersColl;
import com.cyut.toolbox.toolbox.connection.Backgorundwork;
import com.cyut.toolbox.toolbox.model.ItemObject;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class RecyclerViewAdapterCol extends RecyclerView.Adapter<RecyclerViewHoldersColl> {
    public ArrayList<ItemObject> itemList ;
    private String uid;
    private Context context;

    int c_end_hours = 0;
    int c_end_mins = 0;
    int time_check_status = 0;
    int end_Minute = 0;
    int end_Hours = 0;
    int end_Day = 0;
    int end_Way = 0;
    private static MaterialDialog dialog;
    String message;
    private int mExpandedPosition=-1,previousExpandedPosition = -1;

    public RecyclerViewAdapterCol() {

    }
    public RecyclerViewAdapterCol(Context context, ArrayList<ItemObject> itemList,String uid) {
        this.itemList = itemList;
        this.context = context;
        this.uid=uid;
    }

    @Override
    public RecyclerViewHoldersColl onCreateViewHolder(ViewGroup parent, int viewType) {
        final View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_collection, null,false);
        layoutView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        RecyclerViewHoldersColl rcv = new RecyclerViewHoldersColl(layoutView);
        return rcv;
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHoldersColl holder, final int position) {


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

        String lineSep = System.getProperty("line.separator");
        String m=itemList.get(position).getDetail().replaceAll("<br />", lineSep);

        holder.message.setText(m);

        holder.Money.setText("$"+itemList.get(position).getMoney());

        String status=itemList.get(position).getStatus();
        holder.Status.setText(status);


        String short_time=string_sub(itemList.get(position).getTime());
        String short_until=string_sub(itemList.get(position).getUntil());

        holder.time.setText(short_time+" 至  "+short_until);

        if (status.equals("待接案")){
            holder.Status.setTextColor(Color.parseColor("#ff3333"));
        }else{
            holder.Status.setTextColor(Color.parseColor("#908e8d"));
        }

        if (itemList.get(position).getPid().equals(uid)){
            holder.bg.setBackgroundColor(Color.parseColor("#72c6cc"));
        }else{
            holder.bg.setBackgroundColor(Color.parseColor("#dfffffff"));
        }

        final boolean isExpanded = position==mExpandedPosition;
        if (itemList.get(position).getStatus().equals("待接案")&&isExpanded){
            holder.send.setVisibility(View.VISIBLE);
            holder.ans.setVisibility(View.VISIBLE);
        }else{
            holder.send.setVisibility(View.GONE);
            holder.ans.setVisibility(View.GONE);
        }
        holder.time_title.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.time.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.content.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.message.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        if (isExpanded)
            previousExpandedPosition = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandedPosition = isExpanded ? -1:position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
            }
        });


        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uid.equals(itemList.get(position).getRid())){
                    Toast.makeText(context,"你不能接自己的案子",Toast.LENGTH_SHORT).show();

                }else if(status.equals("待接案")){
                    SendMessage(uid,itemList.get(position).getCid());
                }else{
                    Toast.makeText(context,"此案件已完成或在進行中",Toast.LENGTH_SHORT).show();
                }

            }
        });



        holder.ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return this.itemList.size();
    }


    private void customDialog(final String count, final String title, final String data,final String money, final String message,final String time,final String until,final String rid,final String cid,final String uid,final String status){
        boolean wrapInScrollView = true;

        dialog=new MaterialDialog.Builder(context)
                .customView(R.layout.detaildialog, wrapInScrollView)
                .backgroundColorRes(R.color.colorBackground)
                .build();

        View item = dialog.getCustomView();

        ImageView imageView=(ImageView)item.findViewById(R.id.dialog_image);
        ImageView send=(ImageView)item.findViewById(R.id.sendmessage);
        TextView tv_title=(TextView)item.findViewById(R.id.dialog_title);
        TextView tv_data=(TextView)item.findViewById(R.id.dialog_data);
        TextView tv_message=(TextView)item.findViewById(R.id.dialog_message);
        TextView tv_time=(TextView)item.findViewById(R.id.d_time);
        TextView tv_umtil=(TextView)item.findViewById(R.id.d_until);
        TextView tv_money=item.findViewById(R.id.dialog_money);
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
        String lineSep = System.getProperty("line.separator");
        tv_money.setText("$"+money);
        String m=message.replaceAll("<br />", lineSep);
        String t;
        if (title.length()>20){
            t=title.substring(0,20)+"...";
        }
        else{
            t=title;
        }
        tv_title.setText(t);
        tv_data.setText(data);
        tv_message.setText(m);

        String short_time=string_sub(time);
        String short_until=string_sub(until);
        tv_time.setText("時間限制");
        tv_umtil.setText(short_time+"\n      至\n"+short_until);


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

    public static void dissmissDialog() {
        if(dialog!=null){
            dialog.dismiss();
        }
    }

    private String string_sub(String original){
        int start_index=original.indexOf("-");
        int last_index=original.lastIndexOf(":");

        return original.substring(start_index+1,last_index);
    }

    public void SendMessage(final String uid,final String cid) {
        boolean wrapInScrollView = true;

        dialog=new MaterialDialog.Builder(context)
                .customView(R.layout.wantcase, wrapInScrollView)
                .backgroundColorRes(R.color.colorBackground)
                .build();

        View item = dialog.getCustomView();


        final MaterialEditText sm_message=(MaterialEditText)item.findViewById(R.id.to_message);
        Button button=(Button)item.findViewById(R.id.setting_time);
        final TextView sm_time=(TextView)item.findViewById(R.id.ut_time);
        ImageView send=item.findViewById(R.id.send_job_message);
        message="";

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

                message=sm_message.getText().toString();
                sm_time.setText("正在設定時間");

                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                // Create a new instance of TimePickerDialog and return it
                new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        sm_time.setText("您設定的案件有效時間:\n" + hourOfDay + "個小時" + minute + "分");
                        c_end_hours = hourOfDay;
                        c_end_mins = minute;
                        Log.d("Tag", "get_time_end_hour=" + c_end_hours + "min=" + c_end_mins);
                    }
                }, hour, minute, true).show();


                time_check_status = 0;

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        dialog.show();

    }


    public void deleteItem(int index) {
        itemList.remove(index);
        notifyItemRemoved(index);
    }


    public String getItemCid(int position) {
        return itemList.get(position).getCid();
    }
}