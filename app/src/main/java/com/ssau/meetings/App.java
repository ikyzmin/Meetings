package com.ssau.meetings;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.ssau.meetings.service.DatabaseService;

public class App extends Application {

    private static volatile App instance;

    public static App getInstance() {
        App localInstance = instance;
        if (localInstance == null) {
            synchronized (App.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new App();
                }
            }
        }
        return localInstance;
    }

    private int runningActivities = 0;

    public App(){
        super();
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(instance, DatabaseService.class));
        instance.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityStarted(Activity activity) {
                runningActivities++;
            }

            @Override
            public void onActivityStopped(Activity activity) {
                runningActivities--;
            }


            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }
        });
    }

    public boolean isForeground() {
        return runningActivities > 0;
    }
}
