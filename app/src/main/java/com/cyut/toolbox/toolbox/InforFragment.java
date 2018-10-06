package com.cyut.toolbox.toolbox;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.net.Uri;
import android.widget.ImageView;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class InforFragment extends Fragment {
    public static final String KEY = "STATUS";
    private View view;
    ImageView imageView,imageButton;
    String u_image,uid;
    Uri picUri;
    String path;
    boolean imgboo;
    private static final int REQUEST_EXTERNAL_STORAGE = 200;
    private static final int PICKER = 100;
    public InforFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_infor, container, false);


        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(KEY, MODE_PRIVATE);
        uid=sharedPreferences.getString("uid",null);


        LoadUser(uid);

        return view;
    }
    public void LoadUser(final String uid){
        String url ="http://163.17.5.182/app/loaduser.php";
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
                            final List<Item> itemList=posts;

                            final TextView textView_name=(TextView)view.findViewById(R.id.textView_name);
                            final TextView textView_nickname=(TextView)view.findViewById(R.id.textView_nickname);
                            final TextView textView_phone=(TextView)view.findViewById(R.id.textView_phone);
                            final TextView textView_pid=(TextView)view.findViewById(R.id.textView_pid);
                            final TextView textView_area=(TextView)view.findViewById(R.id.textView_area);
                            final TextView textView_introduce=(TextView)view.findViewById(R.id.textView_introduce);
                            imageButton=(ImageView)view.findViewById(R.id.imageView8) ;
                            imageView=view.findViewById(R.id.imageView);

                            Picasso.get().load("https://imgur.com/"+itemList.get(0).getImage()+".jpg").fit().centerInside().into(imageView);
                            textView_nickname.setText(itemList.get(0).getNickname());
                            textView_name.setText(itemList.get(0).getName());
                            textView_phone.setText(itemList.get(0).getPhone());
                            textView_pid.setText(itemList.get(0).getIdentity());
                            textView_area.setText(itemList.get(0).getAddress());
                            String lineSep = System.getProperty("line.separator");
                            String m=itemList.get(0).getIntroduce().replaceAll("<br />", lineSep);
                            textView_introduce.setText(m);


                            imageButton.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v) {
                                    Log.v(TAG, " click");
                                    Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
                                    picker.setType("image/*");
                                    picker.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                                    Intent destIntent = Intent.createChooser(picker, null);
                                    startActivityForResult(destIntent, PICKER);

                                }
                            });


                            FloatingActionButton floatingActionButton=(FloatingActionButton)getActivity().findViewById(R.id.fab);
                            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    customDialog(itemList.get(0).getNickname(),itemList.get(0).getAddress(),itemList.get(0).getPhone());
                                }
                            });

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

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);
    }

    private void customDialog(final String nickname, final String area, final String phone){
        boolean wrapInScrollView = true;

        final View item = LayoutInflater.from(view.getContext()).inflate(R.layout.alert_edit, null);

        MaterialDialog.Builder dialog =new MaterialDialog.Builder(view.getContext());
        dialog.customView(item,wrapInScrollView);
        dialog.backgroundColorRes(R.color.colorBackground);
        dialog.positiveText("確認修改");
        dialog.negativeText("取消");


        final EditText editText_nickname=(EditText)item.findViewById(R.id.editText_nickname);
        final EditText editText_phone = (EditText) item.findViewById(R.id.editText_phone);
        final EditText editText_area = (EditText) item.findViewById(R.id.editText_area);
        editText_nickname.setText(nickname, TextView.BufferType.EDITABLE);
        editText_phone.setText(phone, TextView.BufferType.EDITABLE);
        editText_area.setText(area, TextView.BufferType.EDITABLE);

        final String u_address=editText_area.getText().toString();
        final String u_phone=editText_phone.getText().toString();
        final String u_nickname=editText_nickname.getText().toString();

        dialog.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                final String u_address=editText_area.getText().toString();
                final String u_phone=editText_phone.getText().toString();
                final String u_nickname=editText_nickname.getText().toString();

                if (u_phone.equals("")||u_nickname.equals("")||u_address.equals("")){
                    alertDialog("欄位有空值",getResources().getString(R.string.toast_missdata),"OK");//須修正
                }else if(!editText_phone.getText().toString().matches("[0][9][0-9]{8}")){
                    alertDialog("電話號碼輸入有誤","請在確認一次輸入的訊息","OK");
                }else{

                    String type = "member_update";
                    Backgorundwork backgorundwork = new Backgorundwork(view.getContext());
                    backgorundwork.execute(type,uid ,u_nickname,u_phone,u_address);
                    Reload();

                }
            }
        });

        dialog.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    //Reload Fragment
    public void Reload(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
    private void alertDialog(String T, String M, String P){
        MaterialDialog.Builder dialog =new MaterialDialog.Builder(getContext());
        dialog.content(M);
        dialog.title(T);
        dialog.positiveText(P);

        dialog.show();

    }

    //內部上傳圖片功能
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                path = getPath(InforFragment.this.getContext(), uri);
                imageView.setImageURI(uri);
                picUri = uri;
                imgboo = true;
                Bitmap bitmap = getResizedBitmap(path); //程式寫在後面

                //將 Bitmap 轉為 base64 字串
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] bitmapData = bos.toByteArray();
                String imageBase64 = Base64.encodeToString(bitmapData, Base64.DEFAULT);
                Log.d("imageBase64", "infor: " + imageBase64);

                imgurUpload(imageBase64);
                Toast.makeText(InforFragment.this.getContext(), path, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    private Bitmap getResizedBitmap(String imagePath) {
        // 取得原始圖檔的bitmap與寬高
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        int width = options.outWidth, height = options.outHeight;


        final int MAX_WIDTH = 1024;
        float resize = 1; // 縮小值 resize 可為任意小數
        if (width > MAX_WIDTH) {
            resize = ((float) MAX_WIDTH) / width;
        }

        // 產生縮圖需要的參數 matrix
        Matrix matrix = new Matrix();
        matrix.postScale(resize, resize); // 設定寬高的縮放比例

        // 產生縮小後的圖
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }
//上傳至imgur功能


    private void imgurUpload(final String image) { //插入圖片
        Log.d("上傳圖片", "True");
        String urlString = "https://imgur-apiv3.p.mashape.com/3/image/";
        String mashapeKey = "XR6S0dZU7Hmsh289zCmGwLH8tQNrp1KA0jfjsn8EHvOt1q8QR5"; //設定自己的 Mashape Key
        String clientId = "6989dec20061743"; //設定自己的 Clinet ID

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Mashape-Key", mashapeKey);
        client.addHeader("Authorization", "Client-ID " + clientId);
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.put("image", image);
        client.post(urlString, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (!response.optBoolean("success") || !response.has("data")) {
                    Log.d("editor", "response: " + response.toString());
                    return;
                }
                JSONObject data = response.optJSONObject("data");
                Log.d("editor", "link: " + data.optString("link"));
                String link = data.optString("link", "");
                u_image = link.substring(link.length() - 11, link.length() - 4);//取得imgur網址後七碼
                //update image
                String type = "image_update";
                Backgorundwork backgorundwork = new Backgorundwork(view.getContext());
                backgorundwork.execute(type,uid ,u_image);
                Reload();
                Log.d("imgur", "onSuccess: " + u_image);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject error) {

                Log.d("editor", "error: " + error.toString());
                if (error.has("data")) {
                    JSONObject data = error.optJSONObject("data");
                    android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(InforFragment.this.getContext())
                            .setTitle("Error: " + statusCode + " " + e.getMessage())
                            .setMessage(data.optString("error", ""))
                            .setPositiveButton("確定", null)
                            .create();
                    dialog.show();
                }
            }
        });
    }

}
