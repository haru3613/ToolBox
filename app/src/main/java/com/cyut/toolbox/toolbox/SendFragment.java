package com.cyut.toolbox.toolbox;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
//我的發案

/**
 * A simple {@link Fragment} subclass.
 */
public class SendFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerViewAdapterMsg adapter;
    String uid;
    View v;
    public static final String KEY = "STATUS";
    public SendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_send, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.rv_message);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(view.getContext()));
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        v=view;
        uid="";

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(KEY, MODE_PRIVATE);
        String mail=sharedPreferences.getString("Mail",null);



        if (mail!=null){
            LoadUser(mail);
        }

        return view;
    }


    public void Message(final String Uid){
        String url ="http://163.17.5.182/message.php";
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
                            posts = Arrays.asList(mGson.fromJson(response, ItemObject[].class));
                            adapter = new RecyclerViewAdapterMsg(v.getContext(), posts);
                            recyclerView.setAdapter(adapter);
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
                params.put("uid",Uid);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(stringRequest);
    }


    public void LoadUser(final String mail){
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
                            posts = Arrays.asList(mGson.fromJson(response, Item[].class));
                            List<Item> itemList=posts;
                            //讀取自己發的案子
                            Message(itemList.get(0).getUid());
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
        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(stringRequest);
    }

}
