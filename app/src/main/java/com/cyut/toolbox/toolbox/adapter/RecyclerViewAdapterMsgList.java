package com.cyut.toolbox.toolbox.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cyut.toolbox.toolbox.R;
import com.cyut.toolbox.toolbox.RecyclerViewMsgListHolders;
import com.cyut.toolbox.toolbox.connection.Backgorundwork;
import com.cyut.toolbox.toolbox.model.Item;
import com.cyut.toolbox.toolbox.model.ItemObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import com.google.gson.reflect.TypeToken;
import com.qiscus.sdk.data.model.QiscusChatRoom;
import com.qiscus.sdk.data.remote.QiscusApi;
import com.qiscus.sdk.ui.QiscusGroupChatActivity;
import com.qiscus.sdk.util.QiscusRxExecutor;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

//訊息列表
public class RecyclerViewAdapterMsgList extends RecyclerView.Adapter<RecyclerViewMsgListHolders> {
    public List<QiscusChatRoom> qiscusChatRooms;
    private Context context;
    String uid;
    String imagesite,self_mail,result_mail;

    public RecyclerViewAdapterMsgList(Context context, List<QiscusChatRoom> qiscusChatRooms,String uid) {
        this.qiscusChatRooms = qiscusChatRooms;
        this.context = context;
        this.uid=uid;
    }


    @Override
    public RecyclerViewMsgListHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        final View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_msg_list, null);
        RecyclerViewMsgListHolders rcv = new RecyclerViewMsgListHolders(layoutView);

        return rcv;
    }
    @Override
    public void onBindViewHolder(RecyclerViewMsgListHolders holder, final int position) {

        String strlist[];
        strlist=(qiscusChatRooms.get(position).getDistinctId()).split(" ");
        LoadSelfMail(uid,strlist,holder);


        if(qiscusChatRooms.get(position).getName()!=""){
            holder.Name.setText(qiscusChatRooms.get(position).getName());
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QiscusRxExecutor.execute(QiscusApi.getInstance().getChatRoom(qiscusChatRooms.get(position).getId()),
                        new QiscusRxExecutor.Listener<QiscusChatRoom>() {
                            @Override
                            public void onSuccess(QiscusChatRoom qiscusChatRoom) {
                                context.startActivity(QiscusGroupChatActivity.
                                        generateIntent(context, qiscusChatRoom));
                            }
                            @Override
                            public void onError(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
            }


        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {


                return true;
            }
        });


    }
    @Override
    public int getItemCount() {
        return this.qiscusChatRooms.size();
    }


    public void mail_split(String mail){



    }


    public void normalDialogEvent(final String id) {
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(context);
        dialog.title("是否刪除與此人的訊息");
        dialog.positiveText("確定");
        dialog.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {

            }
        });

        dialog.show();
    }




    public void LoadUser(final String mail, final ImageView image){
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
                            Log.d(TAG, "Response " + response);
                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();
                            List<Item> posts = new ArrayList<Item>();
                            if (!response.contains("Undefined")){
                                posts = Arrays.asList(mGson.fromJson(response, Item[].class));
                                imagesite=posts.get(0).getImage();
                                Picasso.get().load("https://imgur.com/"+imagesite+".jpg").into(image);
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
                params.put("mail",mail);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    public void LoadSelfMail(final String uid,final String[] list,RecyclerViewMsgListHolders holder){
        String url ="http://163.17.5.182/app/loaduser.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response:",response);
                        try {
                            byte[] u = response.getBytes(
                                    "UTF-8");
                            response = new String(u, "UTF-8");
                            Type listType = new TypeToken<ArrayList<Item>>() {}.getType();
                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();
                            ArrayList<Item> posts = new ArrayList<Item>();
                            if (!response.contains("Undefined")) {
                                posts = mGson.fromJson(response, listType);
                                self_mail=posts.get(0).getMail();
                                if (list[0].equals(self_mail)){
                                    LoadUser(list[1],holder.imag);
                                }else if (list[1].equals(self_mail)){
                                    LoadUser(list[0],holder.imag);
                                }
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
                params.put("uid",uid);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }



}