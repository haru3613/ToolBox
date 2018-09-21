package com.cyut.toolbox.toolbox;

/**
 * Created by Haru on 2018/1/7.
 */

import com.google.gson.annotations.SerializedName;


public class ItemMsg {

    @SerializedName("c_cid")
    private String cid;
    @SerializedName("m_mid")
    private String mid;
    @SerializedName("fc_cid")
    private String fcid;
    @SerializedName("u_uid")
    private String uid;
    @SerializedName("m_message")
    private String message;
    @SerializedName("m_time")
    private String time;


    public ItemMsg(String cid,String mid,String fcid,String uid,String message,String time) {
        this.mid=mid;
        this.fcid=fcid;
        this.message=message;
        this.uid=uid;
        this.time=time;
        this.cid=cid;
    }
    public String getMid() {
        return mid;
    }
    public String getFcid() {
        return fcid;
    }
    public String getUid() {
        return uid;
    }
    public String getMessage() {
        return message;
    }
    public String getTime() {
        return time;
    }
    public String getCid() {
        return cid;
    }




}