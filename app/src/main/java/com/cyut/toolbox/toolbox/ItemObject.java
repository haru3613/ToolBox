package com.cyut.toolbox.toolbox;

/**
 * Created by Haru on 2018/1/7.
 */

import com.google.gson.annotations.SerializedName;


public class ItemObject {
    @SerializedName("c_category")
    private String CategoryImage;
    @SerializedName("c_title")
    private String Title;
    @SerializedName("c_city")
    private String city;
    @SerializedName("c_town")
    private String town;
    @SerializedName("c_road")
    private String road;
    @SerializedName("c_address")
    private String address;
    @SerializedName("c_money")
    private String money;
    @SerializedName("c_detail")
    private String detail;
    @SerializedName("c_time")
    private String time;
    @SerializedName("c_status")
    private String status;
    @SerializedName("c_until")
    private String until;
    @SerializedName("c_cid")
    private String cid;
    @SerializedName("c_rid")
    private String rid;
    @SerializedName("c_pid")
    private String pid;


    public ItemObject(String CategoryImage, String Title,String city,String town,String road,String address,String money,String detail,String time,String until,String status,String cid,String rid,String pid) {
        this.CategoryImage = CategoryImage;
        this.Title = Title;
        this.city=city;
        this.town=town;
        this.road=road;
        this.address=address;
        this.money=money;
        this.detail=detail;
        this.time=time;
        this.status=status;
        this.cid=cid;
        this.until=until;
        this.rid=rid;
        this.pid=pid;
    }
    public String getCategoryImage() {
        return CategoryImage;
    }
    public String getTitle() {
        return Title;
    }
    public String getCity() {
        return city;
    }
    public String getTown() {
        return town;
    }
    public String getRoad() {
        return road;
    }
    public String getAddress() {
        return address;
    }
    public String getMoney() {
        return money;
    }
    public String getDetail(){
        return detail;
    }
    public String getTime() {
        return time;
    }
    public String getStatus() {
        return status;
    }
    public String getUntil() {
        return until;
    }
    public String getCid() {
        return cid;
    }
    public String getPid(){
        return pid;
    }
    public String getRid() {
        return rid;
    }


}