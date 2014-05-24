package com.badlogic.androidgames.servicestartonboot.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnBootCompletedReceiver extends BroadcastReceiver {

    private static final String TAG = "OnBootCompletedReceiver";

    public OnBootCompletedReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Log.i(TAG, "onCreate called");
            Intent serviceIntent = new Intent(context, MyService.class);
            context.startService(serviceIntent);
        }
    }
}
