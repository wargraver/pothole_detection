package com.example.pitholemanagment.Network;

import com.example.pitholemanagment.Model.Report_Pothole_Model;
import com.example.pitholemanagment.Model.Response;
import com.example.pitholemanagment.Model.Token;
import com.example.pitholemanagment.Model.user;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Jsonapi
{
    @POST("signup")
    Call<Response> register(@Body user us);
    @POST("login")
    Call<Response> login(@Body user us);
    @POST("info")
    Call<Response> getuser(@Body Token token);
    @POST("pending")
    Call<Response> getreports(@Body Token tk);
    @POST("resolved")
    Call<Response> getreportsresolved(@Body Token tk);
    @POST("create")
    Call<Response> reportpothole(@Body Report_Pothole_Model report_pothole_model);
    @POST("logout")
    Call<Response> logout(@Body Token tk);
    @POST("forgotpassword")
    Call<Response> resetpass(@Body user us);
}
