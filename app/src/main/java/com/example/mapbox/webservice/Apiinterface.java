package com.example.mapbox.webservice;


import com.example.mapbox.model.Login_Model;
import com.example.mapbox.model.NearestModel;
import com.example.mapbox.model.Profile_pojo;
import com.example.mapbox.model.Request_model;
import com.example.mapbox.model.viewRequest_model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface Apiinterface {

    //@FormUrlEncoded
    @GET("pickme.php")
    Call<Login_Model> getlogindata(@Query("key") String token, @Query("uname") String userid, @Query("pass") String pass);

    @FormUrlEncoded
    @POST("pickme.php")
    Call<Login_Model> getRegisterData(@Field("key") String token1,
                                      @Field("name") String userid1, @Field("email") String pass1,
                                      @Field("phone") String userid2, @Field("address") String pass2,
                                      @Field("gender") String userid3, @Field("utype") String pass3,
                                      @Field("vname") String userid4, @Field("vno") String pass4,
                                      @Field("seat") String userid5, @Field("lisence") String pass5,
                                      @Field("photo") String userid, @Field("token") String token,
                                      @Field("cnum") String cnum, @Field("cvv") String cvv,@Field("pin") String pin);

    @FormUrlEncoded
    @POST("pickme.php")
    Call<Login_Model> updatevehicledata(@Field("key") String token1,
                                        @Field("vname") String userid4, @Field("vno") String pass4,
                                        @Field("seat") String userid5, @Field("lisence") String pass5,
                                        @Field("uid") String token);

    @FormUrlEncoded
    @POST("pickme.php")
    Call<Login_Model> getRide(@Field("key") String token,
                              @Field("from") String userid1, @Field("to") String pass1,
                              @Field("date") String userid2, @Field("time") String pass2,
                              @Field("clat") String userid3, @Field("clon") String pass3,
                              @Field("dlat") String userid4, @Field("dlon") String pass4, @Field("uid") String uid);

    @GET("pickme.php")
    Call<Login_Model> locupdate(@Query("key") String token, @Query("uid") String userid, @Query("lat") String pass, @Query("lon") String lon);

    @GET("pickme.php")
    Call<Profile_pojo> getprofile(@Query("key") String token, @Query("uid") String userid);


    @FormUrlEncoded
    @POST("pickme.php")
    Call<List<NearestModel>> searchnear1(@Field("key") String token, @Field("rdate") String date, @Field("dest") String dest);

    @FormUrlEncoded
    @POST("pickme.php")
    Call<List<NearestModel>> searchnear(@Field("key") String token, @Field("lat") String uid, @Field("lon") String dlon, @Field("rdate") String date);

    @FormUrlEncoded
    @POST("pickme.php")
    Call<Login_Model> sendreq(@Field("key") String token, @Field("uid") String userid,
                              @Field("rid") String pass, @Field("date") String date, @Field("token") String token1);

    @GET("pickme.php")
    Call<List<viewRequest_model>> getRequest(@Query("key") String token, @Query("uid") String uid);

    @GET("pickme.php")
    Call<Login_Model> RequestDo(@Query("key") String token, @Query("reqid") String type, @Query("action") String act, @Query("token") String tok
            , @Query("uid") String uid);

    @GET("pickme.php")
    Call<Login_Model> finishride(@Query("key") String token, @Query("num") String type, @Query("uid") String uid, @Query("reqid") String req, @Query("rid") String rid);

    @FormUrlEncoded
    @POST("pickme.php")
    Call<List<Request_model>> getMyride(@Field("key") String token, @Field("uid") String uid);

    @FormUrlEncoded
    @POST("pickme.php")
    Call<Login_Model> sendRating(@Field("key") String token, @Field("uid") String type, @Field("rid") String typ1e, @Field("rating") String type2);

    @FormUrlEncoded
    @POST("pickme.php")
    Call<Login_Model> addparent(@Field("key") String token, @Field("pname") String type, @Field("pmobile") String typ1e, @Field("uid") String type2);

    @FormUrlEncoded
    @POST("pickme.php")
    Call<Login_Model> mychild(@Field("key") String token, @Field("pchid") String userid);

    @FormUrlEncoded
    @POST("pickme.php")
    Call<Login_Model> pay(@Field("key") String token, @Field("uid") String uid,@Field("rid") String rid,@Field("amnt") String amt,@Field("bal") String bal);

}
