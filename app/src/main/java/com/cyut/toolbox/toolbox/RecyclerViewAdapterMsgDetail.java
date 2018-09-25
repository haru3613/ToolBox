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

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class RecyclerViewAdapterMsgDetail extends RecyclerView.Adapter<RecyclerViewMsgDetailHolders> {
    public List<ItemMsg> itemList;
    private Context context;

    public static final String KEY = "STATUS";
    public RecyclerViewAdapterMsgDetail(Context context, List<ItemMsg> itemList) {
        this.itemList = itemList;
        this.context = context;
    }
    String name;
    String time;
    String email;
    String message,uid,r_uid;
    @Override
    public RecyclerViewMsgDetailHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        final View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_msg, null);
        RecyclerViewMsgDetailHolders rcv = new RecyclerViewMsgDetailHolders(layoutView);

        return rcv;
    }
    @Override
    public void onBindViewHolder(final RecyclerViewMsgDetailHolders holder, final int position) {



        if(itemList.get(position).getUid()!=""){
            r_uid=itemList.get(position).getUid();
            LoadUser(itemList.get(position).getUid(),holder);
        }



        message=itemList.get(position).getMessage();
        if(message!=""){
            if(message.length()>8){
                holder.ap_message.setText(message.substring(0,8)+"...");
            }

        }else{
            holder.ap_message.setText("無訊息");
        }

        if(itemList.get(position).getTime()!=""){
            holder.ap_time.setText(time);
        }

        uid="";
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY, MODE_PRIVATE);
        String mail=sharedPreferences.getString("Mail",null);


        if (mail!=null){
            LoadUid(mail);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: "+name);
                if (message.equals("")){
                    dialog(itemList.get(position).getTime(),email,"無訊息",itemList.get(position).getCid());
                }else {
                    dialog(itemList.get(position).getTime(),email,message,itemList.get(position).getCid());
                }


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

    public void dialog(String time,final String mail,final String message,final String cid){
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(context);
        dialog.title("是否選擇此位工具人");
        dialog.content("截止日期："+time+"\n姓名："+name+"\n訊息："+message);
        dialog.positiveText("確定");
        dialog.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(final MaterialDialog dialog, DialogAction which) {

                LoadCase(cid);


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
                                name= itemList.get(0).getName();
                                holder.ap_name.setText(name);
                                email=itemList.get(0).getMail();
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
    public void LoadCase(final String cid){
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

    public void LoadUid(final String mail){
        String url ="http://163.17.5.182/loaduser.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response:",response);
                        try {
                            byte[] u = response.getBytes(
                                    "UTF-8");
                            response = new String(u, "UTF-8");
                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();
                            List<Item> posts = new ArrayList<Item>();
                            posts = Arrays.asList(mGson.fromJson(response, Item[].class));
                            List<Item> itemList=posts;
                            Log.d(TAG, "Get Uid");
                            uid= itemList.get(0).getUid();
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
                params.put("mail",mail+"@gm.cyut.edu.tw");
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}