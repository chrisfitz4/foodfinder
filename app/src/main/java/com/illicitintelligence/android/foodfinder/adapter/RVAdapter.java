package com.illicitintelligence.android.foodfinder.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.illicitintelligence.android.foodfinder.R;
import com.illicitintelligence.android.foodfinder.model.Result;
import com.illicitintelligence.android.foodfinder.util.Constants;
import com.illicitintelligence.android.foodfinder.viewmodel.MapsViewModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private List<Result> resultList;
    private Context context;
    private Delegate delegate;
    MapsViewModel viewModel;

    public interface Delegate{
        void openFragment(int position);
    }

    public RVAdapter(List<Result> resultList, Context context, Delegate delegate) {
        this.resultList = resultList;
        this.context = context;
        this.delegate = delegate;
    }

    public void upDateResults(List<Result> results){
        resultList = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(resultList.get(position).getName());
        String reference ="";
        try{
            reference = resultList.get(position).getPhotos().get(0).getPhotoReference();
            Glide.with(context).load(String.format(Constants.SAMPLE, R.dimen.rv_dimen, R.dimen.rv_dimen-40, reference,Constants.API_KEY)).into(holder.image);
        }catch (NullPointerException n){
            Log.d("TAG_X", "onBindViewHolder: ");
            Glide.with(context).load(resultList.get(position).getIcon()).into(holder.image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.openFragment(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
        }
    }



//    public void searchPhoto(int maxWidth, int maxHeight, String reference){
//        retrofitMapsInstance.searchImage(maxWidth,maxHeight,reference).enqueue(new Callback<Image>() {
//            @Override
//            public void onResponse(Call<Image> call, Response<Image> response) {
//                pictures.setValue(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<Image> call, Throwable t) {
//                Log.d("TAG_X", "onFailure: "+t.getMessage());
//            }
//        });
//    }
}
