package com.cyut.toolbox.toolbox.model;

/**
 * Created by Haru on 2018/1/7.
 */

import com.google.gson.annotations.SerializedName;


public class ItemQanda {

    @SerializedName("q_qid")
    private String q_qid;
    @SerializedName("q_cid")
    private String q_cid;
    @SerializedName("q_pid")
    private String q_pid;
    @SerializedName("q_aid")
    private String q_aid;
    @SerializedName("q_ptext")
    private String q_ptext;
    @SerializedName("q_atext")
    private String q_atext;
    @SerializedName("q_created_at")
    private String q_created_at;
    @SerializedName("q_updated_at")
    private String q_updated_at;


    public ItemQanda(String q_qid, String q_cid, String q_pid, String q_aid, String q_ptext, String q_atext, String q_created_at, String q_updated_at) {
        this.q_qid=q_qid;
        this.q_cid=q_cid;
        this.q_pid=q_pid;
        this.q_aid=q_aid;
        this.q_ptext=q_ptext;
        this.q_atext=q_atext;
        this.q_created_at=q_created_at;
        this.q_updated_at=q_updated_at;
    }

    public String getQ_qid() {
        return q_qid;
    }

    public String getQ_cid() {
        return q_cid;
    }

    public String getQ_pid() {
        return q_pid;
    }

    public String getQ_aid() {
        return q_aid;
    }

    public String getQ_ptext() {
        return q_ptext;
    }

    public String getQ_atext() {
        return q_atext;
    }

    public String getQ_created_at() {
        return q_created_at;
    }

    public String getQ_updated_at() {
        return q_updated_at;
    }

    public void setQ_qid(String q_qid) {
        this.q_qid = q_qid;
    }

    public void setQ_cid(String q_cid) {
        this.q_cid = q_cid;
    }

    public void setQ_pid(String q_pid) {
        this.q_pid = q_pid;
    }

    public void setQ_aid(String q_aid) {
        this.q_aid = q_aid;
    }

    public void setQ_ptext(String q_ptext) {
        this.q_ptext = q_ptext;
    }

    public void setQ_atext(String q_atext) {
        this.q_atext = q_atext;
    }

    public void setQ_created_at(String q_created_at) {
        this.q_created_at = q_created_at;
    }

    public void setQ_updated_at(String q_updated_at) {
        this.q_updated_at = q_updated_at;
    }
}