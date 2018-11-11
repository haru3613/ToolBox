package com.cyut.toolbox.toolbox.model;

/**
 * Created by Haru on 2018/1/7.
 */

import com.google.gson.annotations.SerializedName;


public class ItemRating {

    @SerializedName("rt_id")
    private String rt_id;
    @SerializedName("category")
    private String category;
    @SerializedName("grade")
    private String grade;
    @SerializedName("rid")
    private String rid;
    @SerializedName("cid")
    private String cid;
    @SerializedName("pid")
    private String pid;
    @SerializedName("content")
    private String content;
    @SerializedName("time")
    private String time;


    public ItemRating(String rt_id, String category, String grade, String rid,String content,String pid,String cid,String time) {
        this.rt_id=rt_id;
        this.category=category;
        this.grade=grade;
        this.rid=rid;
        this.pid=pid;
        this.cid=cid;
        this.content=content;
        this.time=time;
    }

    public String getRt_id() {
        return rt_id;
    }

    public String getCategory() {
        return category;
    }

    public String getGrade() {
        return grade;
    }

    public String getRid() {
        return rid;
    }

    public String getCid() {
        return cid;
    }

    public String getPid() {
        return pid;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}