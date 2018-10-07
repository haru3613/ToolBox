package com.cyut.toolbox.toolbox.model;

/**
 * Created by Haru on 2018/1/7.
 */

import com.google.gson.annotations.SerializedName;


public class ItemWallet {

    @SerializedName("w_wid")
    private String w_wid;
    @SerializedName("w_cid")
    private String w_cid;
    @SerializedName("w_pid")
    private String w_pid;
    @SerializedName("w_rid")
    private String w_rid;
    @SerializedName("w_money")
    private String w_money;
    @SerializedName("w_status")
    private String w_status;



    public ItemWallet(String w_wid, String w_cid, String w_pid, String w_rid, String w_money, String w_status) {
        this.w_wid=w_wid;
        this.w_cid=w_cid;
        this.w_pid=w_pid;
        this.w_rid=w_rid;
        this.w_money=w_money;
        this.w_status=w_status;
    }

    public String getW_wid() {
        return w_wid;
    }

    public String getW_cid() {
        return w_cid;
    }

    public String getW_pid() {
        return w_pid;
    }

    public String getW_rid() {
        return w_rid;
    }

    public String getW_money() {
        return w_money;
    }

    public String getW_status() {
        return w_status;
    }
}