package com.ssau.meetings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssau.meetings.database.Meet;

public class MeetActivity extends AppCompatActivity {


    private final static String MEET_KEY_EXTRA = "meetArg";
    private String meetKey;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();


    public static void startMe(Context context, String meetKey) {
        Intent intent = new Intent(context, MeetActivity.class);
        intent.putExtra(MEET_KEY_EXTRA, meetKey);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        meetKey = intent.getStringExtra(MEET_KEY_EXTRA);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meetKey = getIntent().getStringExtra(MEET_KEY_EXTRA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        database.getReference().child("meetings").orderByKey().equalTo(meetKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("test", dataSnapshot.getValue(Meet.class).title);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
