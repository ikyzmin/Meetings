package com.ssau.meetings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssau.meetings.adapters.MeetingsAdapter;
import com.ssau.meetings.database.Meet;
import com.ssau.meetings.database.Meetings;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private FloatingActionButton addMeet;
    private final ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Meet meet = dataSnapshot.getValue(Meet.class);
            Date meetDate = null;
            try {
                meetDate = Meet.DATE_FORMATTER.parse(meet.start);
                Calendar calendar = Calendar.getInstance();
                Calendar dateCalendar = Calendar.getInstance();
                dateCalendar.setTime(meetDate);
                if (calendar.get(Calendar.DAY_OF_MONTH) == dateCalendar.get(Calendar.DAY_OF_MONTH)) {
                    meetingsAdapter.addItem(dataSnapshot.getKey(), dataSnapshot.getValue(Meet.class));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Meet meet = dataSnapshot.getValue(Meet.class);
            Date meetDate = null;
            try {
                meetDate = Meet.DATE_FORMATTER.parse(meet.start);
                Calendar calendar = Calendar.getInstance();
                Calendar dateCalendar = Calendar.getInstance();
                dateCalendar.setTime(meetDate);
                if (calendar.get(Calendar.DAY_OF_MONTH) == dateCalendar.get(Calendar.DAY_OF_MONTH)) {
                    meetingsAdapter.changeItem(dataSnapshot.getKey(), dataSnapshot.getValue(Meet.class));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Meet meet = dataSnapshot.getValue(Meet.class);
            Date meetDate = null;
            try {
                meetDate = Meet.DATE_FORMATTER.parse(meet.start);
                Calendar calendar = Calendar.getInstance();
                Calendar dateCalendar = Calendar.getInstance();
                dateCalendar.setTime(meetDate);
                if (calendar.get(Calendar.DAY_OF_MONTH) == dateCalendar.get(Calendar.DAY_OF_MONTH)) {
                    meetingsAdapter.removeItem(dataSnapshot.getKey());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

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
        meetingsAdapter = new MeetingsAdapter(meetList, new MeetingsAdapter.OnDeleteButtonClickListener() {
            @Override
            public void onDeleteClicked(String itemId) {
                database.getReference("meetings").child(itemId).removeValue();
            }

            @Override
            public void onClick(View v) {

            }
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        meetingsRecycler.setAdapter(meetingsAdapter);
        meetingsRecycler.addItemDecoration(dividerItemDecoration);
        meetingsRecycler.setItemAnimator(new LandingAnimator(new AccelerateDecelerateInterpolator()));
        addMeet = (FloatingActionButton) findViewById(R.id.add_meet);
        addMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMeetActivity.startMe(MeetingsActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_meeting:
                search();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void search() {
        MeetingSearchActivity.start(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        database.getReference("meetings").addChildEventListener(childEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        database.getReference().removeEventListener(childEventListener);
    }


}
