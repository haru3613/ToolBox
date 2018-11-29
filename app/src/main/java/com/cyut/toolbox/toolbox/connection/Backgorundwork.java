package com.cyut.toolbox.toolbox.connection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyut.toolbox.toolbox.LoadingView;
import com.cyut.toolbox.toolbox.LoginActivity;
import com.cyut.toolbox.toolbox.adapter.RecyclerViewAdapter;
import com.cyut.toolbox.toolbox.adapter.RecyclerViewAdapterCol;
import com.cyut.toolbox.toolbox.changepassword;
import com.google.android.gms.common.util.IOUtils;


import org.json.JSONObject;

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

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

/**
 * Created by Haru on 2017/12/19.
 */

public class Backgorundwork extends AsyncTask<String,Void,String> {
    private Context context;
    private MaterialDialog.Builder alertDialog;
    public static final String KEY = "STATUS";
    private static final String ACTIVITY_TAG ="Logwrite";
    String usermail;
    public Backgorundwork (Context ctx){
        this.context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        Log.d(Backgorundwork.ACTIVITY_TAG,"Let's run~~~~");
        String type =params[0];

        String check_id_match_mail_url = "http://163.17.5.182/check_id_match_mail_url.php";
        String changepsw = "http://163.17.5.182/changepsw.php";
        if(type.equals("login")){
            Log.d(Backgorundwork.ACTIVITY_TAG,"login if run");
            try {
                String mail = params[1];
                String pwd = params[2];
                String login_url ="http://163.17.5.182/app/login.php";
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("u_mail","UTF-8")+"="+URLEncoder.encode(mail,"UTF-8")+"&"
                        +URLEncoder.encode("u_pwd","UTF-8")+"="+URLEncoder.encode(pwd,"UTF-8");
                Log.d("POST_DATA", "doInBackground: "+post_data);



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
        }else if(type.equals("signup")){
            String mail=params[1];
            String pwd=params[2];
            String identity=params[3];
            String sex=params[4];
            String name=params[5];
            String nickname=params[6];
            String phone=params[7];
            String address=params[8];
            String image=params[9];
            String verify=params[10];
            String verifyCode=params[11];
            String introduce=params[12];
            String server=params[13];

            Log.d("image src", "doInBackground: "+image);
            String sign_url ="http://163.17.5.182/app/register.php";
            try {

                URL url = new URL(sign_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("u_mail","UTF-8")+"="+URLEncoder.encode(mail,"UTF-8")+"&"+
                        URLEncoder.encode("u_pwd","UTF-8")+"="+URLEncoder.encode(pwd,"UTF-8")+"&"+
                        URLEncoder.encode("u_identity","UTF-8")+"="+URLEncoder.encode(identity,"UTF-8")+"&"+
                        URLEncoder.encode("u_image","UTF-8")+"="+URLEncoder.encode(image,"UTF-8")+"&"+
                        URLEncoder.encode("u_sex","UTF-8")+"="+URLEncoder.encode(sex,"UTF-8")+"&"+
                        URLEncoder.encode("u_name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                        URLEncoder.encode("u_nickname","UTF-8")+"="+URLEncoder.encode(nickname,"UTF-8")+"&"+
                        URLEncoder.encode("u_phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"+
                        URLEncoder.encode("u_address","UTF-8")+"="+URLEncoder.encode(address,"UTF-8")+"&"+
                        URLEncoder.encode("u_verify","UTF-8")+"="+URLEncoder.encode(verify,"UTF-8")+"&"+
                        URLEncoder.encode("u_verifyCode","UTF-8")+"="+URLEncoder.encode(verifyCode,"UTF-8")+"&"+
                        URLEncoder.encode("u_introduce","UTF-8")+"="+URLEncoder.encode(introduce,"UTF-8")+"&"+
                        URLEncoder.encode("u_server","UTF-8")+"="+URLEncoder.encode(server,"UTF-8");
                        Log.d("POST_DATA", "doInBackground: "+post_data);
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
        }else if(type.equals("collection")){
            String cid=params[1];
            String uid=params[2];

            String sign_url ="http://163.17.5.182/addcoll.php";
            try {

                URL url = new URL(sign_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("c_cid","UTF-8")+"="+URLEncoder.encode(cid,"UTF-8")+"&"+
                        URLEncoder.encode("u_uid","UTF-8")+"="+URLEncoder.encode(uid,"UTF-8")  ;                 ;
                Log.d("POST_DATA", "doInBackground: "+post_data);
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
        }else if(type.equals("sendmessage")){
            String cid=params[1];
            String uid=params[2];
            String message=params[3];
            String h=params[4];
            String m=params[5];

            String send_url ="http://163.17.5.182/sendmessage.php";
            try {

                URL url = new URL(send_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("c_cid","UTF-8")+"="+URLEncoder.encode(cid,"UTF-8")+"&"+
                        URLEncoder.encode("u_uid","UTF-8")+"="+URLEncoder.encode(uid,"UTF-8")+"&"+
                        URLEncoder.encode("m_message","UTF-8")+"="+URLEncoder.encode(message,"UTF-8")+"&"+
                        URLEncoder.encode("c_until_h","UTF-8")+"="+URLEncoder.encode(h,"UTF-8")+"&"+
                        URLEncoder.encode("c_until_m","UTF-8")+"="+URLEncoder.encode(m,"UTF-8");
                Log.d("POST_DATA", "doInBackground: "+post_data);
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
        }else if(type.equals("deleteColl")) {
            String c_cid = params[1];
            String u_uid = params[2];

            String delete_url = "http://163.17.5.182/deletecase_android.php";
            try {

                URL url = new URL(delete_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("c_cid", "UTF-8") + "=" + URLEncoder.encode(c_cid, "UTF-8") + "&" +
                        URLEncoder.encode("u_uid", "UTF-8") + "=" + URLEncoder.encode(u_uid, "UTF-8");

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
        }else if(type.equals("deleteMessage")) {
            String c_cid = params[1];

            String delete_url = "http://163.17.5.182/deletemessage_android.php";
            try {

                URL url = new URL(delete_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("c_cid", "UTF-8") + "=" + URLEncoder.encode(c_cid, "UTF-8") ;

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
        }else if(type.equals("RevokeCase")) {
            String m_mid = params[1];
            String u_uid = params[2];

            String delete_url = "http://163.17.5.182/app/cancelcase.php";
            try {

                URL url = new URL(delete_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("m_mid", "UTF-8") + "=" + URLEncoder.encode(m_mid, "UTF-8")+
                        URLEncoder.encode("u_uid", "UTF-8") + "=" + URLEncoder.encode(u_uid, "UTF-8");

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
        }else if(type.equals("deciderid")) {
            String c_cid = params[4];
            String c_rid=params[1];
            String c_money=params[2];
            String u_uid = params[3];

            String delete_url = "http://163.17.5.182/app/deciderid.php";
            try {

                URL url = new URL(delete_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("c_cid", "UTF-8") + "=" + URLEncoder.encode(c_cid, "UTF-8") + "&" +
                        URLEncoder.encode("u_uid", "UTF-8") + "=" + URLEncoder.encode(u_uid, "UTF-8") + "&" +
                        URLEncoder.encode("c_rid", "UTF-8") + "=" + URLEncoder.encode(c_rid, "UTF-8") + "&" +
                        URLEncoder.encode("c_money", "UTF-8") + "=" + URLEncoder.encode(c_money, "UTF-8");

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
        }else if(type.equals("image_update")) {
            String u_image = params[2];
            String u_uid = params[1];

            String delete_url = "http://163.17.5.182/app/image_update.php";
            try {

                URL url = new URL(delete_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("u_image", "UTF-8") + "=" + URLEncoder.encode(u_image, "UTF-8") + "&" +
                        URLEncoder.encode("u_uid", "UTF-8") + "=" + URLEncoder.encode(u_uid, "UTF-8");

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
        }else if(type.equals("member_update")) {
            String u_nickname = params[2];
            String u_uid = params[1];
            String u_phone = params[3];
            String u_address = params[4];
            String u_introduce = params[5];

            String delete_url = "http://163.17.5.182/app/member_update.php";
            try {

                URL url = new URL(delete_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("u_nickname", "UTF-8") + "=" + URLEncoder.encode(u_nickname, "UTF-8") + "&" +
                        URLEncoder.encode("u_phone", "UTF-8") + "=" + URLEncoder.encode(u_phone, "UTF-8") + "&" +
                        URLEncoder.encode("u_address", "UTF-8") + "=" + URLEncoder.encode(u_address, "UTF-8") + "&" +
                        URLEncoder.encode("u_uid", "UTF-8") + "=" + URLEncoder.encode(u_uid, "UTF-8")+ "&" +
                        URLEncoder.encode("u_introduce", "UTF-8") + "=" + URLEncoder.encode(u_introduce, "UTF-8");

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
        }else if(type.equals("update_user")) {

            String token = params[1];
            String uid=params[2];
            String delete_url = "http://163.17.5.182/app/update_token.php";
            try {

                URL url = new URL(delete_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8")+"&"+
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
        }else if(type.equals("case_status")) {
            String c_status = params[2];
            String c_cid = params[1];


            String delete_url = "http://163.17.5.182/app/case_status.php";
            try {

                URL url = new URL(delete_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("c_cid", "UTF-8") + "=" + URLEncoder.encode(c_cid, "UTF-8") + "&" +
                        URLEncoder.encode("c_status", "UTF-8") + "=" + URLEncoder.encode(c_status, "UTF-8") ;

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
        }else if(type.equals("check_email_match_id")) {
            try {
                String mail = params[1];
                usermail=mail;
                String id = params[2];

                URL url = new URL(check_id_match_mail_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("u_mail","UTF-8")+"="+URLEncoder.encode(mail,"UTF-8")+"&"
                        +URLEncoder.encode("u_identity","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
                Log.d("POST_DATA", "doInBackground: "+post_data);

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
        }else if(type.equals("changepsw")) {
            try {
                String mail = params[1];
                String psw = params[2];
                String psw2 = params[3];
                String delete_url = "http://163.17.5.182/app/case_status.php";
                URL url = new URL(changepsw);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("u_mail","UTF-8")+"="+URLEncoder.encode(mail,"UTF-8")+"&"
                        +URLEncoder.encode("u_pwd","UTF-8")+"="+URLEncoder.encode(psw,"UTF-8")+"&"
                        +URLEncoder.encode("u_pwd2","UTF-8")+"="+URLEncoder.encode(psw2,"UTF-8");
                Log.d("POST_DATA", "doInBackground: "+post_data);

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
        }else if(type.equals("finish_case")) {

            String cid = params[1];
            String status = params[2];
            String rid = params[4];
            String money = params[3];

            String delete_url = "http://163.17.5.182/app/finish_case.php";
            try {

                URL url = new URL(delete_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("c_cid", "UTF-8") + "=" + URLEncoder.encode(cid, "UTF-8") + "&" +
                        URLEncoder.encode("c_status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8")+ "&" +
                        URLEncoder.encode("c_money", "UTF-8") + "=" + URLEncoder.encode(money, "UTF-8")+ "&" +
                        URLEncoder.encode("c_rid", "UTF-8") + "=" + URLEncoder.encode(rid, "UTF-8");

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

        }else if(type.equals("insert_qanda")) {

            String cid = params[2];
            String text = params[1];
            String pid = params[3];
            String aid = params[4];

            String delete_url = "http://163.17.5.182/app/insert_qanda.php";
            try {

                URL url = new URL(delete_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("q_cid", "UTF-8") + "=" + URLEncoder.encode(cid, "UTF-8") + "&" +
                        URLEncoder.encode("q_ptext", "UTF-8") + "=" + URLEncoder.encode(text, "UTF-8") + "&" +
                        URLEncoder.encode("q_pid", "UTF-8") + "=" + URLEncoder.encode(pid, "UTF-8")+ "&" +
                        URLEncoder.encode("q_aid", "UTF-8") + "=" + URLEncoder.encode(aid, "UTF-8");

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
        }else if(type.equals("update_qanda")) {

            String qid = params[1];
            String text = params[2];

            String delete_url = "http://163.17.5.182/app/update_qanda.php";
            try {

                URL url = new URL(delete_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("q_qid", "UTF-8") + "=" + URLEncoder.encode(qid, "UTF-8") + "&" +
                        URLEncoder.encode("q_atext", "UTF-8") + "=" + URLEncoder.encode(text, "UTF-8") ;

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
        }else if(type.equals("insert_rating")) {

            String content = params[2];
            String rating = params[1];
            String uid = params[3];
            String category = params[4];
            String muid = params[5];
            String cid=params[6];
            String delete_url = "http://163.17.5.182/app/insert_rating.php";
            try {

                URL url = new URL(delete_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("category", "UTF-8") + "=" + URLEncoder.encode(category, "UTF-8") + "&" +
                        URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(uid, "UTF-8") + "&" +
                        URLEncoder.encode("rating", "UTF-8") + "=" + URLEncoder.encode(rating, "UTF-8")+ "&" +
                        URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(content, "UTF-8")+ "&" +
                        URLEncoder.encode("muid", "UTF-8") + "=" + URLEncoder.encode(muid, "UTF-8")+ "&" +
                        URLEncoder.encode("cid", "UTF-8") + "=" + URLEncoder.encode(cid, "UTF-8");

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
        }else if(type.equals("insert_report")) {

            String content = params[4];
            String pid=params[3];
            String uid = params[2];
            String cid=params[1];
            String detail=params[5];
            String delete_url = "http://163.17.5.182/app/insert_report.php";
            try {

                URL url = new URL(delete_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(uid, "UTF-8") + "&" +
                        URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(content, "UTF-8")+ "&" +
                        URLEncoder.encode("pid", "UTF-8") + "=" + URLEncoder.encode(pid, "UTF-8")+ "&" +
                        URLEncoder.encode("cid", "UTF-8") + "=" + URLEncoder.encode(cid, "UTF-8")+ "&" +
                        URLEncoder.encode("detail", "UTF-8") + "=" + URLEncoder.encode(detail, "UTF-8");
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
        }

        return null;

    }


    @Override
    protected void onPreExecute() {
        alertDialog = new MaterialDialog.Builder(context);
        alertDialog.title("ERROR");
    }

    @Override
    protected void onPostExecute(String result) {
        //alertDialog.content(result).show();
        Log.d(TAG, "onPostExecute: "+result);
        if(result.contains("登入成功"))
        {
            Toast.makeText(context, "登入中…", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = context.getSharedPreferences(KEY , MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("Status" , true).apply();
            Intent toLoadView=new Intent(context,LoadingView.class);
            context.startActivity(toLoadView);
            ((Activity)context).finish();
        }else if (result.contains("登入失敗")){
            Toast.makeText(context, "登入失敗！請檢查帳號密碼是否有誤", Toast.LENGTH_SHORT).show();
            ((LoginActivity)context).Login.setEnabled(true);
        }
        else if (result.contains("註冊成功")){
            Toast.makeText(context, "註冊成功！請至信箱收取驗證信", Toast.LENGTH_SHORT).show();
            Intent toLogin=new Intent(context,LoginActivity.class);
            context.startActivity(toLogin);
            ((Activity)context).finish();
        }else if (result.contains("驗證信發送失敗")){
            Toast.makeText(context, "驗證信發送失敗，請檢查Email是否輸入錯誤", Toast.LENGTH_SHORT).show();
            ((LoginActivity)context).Login.setEnabled(true);
        }else if (result.contains("註冊失敗")){
            Toast.makeText(context, "註冊失敗，請重新輸入一次資料", Toast.LENGTH_SHORT).show();
            ((LoginActivity)context).Login.setEnabled(true);
        }else if (result.contains("此帳號已被註冊")){
            Toast.makeText(context, "此帳號已被註冊", Toast.LENGTH_SHORT).show();
            ((LoginActivity)context).Login.setEnabled(true);
        }else if (result.contains("收藏成功")){
            Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
        }else if(result.contains("接案成功")){
            RecyclerViewAdapterCol adapterCol=new RecyclerViewAdapterCol();
            RecyclerViewAdapter adapter=new RecyclerViewAdapter();
            adapterCol.dissmissDialog();
            adapter.dissmissDialog();
            Toast.makeText(context, "申請成功", Toast.LENGTH_SHORT).show();
        }else if(result.contains("刪除成功")){
            cancel(true);
            Toast.makeText(context, "刪除成功", Toast.LENGTH_SHORT).show();
        }else if(result.contains("刪除失敗")){
            Toast.makeText(context, "刪除失敗", Toast.LENGTH_SHORT).show();
        }else if(result.contains("取消成功")){
            Toast.makeText(context, "取消成功", Toast.LENGTH_SHORT).show();
        }else if(result.contains("取消失敗")){
            Toast.makeText(context, "取消失敗", Toast.LENGTH_SHORT).show();
        }else if(result.contains("決定成功")){
            Toast.makeText(context, "決定成功", Toast.LENGTH_SHORT).show();
        }else if(result.contains("決定失敗")) {
            Toast.makeText(context, "決定失敗", Toast.LENGTH_SHORT).show();
        }else if(result.contains("更新成功")){
            Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
        }else if(result.contains("更新失敗")) {
            Toast.makeText(context, "更新失敗", Toast.LENGTH_SHORT).show();
        }else if(result.contains("檢查失敗")) {
            Toast.makeText(context, "檢查失敗", Toast.LENGTH_SHORT).show();
        }else if(result.contains("轉帳成功")) {
            Toast.makeText(context, "轉帳成功", Toast.LENGTH_SHORT).show();
        }else if(result.contains("轉帳失敗")) {
            Toast.makeText(context, "轉帳失敗", Toast.LENGTH_SHORT).show();
        }else if(result.contains("檢查成功")) {
            Toast.makeText(context, "檢查成功", Toast.LENGTH_SHORT).show();
            Intent tochangepsw=new Intent(context,changepassword.class);
            Bundle bundle = new Bundle();
            bundle.putString("u_email",usermail);
            tochangepsw.putExtras(bundle);
            context.startActivity(tochangepsw);
            ((Activity)context).finish();
        }else if(result.contains("尚未驗證")) {
            Toast.makeText(context, "尚未驗證", Toast.LENGTH_SHORT).show();
        }else if(result.contains("無此帳號")) {
            Toast.makeText(context, "無此帳號", Toast.LENGTH_SHORT).show();
        }else if(result.contains("密碼不得是空值")) {
            Toast.makeText(context, "密碼不得是空值", Toast.LENGTH_SHORT).show();
        }else if(result.contains("請確認密碼相同")) {
            Toast.makeText(context, "請確認密碼相同", Toast.LENGTH_SHORT).show();
        }else if (result.contains("已經接過這個案子") ){
            Toast.makeText(context, "已經接過這個案子", Toast.LENGTH_SHORT).show();
        }else if(result.contains("密碼變更成功")) {
            Toast.makeText(context, "密碼變更成功", Toast.LENGTH_SHORT).show();
            Intent toLogin=new Intent(context,LoginActivity.class);
            context.startActivity(toLogin);
            ((Activity)context).finish();
        }else if(result.contains("qinsert success")) {
            Toast.makeText(context, "詢問成功", Toast.LENGTH_SHORT).show();
        }else if(result.contains("qupdate success")) {
            Toast.makeText(context, "回答成功", Toast.LENGTH_SHORT).show();
        }else if(result.contains("評分成功")) {
            Toast.makeText(context, "評分成功", Toast.LENGTH_SHORT).show();
        }else if (result.contains("DOCTYPE")) {
            Log.d("Result", "onPostExecute: " + result);
            Toast.makeText(context, "系統出錯，請再試一次", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            Log.d("Result", "onPostExecute: "+result);
        }

    }



    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}
