package com.chris_corey.c196project;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Date;
import java.util.Calendar;

public class AddCourseActivity extends AppCompatActivity {
    DBHelper dbHelper;
    String selectedTermId;
    EditText titleText, mentorNameText, mentorPhoneText, mentorEmailText;
    Button displayStartDate, displayEndDate;
    Spinner statusSpinner;

    Course course;

    DatePickerDialog.OnDateSetListener startDateListener;
    DatePickerDialog.OnDateSetListener endDateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        Intent intent = getIntent();
        selectedTermId = intent.getStringExtra("SELECTED_TERM_ID");

        dbHelper = new DBHelper(AddCourseActivity.this);

        titleText = findViewById(R.id.edit_text_add_course_title);
        mentorNameText = findViewById(R.id.edit_text_add_course_mentor_name);
        mentorPhoneText = findViewById(R.id.edit_text_add_course_mentor_phone);
        mentorEmailText = findViewById(R.id.edit_text_add_course_mentor_email);
        statusSpinner = findViewById(R.id.spinner_add_course_status);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.course_status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        Button submitButton = findViewById(R.id.btn_add_course_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Validation
                if (!validate()) {
                    Toast.makeText(AddCourseActivity.this, "Bad Validation", Toast.LENGTH_SHORT).show();
                    return;
                }


                String title = titleText.getText().toString();
                Date startDate = Date.valueOf(displayStartDate.getText().toString());
                Date endDate = Date.valueOf(displayEndDate.getText().toString());
                String status = statusSpinner.getSelectedItem().toString();
                String mentorName = mentorNameText.getText().toString();
                String mentorPhone = mentorPhoneText.getText().toString();
                String mentorEmail = mentorEmailText.getText().toString();

                dbHelper.addCourse(title, startDate, endDate, "Notes", status, mentorName, mentorPhone, mentorEmail, Integer.valueOf(selectedTermId));

                course = dbHelper.getLatestCourse();

                // Alarms
                handleAlarms(course);
//                cancelAlarm();

                onBackPressed();
            }
        });

        Button cancelButton = findViewById(R.id.btn_add_course_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        displayStartDate = findViewById(R.id.btn_add_course_start_date);
        displayStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddCourseActivity.this, startDateListener, year, month, day);

                datePickerDialog.show();

            }
        });

        displayEndDate = findViewById(R.id.btn_add_course_end_date);
        displayEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddCourseActivity.this, endDateListener, year, month, day);
                datePickerDialog.show();
            }
        });

        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
//                displayStartDate.setText(month+"/"+day+"/"+year);
                displayStartDate.setText(year + "-" + month + "-" + day);
            }
        };

        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
//                displayEndDate.setText(month+"/"+day+"/"+year);
                displayEndDate.setText(year + "-" + month + "-" + day);
            }
        };
    }

    private boolean validate() {
        if (titleText.getText().length() == 0) {
            return false;
        }
        if (displayStartDate.getText().toString().equals("Start Date") || displayEndDate.getText().toString().equals("End Date")) {
            return false;
        } else if (Date.valueOf(displayStartDate.getText().toString()).before(Calendar.getInstance().getTime()) || Date.valueOf(displayEndDate.getText().toString()).before(Calendar.getInstance().getTime())) {
            return false;
        } else if (Date.valueOf(displayEndDate.getText().toString()).before(Date.valueOf(displayStartDate.getText().toString()))) {
            return false;
        }

        if (mentorNameText.getText().length() == 0) {
            return false;
        }
        if (mentorPhoneText.getText().length() == 0) {
            return false;
        }
        if (mentorEmailText.getText().length() == 0) {
            return false;
        }

        return true;
    }

    private void handleAlarms(Course course) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(course.getStartDate());
//        cal.setTimeInMillis(cal.getTimeInMillis() + 5000);
        startDateAlarm(startCal);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(course.getEndDate());
        endDateAlarm(endCal);

//        cal.set(Calendar.HOUR_OF_DAY, 8);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
    }

    private void startDateAlarm(Calendar cal) {
    //        dbHelper.addNotification(course.getStartDate(), "course_start", course.getId());
        Notification notification = dbHelper.getLatestNotification();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlertReceiver.class);
//        intent.putExtra("START_OR_END", "start");
//        intent.putExtra("NOTIFICATION_CHANNEL", "course");
        intent.putExtra("NOTIFICATION_TYPE", notification.getType());
        intent.putExtra("CHANNEL_ID", String.valueOf(notification.getRequestCode()));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notification.getRequestCode(), intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    private void endDateAlarm(Calendar cal) {
        dbHelper.addNotification(course.getEndDate(), "course_end", course.getId());
        Notification notification = dbHelper.getLatestNotification();


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlertReceiver.class);
//        intent.putExtra("START_OR_END", "end");
//        intent.putExtra("NOTIFICATION_CHANNEL", "course");
        intent.putExtra("NOTIFICATION_TYPE", notification.getType());
        intent.putExtra("CHANNEL_ID", String.valueOf(notification.getRequestCode()));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notification.getRequestCode(), intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        for (Notification notification : dbHelper.getCourseNotificationsFromParentId(String.valueOf(course.getId()))){

            Intent startIntent = new Intent(this, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notification.getRequestCode(), startIntent, 0);
            alarmManager.cancel(pendingIntent);
        }
    }
}
