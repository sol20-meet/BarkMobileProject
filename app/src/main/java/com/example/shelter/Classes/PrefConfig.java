package com.example.shelter.Classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.example.shelter.MyReceiverBattery;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class PrefConfig {
    private static final String LIST_KEY = "list_key";
    public static void saveReceiver(Context context, MyReceiverBattery myReceiverBattery) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(myReceiverBattery);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LIST_KEY, jsonString);
        editor.apply();
    }

    public static MyReceiverBattery getReceiver(Context context)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString(LIST_KEY, "");

        Gson gson = new Gson();
        Type type = new TypeToken<MyReceiverBattery>(){}.getType();
        MyReceiverBattery myReceiverBattery = gson.fromJson(jsonString, type);
        return myReceiverBattery;
    }
}


