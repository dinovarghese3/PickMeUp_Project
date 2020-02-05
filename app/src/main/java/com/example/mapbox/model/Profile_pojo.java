package com.example.mapbox.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile_pojo {
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("avgrating")
    @Expose
    private String avgrating;

    public String getAvgrating() {
        return avgrating;
    }

    public void setAvgrating(String avgrating) {
        this.avgrating = avgrating;
    }

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("utype")
    @Expose
    private String utype;
    @SerializedName("vname")
    @Expose
    private String vname;
    @SerializedName("vno")
    @Expose
    private String vno;
    @SerializedName("seat")
    @Expose
    private String seat;
    @SerializedName("lisence")
    @Expose
    private String lisence;
    @SerializedName("photo")
    @Expose
    private String photo;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUtype() {
        return utype;
    }

    public void setUtype(String utype) {
        this.utype = utype;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getVno() {
        return vno;
    }

    public void setVno(String vno) {
        this.vno = vno;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getLisence() {
        return lisence;
    }

    public void setLisence(String lisence) {
        this.lisence = lisence;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
