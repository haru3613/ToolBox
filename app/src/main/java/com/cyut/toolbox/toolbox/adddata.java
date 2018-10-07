package com.cyut.toolbox.toolbox;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyut.toolbox.toolbox.adapter.RecyclerViewAdapterCategory;
import com.cyut.toolbox.toolbox.connection.Send_Data_Backworker;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class adddata extends AppCompatActivity{
    private AsyncHttpClient client;
    private TextView text_viewtime, text_until_time, text_end_until_time, text_case_done_viewtime1, text_case_done_viewtime2, text_view_done_type, textView2;
    private static final int msgKey1 = 1;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private String category;
    private TimePickerDialog timePickerDialog;
    private RadioButton rb_cmp_non, rb_cmp_one, rb_cmp_local;
    private EditText edt_title,edt_money,edt_detail,spinner_other;
    Spinner  spinner_local, spinner_road;
    private Button bt_timeout, bt_case_done_A, bt_case_done_B,bt_sendcase;
    String spinner_road_data;
    int c_end_hours = 0;
    int c_end_mins = 0;
    int time_check_status = 0;
    int end_Minute = 0;
    int end_Hours = 0;
    int end_Day = 0;
    int end_Way = 0;
    public static final String KEY = "STATUS";
    String uid;
    int casedone_hours = 0,casedone_mins = 0,casedone_Day = 0,casedone_Year = 0,casedone_Month;
    String case_disapear_hours,case_disapear_mins,case_disapear_Day,case_disapear_Year,case_disapear_Month,case_disapear_Sec;
    int case_finish_start_hours,case_finish_start_mins,case_finish_start_Day,case_finish_start_Year,case_finish_start_Month,case_finish_start_Sec;
    int case_finish_end_hours,case_finish_end_mins,case_finish_end_Day,case_finish_end_Year,case_finish_end_Month,case_finish_end_Sec;
    String date_time_1 = "";
    String date_time_2 = "";
    String erroMessage="";
    String radioCheck="0";
    MaterialDialog.Builder alertDialog;
    private RecyclerViewAdapterCategory adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddata);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        client = new AsyncHttpClient();
        //--授權---------------------------
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //--------------------------------

        //---------連結前端物件-------------------
        //EditText
        edt_title =(EditText)findViewById(R.id.edt_title);
        edt_money =(EditText)findViewById(R.id.edt_money);
        edt_detail =(EditText)findViewById(R.id.edt_detail);
        spinner_other=(EditText)findViewById(R.id.spinner_other);
        //spinner
        spinner_local = (Spinner) findViewById(R.id.spinner_local);
        spinner_road = (Spinner) findViewById(R.id.spinner_road);
        //TextView
        text_viewtime = (TextView) findViewById(R.id.text_viewtime);
        text_until_time = (TextView) findViewById(R.id.text_until_time);
        textView2 = (TextView) findViewById(R.id.textView2);
        text_end_until_time = (TextView) findViewById(R.id.text_end_until_time);
        text_case_done_viewtime1 = findViewById(R.id.text_case_done_viewtime1);
        text_case_done_viewtime2 = findViewById(R.id.text_case_done_viewtime2);
        text_view_done_type = findViewById(R.id.text_view_done_type);
        //RadioButton
        rb_cmp_non = (RadioButton) findViewById(R.id.rb_cmp_non);
        rb_cmp_one = (RadioButton) findViewById(R.id.rb_cmp_one);
        rb_cmp_local = (RadioButton) findViewById(R.id.rb_cmp_local);
        //Button
        bt_case_done_A = findViewById(R.id.bt_case_done_A);
        bt_case_done_B = findViewById(R.id.bt_case_done_B);
        bt_sendcase = findViewById(R.id.bt_sendcase);
        //--------------------------------------------------------------
        //--spinner類別Open~

        town_spinner();
        road_spinner();
        category="";
        ArrayList<String> categorylist=new ArrayList<String>();

        categorylist.add("日常");
        categorylist.add("接送");
        categorylist.add("外送");
        categorylist.add("課業");
        categorylist.add("修繕");
        categorylist.add("除蟲");


        LinearLayoutManager layoutManager
                = new LinearLayoutManager(adddata.this, LinearLayoutManager.HORIZONTAL, false);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.category);
        recyclerView.setBackgroundColor(Color.parseColor("#dfffffff"));
        recyclerView.setLayoutManager(layoutManager);
        Log.d(TAG, "list: "+categorylist);
        adapter=new RecyclerViewAdapterCategory(adddata.this,categorylist);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecylerItemClickListener(adddata.this, recyclerView, new RecylerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                category=adapter.itemList.get(position);
                adapter.setAlpha(position);
                Toast.makeText(adddata.this,"你選擇："+adapter.itemList.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));

        uid="";
        SharedPreferences sharedPreferences = adddata.this.getSharedPreferences(KEY, MODE_PRIVATE);
        uid=sharedPreferences.getString("uid",null);




        //--------------------------------------------------------------
        spinner_local.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                String selected_town = spinner_local.getSelectedItem().toString();
                Toast.makeText(adddata.this, "您選擇" + selected_town, Toast.LENGTH_SHORT).show();
                Log.d("TAG", "我有選擇" + selected_town);
                RoadProesss(selected_town);
            }

            @Override
            public void onNothingSelected(AdapterView arg0) {
                road_spinner();
                Log.d("TAG", "我沒有選擇QQ");
            }
        });
        //--時鐘開啟
        new TimeThread().start();
        //-----------Radio Button 監聽器
        RadioGroup rb_group_comp = (RadioGroup) findViewById(R.id.rb_group_comp);
        rb_group_comp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int p = group.indexOfChild((RadioButton) findViewById(checkedId));
                int count = group.getChildCount();
                switch (checkedId) {
                    case (R.id.rb_cmp_non):
                        radioCheck="1";
                        textView2.setText("");
                        text_view_done_type.setText("不指定案件的有效時間");
                        bt_case_done_A.setVisibility(View.INVISIBLE);
                        bt_case_done_B.setVisibility(View.INVISIBLE);
                        text_case_done_viewtime1.setText("");
                        text_case_done_viewtime2.setText("");
                        Log.d("TAG", "Non");
                        break;
                    case (R.id.rb_cmp_one):
                        radioCheck="2";
                        textView2.setText("");
                        text_case_done_viewtime2.setText("");
                        bt_case_done_A.setVisibility(View.VISIBLE);
                        bt_case_done_B.setVisibility(View.INVISIBLE);
                        bt_case_done_A.setText("設定指定完成時間");
                        text_view_done_type.setText("以下為您指定的完成時間");
                        Log.d("TAG", "one");
                        break;
                    case (R.id.rb_cmp_local):
                        radioCheck="3";
                        textView2.setText("~");
                        bt_case_done_A.setText("設定起始區間");
                        bt_case_done_B.setText("設定最終區間");
                        bt_case_done_A.setVisibility(View.VISIBLE);
                        bt_case_done_B.setVisibility(View.VISIBLE);
                        text_view_done_type.setText("以下為您指定的完成區間");
                        Log.d("TAG", "local");
                        break;
                }
            }
        });


        bt_sendcase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkall=0;
                int Money=0;
                String send_Money="0";
                String type="check_ok";
                String send_class=category;
                String send_Title=edt_title.getText().toString();
                if (send_Title.length()<4){
                    erroMessage=erroMessage+"標題至少為四個字唷\n";
                }else if (send_class.equals("")){
                    erroMessage=erroMessage+"您尚未選擇分類";
                }
                if (!edt_money.getText().toString().equals("")){
                    Money=Integer.parseInt(edt_money.getText().toString());
                    if (Money<20){
                        erroMessage=erroMessage+"金額必須為20元以上唷\n";
                    }
                    else {
                        send_Money= String.valueOf(Money);
                    }
                }else{
                    erroMessage=erroMessage+"金額記得填唷\n";
                }

                String send_city="台中市";
                String send_town=spinner_local.getSelectedItem().toString();
                String send_road=spinner_road.getSelectedItem().toString();
                String send_otherdetail=spinner_other.getText().toString();
                if (send_otherdetail.length()<2){
                    erroMessage=erroMessage+"詳細地址至少需要2個字唷\n";
                }
                String send_detail=edt_detail.getText().toString();
                if (send_detail.length()<10){
                    erroMessage=erroMessage+"詳細工作內容至少需要10個字唷\n";
                }
                String send_case_disapear=toTimeFormat(case_disapear_Year,case_disapear_Month,case_disapear_Day,case_disapear_hours,case_disapear_mins,case_disapear_Sec);
                String send_case_finish_start=toTimeFormat1(case_finish_start_Year,case_finish_start_Month,case_finish_start_Day,case_finish_start_hours,case_finish_start_mins,case_finish_start_Sec);
                String send_case_finish_end=toTimeFormat1(case_finish_end_Year,case_finish_end_Month,case_finish_end_Day,case_finish_end_hours,case_finish_end_mins,case_finish_end_Sec);
                switch (radioCheck) {
                    case ("1"):
                        send_case_finish_start="";
                        send_case_finish_end="";
                        break;
                    case("2"):
                        send_case_finish_end=send_case_finish_start;
                        send_case_finish_start="";
                        break;
                    case("3"):
                        if (send_case_finish_start.equals("0000-00-00 00:00:00")||send_case_finish_end.equals("0000-00-00 00:00:00")){
                            erroMessage=erroMessage+"案件完成區間有尚未填到的唷\n";
                        }
                        break;
                }
                if (erroMessage.equals("")){
                    Send_Data_Backworker SDB = new Send_Data_Backworker(adddata.this);
                    Log.d(TAG, "onClick: uid"+uid);
                    SDB.execute(type, send_class,send_Title,send_Money,send_city,send_town,send_road,send_otherdetail,send_detail,send_case_disapear,send_case_finish_start,send_case_finish_end,uid);
                    Intent intent=new Intent(adddata.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{


                    alertDialog = new MaterialDialog.Builder(adddata.this)
                            .title("發案內容錯誤")
                            .content("****請檢查以下有可能的錯誤:****\n"+erroMessage)
                            .positiveText("確定");
                    alertDialog.backgroundColorRes(R.color.colorBackground);
                    MaterialDialog dialog = alertDialog.build();
                    dialog.show();

                    erroMessage="";
                }

            }


        });
    }


    //----------------------------------------------------------------------------------



    //------------------------跟php要鄉鎮資料 to Spinner_town----------------------------------------
    private void town_spinner() {
        ArrayAdapter<CharSequence> a = ArrayAdapter.createFromResource(adddata.this,
                R.array.area01,
                R.layout.spinner_item);
        spinner_local.setAdapter(a);

    }

    //--------------------Road Spinner --------------------------
    private void road_spinner() {
        ArrayList<String> Arry_road = new ArrayList<String>();
        Arry_road.add("路街名稱");
        ArrayAdapter<String> road = new ArrayAdapter<>(adddata.this, android.R.layout.simple_dropdown_item_1line, Arry_road);
        spinner_road.setAdapter(road);
    }


    private void RoadProesss(String select){
        switch (select){
            case "大甲區":
                ArrayAdapter<CharSequence> a = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area01_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(a);
                break;
            case "大安區":
                ArrayAdapter<CharSequence> b = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area02_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(b);
                break;
            case "大肚區":
                ArrayAdapter<CharSequence> c = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area03_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(c);
                break;
            case "大里區":
                ArrayAdapter<CharSequence> d = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area04_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(d);
                break;
            case "大雅區":
                ArrayAdapter<CharSequence> e = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area05_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(e);
                break;
            case "中區":
                ArrayAdapter<CharSequence> f = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area06_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(f);
                break;
            case "太平區":
                ArrayAdapter<CharSequence> g = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area07_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(g);
                break;
            case "北區":
                ArrayAdapter<CharSequence> h = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area08_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(h);
                break;
            case "北屯區":
                ArrayAdapter<CharSequence> i = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area09_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(i);
                break;
            case "外埔區":
                ArrayAdapter<CharSequence> j = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area10_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(j);
                break;
            case "石岡區":
                ArrayAdapter<CharSequence> k = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area11_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(k);
                break;
            case "后里區":
                ArrayAdapter<CharSequence> l = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area12_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(l);
                break;
            case "西區":
                ArrayAdapter<CharSequence> m = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area13_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(m);
                break;
            case "西屯區":
                ArrayAdapter<CharSequence> n = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area14_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(n);
                break;
            case "沙鹿區":
                ArrayAdapter<CharSequence> o = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area15_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(o);
                break;
            case "和平區":
                ArrayAdapter<CharSequence> p = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area16_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(p);
                break;
            case "東區":
                ArrayAdapter<CharSequence> q = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area17_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(q);
                break;
            case "東勢區":
                ArrayAdapter<CharSequence> r = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area18_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(r);
                break;
            case "南區":
                ArrayAdapter<CharSequence> s = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area19_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(s);
                break;
            case "南屯區":
                ArrayAdapter<CharSequence> t = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area20_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(t);
                break;
            case "烏日區":
                ArrayAdapter<CharSequence> u = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area21_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(u);
                break;

            case "神岡區":
                ArrayAdapter<CharSequence> v = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area22_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(v);
                break;

            case "梧棲區":
                ArrayAdapter<CharSequence> w = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area23_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(w);
                break;

            case "清水區":
                ArrayAdapter<CharSequence> x = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area24_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(x);
                break;

            case "新社區":
                ArrayAdapter<CharSequence> y = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area25_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(y);
                break;

            case "潭子區":
                ArrayAdapter<CharSequence> z = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area26_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(z);
                break;

            case "龍井區":
                ArrayAdapter<CharSequence> ab = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area27_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(ab);
                break;

            case "豐原區":
                ArrayAdapter<CharSequence> ac = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area28_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(ac);
                break;

            case "霧峰區":
                ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(adddata.this,
                        R.array.area29_road,
                        R.layout.spinner_item);
                spinner_road.setAdapter(ad);
                break;
        }

    }

    //-----------------------------------------------------------
    public void bt_complete_A(View view) {
        Complete_case_datePicker1();
    }

    public void bt_complete_B(View view) {
        Complete_case_datePicker2();
    }
    private void Complete_case_datePicker1(){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        casedone_Year = c.get(Calendar.YEAR);
        casedone_Month = c.get(Calendar.MONTH);
        casedone_Day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                        date_time_1 =year + "年"+(monthOfYear + 1) + "月" +dayOfMonth+"號";
                        case_finish_start_Year=year;
                        case_finish_start_Month=monthOfYear+1;
                        case_finish_start_Day=dayOfMonth;
                        //*************Call Time Picker Here ********************
                        Complete_case_tiemPicker1();
                    }
                },  casedone_Year,  casedone_Month,  casedone_Day);
        datePickerDialog.show();

    }
    private void Complete_case_tiemPicker1(){
        final Calendar c = Calendar.getInstance();
        casedone_hours = c.get(Calendar.HOUR_OF_DAY);
        casedone_mins = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                        casedone_hours = hourOfDay;
                        casedone_mins = minute;
                        case_finish_start_hours=hourOfDay;
                        case_finish_start_mins=minute;
                        date_time_2=date_time_1+hourOfDay + "點" + minute+"分";
                        text_case_done_viewtime1.setText(date_time_2);
                    }
                }, casedone_hours, casedone_mins, true);
        timePickerDialog.show();
    }
    private void Complete_case_datePicker2(){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        casedone_Year = c.get(Calendar.YEAR);
        casedone_Month = c.get(Calendar.MONTH);
        casedone_Day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                        date_time_1 =year + "年"+(monthOfYear + 1) + "月" +dayOfMonth+"號";
                        case_finish_end_Year=year;
                        case_finish_end_Month=monthOfYear+1;
                        case_finish_end_Day=dayOfMonth;
                        //*************Call Time Picker Here ********************
                        Complete_case_tiemPicker2();
                    }
                },  casedone_Year,  casedone_Month,  casedone_Day);
        datePickerDialog.show();
    }
    private void Complete_case_tiemPicker2(){
        final Calendar c = Calendar.getInstance();
        casedone_hours = c.get(Calendar.HOUR_OF_DAY);
        casedone_mins = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                        casedone_hours = hourOfDay;
                        casedone_mins = minute;
                        case_finish_end_hours=hourOfDay;
                        case_finish_end_mins=minute;
                        date_time_2=date_time_1+hourOfDay + "點" + minute+"分";
                        text_case_done_viewtime2.setText(date_time_2);
                    }
                }, casedone_hours, casedone_mins, true);
        timePickerDialog.show();
    }


    //--------------------Now_Time-----------------------------
    public class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    @SuppressLint("HandlerLeak")
    //--------------Now time text view---------
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    text_viewtime.setText(getTime());
                    break;
                default:
                    break;
            }
        }
    };

    //now time _get time
    public String getTime() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR));
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        String fWay = end_Day(mWay);
        String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mMinute = String.valueOf(c.get(Calendar.MINUTE));
        String mSecond = String.valueOf(c.get(Calendar.SECOND));
        return mYear + "年" + mMonth + "月" + mDay + "日" + "  " + "星期" + fWay + "  " + mHour + ":" + mMinute + ":" + mSecond;
    }

    //---------------- SetTimeout--------Choose the time to end case-----------------------------------------------------
    public void timeout(View view) {
        this.c_end_hours = 0;
        this.c_end_mins = 0;
        this.time_check_status = 0;
        this.end_Minute = 0;
        this.end_Hours = 0;
        this.end_Day = 0;
        this.end_Way = 0;
        bt_timeout = (Button) findViewById(R.id.bt_timeout);
        text_end_until_time.setText("正在設定時間");
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // Create a new instance of TimePickerDialog and return it
        new TimePickerDialog(adddata.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                text_until_time.setText("您設定的案件有效時間:" + hourOfDay + "個小時" + minute + "分");
                c_end_hours = hourOfDay;
                c_end_mins = minute;
                Log.d("Tag", "get_time_end_hour=" + c_end_hours + "min=" + c_end_mins);
            }
        }, hour, minute, true).show();
        time_check_status = 0;
        new end_TimeThread().start();
    }


    //--------------------------------Comepelete choose time ---------------------------------------------------------
    public class end_TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    end_mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    @SuppressLint("HandlerLeak")
    //--------------text view---------
    private Handler end_mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    if (c_end_hours != 0 && c_end_mins != 0) {
                        text_end_until_time.setText(end_getTime());
                    } else {
                        text_end_until_time.setText("");
                    }

                    break;
                default:
                    break;
            }
        }
    };
    //get time


    public String end_getTime() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        if (time_check_status == 0) {
            int end_Minute = 0;
            int end_Hours = 0;
            int end_Day = 0;
            int end_Way = 0;
            int m_add_Hour = 0;

            int d_Minute = c.get(Calendar.MINUTE) + c_end_mins;
            //-------最後要加總或減的值---------------------

            //-----------判斷是否超出常理時間------------------
            if (d_Minute > 59) {
                m_add_Hour = 1;
                end_Minute = d_Minute - 60;
                this.end_Minute = end_Minute;
            } else {

                end_Minute = d_Minute;
                this.end_Minute = end_Minute;
            }
            int d_Hour = c.get(Calendar.HOUR_OF_DAY) + c_end_hours + m_add_Hour;
            if (d_Hour >= 24) {
                end_Hours = Math.abs(d_Hour - 24);
                this.end_Hours = end_Hours;
                end_Day = 1;
                this.end_Day = end_Day;
                end_Way = 1;
                this.end_Way = end_Way;
            } else {
                end_Hours = d_Hour;
                this.end_Hours = end_Hours;
            }
            time_check_status = 1;
        }


        String fHour = String.valueOf(end_Hours);
        String fMinute = String.valueOf(end_Minute);
        String fDay = end_Day(String.valueOf(c.get(Calendar.DAY_OF_MONTH) + this.end_Day));
        String fWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK) + this.end_Way - 1);
        String mYear = String.valueOf(c.get(Calendar.YEAR));
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);
        String mSecond = String.valueOf(c.get(Calendar.SECOND));
        this.case_disapear_Year=mYear;
        this.case_disapear_Month=mMonth;
        this.case_disapear_Day=fDay;
        this.case_disapear_hours=fHour;
        this.case_disapear_mins=fMinute;
        this.case_disapear_Sec=mSecond;

        return mYear + "年" + mMonth + "月" + fDay + "日" + "  " + "星期" + fWay + "  " + fHour + ":" + fMinute + ":" + mSecond;


    }

    public String end_Day(String mWay) {
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mWay;
    }


    //---------------------------------------send_data to php and database----------------------------------------------------------

    public String toTimeFormat(String y, String month, String day, String hour, String min, String sec){
        String formatetime=y+"-"+month+"-"+day+" "+hour+":"+min+":"+sec;
        return formatetime;
    }
    public String toTimeFormat1(int y, int month, int day, int hour, int min, int sec){
        String formatetime=y+"-"+month+"-"+day+" "+hour+":"+min+":"+sec;
        return formatetime;
    }



}



