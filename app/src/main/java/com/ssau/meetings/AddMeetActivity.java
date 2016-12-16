package com.ssau.meetings;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ssau.meetings.database.Meet;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class AddMeetActivity extends AppCompatActivity {

    private AppCompatTextView nameTextView;
    private AppCompatTextView descriptionTextView;
    private AppCompatTextView startTimeTextView;
    private AppCompatTextView endTimeTextView;
    private AppCompatButton pickImageButton;
    private final static int PICKFILE_REQUEST_CODE = 124;
    private Date startDate;
    private Uri path;
    private Date endDate;
    private SwitchCompat participatingSwitch;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://samara-university-meetings.appspot.com");
    StorageReference pathReference;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private AppCompatButton addMettingButton;
    private View.OnClickListener nameDescriptionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showTextInputDialog(v.getId());
        }
    };
    private Calendar dateAndTime;
    private final DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateAndTime.set(year, month, dayOfMonth);
            startTimeTextView.setText(Meet.CARD_FORMATTER.format(dateAndTime.getTime()));
            startDate = dateAndTime.getTime();
        }
    };
    private final DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateAndTime.set(year, month, dayOfMonth);
            endTimeTextView.setText(Meet.CARD_FORMATTER.format(dateAndTime.getTime()));
            endDate = dateAndTime.getTime();
        }
    };


    public static void startMe(Context context) {
        Intent intent = new Intent(context, AddMeetActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_add_meet);
        pickImageButton = (AppCompatButton)findViewById(R.id.pick_image);
        nameTextView = (AppCompatTextView) findViewById(R.id.meet_name);
        descriptionTextView = (AppCompatTextView) findViewById(R.id.meet_description);
        startTimeTextView = (AppCompatTextView) findViewById(R.id.meet_start_date);
        endTimeTextView = (AppCompatTextView) findViewById(R.id.meet_end_date);
        dateAndTime = Calendar.getInstance();
        participatingSwitch = (SwitchCompat) findViewById(R.id.participating_switch);
        addMettingButton = (AppCompatButton) findViewById(R.id.add_meeting_button);
        startTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddMeetActivity.this, startDateSetListener,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
        endTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddMeetActivity.this, endDateSetListener,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
        addMettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Meet meet = new Meet();
                meet.id = UUID.randomUUID().toString();
                meet.start = Meet.DATE_FORMATTER.format(startDate);
                if (path!=null) {
                    meet.photoName = path.getLastPathSegment();
                }
                meet.end = Meet.DATE_FORMATTER.format(endDate);
                meet.title = nameTextView.getText().toString();
                meet.participate = participatingSwitch.isChecked();
                meet.description = descriptionTextView.getText().toString();
                database.getReference("meetings").child(meet.id).setValue(meet);

                if (path!=null){
                    StorageReference photoRef = storageRef.child(path.getLastPathSegment());
                    UploadTask uploadTask = photoRef.putFile(path);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        }
                    });

                }
                finish();
            }
        });
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            }
        });
        nameTextView.setOnClickListener(nameDescriptionListener);
        descriptionTextView.setOnClickListener(nameDescriptionListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void showTextInputDialog(@IdRes final int viewId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(viewId == R.id.meet_name ? R.string.add_name_dialog_title : R.string.add_description_dialog_title);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (viewId == R.id.meet_name) {
                    nameTextView.setText(input.getText().toString());
                } else {
                    descriptionTextView.setText(input.getText().toString());
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            path = data.getData();
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


}
