package com.Sujal_Industries.Notes.SelfNotes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

public class BootReceiver extends BroadcastReceiver {
    List<Alarm> alarmList;
    AlarmManager alarmManager;
    Intent i;
    PendingIntent alarmIntent;
    String title, description;
    int hour, minutes, uid;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                //Creating Objects...
                alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                i = new Intent(context.getApplicationContext(), AlarmReceiver.class);
                //Reading List...
                alarmList = Alarm.listAll(Alarm.class);
                for (Alarm alarm : alarmList) {
                    //Retrieving Data From Object...
                    title = alarm.getTitle();
                    description = alarm.getDescription();
                    hour = alarm.getHour();
                    minutes = alarm.getMinutes();
                    uid = alarm.getUnino();
                    //Setting data to intent...
                    i.putExtra("Title", title);
                    i.putExtra("Description", description);
                    alarmIntent = PendingIntent.getBroadcast(context.getApplicationContext(), uid, i, 0);
                    //Specifying Time...
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minutes);
                    //Setting Alarm...
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
                }
            }
        }
    }
}
