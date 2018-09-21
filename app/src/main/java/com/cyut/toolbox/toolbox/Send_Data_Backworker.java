package com.cyut.toolbox.toolbox;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by AiYu on 2018/3/2/002.
 */

public class Send_Data_Backworker extends AsyncTask<String,Void,String>{
    Context context;
    Send_Data_Backworker (Context ctx){
        context = ctx;
    }
    MaterialDialog.Builder alertDialog;
    @Override
    protected String doInBackground(String... strings) {
        //type, send_class,send_Title,send_Money,send_city,send_town,send_road,send_otherdetail,send_detail,send_case_disapear,send_case_finish_start,send_case_finish_end
        String type =strings[0];
        String send_class=strings[1];
        String send_Title=strings[2];
        String send_Money=strings[3];
        String send_city=strings[4];
        String send_town=strings[5];
        String send_road=strings[6];
        String send_otherdetail=strings[7];
        String send_detail=strings[8];
        String send_case_disapear=strings[9];
        String send_case_finish_start=strings[10];
        String send_case_finish_end=strings[11];
        String uid=strings[12];

        String login_url ="http://163.17.5.182/addcase_for_android.php";
        if(type.equals("check_ok")){
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data =
                        URLEncoder.encode("c_category","UTF-8")+"="+URLEncoder.encode(send_class,"UTF-8")+"&"
                                +URLEncoder.encode("c_title","UTF-8")+"="+URLEncoder.encode(send_Title,"UTF-8")+"&"
                                +URLEncoder.encode("c_money","UTF-8")+"="+URLEncoder.encode(send_Money,"UTF-8")+"&"
                                +URLEncoder.encode("c_city","UTF-8")+"="+URLEncoder.encode(send_city,"UTF-8")+"&"
                                +URLEncoder.encode("c_town","UTF-8")+"="+URLEncoder.encode(send_town,"UTF-8")+"&"
                                +URLEncoder.encode("c_road","UTF-8")+"="+URLEncoder.encode(send_road,"UTF-8")+"&"
                                +URLEncoder.encode("c_address","UTF-8")+"="+URLEncoder.encode(send_otherdetail,"UTF-8")+"&"
                                +URLEncoder.encode("c_detail","UTF-8")+"="+URLEncoder.encode(send_detail,"UTF-8")+"&"
                                +URLEncoder.encode("c_until","UTF-8")+"="+URLEncoder.encode(send_case_disapear,"UTF-8")+"&"
                                +URLEncoder.encode("setstart","UTF-8")+"="+URLEncoder.encode(send_case_finish_start,"UTF-8")+"&"
                                +URLEncoder.encode("setend","UTF-8")+"="+URLEncoder.encode(send_case_finish_end,"UTF-8")+"&"
                                +URLEncoder.encode("u_pid","UTF-8")+"="+URLEncoder.encode(uid,"UTF-8")+"&"
                                +URLEncoder.encode("u_rid","UTF-8")+"="+URLEncoder.encode(uid,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                String result="";
                String line=null;
                while((line = bufferedReader.readLine())!= null) {
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
        }
        return null;

    }
    //------------執行前設置
    @Override
    protected void onPreExecute() {
    }

    //-------------------背景完成後
    @Override
    protected void onPostExecute(String result) {
        alertDialog = new MaterialDialog.Builder(context)
                .title("發案結果")
                .content(result)
                .positiveText("確定");
        alertDialog.backgroundColorRes(R.color.colorBackground);
        MaterialDialog dialog = alertDialog.build();
        dialog.show();
        Toast.makeText(context, "發案成功", Toast.LENGTH_SHORT).show();

    }
    //------------------進度
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}

