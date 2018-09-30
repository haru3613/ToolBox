package com.cyut.toolbox.toolbox;

/**
 * Created by Haru on 2018/1/7.
 */

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import static android.content.ContentValues.TAG;


public class UpdateManager {


    HashMap<String, String> mHashMap;



    private ProgressDialog pDialog;
    private Context mContext;


    public UpdateManager(Context context) {
        this.mContext = context;
    }


    public void checkUpdate() {
        if (isUpdate()) {
            showNoticeDialog();
        } else {
            //已經是最新版本
        }
    }

    /**
     * @return
     */
    private boolean isUpdate() {

        int versionCode = getVersionCode(mContext);
        Log.d(TAG, "VersionCode: " + versionCode);
        String upurl = "http://163.17.5.182/version.xml";
        try {
            URL url = new URL(upurl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inStream = httpURLConnection.getInputStream();
            ParseXmlService service = new ParseXmlService();
            try {
                mHashMap = service.parseXml(inStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null != mHashMap) {
                int serviceCode = Integer.valueOf(mHashMap.get("version"));
                if (serviceCode > versionCode) {
                    Log.d(TAG, "serviceCode: "+serviceCode);
                    Log.d(TAG, "versionCode: "+versionCode);
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     *
     *
     * @param context
     * @return
     */
    private int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    private void showNoticeDialog() {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
        builder.title("ToolBox更新");
        builder.content("已經有新版本了，趕快去下載吧");
        builder.positiveText("更新");
        // 更新
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {

                dialog.dismiss();
                downloadApk();
            }
        });

        builder.show();

    }



    private void downloadApk() {
        new DownloadFileFromURL().execute(mHashMap.get("url"));
    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");

            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage("更新中，請稍後...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                String root = Environment.getExternalStorageDirectory().toString() + "/download/";

                System.out.println("Downloading");
                URL url = new URL(f_url[0]);
                Log.d(TAG, "URL:"+url);
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                File file = new File(root);
                file.mkdirs();
                File outputFile = new File(file, mHashMap.get("name"));

                if(outputFile.exists()){
                    outputFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = httpConnection.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }


        /**
         * After completing background task
         **/
        @Override
        protected void onPostExecute(String file_url) {
            System.out.println("Downloaded");
            installApk();

            pDialog.dismiss();
        }

    }

    public void installApk() {
        if (mContext == null) {
            return;
        }
        Intent install = new Intent(Intent.ACTION_VIEW);
        File apkFile = new File(Environment.getExternalStorageDirectory() + "/download/" +  mHashMap.get("name"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName()+".fileprovider", apkFile);
            install.setData(contentUri);
            install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(install);
        Toast.makeText(mContext,"更新成功",Toast.LENGTH_SHORT).show();

    }
}