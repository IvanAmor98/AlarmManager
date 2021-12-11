package com.example.alarmmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DatePicker datePicker;
    TimePicker timePicker;

    static AlarmManager alarmManager;
    static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;
        alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();

        datePicker = findViewById(R.id.datePicker);
        datePicker.findViewById(getResources().getIdentifier("year","id","android")).setVisibility(View.GONE);
        datePicker.init(datePicker.getYear(), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);

        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        if (getIntent().getStringExtra("Alarm") != null) {
            Toast.makeText(MainActivity.this, "Alarma recibida", Toast.LENGTH_SHORT).show();
            ((TextView)findViewById(R.id.output)).setText("Es el cumpleaños de: " + getIntent().getStringExtra("message"));
        }
    }

    public void setAlarm(View v) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        calendar.set(Calendar.MONTH, datePicker.getMonth());
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM HH:mm ");

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("timeInMillis", calendar.getTimeInMillis());
        intent.putExtra("message", ((TextView)findViewById(R.id.input)).getText().toString());
        final int id = (int) System.currentTimeMillis();
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, id, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        Toast.makeText(MainActivity.this, "Alarma creada el " + sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
    }

    public static void addRepeat(long timeInMillis, String message) {
        Calendar repeat = Calendar.getInstance();
        repeat.setTimeInMillis(timeInMillis);
        repeat.add(Calendar.YEAR, 1);

        Intent intent = new Intent(instance, AlarmReceiver.class);
        intent.putExtra("timeInMillis", repeat.getTimeInMillis());
        intent.putExtra("message", message);
        final int id = (int) System.currentTimeMillis();
        PendingIntent alarmIntent = PendingIntent.getBroadcast(instance, id, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, repeat.getTimeInMillis(), alarmIntent);
    }
}