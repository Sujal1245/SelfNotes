package com.Sujal_Industries.Notes.SelfNotes;

import android.content.Context;
import android.content.SharedPreferences;

import com.orm.SugarApp;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

public class MyApplication extends SugarApp {
    private static final String spFileKey = "SelfNotes.SECRET_FILE";
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences=getSharedPreferences(spFileKey,MODE_PRIVATE);
        boolean isNight=sharedPreferences.getBoolean("isNight",false);
        if(isNight)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
}
