package com.cyut.toolbox.toolbox;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.gson.reflect.TypeToken;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
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
public class SendFragment extends Fragment implements SearchView.OnQueryTextListener{
    private View view;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    protected static RecyclerViewAdapterMsg adapter;
    String uid;
    View v;
    String SearchString;
    public static final String KEY = "STATUS";
    public SendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // What i have added is this
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_send, container, false);
        v=view;
        uid="";
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(KEY, MODE_PRIVATE);
        uid=sharedPreferences.getString("uid",null);
        recyclerView = (RecyclerView)view.findViewById(R.id.rv_message);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(view.getContext()));
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);



        FloatingActionButton floatingActionButton=(FloatingActionButton)getActivity().findViewById(R.id.fab);
        floatingActionButton.setVisibility(View.GONE);



        Message(uid);



        return view;
    }


    public void Message(final String uid){
        Log.d(TAG, "Message: uid"+uid);
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
                            Type listType = new TypeToken<ArrayList<ItemObject>>() {}.getType();
                            ArrayList<ItemObject> posts = new ArrayList<ItemObject>();
                            if (!response.contains("Undefined")){
                                posts = mGson.fromJson(response, listType);
                            }

                            if (posts.isEmpty()){
                                Toast.makeText(v.getContext(),"尚無案件",Toast.LENGTH_SHORT).show();
                            }else{
                                adapter = new RecyclerViewAdapterMsg(v.getContext(), posts,uid);
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
                params.put("uid",uid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(stringRequest);
    }
    //Reload Fragment
    public void Reload(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }



    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: !");
        inflater.inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView sv = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(this);
        sv.setIconifiedByDefault(false);
        sv.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Reload();
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;  // Return true to expand action view
            }
        });

        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SearchString=item.toString();
        switch (item.getItemId()) {
            case R.id.refresh:
                SearchV(SearchString);
                return true;
            case R.id.nav_1:
                SearchV(SearchString);
                Log.d(TAG, item.toString());
                return true;
            case R.id.nav_2:
                SearchV(SearchString);
                return true;
            case R.id.nav_3:
                SearchV(SearchString);
                return true;
            case R.id.nav_4:
                SearchV(SearchString);
                return true;
            case R.id.nav_5:
                SearchV(SearchString);
                return true;
            case R.id.nav_6:
                SearchV(SearchString);
                return true;
            case R.id.nav_7:
                Message(uid);
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);

    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        SearchQuery(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(!newText.equals("")){
            //TODO 字串篩選
        }
        return false;
    }

    public void SearchV(final String SearchString){
        Log.d(TAG, "SearchV: "+SearchString);
        Log.d(TAG, "SearchV: uid"+uid);
        String url ="http://163.17.5.182/app/message_search.php";
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
                            Type listType = new TypeToken<ArrayList<ItemObject>>() {}.getType();
                            ArrayList<ItemObject> posts = new ArrayList<ItemObject>();
                            if (!response.contains("Undefined")) {
                                posts = mGson.fromJson(response, listType);
                                adapter = new RecyclerViewAdapterMsg(view.getContext(), posts,uid);
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
                params.put("search",SearchString);
                params.put("uid",uid);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);
    }



    public void SearchQuery(final String SearchString){
        Log.d(TAG, "SearchQuery: "+SearchString);
        Log.d(TAG, "SearchV: uid"+uid);
        String url ="http://163.17.5.182/app/message_querysearch.php";
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
                            Type listType = new TypeToken<ArrayList<ItemObject>>() {}.getType();
                            ArrayList<ItemObject> posts = new ArrayList<ItemObject>();
                            if (!response.contains("Undefined")) {
                                posts = mGson.fromJson(response, listType);
                                adapter = new RecyclerViewAdapterMsg(view.getContext(), posts,uid);
                                recyclerView.setAdapter(adapter);
                            }else{
                                Toast.makeText(getContext(),"沒有搜尋到相關案件",Toast.LENGTH_SHORT).show();
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
                params.put("search",SearchString);
                params.put("uid",uid);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);
    }

}
