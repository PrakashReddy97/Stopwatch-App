package com.example.stopwatchapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.security.acl.NotOwnerException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {



    long startTime,updatedTime = 0L;
    Handler handler = new Handler();
    Intent intent;
    int timer;
    NotificationChannel chan;
    NotificationManager manager;
    Notification notification;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG","service is created");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());




    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.stopwatchapp";
        String channelName = "My Background Service";
        chan = new NotificationChannel( NOTIFICATION_CHANNEL_ID,  "My Background Service", NotificationManager.IMPORTANCE_NONE);

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(chan);



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .build();

        startForeground(2, notification);
        Log.d("TAG","Foreground Service started");


    }

    public int onStartCommand(Intent intent, int flags, int startId){
        startTime = SystemClock.uptimeMillis();
        Log.d("SERVICE","SERVICE STARTED");
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,1000);


        return START_STICKY;
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sendMyMessage();
            handler.postDelayed(runnable,1000);

        }
    };

    private void sendMyMessage(){

        updatedTime = SystemClock.uptimeMillis()-startTime;



        timer = (int)updatedTime/1000;


        intent = new Intent("MYTIMERDATA");

        intent.putExtra("DATA",timer);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Inside","My service is destroyed");


    }
}

