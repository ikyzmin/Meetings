package com.ssau.meetings;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartupActivity extends AppCompatActivity {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            MeetingsActivity.startMe(this);
            finish();
        } else {
            LoginActivity.startMe(this);
            finish();
        }
    }
}
