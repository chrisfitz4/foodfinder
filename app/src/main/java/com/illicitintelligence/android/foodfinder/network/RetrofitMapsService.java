package com.illicitintelligence.android.foodfinder.network;

import android.media.Image;

import com.illicitintelligence.android.foodfinder.model.FoodFound;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitMapsService {

    @GET("/maps/api/place/nearbysearch/json")
    Call<FoodFound> searchFood(@Query("location") String latlng,
                               @Query("rankby") String distance,
                               @Query("type") String searchFor,
                               @Query("keyword") String foodType,
                               @Query("key") String apiKey);


}
