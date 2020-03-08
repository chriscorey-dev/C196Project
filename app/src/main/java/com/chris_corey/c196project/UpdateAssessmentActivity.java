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

public class UpdateAssessmentActivity extends AppCompatActivity {
    DBHelper dbHelper;
    String selectedAssessmentId;

    EditText titleText;
    Button displayDueDate;

    DatePickerDialog.OnDateSetListener dueDateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);

        Intent intent = getIntent();
        selectedAssessmentId = intent.getStringExtra("SELECTED_ASSESSMENT_ID");

        titleText = findViewById(R.id.edit_text_add_assessment_title);
        displayDueDate = findViewById(R.id.btn_add_assessment_due_date);


        getDB();
        final Assessment assessment = dbHelper.getAssessmentFromId(selectedAssessmentId);

        titleText.setText(assessment.getTitle());
        displayDueDate.setText(assessment.getDueDate().toString());


        Button submitButton = findViewById(R.id.btn_add_assessment_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: Validation
                String title = titleText.getText().toString();
                Date dueDate = Date.valueOf(displayDueDate.getText().toString());

                assessment.setTitle(title);
                assessment.setDueDate(dueDate);

                dbHelper.updateAssessment(assessment);
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

        displayDueDate= findViewById(R.id.btn_add_assessment_due_date);
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
                displayDueDate.setText(year+"-"+month+"-"+day);
            }
        };
    }

    private void getDB() {
        dbHelper = new DBHelper(UpdateAssessmentActivity.this);
        dbHelper.getWritableDatabase();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }
}
