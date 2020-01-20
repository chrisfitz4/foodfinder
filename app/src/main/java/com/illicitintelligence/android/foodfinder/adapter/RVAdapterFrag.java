package com.illicitintelligence.android.foodfinder.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.illicitintelligence.android.foodfinder.R;
import com.illicitintelligence.android.foodfinder.model.Photo;
import com.illicitintelligence.android.foodfinder.util.Constants;

import java.util.List;

public class RVAdapterFrag extends RecyclerView.Adapter<RVAdapterFrag.ViewHolder> {


    private List<Photo> photos;
    private Context context;

    public RVAdapterFrag(List<Photo> photos, Context context) {
        this.photos = photos;
        this.context = context;
    }

    public void updateList(List<Photo> photos){
        this.photos = photos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            String reference = String.format(Constants.SAMPLE,
                    200,
                    200,
                    photos.get(position).getPhotoReference(),
                    Constants.API_KEY);
            Log.d("TAG_X", "onBindViewHolder: " + reference);
            Glide.with(context)
                    .load(reference)
                    .into(holder.imageView);
        }catch (NullPointerException n){
            Log.d("TAG_X", "onBindViewHolder: null pointer exception");
            Glide.with(context)
                    .load("https://cdn3.vectorstock.com/i/1000x1000/86/97/bakery-food-cartoon-vector-23468697.jpg")
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photo_restaurant);
        }
    }
}
