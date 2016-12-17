package com.ssau.meetings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssau.meetings.database.Meet;

/**
 * Created by Илья on 17.12.2016.
 */

public class MeetingSearchActivity extends AppCompatActivity {

    AppCompatEditText searchEditText;
    AppCompatButton searchButton;

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
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = mFirebaseDatabaseReference.child("meetings").orderByChild("description").startAt(searchEditText.getText().toString()).limitToFirst(1);
                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Meet meet = dataSnapshot.getValue(Meet.class);
                        if (meet != null) {
                            MeetActivity.startMe(MeetingSearchActivity.this, dataSnapshot.getKey());
                        }

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
