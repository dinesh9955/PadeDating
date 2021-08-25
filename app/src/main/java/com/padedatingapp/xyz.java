package com.padedatingapp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.time.Instant;
import java.util.ArrayList;

public class xyz extends AppCompatActivity {

    private static final String TAG = "xyz";

    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.xyz);

        button = findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                MainModel mainModel = new MainModel();
                mainModel.setTrip_id("1");
                mainModel.setStart_time(""+ Instant.now().toString());
                mainModel.setEnd_time(""+Instant.now().toString());
                mainModel.setLocations(getLocations());

                String sssss = new Gson().toJson(mainModel);

                Log.e(TAG, "sssss "+sssss);
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<LocationsModel> getLocations() {
        ArrayList<LocationsModel> locationsModels = new ArrayList<>();
        LocationsModel model = new LocationsModel();
        model.setLatitude(30.43345);
        model.setLongitude(70.33345);
        model.setAccuracy(3.5);
        model.setTimestamp(""+Instant.now().toString());
        locationsModels.add(model);
        return locationsModels;
    }
}
