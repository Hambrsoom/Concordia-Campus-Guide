package com.example.concordia_campus_guide.InfoCardFragment;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.concordia_campus_guide.Models.Buildings;
import com.example.concordia_campus_guide.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

// This class is for the business logic
public class InfoCardFragmentViewModel extends AndroidViewModel {


    public InfoCardFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public Buildings readJsonFile(Context context){
        String json;
        Buildings buildings = new Buildings();

        try{
            InputStream is = getApplication().getResources().openRawResource(R.raw.buildings_info);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            buildings = new Gson().fromJson(json, Buildings.class);
        } catch (IOException e){
            e.printStackTrace();
        }

        return buildings;
    }

}
