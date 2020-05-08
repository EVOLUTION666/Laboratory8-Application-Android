package com.example.geogooglemapslab8.network;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public class Cordinates {

    public LatLng getLatLngFromAddress(String address, Context context) {

        final Geocoder geocoder=new Geocoder(context);

        try {
            List<Address> addressList = geocoder.getFromLocationName(address, 1);
            if(addressList!=null){
                return new LatLng(addressList.get(0).getLatitude(),addressList.get(0).getLongitude());
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
