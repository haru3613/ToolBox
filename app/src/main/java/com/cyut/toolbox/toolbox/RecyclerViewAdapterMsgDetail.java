package com.cyut.toolbox.toolbox;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import static android.content.Context.MODE_PRIVATE;

public class RecyclerViewAdapterMsgDetail extends RecyclerView.Adapter<RecyclerViewMsgDetailHolders> {
    public List<ItemMsg> itemList;
    private Context context;
    String name , email,uid;
    public static final String KEY = "STATUS";
    public RecyclerViewAdapterMsgDetail(Context context, List<ItemMsg> itemList,String uid) {
        this.itemList = itemList;
        this.context = context;
        this.uid=uid;
    }


    @Override
    public RecyclerViewMsgDetailHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        final View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_msg, null);
        RecyclerViewMsgDetailHolders rcv = new RecyclerViewMsgDetailHolders(layoutView);

        return rcv;
    }
    @Override
    public void onBindViewHolder(final RecyclerViewMsgDetailHolders holder, final int position) {

        name="";
        email="";

        if(itemList.get(position).getUid()!=""){
            LoadUser(itemList.get(position).getUid(),holder);
        }




        if(!itemList.get(position).getMessage().equals("")){
            if(itemList.get(position).getMessage().length()>8){
                holder.ap_message.setText(itemList.get(position).getMessage().substring(0,8)+"...");
            }

        }else{
            holder.ap_message.setText("無訊息");
        }

        if(itemList.get(position).getTime()!=""){
            holder.ap_time.setText(itemList.get(position).getTime());
        }





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoadUserName(itemList.get(position).getUid(),itemList.get(position).getTime(),itemList.get(position).getMessage(),itemList.get(position).getCid());

            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                normalDialogEvent();
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        return this.itemList.size();
    }




    public void normalDialogEvent() {
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(context);
        dialog.title("是否刪除此案件");
        dialog.positiveText("確定");
        dialog.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                //刪除 TODO
                Toast.makeText(context, "刪除成功", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    public void dialog(String time,final String name,final String mail,final String message,final String cid,final String r_uid){
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(context);
        dialog.title("是否選擇此位工具人");
        dialog.content("截止日期："+time+"\n姓名："+name+"\n打招呼的話："+message);
        dialog.positiveText("確定");
        dialog.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(final MaterialDialog dialog, DialogAction which) {

                LoadCase(cid,r_uid);

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


                Toast.makeText(context, "成功，即將開啟聊天室", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }


    public void LoadUser(final String uid,final RecyclerViewMsgDetailHolders holder){
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

                                holder.ap_name.setText(itemList.get(0).getName());

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
    public void LoadUserName(final String uid,final String time,final String message,final String cid){
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
                            if (!response.contains("Undefined")){
                                List<Item> posts = new ArrayList<Item>();
                                posts = Arrays.asList(mGson.fromJson(response, Item[].class));
                                List<Item> itemList=posts;
                                name= itemList.get(0).getName();
                                email=itemList.get(0).getMail();
                                dialog(time,name,email,message,cid,uid);
                                Log.d(TAG, "onResponse:"+name+"/"+email);
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
    //讀取案件rid及money，並且更新案件進入進行中，並將錢存入第三方
    public void LoadCase(final String cid,final String r_uid){
        String url ="http://163.17.5.182/app/getcase.php";
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
                            List<ItemObject> posts = new ArrayList<ItemObject>();
                            if (!response.contains("Undefined")){
                                posts = Arrays.asList(mGson.fromJson(response, ItemObject[].class));
                                Backgorundwork backgorundwork = new Backgorundwork(context);
                                Log.d(TAG, "Load Case Success!");
                                backgorundwork.execute("deciderid",r_uid,posts.get(0).getMoney(),uid,cid);
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



}