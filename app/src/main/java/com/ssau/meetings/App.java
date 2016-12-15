package com.ssau.meetings;

import android.app.Application;
import android.content.Intent;

import com.ssau.meetings.service.DatabaseService;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this,DatabaseService.class));
    }
}
