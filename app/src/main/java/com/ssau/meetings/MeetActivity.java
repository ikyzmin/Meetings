package com.ssau.meetings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ssau.meetings.adapters.UsersAdapter;
import com.ssau.meetings.database.Meet;
import com.ssau.meetings.widget.MeetParticipateButton;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class MeetActivity extends AppCompatActivity {


    private AppCompatTextView meetTitle;
    private AppCompatTextView meetDescription;
    private AppCompatTextView meetStartDate;
    private AppCompatTextView meetEndDate;
    private AppCompatImageView meetingsPhoto;
    private MeetParticipateButton participateButton;
    private UsersAdapter adapter;
    private ListView usersList;

    private final ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            final Meet meet = dataSnapshot.getValue(Meet.class);
            meetTitle.setText(meet.title);
            participateButton.setParticipated(meet.participate);
            adapter.setUsers(meet.users);
            meetDescription.setText(meet.description);
            participateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/meetings/" + meetKey + "/participate/", !meet.participate);
                    database.getReference().updateChildren(childUpdates);
                }
            });
            if (meet.photoName != null) {
                pathReference = storageRef.child(meet.photoName);
                Glide.with(MeetActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(pathReference)
                        .into(meetingsPhoto);
                participateButton.setParticipated(meet.participate);
            }
            try {
                meetStartDate.setText(Meet.CARD_FORMATTER.format(meet.getStartDate()));
                meetEndDate.setText(Meet.CARD_FORMATTER.format(meet.getEndDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            final Meet meet = dataSnapshot.getValue(Meet.class);
            participateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/meetings/" + meetKey + "/participate/", !meet.participate);
                    database.getReference().updateChildren(childUpdates);
                }
            });
            if (meet.photoName != null) {
                pathReference = storageRef.child(meet.photoName);
                Glide.with(MeetActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(pathReference)
                        .into(meetingsPhoto);
                participateButton.setParticipated(meet.participate);
            }
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
    };

    private final static String MEET_KEY_EXTRA = "meetArg";
    private String meetKey;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://samara-university-meetings.appspot.com");
    StorageReference pathReference;

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
        setContentView(R.layout.a_meet);
        meetKey = getIntent().getStringExtra(MEET_KEY_EXTRA);
        meetTitle = (AppCompatTextView) findViewById(R.id.meet_name);
        usersList = (ListView) findViewById(R.id.users_list);
        adapter = new UsersAdapter(this);
        meetingsPhoto = (AppCompatImageView) findViewById(R.id.meeting_photo);
        usersList.setAdapter(adapter);
        participateButton = (MeetParticipateButton) findViewById(R.id.participated_button);
        meetDescription = (AppCompatTextView) findViewById(R.id.meet_subtitle);
        meetStartDate = (AppCompatTextView) findViewById(R.id.meet_start_date);
        meetEndDate = (AppCompatTextView) findViewById(R.id.meet_end_date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        database.getReference().child("meetings").orderByKey().equalTo(meetKey).addChildEventListener(childEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        database.getReference().removeEventListener(childEventListener);
    }
}
