package com.example.mapbox.model;

public class Login_Model {
    String message,uid,utype,pchid,clat,clon;

    public String getPchid() {
        return pchid;
    }

    public String getClat() {
        return clat;
    }

    public void setClat(String clat) {
        this.clat = clat;
    }

    public String getClon() {
        return clon;
    }

    public void setClon(String clon) {
        this.clon = clon;
    }

    public void setPchid(String pchid) {
        this.pchid = pchid;
    }

    public String getUtype() {
        return utype;
    }

    public void setUtype(String utype) {
        this.utype = utype;
    }

    public Login_Model(String message, String uid) {
        this.message = message;
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
