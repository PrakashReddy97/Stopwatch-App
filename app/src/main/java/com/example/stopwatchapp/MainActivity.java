package com.example.stopwatchapp;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Time;

public class MainActivity extends AppCompatActivity {

    private TextView timertxt;
    private Button startTimerBtn;

    int Seconds, Minutes;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        timertxt = findViewById(R.id.timerset);
        startTimerBtn = findViewById(R.id.startBtn);

        Log.d("TAG","Oncreate of activity");



        LocalBroadcastManager.getInstance(this).registerReceiver(myBroadCastReceiver,new IntentFilter("MYTIMERDATA"));


        final boolean serviceRunningStatus = isServiceRunning(MyService.class);

        System.out.println(serviceRunningStatus);

        startTimerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!serviceRunningStatus){
                    intent = new Intent(v.getContext(),MyService.class);
                    v.getContext().startService(intent);

                }
            }
        });




    }


    private BroadcastReceiver myBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int time = intent.getIntExtra("DATA",0);
           // Log.d("TAG","current Time is: "+time);


            Minutes = time / 60;

            Seconds = time % 60;

            timertxt.setText("" + Minutes + ":" + String.format("%02d", Seconds));


        }
    };

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TAG","Inside on stop of activity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TAG","Inside on destroy of activity");



    }
}
