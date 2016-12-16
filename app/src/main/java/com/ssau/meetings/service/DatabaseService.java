package com.ssau.meetings.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.ssau.meetings.App;
import com.ssau.meetings.MeetActivity;
import com.ssau.meetings.MeetingsActivity;
import com.ssau.meetings.R;
import com.ssau.meetings.database.Meet;

import static com.ssau.meetings.MeetActivity.MEET_KEY_EXTRA;

/**
 * Created by Илья on 15.12.2016.
 */

public class DatabaseService extends Service {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();


    private final ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            pushNotification(R.string.added_meeting, dataSnapshot.getValue(Meet.class));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            pushNotification(R.string.changed_meeting, dataSnapshot.getValue(Meet.class));
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            pushNotification(R.string.deleted_meeting, dataSnapshot.getValue(Meet.class));
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public DatabaseService() {
        super();
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        database.getReference("meetings").addChildEventListener(childEventListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.getReference("meetings").removeEventListener(childEventListener);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void pushNotification(@StringRes int string, Meet meet) {
        if (!App.getInstance().isForeground()) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.circle)
                            .setContentTitle(getString(R.string.notification_title))
                            .setContentText(getString(string, meet.title));
            Intent notifyIntent =
                    new Intent(this, MeetActivity.class);
            notifyIntent.putExtra(MEET_KEY_EXTRA, meet.id);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MeetingsActivity.class);
            stackBuilder.addNextIntent(notifyIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
        }
    }
}
