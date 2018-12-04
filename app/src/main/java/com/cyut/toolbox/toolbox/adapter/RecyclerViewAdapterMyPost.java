package com.cyut.toolbox.toolbox.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
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
import com.cyut.toolbox.toolbox.ChatroomActivity;
import com.cyut.toolbox.toolbox.R;
import com.cyut.toolbox.toolbox.RecyclerViewHolders;
import com.cyut.toolbox.toolbox.RecyclerViewHoldersMyPost;
import com.cyut.toolbox.toolbox.SimpleDividerItemDecoration;
import com.cyut.toolbox.toolbox.connection.Backgorundwork;
import com.cyut.toolbox.toolbox.model.Item;
import com.cyut.toolbox.toolbox.model.ItemMsg;
import com.cyut.toolbox.toolbox.model.ItemObject;
import com.cyut.toolbox.toolbox.model.ItemRating;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class RecyclerViewAdapterMyPost extends RecyclerView.Adapter<RecyclerViewHoldersMyPost> {
    public ArrayList<ItemObject> itemList;
    private Context context;
    private String uid,report_reason;
    private RecyclerViewAdapterMsgDetail adapter;
    private RecyclerView recyclerView;
    private View item_report;
    private LinearLayoutManager layoutManager;
    private RecyclerViewAdapterRating adapterRating;
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
            holder.Title.setText(itemList.get(position).getTitle().substring(0,8)+"\n"+itemList.get(position).getTitle().substring(8));
        }else{
            holder.Title.setText(itemList.get(position).getTitle());
        }

        String Address=itemList.get(position).getCity()+itemList.get(position).getTown()+itemList.get(position).getRoad();

        holder.Area.setText(Address);

        LoadEvaluation(itemList.get(position).getPid(),holder);

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
                    backgorundwork.execute("finish_case",itemList.get(position).getCid(),"評價中",itemList.get(position).getMoney(),itemList.get(position).getRid());
                    itemList.get(position).setStatus("評價中");
                    notifyItemChanged(position);
                    Toast.makeText(context,"請稍後...",Toast.LENGTH_SHORT).show();
                    try {
                        line_notify(itemList.get(position).getRid(),"https://a238c12f.ngrok.io/send_lineNotify",itemList.get(position).getCid(),"case_score");

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Toast.makeText(context,"請稍後...",Toast.LENGTH_SHORT).show();
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
            holder.tomessage.setVisibility(View.VISIBLE);
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
        holder.ratingBar.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.progress.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.tool_title.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.rating_title.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.view_rating.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.report.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.linearLayout.setVisibility(View.GONE);
        holder.itemView.setActivated(isExpanded);
        if (isExpanded)
            previousExpandedPosition = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemList.get(position).getStatus().equals("評價中")){
                    showRatingDialog(itemList.get(position).getRid(),itemList.get(position).getCategoryImage(),itemList.get(position).getCid(),itemList.get(position).getPid());
                }else{
                    mExpandedPosition = isExpanded ? -1:position;
                    notifyItemChanged(previousExpandedPosition);
                    notifyItemChanged(position);
                }

            }
        });

        holder.tomessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context,ChatroomActivity.class);
                intent.putExtra("cid", itemList.get(position).getCid());//此方式可以放所有基本型別
                if (itemList.get(position).getPid().equals(uid))
                    intent.putExtra("other_uid", itemList.get(position).getRid());
                else
                    intent.putExtra("other_uid", itemList.get(position).getPid());
                context.startActivity(intent);
                Toast.makeText(context, "即將開啟聊天室", Toast.LENGTH_SHORT).show();
            }
        });

        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.linearLayout.setVisibility(View.VISIBLE);
            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportAlert(itemList.get(position).getCid(),uid,itemList.get(position).getPid());

            }
        });
        holder.view_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RatingAlert(itemList.get(position).getPid());
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

    private void ReportAlert(final String cid,final String uid,final String pid){
        boolean wrapInScrollView = true;

        item_report = LayoutInflater.from(context).inflate(R.layout.dialog_report, null);

        final MaterialDialog dialog =new MaterialDialog.Builder(context)
                .customView(item_report,wrapInScrollView )
                .backgroundColorRes(R.color.colorBackground)
                .build();

        ImageView send=item_report.findViewById(R.id.correct_send);
        TextView cancel=item_report.findViewById(R.id.cancel);
        RadioButton rb01=item_report.findViewById(R.id.rb01);
        RadioButton rb02=item_report.findViewById(R.id.rb02);
        RadioButton rb03=item_report.findViewById(R.id.rb03);
        RadioButton rb04=item_report.findViewById(R.id.rb04);
        RadioButton rb05=item_report.findViewById(R.id.rb05);
        RadioButton rb06=item_report.findViewById(R.id.rb06);

        EditText detail_content=item_report.findViewById(R.id.detail_content);

        rb01.setOnCheckedChangeListener(mOnCheckedChangeListener);
        rb02.setOnCheckedChangeListener(mOnCheckedChangeListener);
        rb03.setOnCheckedChangeListener(mOnCheckedChangeListener);
        rb04.setOnCheckedChangeListener(mOnCheckedChangeListener);
        rb05.setOnCheckedChangeListener(mOnCheckedChangeListener);
        rb06.setOnCheckedChangeListener(mOnCheckedChangeListener);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!report_reason.equals("")){
                    String Detail=detail_content.getText().toString();
                    Backgorundwork backgorundwork = new Backgorundwork(context);
                    backgorundwork.execute("insert_report",cid,uid,pid,report_reason,Detail);
                }else{
                    Toast.makeText(context,"請選擇你檢舉的原因",Toast.LENGTH_SHORT).show();
                }
            }
        });


        dialog.show();

    }

    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            EditText editText=item_report.findViewById(R.id.other_reason);
            switch (buttonView.getId()) {
                case R.id.rb01:
                    editText.setVisibility(View.INVISIBLE);
                    report_reason="案件含有不雅名稱";
                    break;
                case R.id.rb02:
                    editText.setVisibility(View.INVISIBLE);
                    report_reason="態度惡劣";
                    break;
                case R.id.rb03:
                    editText.setVisibility(View.INVISIBLE);
                    report_reason="持續騷擾";
                    break;
                case R.id.rb04:
                    editText.setVisibility(View.INVISIBLE);
                    report_reason="未達成工作需求";
                    break;
                case R.id.rb05:
                    editText.setVisibility(View.INVISIBLE);
                    report_reason="遲遲不肯按確認鍵";
                    break;
                case R.id.rb06:
                    editText.setVisibility(View.VISIBLE);
                    report_reason=editText.getText().toString();
                    break;
            }
        }
    };
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


    public void LoadEvaluation(final String uid,final RecyclerViewHoldersMyPost holder){
        Log.d(ContentValues.TAG, "uid："+uid);
        String url="http://163.17.5.182/app/load_my_boss_evaluation.php";
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
                                holder.ratingBar.setRating(0);
                            }else{
                                if (!TextUtils.isEmpty(posts.get(0).getGrade()))
                                    holder.ratingBar.setRating(Float.parseFloat(posts.get(0).getGrade()));
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

    private void RatingAlert(final String pid){
        boolean wrapInScrollView = true;

        final View item = LayoutInflater.from(context).inflate(R.layout.dialog_rating_view, null);

        final MaterialDialog dialog =new MaterialDialog.Builder(context)
                .title("雇主評價")
                .customView(item,wrapInScrollView )
                .backgroundColorRes(R.color.colorBackground)
                .build();

        recyclerView = (RecyclerView)item.findViewById(R.id.dialog_recyclerview);
        layoutManager = new LinearLayoutManager(item.getContext());
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setHasFixedSize(false);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(context));
        recyclerView.setLayoutManager(layoutManager);

        LoadEvaluation(pid);



        dialog.show();

    }
    private void showRatingDialog(String suid,String category,String cid,String pid){
        Log.d(TAG, "接案人:"+suid);
        Log.d(TAG, "分類:"+category);
        boolean wrapInScrollView = true;
        MaterialDialog dialog=new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_rating, wrapInScrollView)
                .backgroundColorRes(R.color.colorBackground)
                .build();
        final View item = dialog.getCustomView();
        RatingBar ratingBar=item.findViewById(R.id.ratingBar);
        EditText content=item.findViewById(R.id.content);
        Button send=item.findViewById(R.id.sendout);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Backgorundwork backgorundwork=new Backgorundwork(context);
                backgorundwork.execute("insert_rating",Float.toString(ratingBar.getRating()),content.getText().toString(),suid,category,uid,cid,pid);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    String getItemCid(int position) {
        return itemList.get(position).getCid();
    }
    public void LoadEvaluation(final String uid){
        Log.d(ContentValues.TAG, "uid："+uid);
        String url="http://163.17.5.182/app/load_my_boss_evaluation.php";
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
                                Toast.makeText(context,"尚未有人評分",Toast.LENGTH_SHORT).show();
                            }else{
                                adapterRating = new RecyclerViewAdapterRating(context, posts,uid);
                                recyclerView.setAdapter(adapterRating);
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
    private void line_notify(final String account,final String url,final String cid,final String type ) throws JSONException {
        HttpURLConnection urlConnection;


        JSONObject datas = new JSONObject();
        datas.put("caseID",cid);
        datas.put("account",account);
        datas.put("type",type);


        Log.d(TAG, "line_notify: "+datas);


        String data = datas.toString();
        String result = null;
        try {
            //Connect

            urlConnection = (HttpURLConnection) ((new URL(url).openConnection()));
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            //Write
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(data);
            writer.close();
            outputStream.close();

            //Read
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();


            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();
            result = sb.toString();
            urlConnection.disconnect();
            Log.d(TAG, "send_message: "+result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}