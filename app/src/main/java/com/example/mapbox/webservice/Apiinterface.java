package com.example.mapbox.webservice;


import com.example.mapbox.model.Login_Model;
import com.example.mapbox.model.NearestModel;
import com.example.mapbox.model.Profile_pojo;

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
    Call<Login_Model> getRegisterData(@Field("key") String token,
                                      @Field("name") String userid1, @Field("email") String pass1,
                                      @Field("phone") String userid2, @Field("address") String pass2,
                                      @Field("gender") String userid3, @Field("utype") String pass3,
                                      @Field("vname") String userid4, @Field("vno") String pass4,
                                      @Field("seat") String userid5, @Field("lisence") String pass5,
                                      @Field("photo") String userid);

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
    Call<List<NearestModel>> searchnear(@Field("key") String token, @Field("lat") String uid, @Field("lon") String dlon, @Field("rdate") String date);
    @GET("pickme.php")
    Call<Login_Model> sendreq(@Query("key") String token, @Query("uid") String userid, @Query("rid") String pass,@Query("date") String date);
//    @FormUrlEncoded
//    @POST("myride.php")
//    Call<login_model> getComplaint(@Field("key") String token, @Field("rname") String type,
//                                   @Field("rfeed") String userid1, @Field("date") String date);
//    @FormUrlEncoded
//    @POST("myride.php")
//    Call<login_model> getfeedback(@Field("key") String token, @Field("rname") String type,
//                                  @Field("rfeed") String userid1, @Field("date") String date);
//
//    @GET("myride.php")
//    Call<Profile_model> getProfile(@Query("key") String token, @Query("uid") String userid);
//    @FormUrlEncoded
//    @POST("myride.php")
//    Call<login_model> getnewRide(@Field("key") String token, @Field("from") String type,
//                                 @Field("to") String userid1, @Field("date") String token1, @Field("time") String type1,
//                                 @Field("uid") String type2, @Field("route") String type3,
//                                 @Field("slat") String type4, @Field("slon") String type6,
//                                 @Field("dlat") String type5, @Field("dlon") String type7, @Field("km") String type8);
//    @GET("myride.php")
//    Call<List<Myride_model>> getride(@Query("key") String token, @Query("uid") String type);
//    @GET("myride.php")
//    Call<List<Complaint_model>> gecomplaint(@Query("key") String token);
//    @GET("myride.php")
//    Call<List<Feedback_model>> getFeedback(@Query("key") String token);
//    @GET("myride.php")
//    Call<List<riders_model>> getAllRiders(@Query("key") String token);
//    @GET("myride.php")
//    Call<List<User_model>> getAllUsers(@Query("key") String token);
//    @GET("myride.php")
//    Call<List<ViewRides_model>> getRides(@Query("key") String token, @Query("uid") String uid);
//    @GET("myride.php")
//    Call<List<Request_model>> searchride(@Query("key") String token, @Query("dlat") String uid, @Query("dlon") String dlon, @Query("date") String date);
//    @GET("myride.php")
//    Call<List<Request_model>> getMyride(@Query("key") String token, @Query("uid") String uid);
//
//    @GET("myride.php")
//    Call<List<viewRequest_model>> getRequest(@Query("key") String token, @Query("uid") String uid);
//
//
//    @GET("myride.php")
//    Call<List<Complaint_model>> getReward(@Query("key") String token, @Query("uid") String uid);
//
//    @FormUrlEncoded
//    @POST("myride.php")
//    Call<login_model> addpoint(@Field("key") String token, @Field("uid") String type,
//                               @Field("point") String userid1, @Field("km") String km);
//
//    @FormUrlEncoded
//    @POST("myride.php")
//    Call<login_model> checkApproval(@Field("key") String token, @Field("category") String type,
//                                    @Field("uid") String userid1);
//    @FormUrlEncoded
//    @POST("myride.php")
//    Call<login_model> checkuserApproval(@Field("key") String token, @Field("category") String type,
//                                        @Field("uid") String userid1);
//    @FormUrlEncoded
//    @POST("myride.php")
//    Call<login_model> sendRequest(@Field("key") String token, @Field("uid") String type,
//                                  @Field("riderid") String userid1, @Field("rid") String km);
//
//    @FormUrlEncoded
//    @POST("myride.php")
//    Call<login_model> deleteRequest(@Field("key") String token, @Field("reqid") String type);
//
//    @GET("myride.php")
//    Call<login_model> getRiderLoc(@Query("key") String token, @Query("riderid") String type);
//    @GET("myride.php")
//    Call<login_model> finishreq(@Query("key") String token, @Query("reqid") String type);
//    @GET("myride.php")
//    Call<login_model> locupdate(@Query("key") String token, @Query("uid") String type, @Query("type") String utype,
//                                @Query("lat") String lat, @Query("lon") String lon);
//    @GET("myride.php")
//    Call<login_model> RequestDo(@Query("key") String token, @Query("reqid") String type, @Query("action") String act);
//    @GET("myride.php")
//    Call<login_model> startRide(@Query("key") String token, @Query("rid") String type);
//
//    @GET("myride.php")
//    Call<login_model> getCustomerLoc(@Query("key") String token, @Query("rid") String type);
//    @GET("myride.php")
//    Call<login_model> finidhRide(@Query("key") String token, @Query("rid") String type);
//    @GET("myride.php")
//    Call<login_model> deleteRide(@Query("key") String token, @Query("rid") String type);
//    @GET("myride.php")
//    Call<List<NearestModel>> searcnear(@Query("key") String token, @Query("lat") String uid, @Query("lon") String dlon, @Query("rdate") String date);

}
