package com.example.mapbox.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Request_model {
    @SerializedName("rid")
    @Expose
    private String rid;
    @SerializedName("userid")
    @Expose
    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("slat")
    @Expose
    private String slat;
    @SerializedName("slon")
    @Expose
    private String slon;
    @SerializedName("dlat")
    @Expose
    private String dlat;
    @SerializedName("dlon")
    @Expose
    private String dlon;
    @SerializedName("tdate")
    @Expose
    private String tdate;
    @SerializedName("ttime")
    @Expose
    private String ttime;
    @SerializedName("rstatus")
    @Expose
    private String rstatus;
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
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("reqid")
    @Expose
    private String reqid;
    @SerializedName("cdate")
    @Expose
    private String cdate;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSlat() {
        return slat;
    }

    public void setSlat(String slat) {
        this.slat = slat;
    }

    public String getSlon() {
        return slon;
    }

    public void setSlon(String slon) {
        this.slon = slon;
    }

    public String getDlat() {
        return dlat;
    }

    public void setDlat(String dlat) {
        this.dlat = dlat;
    }

    public String getDlon() {
        return dlon;
    }

    public void setDlon(String dlon) {
        this.dlon = dlon;
    }

    public String getTdate() {
        return tdate;
    }

    public void setTdate(String tdate) {
        this.tdate = tdate;
    }

    public String getTtime() {
        return ttime;
    }

    public void setTtime(String ttime) {
        this.ttime = ttime;
    }

    public String getRstatus() {
        return rstatus;
    }

    public void setRstatus(String rstatus) {
        this.rstatus = rstatus;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

}
