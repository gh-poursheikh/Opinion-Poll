package com.example.opinionpoll.utils;

import com.example.opinionpoll.model.Person;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MyWebService {

    String BASE_URL = "http://10.0.2.2:80/"; // WebServer URL on XAMPP for AVDs

    String LOGIN_FEED = "opinion_poll/login.php";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @FormUrlEncoded
    @POST(LOGIN_FEED)
    Call<Person> login(@Field("username") String username, @Field("password") String password);

}
