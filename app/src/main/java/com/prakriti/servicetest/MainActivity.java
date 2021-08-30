package com.prakriti.servicetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtThreadCount;
    private Button btnStartService, btnStopService, btnBindService, btnUnbindService, btnGetRandomNum;
    private Intent serviceIntent;

    private MyService myService;
    private int count = 0;
    private boolean isServiceBound;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(getString(R.string.TAG), "MainActivity, Thread ID: " + Thread.currentThread().getId());

        txtThreadCount = findViewById(R.id.txtThreadCount);

        btnStartService = findViewById(R.id.btnStartService);
        btnStartService.setOnClickListener(this);
        btnStopService = findViewById(R.id.btnStopService);
        btnStopService.setOnClickListener(this);
        btnBindService = findViewById(R.id.btnBindService);
        btnBindService.setOnClickListener(this);
        btnUnbindService = findViewById(R.id.btnUnbindService);
        btnUnbindService.setOnClickListener(this);
        btnGetRandomNum = findViewById(R.id.btnGetRandomNum);
        btnGetRandomNum.setOnClickListener(this);

        serviceIntent = new Intent(getApplicationContext(), MyService.class); // services to be declared in Manifest
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartService: // services are started by explicit intents
                startService(serviceIntent);
                break;
            case R.id.btnStopService: // service must manage its own lifecycle, sys destroys it only for specific cases
                // stopSelf() or stopService() must be called
                stopService(serviceIntent); // not usually explicitly called
                break;
            case R.id.btnBindService:
                bindService();
                break;
            case R.id.btnUnbindService:
                unbindService();
                break;
            case R.id.btnGetRandomNum:
                getRandomNumber();
                break;
        }
    }

    private void bindService() {
        if(serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    isServiceBound = true;
                    // use IBinder service to initialise myService var
                    MyService.MyServiceBinder myServiceBinder = (MyService.MyServiceBinder) service;
                    myService = myServiceBinder.getService();
                }
                @Override
                public void onServiceDisconnected(ComponentName name) {
                    isServiceBound = false;
                }
            };
        }
    }

    private void unbindService() {

    }

    private void getRandomNumber() {

    }
}