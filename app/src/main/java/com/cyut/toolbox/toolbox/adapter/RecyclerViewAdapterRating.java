package com.cyut.toolbox.toolbox.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cyut.toolbox.toolbox.R;
import com.cyut.toolbox.toolbox.RecyclerViewHoldersQanda;
import com.cyut.toolbox.toolbox.RecyclerViewHoldersRating;
import com.cyut.toolbox.toolbox.connection.Backgorundwork;
import com.cyut.toolbox.toolbox.model.Item;
import com.cyut.toolbox.toolbox.model.ItemQanda;
import com.cyut.toolbox.toolbox.model.ItemRating;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class RecyclerViewAdapterRating extends RecyclerView.Adapter<RecyclerViewHoldersRating> {
    public ArrayList<ItemRating> itemList;
    private Context context;
    String name , email,uid;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    public static final String KEY = "STATUS";
    public RecyclerViewAdapterRating(Context context, ArrayList<ItemRating> itemList, String uid) {
        this.itemList = itemList;
        this.context = context;
        this.uid=uid;
    }


    @Override
    public RecyclerViewHoldersRating onCreateViewHolder(ViewGroup parent, int viewType) {
        final View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating, null,false);
        layoutView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        RecyclerViewHoldersRating rcv = new RecyclerViewHoldersRating(layoutView);

        return rcv;
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHoldersRating holder, final int position) {

        switch (itemList.get(position).getCategory()) {
            case "日常":
                holder.imageView.setImageResource(R.drawable.life);
                break;
            case "接送":
                holder.imageView.setImageResource(R.drawable.pickup);
                break;
            case "外送":
                holder.imageView.setImageResource(R.drawable.delivery);
                break;
            case "課業":
                holder.imageView.setImageResource(R.drawable.homework);
                break;
            case "修繕":
                holder.imageView.setImageResource(R.drawable.repair);
                break;
            case "除蟲":
                holder.imageView.setImageResource(R.drawable.debug);
                break;
        }

        if (itemList.get(position).getPid().equals(uid)){
            Log.d(TAG, "onBindViewHolder: "+itemList.get(position).getRid());
            LoadName(itemList.get(position).getRid(),holder);
        }else if (itemList.get(position).getRid().equals(uid)){
            Log.d(TAG, "onBindViewHolder: "+itemList.get(position).getPid());
            LoadName(itemList.get(position).getPid(),holder);
        }


        holder.content.setText(itemList.get(position).getContent());

        holder.ratingBar.setRating(Float.parseFloat(itemList.get(position).getGrade()));

        String t=string_sub(itemList.get(position).getTime());

        holder.date.setText(t);


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

    public void LoadName(final String uid,final RecyclerViewHoldersRating holder){
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
                                name= itemList.get(0).getNickname();
                                holder.title.setText(name);
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