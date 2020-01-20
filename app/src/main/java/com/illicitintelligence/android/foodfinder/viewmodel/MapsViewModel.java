package com.illicitintelligence.android.foodfinder.viewmodel;

import android.media.Image;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.illicitintelligence.android.foodfinder.model.FoodFound;
import com.illicitintelligence.android.foodfinder.network.RetrofitMapsInstance;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsViewModel extends ViewModel {

    private RetrofitMapsInstance retrofitMapsInstance = new RetrofitMapsInstance();
    private MutableLiveData<FoodFound> foodFoundMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Image> pictures = new MutableLiveData<>();



    public void searchFood(String location, String foodType){
        retrofitMapsInstance.searchFood(location,foodType).enqueue(new Callback<FoodFound>() {
            @Override
            public void onResponse(Call<FoodFound> call, Response<FoodFound> response) {
                foodFoundMutableLiveData.setValue(response.body());
                Log.d("TAG_X", "onResponse error: "+response.body().getErrorMessage());
                Log.d("TAG_X", "onResponse status: "+response.body().getStatus());
            }

            @Override
            public void onFailure(Call<FoodFound> call, Throwable t) {
                Log.d("TAG_X", "onFailure: "+t.getMessage());
            }
        });
    }



    public MutableLiveData<FoodFound> getFoodFoundMutableLiveData(){
        return foodFoundMutableLiveData;
    }


}
