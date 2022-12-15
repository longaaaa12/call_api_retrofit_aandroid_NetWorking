package com.example.th_call_api_retrofit.data;

import com.example.th_call_api_retrofit.model.Moto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("moto")
    Call<List<Moto>> getAllMoto();

    //them moi moto
    @POST("/moto")
    Call<Moto> PostMoto(@Body Moto objPro );
    @FormUrlEncoded


    //suaMoto
    @PUT("moto/{id}")
    Call<Moto> UpdateMoto(@Path("id") String id,
                     @Field("name") String name,
                     @Field("price") int price,
                     @Field("color") String color);

    @DELETE("moto/{id}")
    Call<Moto> deleteMoto(@Path("id") int id);
}
