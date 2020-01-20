package com.illicitintelligence.android.foodfinder.network;

import android.content.Context;
import android.media.Image;
import android.util.Log;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.illicitintelligence.android.foodfinder.model.FoodFound;
import com.illicitintelligence.android.foodfinder.util.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitMapsInstance {

    private RetrofitMapsService mapsService;
    private OkHttpClient httpClient;
    private long cacheSize = 10 * 1024 *1024;

    private Cache cache;

    public RetrofitMapsInstance() {
//        this.cache = new Cache(cacheDirectory,cacheSize);
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//            @Override
//            public void log(@NotNull String message) {
//                Log.d("TAG_X", "log: "+message);
//            }
//        });
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        httpClient = new OkHttpClient().newBuilder()
//                .addInterceptor(loggingInterceptor)
//                .build();
        mapsService = createMapsService(createRetrofit());
    }

    private Retrofit createRetrofit(){
        return new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private RetrofitMapsService createMapsService(Retrofit retrofit){
        return retrofit.create(RetrofitMapsService.class);
    }

    public Call<FoodFound> searchFood(String location, String foodType){
        return mapsService.searchFood(location, "distance", "restaurant", foodType, Constants.API_KEY);
    }

}
