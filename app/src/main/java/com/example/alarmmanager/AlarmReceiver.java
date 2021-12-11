package com.example.alarmmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        MainActivity.addRepeat(intent.getLongExtra("timeInMillis", 0), intent.getStringExtra("message"));

        Intent mainActivity = new Intent(context, MainActivity.class);
        mainActivity.putExtra("Alarm", "Alarma Recibida");
        mainActivity.putExtra("message", intent.getStringExtra("message"));
        // Set flag to create new activity if its already running
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mainActivity);
    }
}