package com.illicitintelligence.android.foodfinder.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.illicitintelligence.android.foodfinder.R;
import com.illicitintelligence.android.foodfinder.adapter.RVAdapterFrag;
import com.illicitintelligence.android.foodfinder.model.Result;
import com.illicitintelligence.android.foodfinder.util.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodFragment extends Fragment {

    private Result result;
    private RecyclerView recyclerView;
    private RVAdapterFrag adapter;
    private Button button;
    private static final String TAG = "TAG_X";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(view);
        Bundle bundle = getArguments();
        result = bundle.getParcelable(Constants.RESULT_KEY);
        button = view.findViewById(R.id.directions);
        adapter = new RVAdapterFrag(result.getPhotos(), this.getContext());
        recyclerView = view.findViewById(R.id.food_pictures);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        ((TextView) view.findViewById(R.id.rest_name)).setText(result.getName());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latlong = "";
                latlong += result.getGeometry().getLocation().getLat();
                latlong += ',';
                latlong += result.getGeometry().getLocation().getLng();
                Uri gmmIntentUri = Uri.parse("google.streetview:cbll=" + latlong);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(mapIntent);
                    Log.d("TAG_X", "onClick: success");
                } else {
                    Log.d("TAG_X", "onClick: failed");
                }
            }
        });
    }

}
