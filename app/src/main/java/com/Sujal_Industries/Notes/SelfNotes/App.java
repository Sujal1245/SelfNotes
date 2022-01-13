package com.Sujal_Industries.Notes.SelfNotes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class App extends AppCompatActivity {
    FloatingActionButton add;
    FloatingActionButton changeUI;
    RecyclerView recyclerView;
    LinearLayout alt_layout;
    AlarmAdapter alarmAdapter;
    List<Alarm> alarmList;
    private static final String spFileKey = "SelfNotes.SECRET_FILE";

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        recyclerView = findViewById(R.id.recycler_view);
        alt_layout = findViewById(R.id.alt_linear);
        add = findViewById(R.id.add);
        changeUI = findViewById(R.id.changeUI);

        alarmList = new ArrayList<>();
        alarmAdapter = new AlarmAdapter(alarmList, App.this);

        SharedPreferences sharedPreferences = getSharedPreferences(spFileKey, MODE_PRIVATE);
        boolean isNight = sharedPreferences.getBoolean("isNight", false);
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Removes blinks
        if (recyclerView.getItemAnimator() != null) {
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(alarmAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(alarmAdapter, getApplicationContext(), App.this));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        List<Alarm> alarms = Alarm.listAll(Alarm.class);

        alarmList.addAll(alarms);
        alarmAdapter.notifyDataSetChanged();
        animateRecyclerView();

        manageLayout(alarmList.size() == 0);

        add.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Add.class);
            startActivity(intent);
        });

        changeUI.setOnClickListener(v -> {
            SharedPreferences sharedPreferences1 = getSharedPreferences(spFileKey, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            boolean isNight1 = sharedPreferences1.getBoolean("isNight", false);
            if (isNight1) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("isNight", false);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("isNight", true);
            }
            editor.apply();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();

        alarmList.clear();

        List<Alarm> alarms = Alarm.listAll(Alarm.class);
        alarmList.addAll(alarms);

        alarmAdapter.notifyDataSetChanged();
        animateRecyclerView();

        manageLayout(alarmList.size() == 0);
    }

    //Managing app layout as per the condition...
    private void manageLayout(boolean listEmpty) {
        if (listEmpty) {
            recyclerView.setVisibility(View.GONE);
            alt_layout.setVisibility(View.VISIBLE);
        } else {
            alt_layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void animateRecyclerView() {
        recyclerView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);

                        for (int i = 0; i < recyclerView.getChildCount(); i++) {
                            View v = recyclerView.getChildAt(i);
                            v.setAlpha(0.0f);
                            v.animate().alpha(1.0f)
                                    .setDuration(600)
                                    .setStartDelay(i * 100)
                                    .start();
                        }

                        return true;
                    }
                });
    }
}