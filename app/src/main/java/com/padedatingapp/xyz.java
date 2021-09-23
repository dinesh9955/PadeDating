package com.padedatingapp;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

public class xyz extends AppCompatActivity {

    private static final String TAG = "xyz";

    Button button;
    TextView textView;

    SavePref pref = new SavePref();

    MainModel mainModel = new MainModel();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.xyz);

        pref.SavePref(xyz.this);

        button = findViewById(R.id.button2);
        textView = findViewById(R.id.textView);

        mainModel.setTrip_id("1");
        mainModel.setStart_time(""+ Instant.now().toString());
        mainModel.setEnd_time(""+Instant.now().toString());
       // mainModel.setLocations(getLocations());

       // String sssssxx = new Gson().toJson(mainModel);

//        generateNoteOnSD(xyz.this , "track.txt", "");

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
//                if(!pref.getAddress().equalsIgnoreCase("")){
//                    mainModel = new Gson().fromJson(pref.getAddress(), MainModel.class);
//                    ArrayList<LocationsModel> loc = new ArrayList<>();
//                    try{
//                        loc = mainModel.getLocations();
//                        Log.e(TAG, "sssss "+loc);
//                    }catch (Exception e){
//                        Log.e(TAG, "sssssEE "+e.getMessage());
//                    }
//                    mainModel.setLocations(getLocations(loc));
//                }
//
//
//                String sssss = new Gson().toJson(mainModel);
//                pref.setAddress(sssss);
//
//                Log.e(TAG, "sssssbb "+sssss);
//
//                textView.setText(sssss);
//

                String sssssDD = readFileAsString("/storage/emulated/0/Notes2/track.txt");
                //Log.e(TAG, "sssssDD "+sssssDD);

                if(!sssssDD.equalsIgnoreCase("")){
                    mainModel = new Gson().fromJson(sssssDD, MainModel.class);
                    ArrayList<LocationsModel> loc = new ArrayList<>();
                    try{
                        loc = mainModel.getLocations();
                        Log.e(TAG, "sssss "+loc);
                    }catch (Exception e){
                        Log.e(TAG, "sssssEE "+e.getMessage());
                    }
                    mainModel.setLocations(getLocations(loc));
                }

                String sssss = new Gson().toJson(mainModel);
                generateNoteOnSD(xyz.this , "track.txt", ""+sssss.toString());
                Log.e(TAG, "sssssDD "+sssssDD);
                textView.setText(sssss);

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<LocationsModel> getLocations(ArrayList<LocationsModel> locationsModels) {
        //ArrayList<LocationsModel> locationsModels = new ArrayList<>();
        LocationsModel model = new LocationsModel();
        model.setLatitude(30.43345);
        model.setLongitude(70.33345);
        model.setAccuracy(3.5);
        model.setTimestamp(""+Instant.now().toString());
        locationsModels.add(model);
        return locationsModels;
    }




    public static void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes2");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            Log.e(TAG, "gpxfile "+gpxfile.toString());
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static String readFileAsString(String filePath) {
        String result = "";
        File file = new File(filePath);
        if ( file.exists() ) {
            //byte[] buffer = new byte[(int) new File(filePath).length()];
            FileInputStream fis = null;
            try {
                //f = new BufferedInputStream(new FileInputStream(filePath));
                //f.read(buffer);

                fis = new FileInputStream(file);
                char current;
                while (fis.available() > 0) {
                    current = (char) fis.read();
                    result = result + String.valueOf(current);
                }
            } catch (Exception e) {
                Log.d("TourGuide", e.toString());
            } finally {
                if (fis != null)
                    try {
                        fis.close();
                    } catch (IOException ignored) {
                    }
            }
            //result = new String(buffer);
        }
        return result;
    }

    
}
