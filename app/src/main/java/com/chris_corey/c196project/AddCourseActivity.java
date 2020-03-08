package com.chris_corey.c196project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.sql.Date;
import java.util.Calendar;

public class AddCourseActivity extends AppCompatActivity {
    DBHelper dbHelper;
    String selectedTermId;
    EditText titleText, mentorNameText, mentorPhoneText , mentorEmailText ;
    Button displayStartDate, displayEndDate;

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
        mentorPhoneText  = findViewById(R.id.edit_text_add_course_mentor_phone);
        mentorEmailText  = findViewById(R.id.edit_text_add_course_mentor_email);

        Button submitButton = findViewById(R.id.btn_add_course_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: Validation
                String title = titleText.getText().toString();
                Date startDate = Date.valueOf(displayStartDate.getText().toString());
                Date endDate = Date.valueOf(displayEndDate.getText().toString());
                String mentorName = mentorNameText.getText().toString();
                String mentorPhone = mentorPhoneText.getText().toString();
                String mentorEmail = mentorEmailText.getText().toString();

                dbHelper.addCourse(title, startDate, endDate, "Notes", "Plan To Take", mentorName, mentorPhone, mentorEmail, Integer.valueOf(selectedTermId));
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
                displayStartDate.setText(year+"-"+month+"-"+day);
            }
        };

        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
//                displayEndDate.setText(month+"/"+day+"/"+year);
                displayEndDate.setText(year+"-"+month+"-"+day);
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }
}
