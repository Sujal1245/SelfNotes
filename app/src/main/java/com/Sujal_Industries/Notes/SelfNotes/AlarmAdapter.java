package com.Sujal_Industries.Notes.SelfNotes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.ALARM_SERVICE;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.MyViewHolder> {

    private List<Alarm> alarmList;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;
    private Context context;


    AlarmAdapter(List<Alarm> alarmList, Context context) {
        this.alarmList = alarmList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Alarm alarm = alarmList.get(position);

        boolean expanded = alarm.isExpanded();
        holder.description.setVisibility(expanded ? View.VISIBLE : View.GONE);

        holder.title.setText(alarm.getTitle());
        holder.time.setText(alarm.getTime());
        holder.description.setText(alarm.getDescription());

        holder.itemView.setOnClickListener(v -> {
            // Get the current state of the item
            boolean expanded1 = alarm.isExpanded();
            // Change the state
            alarm.setExpanded(!expanded1);
            // Notify the adapter that item has changed
            notifyItemChanged(position);
        });

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, time, description;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            description = itemView.findViewById(R.id.description);
        }

        void clearAnimation() {
            itemView.clearAnimation();
        }
    }

    void deleteAlarm(final Context context, final AppCompatActivity activity, final int position) {
        //Storing Data...
        final Alarm alarm = alarmList.get(position);
        final int uid = alarm.getUnino();
        final String title = alarm.getTitle();
        final String description = alarm.getDescription();
        final int kMinutes = alarm.getMinutes();
        final int kHour = alarm.getHour();
        //Removing Alarm from RecyclerView...
        alarmList.remove(position);
        notifyItemRemoved(position);
        //Cancelling the Alarm...
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("Title", title);
        intent.putExtra("Description", description);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, uid, intent, 0);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        //Updating Database...
        alarm.delete();
        //Showing Snackbar...
        CoordinatorLayout coordinatorLayout = activity.findViewById(R.id.app_coordinator);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "One Alarm Cancelled", Snackbar.LENGTH_SHORT);
        snackbar.setAction("Undo", v -> {
            //Restoring Item...
            alarmList.add(position, alarm);
            notifyItemInserted(position);
            //Restoring Alarm...
            AlarmManager alarmManager1 = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            Intent intent1 = new Intent(context, AlarmReceiver.class);
            intent1.putExtra("Title", title);
            intent1.putExtra("Description", description);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, uid, intent1, 0);
            //Specifying Time...
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, kHour);
            calendar.set(Calendar.MINUTE, kMinutes);
            //Setting Alarm...
            if (alarmManager1 != null) {
                alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
            }
            //Updating Database...
            alarm.save();
        });
        snackbar.show();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolder holder) {
        holder.clearAnimation();
    }
}
