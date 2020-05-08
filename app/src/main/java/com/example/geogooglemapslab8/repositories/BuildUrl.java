package com.example.geogooglemapslab8.repositories;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

class BuildUrl {

    private static final String predicationBaseUrl = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";

    private static final String baseCoordinatesUrl = "https://maps.googleapis.com/maps/api/directions/json?";

    String buildPredictionUrl(final String predictionCity) {
        return predicationBaseUrl + "input=" + predictionCity + "&key=AIzaSyADKlRc-6Gqulin7yqO65th90nCn1lhaNU";
    }

    String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Building the url to the web service
        String url = baseCoordinatesUrl + parameters + "&key=AIzaSyDanAw2LwnwbLI4oBCWhFdodcLlqXLsLn0";
        Log.v("APP", url);
        return url;
    }
}
