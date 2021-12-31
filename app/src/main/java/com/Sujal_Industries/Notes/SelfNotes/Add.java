package com.Sujal_Industries.Notes.SelfNotes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.List;

public class Add extends AppCompatActivity {

    EditText uTitle, uDescription;
    TextView uTime;
    Button setTime, done;
    LinearLayout linearLayout;
    AlarmManager alarmManager;
    PendingIntent alarmIntent;
    int hour, minute;
    int kHour, kMinutes;
    boolean timeSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //Getting View...
        uTitle = findViewById(R.id.uTitle);
        uDescription = findViewById(R.id.uDescription);
        uTime = findViewById(R.id.uTime);

        setTime = findViewById(R.id.setTime);
        done = findViewById(R.id.button_done);

        linearLayout = findViewById(R.id.linear_layout_add);

        //Setting View Listeners...
        setTime.setOnClickListener(v -> showTimePicker());

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Alarm> alarmList = Alarm.listAll(Alarm.class);
                String title = uTitle.getText().toString();
                String description = uDescription.getText().toString();
                String time = uTime.getText().toString();
                if (!title.equals("")) {
                    if (!description.equals("")) {
                        if (timeSelected) {
                            //Saving Alarm Data...
                            Alarm alarm = new Alarm(title, description, time, kHour, kMinutes, alarmList.size() + 1);
                            alarm.save();
                            //Saving Data in FireStore...
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                String email = user.getEmail();
                                if (email != null) {
                                    DummyAlarm newAlarm = new DummyAlarm(alarm.getTitle(), alarm.getDescription(), alarm.getTime(),
                                            alarm.getHour(), alarm.getMinutes(), alarm.getUnino());
                                    db.collection("users")
                                            .document(email)
                                            .collection("Alarms")
                                            .add(newAlarm);
                                }
                            }
                            //Creating Objects...
                            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                            intent.putExtra("Title", title);
                            intent.putExtra("Description", description);
                            alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmList.size() + 1, intent, 0);
                            //Specifying Time...
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.set(Calendar.HOUR_OF_DAY, kHour);
                            calendar.set(Calendar.MINUTE, kMinutes);
                            //Setting Alarm...
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
                            //Showing Success!
                            Snackbar snackbar = Snackbar.make(linearLayout, "Success!", Snackbar.LENGTH_SHORT);
                            snackbar.addCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    super.onDismissed(transientBottomBar, event);
                                    finish();
                                }
                            });
                            snackbar.show();
                        } else {
                            Snackbar.make(linearLayout, "Please give a time!", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(linearLayout, "Please give a description!", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(linearLayout, "Please give a title!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showTimePicker() {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minutes) -> {
                    timeSelected = true;

                    String hod, moh;

                    if (Integer.toString(hourOfDay).length() == 1) {
                        hod = ("0" + hourOfDay);
                    } else {
                        hod = Integer.toString(hourOfDay);
                    }

                    if (Integer.toString(minutes).length() == 1) {
                        moh = ("0" + minutes);
                    } else {
                        moh = Integer.toString(minutes);
                    }

                    String t;
                    if (hourOfDay == 0 && minutes == 0) {
                        t = hod + ":" + moh + " Midnight";
                    } else if (hourOfDay == 12 && minutes == 0) {
                        t = hod + ":" + moh + " Noon";
                    } else if (hourOfDay < 12) {
                        t = hod + ":" + moh + " AM";
                    } else {
                        t = "0"+ (hourOfDay - 12) + ":" + moh + " PM";
                    }

                    kHour = hourOfDay;
                    kMinutes = minutes;
                    uTime.setText(t);
                }, hour, minute, false);
        timePickerDialog.show();
    }
}