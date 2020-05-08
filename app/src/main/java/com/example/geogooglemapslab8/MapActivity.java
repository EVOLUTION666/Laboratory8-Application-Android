package com.example.geogooglemapslab8;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import android.widget.Toast;

import com.example.geogooglemapslab8.repositories.NetworkRepo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private NetworkRepo networkRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        networkRepo = new NetworkRepo(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final LatLng fromPosition = getIntent().getExtras().getParcelable("from");
        final LatLng toPosition = getIntent().getExtras().getParcelable("to");

        mMap.addMarker(new MarkerOptions().position(fromPosition));
        mMap.addMarker(new MarkerOptions().position(toPosition));

        networkRepo.loadRoad(fromPosition, toPosition);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(fromPosition));
        mMap.setMyLocationEnabled(true);


        networkRepo.gerRoadList().observe(this, modelRoads -> {
            if(modelRoads!=null){
                mMap.addPolyline(modelRoads);
            }else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }

        });

    }

}
