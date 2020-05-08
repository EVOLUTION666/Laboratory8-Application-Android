package com.example.geogooglemapslab8.repositories;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    public List<String> parseData(String jsonObjectData) {
        List<String> listCities = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonObjectData);
            JSONArray prediction = jsonObject.getJSONArray("predictions");
            for (int i = 0; i < prediction.length(); i++) {
                listCities.add(prediction.getJSONObject(i).getString("description"));
            }

        } catch (Exception e) {
            Log.v("APP", "ERROR");
        }
        return listCities;
    }


    public PolylineOptions parseCoordinate(String jsonObjectCoordinate) throws JSONException {

        PolylineOptions lineOptions = new PolylineOptions();

        JSONObject mainJSON = new JSONObject(jsonObjectCoordinate);
        String ststus = mainJSON.getString("status");

        if(ststus.equals("OK")) {
            JSONArray jarray1 = mainJSON.getJSONArray("routes");
            JSONObject jobj1 = jarray1.getJSONObject(0);
            JSONArray jarray2 = jobj1.getJSONArray("legs");
            JSONObject jobj2 = jarray2.getJSONObject(0);
            JSONArray stepArray = jobj2.getJSONArray("steps");

            for (int i = 0; i < stepArray.length(); i++) {

                JSONObject poluline = stepArray.getJSONObject(i).getJSONObject("polyline");

                String steps = poluline.getString("points");

                lineOptions.addAll(decodePoly(steps));
            }
            return lineOptions;
        }else {
            return null;
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

}

