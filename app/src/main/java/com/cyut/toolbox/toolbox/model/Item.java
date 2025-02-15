package com.cyut.toolbox.toolbox.model;

/**
 * Created by Haru on 2018/1/7.
 */

import com.google.gson.annotations.SerializedName;


public class Item {

    @SerializedName("u_mail")
    private String mail;
    @SerializedName("u_pwd")
    private String pwd;
    @SerializedName("u_name")
    private String name;
    @SerializedName("u_nickname")
    private String nickname;
    @SerializedName("u_image")
    private String image;
    @SerializedName("u_phone")
    private String phone;
    @SerializedName("u_address")
    private String address;
    @SerializedName("u_introduce")
    private String introduce;
    @SerializedName("u_identity")
    private String identity;
    @SerializedName("u_uid")
    private String uid;
    @SerializedName("u_money")
    private String money;
    @SerializedName("u_Mtoken")
    private String Mtoken;
    @SerializedName("u_Wtoken")
    private String Wtoken;


    public Item(String mail, String nickname, String name,String image,String phone,String address,String introduce,String identity, String uid,String money,String pwd,String Mtoken,String Wtoken) {
        this.nickname = nickname;
        this.name = name;
        this.mail=mail;
        this.image=image;
        this.address=address;
        this.phone=phone;
        this.introduce=introduce;
        this.identity=identity;
        this.uid=uid;
        this.pwd=pwd;
        this.money=money;
        this.Mtoken=Mtoken;
        this.Wtoken=Wtoken;
    }
    public String getMail() {
        return mail;
    }
    public String getName() {
        return name;
    }
    public String getNickname() {
        return nickname;
    }
    public String getImage() {
        return image;
    }
    public String getPhone() {
        return phone;
    }
    public String getAddress() {
        return address;
    }
    public String getIntroduce() {
        return introduce;
    }
    public String getIdentity() {
        return identity;
    }
    public String getUid() {
        return uid;
    }

    public String getMtoken() {
        return Mtoken;
    }

    public String getWtoken() {
        return Wtoken;
    }

    public String getPwd() {
        return pwd;
    }

    public String getMoney() {
        return money;
    }
}