package com.chris_corey.c196project;

import android.app.DatePickerDialog;
import android.content.Intent;
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

public class UpdateCourseActivity extends AppCompatActivity {
    DBHelper dbHelper;
    String selectedCourseId;

    EditText titleText, mentorNameText, mentorPhoneText, mentorEmailText;
    Button displayStartDate, displayEndDate;
    Spinner statusSpinner;

    DatePickerDialog.OnDateSetListener startDateListener;
    DatePickerDialog.OnDateSetListener endDateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        Intent intent = getIntent();
        selectedCourseId = intent.getStringExtra("SELECTED_COURSE_ID");

        titleText = findViewById(R.id.edit_text_add_course_title);
        statusSpinner = findViewById(R.id.spinner_add_course_status);
        mentorNameText = findViewById(R.id.edit_text_add_course_mentor_name);
        mentorPhoneText = findViewById(R.id.edit_text_add_course_mentor_phone);
        mentorEmailText = findViewById(R.id.edit_text_add_course_mentor_email);
        displayStartDate = findViewById(R.id.btn_add_course_start_date);
        displayEndDate = findViewById(R.id.btn_add_course_end_date);


        getDB();
        final Course course = dbHelper.getCourseFromId(selectedCourseId);


        titleText.setText(course.getTitle());
        displayStartDate.setText(course.getStartDate().toString());
        displayEndDate.setText(course.getEndDate().toString());
        mentorNameText.setText(course.getMentorName());
        mentorPhoneText.setText(course.getMentorPhone());
        mentorEmailText.setText(course.getMentorEmail());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.course_status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        statusSpinner.setSelection(adapter.getPosition(course.getStatus()));


        Button submitButton = findViewById(R.id.btn_add_course_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Validation
                if (!validate()) {
                    Toast.makeText(UpdateCourseActivity.this, "Bad Validation", Toast.LENGTH_SHORT).show();
                    return;
                }



                String title = titleText.getText().toString();
                Date startDate = Date.valueOf(displayStartDate.getText().toString());
                Date endDate = Date.valueOf(displayEndDate.getText().toString());
                String status = statusSpinner.getSelectedItem().toString();
                String mentorName = mentorNameText.getText().toString();
                String mentorPhone = mentorPhoneText.getText().toString();
                String mentorEmail = mentorEmailText.getText().toString();

                course.setTitle(title);
                course.setStartDate(startDate);
                course.setEndDate(endDate);
                course.setStatus(status);
                course.setMentorName(mentorName);
                course.setMentorPhone(mentorPhone);
                course.setMentorEmail(mentorEmail);

                dbHelper.updateCourse(course);
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
                cal.setTime(course.getStartDate());
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateCourseActivity.this, startDateListener, year, month, day);

                datePickerDialog.show();

            }
        });

        displayEndDate = findViewById(R.id.btn_add_course_end_date);
        displayEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(course.getEndDate());
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateCourseActivity.this, endDateListener, year, month, day);
                datePickerDialog.show();
            }
        });

        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                displayStartDate.setText(year+"-"+month+"-"+day);
            }
        };

        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                displayEndDate.setText(year+"-"+month+"-"+day);
            }
        };
    }

    private boolean validate() {
        boolean isValid = true;

        if (titleText.getText().length() == 0) {
            isValid = false;
        }
        if (displayStartDate.getText().toString().equals("Start Date")) {
            isValid = false;
        }
        if (displayEndDate.getText().toString().equals("End Date")) {
            isValid = false;
        }
        if (mentorNameText.getText().length() == 0) {
            isValid = false;
        }
        if (mentorPhoneText.getText().length() == 0) {
            isValid = false;
        }
        if (mentorEmailText.getText().length() == 0) {
            isValid = false;
        }

        return isValid;
    }

    private void getDB() {
        dbHelper = new DBHelper(UpdateCourseActivity.this);
        dbHelper.getWritableDatabase();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }
}
