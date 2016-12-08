package com.ssau.meetings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssau.meetings.adapters.MeetingsAdapter;
import com.ssau.meetings.database.Meet;
import com.ssau.meetings.database.Meetings;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import jp.wasabeef.recyclerview.animators.LandingAnimator;


public class MeetingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Meetings meetings;
    private List<Meet> meetList = new ArrayList<>();
    private RecyclerView meetingsRecycler;
    private MeetingsAdapter meetingsAdapter;

    public static void startMe(Context context) {
        Intent intent = new Intent(context, MeetingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_meetings);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        meetingsRecycler = (RecyclerView) findViewById(R.id.mettings_recycler_view);
        meetingsAdapter = new MeetingsAdapter(meetList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        meetingsRecycler.setAdapter(meetingsAdapter);
        meetingsRecycler.addItemDecoration(dividerItemDecoration);
        meetingsRecycler.setItemAnimator(new LandingAnimator(new AccelerateDecelerateInterpolator()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        database.getReference("meetings").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                meetingsAdapter.addItem(dataSnapshot.getKey(),dataSnapshot.getValue(Meet.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                meetingsAdapter.changeItem(dataSnapshot.getKey(),dataSnapshot.getValue(Meet.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                meetingsAdapter.removeItem(dataSnapshot.getKey());
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
