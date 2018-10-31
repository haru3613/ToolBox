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
import com.cyut.toolbox.toolbox.RecyclerViewHoldersMyPost;
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

public class RecyclerViewAdapterMyPost extends RecyclerView.Adapter<RecyclerViewHoldersMyPost> {
    public ArrayList<ItemObject> itemList;
    private Context context;
    private String uid;
    private RecyclerViewAdapterMsgDetail adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private static MaterialDialog RidDialog;
    public static final String KEY = "STATUS";
    private int mExpandedPosition=-1,previousExpandedPosition = -1;

    public RecyclerViewAdapterMyPost() {

    }

    public RecyclerViewAdapterMyPost(Context context, ArrayList<ItemObject> itemList, String uid) {
        this.itemList = itemList;
        this.context = context;
        this.uid=uid;
    }
    String name,mail;
    @Override
    public RecyclerViewHoldersMyPost onCreateViewHolder(ViewGroup parent, int viewType) {
        final View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_mypost, null,false);
        layoutView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        RecyclerViewHoldersMyPost rcv = new RecyclerViewHoldersMyPost(layoutView);

        return rcv;
    }
    @Override
    public void onBindViewHolder(RecyclerViewHoldersMyPost holder, final int position) {

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

        if (!itemList.get(position).getRid().equals(uid)){
            LoadUserName(itemList.get(position).getRid(),holder);

        }else {
            holder.tool.setText("此案尚未有接案人");
        }


        final boolean isExpanded = position==mExpandedPosition;
        if (itemList.get(position).getStatus().equals("確認中")&&isExpanded){
            holder.progress.setText("工具人已完成\n等待你確認喔");
            holder.finished.setVisibility(View.VISIBLE);
            holder.unfinish.setVisibility(View.VISIBLE);
            holder.tool.setVisibility(View.VISIBLE);
            holder.finished.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //把錢轉過去，並更新案件
                    Backgorundwork backgorundwork = new Backgorundwork(context);
                    backgorundwork.execute("finish_case",itemList.get(position).getCid(),"已完成",itemList.get(position).getMoney(),itemList.get(position).getRid());
                    itemList.get(position).setStatus("已完成");
                    notifyItemChanged(position);
                }
            });
            holder.unfinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Backgorundwork backgorundwork = new Backgorundwork(context);
                    backgorundwork.execute("case_status",itemList.get(position).getCid(),"進行中");
                    itemList.get(position).setStatus("進行中");
                    notifyItemChanged(position);
                }
            });
        }else if(itemList.get(position).getStatus().equals("待接案")&&isExpanded){
            holder.tomessage.setVisibility(View.GONE);
            holder.unfinish.setVisibility(View.GONE);
            holder.finished.setVisibility(View.GONE);
            layoutManager = new LinearLayoutManager(context);
            holder.mypost_ryv.setLayoutManager(layoutManager);
            holder.tool.setVisibility(View.VISIBLE);
            holder.mypost_ryv.setVisibility(View.VISIBLE);
            MessageLoad(itemList.get(position).getCid(),holder);

        }else if (itemList.get(position).getStatus().equals("進行中")&&isExpanded){
            holder.progress.setText("工具人正在進行工作\n請耐心等待...");
            holder.tool.setVisibility(View.VISIBLE);
            holder.unfinish.setVisibility(View.GONE);
            holder.finished.setVisibility(View.GONE);
            holder.mypost_ryv.setVisibility(View.GONE);
        }else if (itemList.get(position).getStatus().equals("已完成")&&isExpanded){
            holder.tool.setVisibility(View.VISIBLE);
            holder.unfinish.setVisibility(View.GONE);
            holder.finished.setVisibility(View.GONE);
            holder.progress.setText("已完成");
        }else{
            holder.unfinish.setVisibility(View.GONE);
            holder.finished.setVisibility(View.GONE);
            holder.mypost_ryv.setVisibility(View.GONE);
            holder.tool.setVisibility(View.GONE);
            holder.tomessage.setVisibility(View.GONE);
        }
        holder.time_title.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.time.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.content.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.message.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.progress_title.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.progress.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.tool_title.setVisibility(isExpanded?View.VISIBLE:View.GONE);

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

        holder.tomessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (Qiscus.hasSetupUser()) {

                    Qiscus.buildChatWith(mail)
                            .build(context)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(intent -> {
                                context.startActivity(intent);
                            }, throwable -> {
                                //do anything if error occurs
                                Log.d(TAG, "onError: " + throwable);
                            });
                }*/

                Toast.makeText(context, "即將開啟聊天室", Toast.LENGTH_SHORT).show();
            }
        });

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
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
        });*/


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


    //讀取接案人名稱及訊息
    public void MessageLoad(final String cid,final RecyclerViewHoldersMyPost holder){
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
                                holder.mypost_ryv.setAdapter(adapter);
                                holder.tool.setVisibility(View.GONE);
                            }else{
                                holder.tool.setText("此案尚未有工具人");
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


    public void LoadUserName(final String uid,final RecyclerViewHoldersMyPost holder){
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
                                holder.tool.setText(name);
                                mail=itemList.get(0).getMail();
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