package com.example.geogooglemapslab8.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.geogooglemapslab8.RequestEmun;
import com.example.geogooglemapslab8.network.Cordinates;
import com.example.geogooglemapslab8.network.Network;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

public class NetworkRepo {

   final private Parser parser = new Parser();
    final private Cordinates cordinates;
   final private Context context;

    public NetworkRepo(Context context) {
        this.context = context;
        cordinates = new Cordinates();
    }

    final private Network network = new Network();

   List<String> data = Arrays.asList("Moscow, Russia", "Amsterdam, Netherlands", "United States");

   final private MutableLiveData<DataModel> listMutableLiveData = new MutableLiveData<>();
   final private MutableLiveData<List<LatLng>> coordinatesLiveData = new MutableLiveData<>();
   final private MutableLiveData<PolylineOptions> mutableLiveData = new MutableLiveData<>();

    public void setData(final String text, final RequestEmun requestEmun) {

       final DataModel dataModel = new DataModel();

            if (text.trim().length() > 2) {
                Network.executorService.submit(() -> {

                    dataModel.setRequestEmun(requestEmun);
                    dataModel.setDataList(parser.parseData(network.getData(new BuildUrl().buildPredictionUrl(text))));
                  // dataModel.setDataList(data);
                    if(!dataModel.getDataList().isEmpty())
                    listMutableLiveData.postValue(dataModel);
                });
            }
    }

    public void getCoordinates(final  String addressTo, final String addressFrom){

        List<LatLng> list = new ArrayList<>();

        Network.executorService.submit(() -> {
            list.add(cordinates.getLatLngFromAddress(addressTo, context));
            list.add(cordinates.getLatLngFromAddress(addressFrom, context));

            coordinatesLiveData.postValue(list);
        });
    }

    public void loadRoad(final LatLng latLngFrom, final LatLng latLngTo){

        Network.executorService.submit(() ->{
            try {
               mutableLiveData.postValue(parser.parseCoordinate(network.getData(new BuildUrl().getUrl(latLngFrom, latLngTo, "driving"))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }


    public LiveData<DataModel> getResponseData() {
        return listMutableLiveData;
    }
    public LiveData<List<LatLng>> getCoordinatesData() {  return coordinatesLiveData; }

    public LiveData<PolylineOptions> gerRoadList() {
        return mutableLiveData;
    }

}