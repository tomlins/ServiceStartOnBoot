package com.badlogic.androidgames.servicestartonboot.app;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;


public class MyService extends Service {

    private static final String TAG = "MyService";

    public MyService() {
    }

    @Override
    public void onCreate() {

        super.onCreate();
        Log.i(TAG, "OnCreate called. Creating thread...");

        Thread background = new Thread(new Runnable() {

            private final HttpClient Client = new DefaultHttpClient();
            private String URL = "http://ifreedating.com/new-profiles.rss";

            private final Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    String aResponse = msg.getData().getString("message");
                    if ((aResponse != null)) {
                        Toast.makeText( getBaseContext(), aResponse, Toast.LENGTH_SHORT).show();
                    }
                }
            };

            // After call for background.start this run method call
            public void run() {
                Looper.prepare();
                Toast.makeText( getBaseContext(), "Contacting server...", Toast.LENGTH_SHORT).show();
                try {
                    HttpGet httpget = new HttpGet(URL);
                    String serverResponse = Client.execute(httpget, new BasicResponseHandler());
                    processServerResponse(serverResponse);
                } catch (Throwable t) {
                    Log.i(TAG, "Thread  exception " + t);
                }
            }

            private void processServerResponse(String serverResponse) {
                if (!serverResponse.equals(null) && !serverResponse.equals("")) {
                    Message msgObj = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("message", serverResponse);
                    msgObj.setData(bundle);
                    handler.sendMessage(msgObj);
                }
            }
        });
        background.start();   // Start Thread
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand called");
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind called");
        return null;
    }

}
