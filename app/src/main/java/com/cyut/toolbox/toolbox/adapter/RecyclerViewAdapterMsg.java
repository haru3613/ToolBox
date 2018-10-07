package com.cyut.toolbox.toolbox.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cyut.toolbox.toolbox.R;
import com.cyut.toolbox.toolbox.RecyclerViewHolders;
import com.cyut.toolbox.toolbox.SimpleDividerItemDecoration;
import com.cyut.toolbox.toolbox.connection.Backgorundwork;
import com.cyut.toolbox.toolbox.model.Item;
import com.cyut.toolbox.toolbox.model.ItemMsg;
import com.cyut.toolbox.toolbox.model.ItemObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qiscus.sdk.Qiscus;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class RecyclerViewAdapterMsg extends RecyclerView.Adapter<RecyclerViewHolders> {
    public ArrayList<ItemObject> itemList;
    private Context context;
    private String uid;
    private RecyclerViewAdapterMsgDetail adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private static MaterialDialog RidDialog;
    public static final String KEY = "STATUS";

    public RecyclerViewAdapterMsg() {

    }

    public RecyclerViewAdapterMsg(Context context, ArrayList<ItemObject> itemList,String uid) {
        this.itemList = itemList;
        this.context = context;
        this.uid=uid;
    }
    String name,mail;
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

        String status=itemList.get(position).getStatus();
        holder.Status.setText(status);

        holder.Money.setText("$"+itemList.get(position).getMoney());

        if (status.equals("待接案")){
            holder.Status.setTextColor(Color.parseColor("#ff3333"));
        }else{
            holder.Status.setTextColor(Color.parseColor("#908e8d"));
        }
        if (itemList.get(position).getPid().equals(uid)){
            holder.bg.setBackgroundColor(Color.parseColor("#72c6cc"));
        }else {
            holder.bg.setBackgroundColor(Color.parseColor("#dfffffff"));
        }

        holder.Status.setText(itemList.get(position).getStatus());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: "+itemList.get(position).getPid());
                Log.d(TAG, "onClick: "+itemList.get(position).getRid());
                if (itemList.get(position).getPid().equals(uid) ){
                    //我發的
                    customDialog(itemList.get(position).getCategoryImage(),itemList.get(position).getTitle(),
                            (itemList.get(position).getCity()+itemList.get(position).getTown()+"\n"+itemList.get(position).getRoad()),itemList.get(position).getMoney(),
                            itemList.get(position).getDetail(),itemList.get(position).getTime(),itemList.get(position).getUntil(),itemList.get(position).getCid(),
                            itemList.get(position).getStatus(),itemList.get(position).getRid(),position);
                }else if(itemList.get(position).getRid().equals(uid)){
                    //我接的
                    myRidDialog(itemList.get(position).getCategoryImage(),itemList.get(position).getTitle(),
                            (itemList.get(position).getCity()+itemList.get(position).getTown()+"\n"+itemList.get(position).getRoad()),itemList.get(position).getMoney(),
                            itemList.get(position).getDetail(),itemList.get(position).getTime(),itemList.get(position).getUntil(),itemList.get(position).getCid(),
                            itemList.get(position).getPid(),itemList.get(position).getStatus(),position);
                }

            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                normalDialogEvent(itemList.get(position).getCid(),uid,position);
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        return this.itemList.size();
    }


    private void customDialog(final String count, final String title, final String data,final String money, final String message,final String time,final String until,final String cid,final String status,final String rid,int position){
        boolean wrapInScrollView = true;

        final View item = LayoutInflater.from(context).inflate(R.layout.mycasedetail, null);

        MaterialDialog.Builder dialog =new MaterialDialog.Builder(context);
        dialog.customView(item,wrapInScrollView);
        dialog.backgroundColorRes(R.color.colorBackground);
        ImageView imageView=(ImageView)item.findViewById(R.id.dialog_image);
        TextView tv_title=(TextView)item.findViewById(R.id.dialog_title);
        TextView tv_data=(TextView)item.findViewById(R.id.dialog_data);
        TextView tv_message=(TextView)item.findViewById(R.id.dialog_message);
        TextView tv_time=(TextView)item.findViewById(R.id.d_time);
        TextView tv_umtil=(TextView)item.findViewById(R.id.d_until);
        final TextView tv_casename=(TextView)item.findViewById(R.id.case_name);
        LinearLayout confirm=(LinearLayout)item.findViewById(R.id.confirm);
        final LinearLayout check_before=(LinearLayout)item.findViewById(R.id.check_before);
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
        String t;
        if (title.length()>20){
            t=title.substring(0,20)+"...";
        }
        else{
            t=title;
        }
        String lineSep = System.getProperty("line.separator");
        String m=message.replaceAll("<br />", lineSep);
        tv_money.setText("$"+money);
        tv_title.setText(t);
        tv_data.setText(data);
        tv_message.setText(m);
        String short_time=string_sub(time);
        String short_until=string_sub(until);
        tv_time.setText("時間限制");
        tv_umtil.setText(short_time+"\n      至\n"+short_until);

        if (!rid.equals(uid)){
            LoadUserName(rid,item);

        }else {
            tv_casename.setText("此案尚未有接案人");
        }

        if (status.equals("確認中")){
            confirm.setVisibility(View.VISIBLE);
            Button finish=(Button)item.findViewById(R.id.btn_finish);
            Button nfinish=(Button)item.findViewById(R.id.btn_nfinish);
            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   //TODO 把錢轉過去
                    Backgorundwork backgorundwork = new Backgorundwork(context);
                    backgorundwork.execute("finish_case",cid,"已完成",money,rid);
                    itemList.get(position).setStatus("已完成");
                    notifyItemChanged(position);
                }
            });

            nfinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Backgorundwork backgorundwork = new Backgorundwork(context);
                    backgorundwork.execute("case_status",cid,"進行中");
                    itemList.get(position).setStatus("進行中");
                    notifyItemChanged(position);
                }
            });
        }else{
            confirm.setVisibility(View.GONE);
        }

        tv_casename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tv_casename.getText().toString().equals("此案尚未有接案人")){
                    Qiscus.buildChatWith(mail)
                            .build(context)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(intent -> {
                                context.startActivity(intent);
                            }, throwable -> {
                                //do anything if error occurs
                                Log.d(TAG, "onError: "+throwable);
                            });

                    Toast.makeText(context, "即將開啟聊天室", Toast.LENGTH_SHORT).show();
                }

            }
        });


        recyclerView = (RecyclerView)item.findViewById(R.id.recyclemsg);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(item.getContext()));
        layoutManager = new LinearLayoutManager(item.getContext());
        recyclerView.setLayoutManager(layoutManager);

        if (status.equals("待接案")){
            Log.d(TAG, "CID: "+cid);
            check_before.setVisibility(View.VISIBLE);
            MessageLoad(cid);
        }else{
            check_before.setVisibility(View.GONE);
        }



        dialog.show();

    }


    private void myRidDialog(final String count, final String title, final String data,final String money, final String message, final String time, final String until, final String cid, final String pid, final String status,int position){
        boolean wrapInScrollView = true;


        RidDialog=new MaterialDialog.Builder(context)
                .customView(R.layout.mygetcase, wrapInScrollView)
                .backgroundColorRes(R.color.colorBackground)
                .build();

        final View item = RidDialog.getCustomView();

        ImageView imageView=(ImageView)item.findViewById(R.id.dialog_image);
        TextView tv_title=(TextView)item.findViewById(R.id.dialog_title);
        TextView tv_data=(TextView)item.findViewById(R.id.dialog_data);
        TextView tv_message=(TextView)item.findViewById(R.id.dialog_message);
        TextView tv_time=(TextView)item.findViewById(R.id.d_time);
        TextView tv_umtil=(TextView)item.findViewById(R.id.d_until);
        final TextView tv_casename=(TextView)item.findViewById(R.id.case_name);
        final Button cancel=(Button)item.findViewById(R.id.cancelcase);
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
        String t;
        if (title.length()>20){
            t=title.substring(0,20)+"...";
        }
        else{
            t=title;
        }
        String lineSep = System.getProperty("line.separator");
        String m=message.replaceAll("<br />", lineSep);
        tv_money.setText("$"+money);
        tv_title.setText(t);
        tv_data.setText(data);
        tv_message.setText(m);
        String short_time=string_sub(time);
        String short_until=string_sub(until);
        tv_time.setText("時間限制");
        tv_umtil.setText(short_time+"\n      至\n"+short_until);


        LoadUserName(pid,item);



        if (status.equals("進行中")){
            cancel.setText("已完成");
        }else if(status.equals("確認中")){
            cancel.setText("我還沒做完");
        }else {
            cancel.setVisibility(View.GONE);
        }


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cancel.getText().equals("已完成")){
                    MaterialDialog dialog=new MaterialDialog.Builder(context)
                            .title("是否已完成")
                            .positiveText("確認")
                            .negativeText("未完成")
                            .content("請詳細檢查是否已完成，選擇完後即不可退回")
                            .backgroundColorRes(R.color.colorBackground)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    Backgorundwork backgorundwork = new Backgorundwork(context);
                                    backgorundwork.execute("case_status",cid,"確認中");
                                    itemList.get(position).setStatus("確認中");
                                    notifyItemChanged(position);
                                }
                            })
                            .build();
                    dialog.show();

                }else if(cancel.getText().equals("我還沒做完")) {
                    Backgorundwork backgorundwork = new Backgorundwork(context);
                    backgorundwork.execute("case_status",cid,"進行中");
                    itemList.get(position).setStatus("進行中");
                    notifyItemChanged(position);
                }

            }
        });

        tv_casename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!tv_casename.getText().toString().equals("此案尚未有接案人")){
                    Qiscus.buildChatWith(mail) //here we use email as userID. But you can make it whatever you want.
                            .build(context, new Qiscus.ChatActivityBuilderListener() {
                                @Override
                                public void onSuccess(Intent intent) {
                                    context.startActivity(intent);
                                }
                                @Override
                                public void onError(Throwable throwable) {
                                    //do anything if error occurs
                                    Log.d(TAG, "onError: "+throwable);
                                }
                            });

                    Toast.makeText(context, "即將開啟聊天室", Toast.LENGTH_SHORT).show();
                }

            }
        });



        RidDialog.show();

    }

    private String string_sub(String original){
        int start_index=original.indexOf("-");
        int last_index=original.lastIndexOf(":");

        return original.substring(start_index+1,last_index);
    }

    public void normalDialogEvent(final String cid,final String uid,final int position) {
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(context);
        dialog.title("是否刪除此案件");
        dialog.positiveText("確定");
        dialog.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                //刪除


            }
        });

        dialog.show();
    }

    public static void dissmissDialog() {
        if(RidDialog!=null){
            RidDialog.dismiss();
        }
    }

    //讀取接案人名稱及訊息
    public void MessageLoad(final String cid){
        String url ="http://163.17.5.182/messagedetail.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response:",response);
                        try {
                            byte[] u = response.getBytes(
                                    "UTF-8");
                            response = new String(u, "UTF-8");
                            Log.d(TAG, "Response " + response);
                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();
                            List<ItemMsg> posts = new ArrayList<ItemMsg>();
                            if (!response.contains("Undefined")){
                                posts = Arrays.asList(mGson.fromJson(response, ItemMsg[].class));
                                adapter = new RecyclerViewAdapterMsgDetail(context, posts,uid);
                                recyclerView.setAdapter(adapter);
                            }

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //do stuffs with response erroe
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("c_cid",cid);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    public void LoadUserName(final String uid,final View item){
        String url ="http://163.17.5.182/loadusername.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response:",response);
                        try {
                            byte[] u = response.getBytes(
                                    "UTF-8");
                            response = new String(u, "UTF-8");
                            Log.d(TAG, "Response " + response);
                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();
                            List<Item> posts = new ArrayList<Item>();
                            if (!response.contains("Undefined")){
                                posts = Arrays.asList(mGson.fromJson(response, Item[].class));
                                List<Item> itemList=posts;
                                name= itemList.get(0).getName();
                                final TextView tv_casename=(TextView)item.findViewById(R.id.case_name);
                                tv_casename.setText(name);
                                mail=itemList.get(0).getMail();
                                Log.d(TAG, "onResponse:"+name);
                            }

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //do stuffs with response erroe
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("u_uid",uid);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    public void DeleteCase(final String cid,final String uid){
        Backgorundwork backgorundwork = new Backgorundwork(context);
        backgorundwork.execute("deleteMessage",cid);
    }

    void deleteItem(int index) {
        itemList.remove(index);
        notifyItemRemoved(index);
    }


    String getItemCid(int position) {
        return itemList.get(position).getCid();
    }



}