package com.example.concordia_campus_guide.database.converters;

import android.os.Build;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

public class StringListConverter  {
    private static final String SPLIT_CHAR = ",";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public String convertToDatabaseColumn(List<String> stringList) {
        if(stringList != null){
            return String.join(SPLIT_CHAR, stringList);
        }
            return null;
    }

    @TypeConverter
    public List<String> convertToEntityAttribute(String string) {
        if (string !=null){
            return Arrays.asList(string.split(SPLIT_CHAR));
        }
        return new ArrayList<>();
    }
}