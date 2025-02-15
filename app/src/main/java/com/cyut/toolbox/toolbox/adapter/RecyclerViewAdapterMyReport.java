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
import com.cyut.toolbox.toolbox.RecyclerViewHoldersMyReport;
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

public class RecyclerViewAdapterMyReport extends RecyclerView.Adapter<RecyclerViewHoldersMyReport> {
    public ArrayList<ItemObject> itemList;
    private Context context;
    private String uid,report_reason;
    private View item_report;
    private RecyclerViewAdapterMsgDetail adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private String ServerUrl="http://35.194.171.235";
    private RecyclerViewAdapterRating adapterRating;
    public static final String KEY = "STATUS";
    private int mExpandedPosition=-1,previousExpandedPosition = -1;

    public RecyclerViewAdapterMyReport() {

    }

    public RecyclerViewAdapterMyReport(Context context, ArrayList<ItemObject> itemList, String uid) {
        this.itemList = itemList;
        this.context = context;
        this.uid=uid;
    }
    String name,mail;
    @Override
    public RecyclerViewHoldersMyReport onCreateViewHolder(ViewGroup parent, int viewType) {
        final View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_myreport, null,false);
        layoutView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        RecyclerViewHoldersMyReport rcv = new RecyclerViewHoldersMyReport(layoutView);

        return rcv;
    }
    @Override
    public void onBindViewHolder(RecyclerViewHoldersMyReport holder, final int position) {

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
        }else {
            holder.Title.setText(itemList.get(position).getTitle());
        }

        String Address=itemList.get(position).getCity()+itemList.get(position).getTown()+itemList.get(position).getRoad();

        holder.Area.setText(Address);



        String lineSep = System.getProperty("line.separator");
        String m=itemList.get(position).getDetail().replaceAll("<br />", lineSep);

        holder.message.setText(m);

        holder.Money.setText("$"+itemList.get(position).getMoney());

        String status=itemList.get(position).getStatus();
        holder.Status.setText(status);

        LoadUserName(itemList.get(position).getPid(),holder);

        String short_time=string_sub(itemList.get(position).getTime());
        String short_until=string_sub(itemList.get(position).getUntil());

        holder.time.setText(short_time+" 至  "+short_until);

        LoadEvaluation(itemList.get(position).getPid(),holder);

        if (status.equals("待接案")){
            holder.Status.setTextColor(Color.parseColor("#ff3333"));
        }else{
            holder.Status.setTextColor(Color.parseColor("#908e8d"));
        }




        final boolean isExpanded = position==mExpandedPosition;
        if (itemList.get(position).getStatus().equals("確認中")&&isExpanded){
           // holder.finished.setImageResource(R.drawable.unfinish);
            holder.finished.setVisibility(View.GONE);
            holder.tool.setVisibility(View.VISIBLE);
            holder.progress.setText("正在等待雇主進行確認");
            /*holder.finished.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Backgorundwork backgorundwork = new Backgorundwork(context);
                    backgorundwork.execute("case_status",itemList.get(position).getCid(),"進行中");
                    itemList.get(position).setStatus("進行中");
                    notifyItemChanged(position);
                }
            });*/

        }else if(itemList.get(position).getStatus().equals("待接案")&&isExpanded){
            layoutManager = new LinearLayoutManager(context);
            holder.tool.setVisibility(View.VISIBLE);
            holder.finished.setVisibility(View.GONE);
            holder.tomessage.setVisibility(View.GONE);
        }else if (itemList.get(position).getStatus().equals("進行中")&&isExpanded){
           // holder.finished.setImageResource(R.drawable.finished);
            holder.tool.setVisibility(View.VISIBLE);
            holder.finished.setVisibility(View.VISIBLE);
            holder.finished.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MaterialDialog dialog=new MaterialDialog.Builder(context)
                            .title("是否已完成")
                            .positiveText("確認")
                            .negativeText("未完成")
                            .content("請詳細檢查是否已完成，選擇完後即不可退回")
                            .backgroundColorRes(R.color.colorBackground)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    Backgorundwork backgorundwork = new Backgorundwork(context);
                                    backgorundwork.execute("case_status",itemList.get(position).getCid(),"確認中");
                                    itemList.get(position).setStatus("確認中");
                                    notifyItemChanged(position);
                                    Toast.makeText(context,"請稍後...",Toast.LENGTH_SHORT).show();
                                    try {

                                        line_notify(itemList.get(position).getPid(),"https://a238c12f.ngrok.io/send_lineNotify",itemList.get(position).getCid(),"case_confirm");

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(context,"請稍後...",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .build( );
                    dialog.show();
                }
            });
        }else if (itemList.get(position).getStatus().equals("已完成")&&isExpanded){
            holder.tool.setVisibility(View.VISIBLE);
            holder.finished.setVisibility(View.GONE);
            holder.progress.setText("已完成");
        }else{
            holder.finished.setVisibility(View.GONE);
            holder.tool.setVisibility(View.GONE);
        }
        holder.time_title.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.time.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.content.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.message.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.progress_title.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.progress.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.ratingBar.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.tool_title.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.rating_title.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.view_rating.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.tomessage.setVisibility(isExpanded?View.VISIBLE:View.GONE);
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
                if (!itemList.get(position).getStatus().equals("已完成"))
                    ReportAlert(itemList.get(position).getCid(),uid,itemList.get(position).getPid());
                else
                    Toast.makeText(context,"案件已經完成，不可以進行檢舉",Toast.LENGTH_SHORT).show();

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





    public void LoadUserName(final String uid,final RecyclerViewHoldersMyReport holder){
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
                    dialog.dismiss();
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
    private void showRatingDialog(String suid,String category,String cid,String pid){
        Log.d(TAG, "接案人:"+uid);
        Log.d(TAG, "分類:"+category);
        boolean wrapInScrollView = true;
        MaterialDialog dialog=new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_rating, wrapInScrollView)
                .backgroundColorRes(R.color.colorBackground)
                .build();
        final View item = dialog.getCustomView();
        TextView title=item.findViewById(R.id.rating_title);
        RatingBar ratingBar=item.findViewById(R.id.ratingBar);
        EditText content=item.findViewById(R.id.content);
        Button send=item.findViewById(R.id.sendout);

        content.setText("很棒的案件體驗!");
        title.setText("給予雇主評價");

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

    public void LoadEvaluation(final String uid,final RecyclerViewHoldersMyReport holder){
        Log.d(ContentValues.TAG, "uid："+uid);
        String url=ServerUrl+"/app/avg_grade_toolman.php";
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

                            if (response.isEmpty()){
                                holder.ratingBar.setRating(0);
                            }else{
                                if (!TextUtils.isEmpty(response)){
                                    Log.d(TAG, "onResponse: "+Float.parseFloat(response));
                                    holder.ratingBar.setRating(Float.parseFloat(response));
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
                params.put("category","全部");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public void LoadEvaluation(final String pid){
        Log.d(ContentValues.TAG, "uid："+pid);
        String url=ServerUrl+"/app/load_my_toolman_evaluation.php";
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
                            if (posts==null ||posts.isEmpty()){
                                Toast.makeText(context,"尚未有人評分",Toast.LENGTH_SHORT).show();
                            }else{
                                adapterRating = new RecyclerViewAdapterRating(context, posts,pid);
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
                params.put("uid",pid);
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