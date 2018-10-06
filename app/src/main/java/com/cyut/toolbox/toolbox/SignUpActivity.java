package com.cyut.toolbox.toolbox;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.StringPrepParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Calendar;


import cz.msebera.android.httpclient.Header;


public class SignUpActivity extends AppCompatActivity {

    String mail, pwd, identity, sex, name, nickname, phone, address, re_pwd,introduce,server;


    private ImageView image;
    boolean imgboo;

    // number of images to select
    String u_image, u_verify, u_verityCode;
    Uri picUri;
    String path;
    private static final int REQUEST_EXTERNAL_STORAGE = 200;
    private static final int PICKER = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //spinner option
        Spinner spinner=(Spinner)findViewById(R.id.spinner_login);

        ArrayAdapter<CharSequence> loginList = ArrayAdapter.createFromResource(SignUpActivity.this,
                R.array.login_option,
                R.layout.spinner_item);
        spinner.setAdapter(loginList);

        TextView UseStatement=(TextView)findViewById(R.id.UseStatement);
        UseStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toUseS=new Intent(SignUpActivity.this,UseState.class);
                SignUpActivity.this.startActivity(toUseS);
            }
        });

        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            getPermissionAccess();
        }

        image = (ImageView) findViewById(R.id.imageview);


        final Button SignUp = (Button) findViewById(R.id.sign);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpp(view);
            }
        });
    }


    //註冊按鈕
    public void signUpp(View v) {
        phone = ((EditText) findViewById(R.id.phone)).getText().toString();
        mail = ((EditText) findViewById(R.id.mailText)).getText().toString();
        identity = ((EditText) findViewById(R.id.pid)).getText().toString();
        if (pidTF(identity)) {
            if (phone.matches("[0][9][0-9]{8}")) {
                if (mail.length() > 0) {
                    if ((mail.substring(0, 1)).equals("s")) {
                        signup();
                    } else {
                        alertDialog(getResources().getString(R.string.email_error_title), getResources().getString(R.string.email_error), "OK");
                    }
                } else {
                    alertDialog(getResources().getString(R.string.email_error_title), getResources().getString(R.string.email_error2), "OK");
                }
            } else {
                alertDialog(getResources().getString(R.string.phone_error_title), getResources().getString(R.string.phone_error), "ok");
            }
        } else {
            alertDialog(getResources().getString(R.string.idcard_error_title), getResources().getString(R.string.idcard_error), "ok");
        }
    }


    //呼叫註冊事件
    public void signup() {

        pwd = ((EditText) findViewById(R.id.password)).getText().toString();
        re_pwd = ((EditText) findViewById(R.id.re_password)).getText().toString();
        nickname = ((EditText) findViewById(R.id.nickname)).getText().toString();
        name = ((EditText) findViewById(R.id.name)).getText().toString();
        address = ((EditText) findViewById(R.id.address)).getText().toString();
        phone = ((EditText) findViewById(R.id.phone)).getText().toString();
        identity = ((EditText) findViewById(R.id.pid)).getText().toString();
        introduce=((EditText) findViewById(R.id.introduce)).getText().toString();
        Spinner spinner=findViewById(R.id.spinner_login);
        server=spinner.getSelectedItem().toString();
        Log.d("pwd", pwd);
        Log.d("repwd", re_pwd);


        if (name.equals("") || nickname.equals("") || address.equals("") ||identity.equals("")||phone.equals("")||introduce.equals("")) {
            alertDialog("欄位有空值", "請勿輸入空值", "OK");
        } else if (mail.length() == 0) {
            alertDialog(getResources().getString(R.string.email_error_title), getResources().getString(R.string.email_error2), "OK");
        } else if (nickname.length() > 11) {
            alertDialog("暱稱過長", "你設定的暱稱太長", "OK");
        } else if (pwd.length() < 5) {
            alertDialog(getResources().getString(R.string.pwd_short_title), getResources().getString(R.string.pwd_short), "OK");
        } else if (!pwd.equals(re_pwd)) {
            alertDialog(getResources().getString(R.string.re_error_title), getResources().getString(R.string.re_error), "OK");
        } else if (path.isEmpty()){
            alertDialog("尚未選擇圖片", "尚未選擇圖片", "OK");
        } else if (introduce.length()<10 ||introduce.length()>255){
            alertDialog("自我介紹錯誤","請重新輸入(最少要十個字，最多255個字)","OK");
        }else{
            try {
                sex = String.valueOf(identity.charAt(1));
                if (sex.equals("1")) {
                    sex = "男性";
                } else {
                    sex = "女性";
                }
            } catch (Exception ex) {
                alertDialog(getResources().getString(R.string.field_error_title), getResources().getString(R.string.field_error), "OK");
            }

            Bitmap bitmap = getResizedBitmap(path); //程式寫在後面

            //將 Bitmap 轉為 base64 字串
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapData = bos.toByteArray();
            String imageBase64 = Base64.encodeToString(bitmapData, Base64.DEFAULT);
            Log.d("imageBase64", "signup: " + imageBase64);
            u_verify = "未驗證";
            u_verityCode = RndCode();

            Toast.makeText(SignUpActivity.this,"註冊中，請稍後....",Toast.LENGTH_LONG).show();

            imgurUpload(imageBase64);
        }

    }
    public String RndCode() {
        int z;
        StringBuilder sb = new StringBuilder();
        int i;
        for (i = 0; i < 4; i++) {
            z = (int) ((Math.random() * 7) % 3);

            if (z == 1) { // 放數字
                sb.append((int) ((Math.random() * 10)));
            } else if (z == 2) { // 放大寫英文
                sb.append((char) (((Math.random() * 26) + 65)));
            } else {// 放小寫英文
                sb.append(((char) ((Math.random() * 26) + 97)));
            }
        }
        String result = sb.toString();
        return result;
    }

    private Bitmap getResizedBitmap(String imagePath) {
        // 取得原始圖檔的bitmap與寬高
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        int width = options.outWidth, height = options.outHeight;

        // 將圖檔等比例縮小至寬度為1024
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





    //呼叫回傳值為null的AlertDialog
    public void alertDialog(String T, String M, String P) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(SignUpActivity.this);
        builder.title(T);
        builder.content(M);
        builder.positiveText(P);
        builder.show();
    }


    //驗證身分證
    private boolean pidTF(String pid) {
        pid = pid.toUpperCase();
        if (pid.matches("[a-zA-Z][1-2][0-9]{8}")) {
            int[] headNum = new int[]{1, 10, 19, 28, 37, 46, 55, 64, 39, 73, 82, 2, 11, 20, 48, 29, 38, 47, 56, 65, 74, 83, 21, 3, 12, 30};
            char[] headCharUpper = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            int index = Arrays.binarySearch(headCharUpper, pid.charAt(0));
            int base = 8;
            int total = 0;
            for (int i = 1; i < 10; i++) {
                int tmp = Integer.parseInt(Character.toString(pid.charAt(i))) * base;
                total += tmp;
                base--;
            }
            total += headNum[index];
            int remain = total % 10;
            int checkNum = (10 - remain) % 10;
            if (Integer.parseInt(Character.toString(pid.charAt(9))) != checkNum) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }


    //內部上傳圖片功能
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                path = getPath(SignUpActivity.this, uri);
                image.setImageURI(uri);
                picUri = uri;
                imgboo = true;
                Toast.makeText(SignUpActivity.this, path, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getPermissionAccess();
                } else {
                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void getPermissionAccess() {

        final TextView upload =  findViewById(R.id.textimg);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
                picker.setType("image/*");
                picker.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                Intent destIntent = Intent.createChooser(picker, null);
                startActivityForResult(destIntent, PICKER);

            }
        });
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
                //連結php
                String type = "signup";
                Backgorundwork backgorundwork = new Backgorundwork(SignUpActivity.this);
                backgorundwork.execute(type,mail , pwd, identity, sex, name, nickname, phone, address, u_image, u_verify, u_verityCode,introduce,server);
                Log.d("imgur", "onSuccess: " + u_image);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject error) {

                Log.d("editor", "error: " + error.toString());
                if (error.has("data")) {
                    JSONObject data = error.optJSONObject("data");
                    AlertDialog dialog = new AlertDialog.Builder(SignUpActivity.this)
                            .setTitle("Error: " + statusCode + " " + e.getMessage())
                            .setMessage(data.optString("error", ""))
                            .setPositiveButton("確定", null)
                            .create();
                    dialog.show();
                }
            }
        });
    }

    public String getDate(){
        // 方法一
        Calendar mCal = Calendar.getInstance();
        CharSequence s = DateFormat.format("yyyy-MM-dd kk:mm:ss", mCal.getTime());    // kk:24小時制, hh:12小時制
        return s.toString();
    }
}




