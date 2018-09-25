package com.cyut.toolbox.toolbox;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
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
import com.qiscus.sdk.data.model.QiscusAccount;
import com.qiscus.sdk.data.model.QiscusChatRoom;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

//訊息列表
public class RecyclerViewAdapterMsgList extends RecyclerView.Adapter<RecyclerViewMsgListHolders> {
    public List<QiscusChatRoom> qiscusChatRooms;
    private Context context;
    String Name;
    String Email;
    ImageView imageView;
    String imagesite;

    public RecyclerViewAdapterMsgList(Context context, List<QiscusChatRoom> qiscusChatRooms) {
        this.qiscusChatRooms = qiscusChatRooms;
        this.context = context;
    }


    @Override
    public RecyclerViewMsgListHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        final View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_msg_list, null);
        RecyclerViewMsgListHolders rcv = new RecyclerViewMsgListHolders(layoutView);

        return rcv;
    }
    @Override
    public void onBindViewHolder(RecyclerViewMsgListHolders holder, final int position) {


        if(qiscusChatRooms.get(position).getName()!=""){
            Name=qiscusChatRooms.get(position).getName();
        }
        if(qiscusChatRooms.get(position).getDistinctId()!=""){
            Email=qiscusChatRooms.get(position).getDistinctId();
        }

        holder.Name.setText(Name);


        Log.d(TAG, "onBindViewHolder: "+Email);


        final String[] split = Email.split(" ");



        Log.d(TAG, "onBindViewHolder: "+split[0]);
        LoadUser(split[0],holder);






        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Qiscus.buildChatWith(split[0]) //here we use email as userID. But you can make it whatever you want.
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
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                //normalDialogEvent(itemList.get(position).getCid(),uid,position);
                return true;
            }
        });


    }
    @Override
    public int getItemCount() {
        return this.qiscusChatRooms.size();
    }




    public void normalDialogEvent(final String uid,final String cid) {
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(context);
        dialog.title("此案件是否已經結束");
        dialog.positiveText("確定");
        dialog.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                String type = "finish";
                Backgorundwork backgorundwork = new Backgorundwork(context);
                backgorundwork.execute(type,cid,uid);
            }
        });

        dialog.show();
    }




    public void LoadUser(final String mail, final RecyclerViewMsgListHolders holder){
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
                                List<Item> itemList=posts;
                                imagesite=itemList.get(0).getImage();
                                Picasso.with(context).load("https://imgur.com/"+imagesite+".jpg").into(holder.imag);
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






}