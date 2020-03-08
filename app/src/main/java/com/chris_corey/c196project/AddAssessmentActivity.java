package com.chris_corey.c196project;

import android.app.DatePickerDialog;
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

public class AddAssessmentActivity extends AppCompatActivity {
    DBHelper dbHelper;
    String selectedCourseId;
    EditText titleText;
    Button displayDueDate;

    RadioGroup radioGroup;
    RadioButton radioButton;

    DatePickerDialog.OnDateSetListener dueDateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);

        Intent intent = getIntent();
        selectedCourseId = intent.getStringExtra("SELECTED_COURSE_ID");

        dbHelper = new DBHelper(AddAssessmentActivity.this);

        titleText = findViewById(R.id.edit_text_add_assessment_title);
        radioGroup = findViewById(R.id.radio_grp_add_assessment);


        Button submitButton = findViewById(R.id.btn_add_assessment_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Validation
                if (!validate()) {
                    Toast.makeText(AddAssessmentActivity.this, "Bad Validation", Toast.LENGTH_SHORT).show();
                    return;
                }



                String title = titleText.getText().toString();
                Date dueDate = Date.valueOf(displayDueDate.getText().toString());

                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);

                String type = (radioButton.getText() == "Objective Assessment") ? "OA" : "PA";


                dbHelper.addAssessment(title, dueDate, type, Integer.valueOf(selectedCourseId));
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
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddAssessmentActivity.this, dueDateListener, year, month, day);
                datePickerDialog.show();

            }
        });

        dueDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
//                displayStartDate.setText(month+"/"+day+"/"+year);
                displayDueDate.setText(year+"-"+month+"-"+day);
            }
        };
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
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
}
