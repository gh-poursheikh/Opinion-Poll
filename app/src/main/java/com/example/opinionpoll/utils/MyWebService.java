package com.example.opinionpoll.utils;

import com.example.opinionpoll.model.Person;
import com.example.opinionpoll.model.Product;
import com.example.opinionpoll.model.Report;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MyWebService {

    String BASE_URL = "http://10.0.2.2:80/"; // WebServer URL on XAMPP for AVDs

    String LOGIN_FEED = "opinion_poll/login.php";
    String PRODUCT_FEED = "opinion_poll/get_product_list.php";
    String RATING_FEED = "opinion_poll/set_rating.php";
    String REPORT_FEED = "opinion_poll/get_report.php";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @FormUrlEncoded
    @POST(LOGIN_FEED)
    Call<Person> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST(PRODUCT_FEED)
    Call<ArrayList<Product>> getProduct(@Field("product_id") int productId);

    @FormUrlEncoded
    @POST(RATING_FEED)
    Call<Map<String, String>> setRating(@FieldMap Map<String, String> params);

    @GET(REPORT_FEED)
    Call<ArrayList<Report>> getReport();
}
