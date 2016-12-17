package com.ssau.meetings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssau.meetings.adapters.MeetingsAdapter;
import com.ssau.meetings.database.Meet;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Илья on 17.12.2016.
 */

public class MeetingSearchActivity extends AppCompatActivity {

    AppCompatEditText searchEditText;
    AppCompatButton searchButton;
    RecyclerView recyclerView;
    MeetingsAdapter meetingsAdapter;
    ArrayList<Meet> meetList;

    public static void start(Context context) {
        Intent intent = new Intent(context, MeetingSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_search);
        searchEditText = (AppCompatEditText) findViewById(R.id.search_edit_text);
        searchButton = (AppCompatButton) findViewById(R.id.search_button);
        recyclerView = (RecyclerView) findViewById(R.id.finded_list);
        meetList = new ArrayList<>();
        meetingsAdapter = new MeetingsAdapter(meetList,null);
        recyclerView.setAdapter(meetingsAdapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = mFirebaseDatabaseReference.child("meetings").orderByChild("description").equalTo(searchEditText.getText().toString());
                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        meetingsAdapter.addItem(dataSnapshot.getKey(), dataSnapshot.getValue(Meet.class));
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
        });
    }
}
