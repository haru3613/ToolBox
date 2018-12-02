package com.cyut.toolbox.toolbox.Fragment;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cyut.toolbox.toolbox.R;
import com.cyut.toolbox.toolbox.SimpleDividerItemDecoration;
import com.cyut.toolbox.toolbox.adapter.RecyclerViewAdapterMyReport;
import com.cyut.toolbox.toolbox.adapter.RecyclerViewAdapterRating;
import com.cyut.toolbox.toolbox.connection.Backgorundwork;
import com.cyut.toolbox.toolbox.model.ItemObject;
import com.cyut.toolbox.toolbox.model.ItemRating;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.VolleyLog.TAG;
import static com.cyut.toolbox.toolbox.Fragment.MainFragment.KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends Fragment {
    private String uid;
    private View v;
    private RecyclerViewAdapterRating adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Button toolman,boss;
    private String classs,total,category;
    private TextView tv_total;

    public RatingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_rating, container, false);
        SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(KEY, MODE_PRIVATE);
        uid=sharedPreferences.getString("uid",null);

        initRatingView();
        FloatingActionButton floatingActionButton=getActivity().findViewById(R.id.fab);
        floatingActionButton.setVisibility(View.GONE);

        recyclerView = (RecyclerView)v.findViewById(R.id.rating_recyclerview);
        layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setHasFixedSize(false);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerView.setLayoutManager(layoutManager);
        classs="雇主";

        category="全部";

        bgwork load_rating=new bgwork(v.getContext());
        load_rating.execute(uid,"全部","http://163.17.5.182/app/avg_grade_boss.php");

        LoadEvaluation(uid,"http://163.17.5.182/app/load_my_boss_evaluation.php");

        toolman.setBackgroundResource(R.color.white);
        boss.setBackgroundResource(R.color.primaryDarkColor);

        toolman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classs="工具人";
                category="全部";
                toolman.setBackgroundResource(R.color.primaryDarkColor);
                boss.setBackgroundResource(R.color.white);
                bgwork load_rating=new bgwork(v.getContext());
                load_rating.execute(uid,"全部","http://163.17.5.182/app/avg_grade_toolman.php");
                LoadEvaluation(uid,"http://163.17.5.182/app/load_my_toolman_evaluation.php");
            }
        });

        boss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classs="雇主";
                category="全部";
                toolman.setBackgroundResource(R.color.white);
                boss.setBackgroundResource(R.color.primaryDarkColor);
                bgwork load_rating=new bgwork(v.getContext());
                load_rating.execute(uid,"全部","http://163.17.5.182/app/avg_grade_boss.php");
                LoadEvaluation(uid,"http://163.17.5.182/app/load_my_boss_evaluation.php");
            }
        });
        return v;
    }
    private void initRatingView(){
        ImageView pickup=v.findViewById(R.id.rating_pickup);
        ImageView delivery=v.findViewById(R.id.rating_delivery);
        ImageView debug=v.findViewById(R.id.rating_debug);
        ImageView homework=v.findViewById(R.id.rating_homework);
        ImageView life=v.findViewById(R.id.rating_life);
        ImageView repair=v.findViewById(R.id.rating_repair);
        tv_total=v.findViewById(R.id.rating_total);
        toolman=v.findViewById(R.id.toolman);
        boss=v.findViewById(R.id.boss);

        pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="";
                category="接送";
                if (classs.equals("雇主"))
                    url="http://163.17.5.182/app/avg_grade_boss.php";
                else
                    url="http://163.17.5.182/app/avg_grade_toolman.php";

                bgwork load_rating=new bgwork(v.getContext());
                load_rating.execute(uid,"接送",url);
            }
        });
        repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="";
                category="修繕";
                if (classs.equals("雇主"))
                    url="http://163.17.5.182/app/avg_grade_boss.php";
                else
                    url="http://163.17.5.182/app/avg_grade_toolman.php";
                bgwork load_rating=new bgwork(v.getContext());
                load_rating.execute(uid,"修繕",url);
            }
        });
        life.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="";
                category="日常";
                if (classs.equals("雇主"))
                    url="http://163.17.5.182/app/avg_grade_boss.php";
                else
                    url="http://163.17.5.182/app/avg_grade_toolman.php";
                bgwork load_rating=new bgwork(v.getContext());
                load_rating.execute(uid,"日常",url);
            }
        });
        homework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="";
                category="課業";
                if (classs.equals("雇主"))
                    url="http://163.17.5.182/app/avg_grade_boss.php";
                else
                    url="http://163.17.5.182/app/avg_grade_toolman.php";
                bgwork load_rating=new bgwork(v.getContext());
                load_rating.execute(uid,"課業",url);
            }
        });
        debug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="";
                category="除蟲";
                if (classs.equals("雇主"))
                    url="http://163.17.5.182/app/avg_grade_boss.php";
                else
                    url="http://163.17.5.182/app/avg_grade_toolman.php";
                bgwork load_rating=new bgwork(v.getContext());
                load_rating.execute(uid,"除蟲",url);
            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="";
                category="外送";
                if (classs.equals("雇主"))
                    url="http://163.17.5.182/app/avg_grade_boss.php";
                else
                    url="http://163.17.5.182/app/avg_grade_toolman.php";
                bgwork load_rating=new bgwork(v.getContext());
                load_rating.execute(uid,"外送",url);
            }
        });
    }


    private class bgwork extends AsyncTask<String , Void , String>{
        Context context;
        private bgwork (Context ctx){
            this.context = ctx;
        }
        @Override
        protected void onPreExecute() {
            //執行前 設定可以在這邊設定
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            //執行中 在背景做事情

            try {
                String uid=params[0];
                String category=params[1];
                String avg_url = params[2];
                URL url = new URL(avg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("category", "UTF-8") + "=" + URLEncoder.encode(category,"UTF-8") + "&" +
                        URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(uid, "UTF-8");

                Log.d("POST_DATA", "doInBackground: " + post_data);
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String result = "";
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //執行中 可以在這邊告知使用者進度
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            //執行後 完成背景任務
            Log.d(TAG, "onPostExecute: "+result);



            if (result==null || result.equals("")){
                tv_total.setText("0");
                Toast.makeText(v.getContext(),"你尚未獲得評價",Toast.LENGTH_SHORT).show();
            }
            else if (category.equals("全部")){
                tv_total.setText(result.substring(0,3));
            }else{
                String[] s;
                s=result.split(";");
                showRatingDialog(s[0],category,s[1]);
            }
        }
    }


    private void showRatingDialog(String value,String category,String count){
        Log.d(ContentValues.TAG, "接案人:"+uid);
        Log.d(ContentValues.TAG, "分類:"+category);
        boolean wrapInScrollView = true;
        MaterialDialog dialog=new MaterialDialog.Builder(v.getContext())
                .customView(R.layout.dialog_rating_value, wrapInScrollView)
                .backgroundColorRes(R.color.colorBackground)
                .build();
        final View item = dialog.getCustomView();
        TextView pr=item.findViewById(R.id.pr_value);
        RatingBar ratingBar=item.findViewById(R.id.value_ratingbar);
        ImageView imageView=item.findViewById(R.id.value_category);
        switch (category) {
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

        if (!value.equals("")){
            ratingBar.setRating(Float.parseFloat(value.substring(0,3)));
        }
        int c=Integer.parseInt(count);
        if (c>10){
        }else{
            pr.setText("由於案件低於十件，所以尚未進行排名");
        }


        dialog.show();
    }

    public void LoadEvaluation(final String uid,String url){
        Log.d(ContentValues.TAG, "uid："+uid);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response:",response);
                        try {
                            byte[] u = response.getBytes(
                                    "UTF-8");
                            response = new String(u, "UTF-8");
                            Log.d(ContentValues.TAG, "Response " + response);
                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();
                            Type listType = new TypeToken<ArrayList<ItemRating>>() {}.getType();
                            ArrayList<ItemRating> posts = new ArrayList<ItemRating>();
                            if (!response.contains("Undefined")) {
                                posts = mGson.fromJson(response, listType);
                            }
                            if (posts.isEmpty()){
                                Toast.makeText(v.getContext(),"尚未有人幫你評分",Toast.LENGTH_SHORT).show();
                            }else{
                                adapter = new RecyclerViewAdapterRating(v.getContext(), posts,uid);
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
}
