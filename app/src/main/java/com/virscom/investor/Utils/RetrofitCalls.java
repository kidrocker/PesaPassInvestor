package com.virscom.investor.Utils;



import com.virscom.investor.Retrofit.Response;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by kiura on 3/28/2018.
 */

public interface RetrofitCalls {
    @FormUrlEncoded
    @POST("api")
    Call<Response> checkUser(
            @Field("trans_type") String trans_type,
            @Field("userID") String userID
    );

    @FormUrlEncoded
    @POST("api")
    Call<Response>registerUser(
            @Field("trans_type") String trans_type,
            @Field("userID") String userID,
            @Field("deviceID") String deviceID,
            @Field("pin") String pin,
            @Field("mobileno") String phone
    );

    @FormUrlEncoded
    @POST("api")
    Call<Response>loginUser(
            @Field("trans_type") String trans_type,
            @Field("pin") int pin,
            @Field("userID") String userID
    );
    @FormUrlEncoded
    @POST("api")
    Call<Response>loanRequest(
            @Field("trans_type") String trans_type,
            @Field("amount") int amount,
            @Field("userID") String userID
    );

    @FormUrlEncoded
    @POST("api")
    Call<Response>updateDetails(
            @Field("trans_type") String trans_type,
            @Field("userID") String userID,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("national_id") String national_id,
            @Field("contact_person") String contact_person,
            @Field("contact_mobile") String contact_mobile
    );
    @FormUrlEncoded
    @POST("api")
    Call<Response>repayLoan(
            @Field("trans_type") String trans_type,
            @Field("userID") String userID,
            @Field("amount") int amount

    );

    @FormUrlEncoded
    @POST("api")
    Call<Response>accountRefresh(
            @Field("trans_type") String trans_type,
            @Field("userID") String userID
    );


}
