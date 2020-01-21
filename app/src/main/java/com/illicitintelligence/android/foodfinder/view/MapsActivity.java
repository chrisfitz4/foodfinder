package com.illicitintelligence.android.foodfinder.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.illicitintelligence.android.foodfinder.R;
import com.illicitintelligence.android.foodfinder.adapter.RVAdapter;
import com.illicitintelligence.android.foodfinder.model.FoodFound;
import com.illicitintelligence.android.foodfinder.model.Result;
import com.illicitintelligence.android.foodfinder.util.Constants;
import com.illicitintelligence.android.foodfinder.viewmodel.MapsViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, PopupMenu.OnMenuItemClickListener, RVAdapter.Delegate {

    private GoogleMap mMap;
    private final int REQUEST_CODE = 717;
    MapsViewModel viewModel;
    String latlong;
    boolean initialMoveComplete = false;
    float zoom = 12f;
    private Marker marker;

    Observer<FoodFound> observer;

    List<Result> results = new ArrayList<>();
    Handler handler = new Handler();

    AnimatedVectorDrawable vectorDrawable;
    @BindView(R.id.menu_chooser)
    ImageView menuChooser;
    @BindView(R.id.rv_foodoptions)
    RecyclerView recyclerView;
    @BindView(R.id.loading_screen)
    ImageView loadingScreen;
    @BindView(R.id.titleScreen)
    TextView titleScreen;
    @BindView(R.id.loading_frame_layout)
    FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ButterKnife.bind(this);

        vectorDrawable = (AnimatedVectorDrawable)loadingScreen.getBackground();
        vectorDrawable.start();


        RVAdapter adapter = new RVAdapter(results,this.getApplicationContext(),this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        viewModel = ViewModelProviders.of(this).get(MapsViewModel.class);

        observer = new Observer<FoodFound>() {
            @Override
            public void onChanged(FoodFound foodFound) {
                mMap.clear();
                results = foodFound.getResults();
                adapter.upDateResults(results);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(0);
                recyclerView.setVisibility(View.VISIBLE);
                Log.d("TAG_X", "onChanged: "+results.toString());
                for(int i = 0; i< results.size(); i++){
                    mMap.addMarker(new MarkerOptions().position(new LatLng(results.get(i).getGeometry().getLocation().getLat(),
                            results.get(i).getGeometry().getLocation().getLng())));
                }
            }
        };
        viewModel.getFoodFoundMutableLiveData().observe(this,observer);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



/////////////////////////permissions

    private void requestPermission(){
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }else{
            setUpListener();
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(4000);
                }catch (InterruptedException i){
                    Log.d("TAG_X", "run: "+i.getMessage());
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        vectorDrawable.stop();
                        loadingScreen.setVisibility(View.GONE);
                        titleScreen.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });
        thread.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE){
            if(permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                setUpListener();
            }else{
                requestPermission();
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        menuChooser.setVisibility(View.VISIBLE);
        requestPermission();
    }

    private void setUpListener(){
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latlong = location.getLatitude()+","+location.getLongitude();
                Log.d("TAG_X", "onLocationChanged: "+latlong);
                if(!initialMoveComplete){
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),zoom));
                    mMap.setMyLocationEnabled(true);
                    initialMoveComplete=true;
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        LocationManager manager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
        try {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
        }catch (SecurityException s){
            Log.d("TAG_X", "setUpListener: "+s.getMessage());
        }
    }




///////////////////////////menu
    @OnClick(R.id.menu_chooser)
    public void onClick(View view){
        PopupMenu menu = new PopupMenu(this,view);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.search_types_menu,menu.getMenu());
        menu.show();
        menu.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        viewModel.searchFood(latlong,item.getTitle().toString());
        Log.d("TAG_X", "onMenuItemClick: "+item.getTitle());
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.getFoodFoundMutableLiveData().removeObserver(observer);
    }

    @Override
    public void openFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RESULT_KEY,results.get(position));
        FoodFragment fragment = new FoodFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(fragment.getTag())
                .add(R.id.frame_layout,fragment)
                .commit();
    }
}
