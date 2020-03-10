package com.chris_corey.c196project;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.sql.Date;
import java.util.Calendar;

public class UpdateAssessmentActivity extends AppCompatActivity {
    DBHelper dbHelper;
    String selectedAssessmentId;

    EditText titleText;
    Button displayDueDate;

    RadioGroup radioGroup;
    RadioButton radioButton, radioButtonOA, radioButtonPA;

    DatePickerDialog.OnDateSetListener dueDateListener;

    Assessment assessment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);

        Intent intent = getIntent();
        selectedAssessmentId = intent.getStringExtra("SELECTED_ASSESSMENT_ID");

        titleText = findViewById(R.id.edit_text_add_assessment_title);
        displayDueDate = findViewById(R.id.btn_add_assessment_due_date);
        radioGroup = findViewById(R.id.radio_grp_add_assessment);
        radioButtonOA = findViewById(R.id.radio_btn_add_assessment_OA);
        radioButtonPA = findViewById(R.id.radio_btn_add_assessment_PA);

        getDB();
        assessment = dbHelper.getAssessmentFromId(selectedAssessmentId);

        titleText.setText(assessment.getTitle());
        displayDueDate.setText(assessment.getDueDate().toString());

        if (assessment.getType().equals("OA")) {
            radioButtonOA.setChecked(true);
        } else {
            radioButtonPA.setChecked(true);
        }


        Button submitButton = findViewById(R.id.btn_add_assessment_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Validation
                if (!validate()) {
                    Toast.makeText(UpdateAssessmentActivity.this, "Bad Validation", Toast.LENGTH_SHORT).show();
                    return;
                }



                String title = titleText.getText().toString();
                Date dueDate = Date.valueOf(displayDueDate.getText().toString());

                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);

                String type = (radioButton.getText().toString().equals("Objective Assessment")) ? "OA" : "PA";

                assessment.setTitle(title);
                assessment.setDueDate(dueDate);
                assessment.setType(type);

                dbHelper.updateAssessment(assessment);


                cancelAlarm();
                handleAlarms(assessment);
                onBackPressed();
            }
        });

        Button cancelButton = findViewById(R.id.btn_add_assessment_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        displayDueDate = findViewById(R.id.btn_add_assessment_due_date);
        displayDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(assessment.getDueDate());
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateAssessmentActivity.this, dueDateListener, year, month, day);

                datePickerDialog.show();

            }
        });

        dueDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                displayDueDate.setText(year + "-" + month + "-" + day);
            }
        };
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }

    private void getDB() {
        dbHelper = new DBHelper(UpdateAssessmentActivity.this);
        dbHelper.getWritableDatabase();
    }

    private boolean validate() {
        boolean isValid = true;

        if (titleText.getText().length() == 0) {
            isValid = false;
        }
        if (displayDueDate.getText().toString().equals("Due Date")) {
            isValid = false;
        }

        return isValid;
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }

    private void handleAlarms(Assessment assessment) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(assessment.getDueDate());
        dueDateAlarm(startCal);
    }

    private void dueDateAlarm(Calendar cal) {
        dbHelper.addNotification(assessment.getDueDate(), "assessment_due", assessment.getId());
        Notification notification = dbHelper.getLatestNotification();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("NOTIFICATION_TYPE", notification.getType());
        intent.putExtra("CHANNEL_ID", String.valueOf(notification.getRequestCode()));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notification.getRequestCode(), intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        for (Notification notification : dbHelper.getAssessmentNotificationsFromParentId(String.valueOf(assessment.getId()))) {
            dbHelper.deleteNotification(String.valueOf(notification.getRequestCode()));

            Intent startIntent = new Intent(this, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notification.getRequestCode(), startIntent, 0);
            alarmManager.cancel(pendingIntent);
        }
    }
}
