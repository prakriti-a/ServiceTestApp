package com.prakriti.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Random;

public class MyService extends Service {

    private int randomNumber;
    private final int MAX = 100;
    private final int MIN = 0;
    private boolean isRandomGeneratorOn;

    public class MyServiceBinder extends Binder { // or implement IBinder

        public MyService getService() {
            return MyService.this;
        }
    }

    private IBinder iBinder = new MyServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { // -> for bound service
        Log.i(getString(R.string.TAG), "Inside onBind");
        return iBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) { // returns a flag that determines service's behaviour
        // executed whenever you start a service
        Log.i(getString(R.string.TAG), "onStartCommand, Thread ID: " + Thread.currentThread().getId());
        isRandomGeneratorOn =  true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomNumberGenerator(); // should not run on Main thread, causes ANR error
            }
        }).start();
//        stopSelf(); // service calls this to stop itself (onDestroy is called)
        return START_STICKY;
    }

    private void startRandomNumberGenerator() {
        while (isRandomGeneratorOn) {
            try {
                Thread.sleep(2000);
                if(isRandomGeneratorOn) {
                    randomNumber = new Random().nextInt(MAX) + MIN;
                    Log.i(getString(R.string.TAG), "Thread ID: " + Thread.currentThread().getId() + ", Random no: " + randomNumber);
                }
            } catch (InterruptedException e) {
                Log.e(getString(R.string.TAG), "Thread interrupted");
            }
        }
    }

    private void stopRandomNumberGenerator() {
        isRandomGeneratorOn = false;
    }

    public int getRandomNumber() {
        return randomNumber;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomNumberGenerator();
        Log.i(getString(R.string.TAG), "Service Destroyed");
    }
}
