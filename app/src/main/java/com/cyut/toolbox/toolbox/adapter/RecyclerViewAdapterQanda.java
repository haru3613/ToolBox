package com.cyut.toolbox.toolbox.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.cyut.toolbox.toolbox.RecyclerViewHoldersMyPost;
import com.cyut.toolbox.toolbox.RecyclerViewHoldersQanda;
import com.cyut.toolbox.toolbox.RecyclerViewMsgDetailHolders;
import com.cyut.toolbox.toolbox.SimpleDividerItemDecoration;
import com.cyut.toolbox.toolbox.connection.Backgorundwork;
import com.cyut.toolbox.toolbox.model.Item;
import com.cyut.toolbox.toolbox.model.ItemMsg;

import com.cyut.toolbox.toolbox.model.ItemQanda;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static android.content.ContentValues.TAG;

public class RecyclerViewAdapterQanda extends RecyclerView.Adapter<RecyclerViewHoldersQanda> {
    public ArrayList<ItemQanda> itemList;
    private Context context;
    String name , email,uid;
    private String ServerUrl="http://35.194.171.235";
    private int mExpandedPosition=-1,previousExpandedPosition = -1;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    public static final String KEY = "STATUS";
    public RecyclerViewAdapterQanda(Context context, ArrayList<ItemQanda> itemList,String uid) {
        this.itemList = itemList;
        this.context = context;
        this.uid=uid;
    }


    @Override
    public RecyclerViewHoldersQanda onCreateViewHolder(ViewGroup parent, int viewType) {
        final View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, null,false);
        layoutView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        RecyclerViewHoldersQanda rcv = new RecyclerViewHoldersQanda(layoutView);

        return rcv;
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHoldersQanda holder, final int position) {


        LoadQName(itemList.get(position).getQ_pid(),holder);

        holder.q_ans.setText(itemList.get(position).getQ_ptext());

        String short_time=string_sub(itemList.get(position).getQ_created_at());
        String a_short_time=string_sub(itemList.get(position).getQ_updated_at());
        holder.time.setText(short_time);
        holder.a_ans.setText(itemList.get(position).getQ_atext());

        holder.a_time.setText(a_short_time);
        LoadAName(itemList.get(position).getQ_aid(),holder);
        final boolean isExpanded = position==mExpandedPosition;


        if (uid.equals(itemList.get(position).getQ_aid())){
            holder.q_message.setVisibility(View.VISIBLE);
        }else{
            holder.q_message.setVisibility(View.GONE);
        }

        if (itemList.get(position).getQ_atext()!=null&&isExpanded){
            holder.a_ans.setVisibility(View.VISIBLE);
            holder.a_nickname.setVisibility(View.VISIBLE);
            holder.a_time.setVisibility(View.VISIBLE);
            holder.a_headpic.setVisibility(View.VISIBLE);

        }else{
            holder.a_ans.setVisibility(View.GONE);
            holder.a_nickname.setVisibility(View.GONE);
            holder.a_time.setVisibility(View.GONE);
            holder.a_headpic.setVisibility(View.GONE);
        }

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


        holder.q_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(context)
                        .title("回答此問題")
                        .inputType(InputType.TYPE_CLASS_TEXT )
                        .input("請說", null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                // Do something
                                Log.d(TAG, "onInput: "+input);
                                Backgorundwork backgorundwork = new Backgorundwork(context);
                                backgorundwork.execute("update_qanda",itemList.get(position).getQ_qid(),input.toString());
                                itemList.get(position).setQ_atext(input.toString());
                                notifyItemChanged(position);
                            }
                        }).show();

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
        return this.itemList.size();
    }



    private String string_sub(String original){
        int start_index=original.indexOf("-");
        int last_index=original.lastIndexOf(":");

        return original.substring(start_index+1,last_index);
    }


    public void LoadQName(final String uid,final RecyclerViewHoldersQanda holder){
        String url =ServerUrl+"/app/loadusername.php";
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
                                name= itemList.get(0).getNickname();
                                holder.q_nickname.setText(name);
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
    public void LoadAName(final String uid,final RecyclerViewHoldersQanda holder){
        String url =ServerUrl+"/app/loadusername.php";
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
                                name= itemList.get(0).getNickname();
                                holder.a_nickname.setText(name);
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

}