package com.example.geogooglemapslab8;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geogooglemapslab8.network.Cordinates;
import com.example.geogooglemapslab8.repositories.NetworkRepo;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView editTextFrom, editTextTo;
    private NetworkRepo networkRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button routeBtn = findViewById(R.id.button);
        editTextFrom = findViewById(R.id.editText);
        editTextTo = findViewById(R.id.autoCompleteTextView);

        networkRepo = new NetworkRepo(this);

       editTextFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editTextFrom.isPerformingCompletion()) {
                    networkRepo.setData(s.toString(), RequestEmun.LocationFrom);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

       editTextTo.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               if(!editTextTo.isPerformingCompletion()) {
                   networkRepo.setData(s.toString(), RequestEmun.LocationTo);
               }
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

      networkRepo.getResponseData().observe(this, dataModel -> {
          ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, dataModel.getDataList());


          if(dataModel.getDataList().toString().length() <= 3){
              editTextFrom.setDropDownHeight(120 * dataModel.getDataList().toString().length());
              editTextTo.setDropDownHeight(120 * dataModel.getDataList().toString().length());
          }else {
              editTextFrom.setDropDownHeight(120 * 3);
              editTextTo.setDropDownHeight(120 * 3);
          }


          switch (dataModel.getRequestEmun()){
             case LocationFrom:
                 editTextFrom.setAdapter(arrayAdapter);
                 editTextFrom.showDropDown();
                 break;
             case LocationTo:
                 editTextTo.setAdapter(arrayAdapter);
                 editTextTo.showDropDown();
         }
      });

      routeBtn.setOnClickListener(v -> {

          Log.v("APP", "cord" + new Cordinates().getLatLngFromAddress(editTextTo.getText().toString(), this));

          networkRepo.getCoordinates(editTextFrom.getText().toString(),editTextTo.getText().toString());
      });

      networkRepo.getCoordinatesData().observe(this, latLng -> {

          if(latLng.get(0) != null && latLng.get(1) != null) {
              Intent intent = new Intent(this, MapActivity.class);

              intent.putExtra("from", latLng.get(0));
              intent.putExtra("to", latLng.get(1));

              startActivity(intent);
          }else {
              Toast.makeText(this,"Null suka", Toast.LENGTH_SHORT).show();
          }
      });

    }
}