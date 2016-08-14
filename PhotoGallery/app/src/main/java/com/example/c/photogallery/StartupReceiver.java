package com.example.c.photogallery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartupReceiver extends BroadcastReceiver {
    public StartupReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("StartupReceiver", "StartupReceiver");
        PollService.setServiceAlarm(context, true);
    }
}
