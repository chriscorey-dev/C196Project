package com.chris_corey.c196project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;
import java.util.Calendar;

public class UpdateTermActivity extends AppCompatActivity {
    DBHelper dbHelper;
    String selectedTermId;

    EditText titleText;
    Button displayStartDate, displayEndDate;

    DatePickerDialog.OnDateSetListener startDateListener;
    DatePickerDialog.OnDateSetListener endDateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        Intent intent = getIntent();
        selectedTermId = intent.getStringExtra("SELECTED_TERM_ID");

        titleText = findViewById(R.id.edit_text_add_term_title);
        displayStartDate = findViewById(R.id.btn_add_term_start_date);
        displayEndDate = findViewById(R.id.btn_add_term_end_date);


        getDB();
        final Term term = dbHelper.getTermFromId(selectedTermId);

        titleText.setText(term.getTitle());
        displayStartDate.setText(term.getStartDate().toString());
        displayEndDate.setText(term.getEndDate().toString());




        Button submitButton = findViewById(R.id.btn_add_term_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Validation
                if (!validate()) {
                    Toast.makeText(UpdateTermActivity.this, "Bad Validation", Toast.LENGTH_SHORT).show();
                    return;
                }


                String title = titleText.getText().toString();
                Date startDate = Date.valueOf(displayStartDate.getText().toString());
                Date endDate = Date.valueOf(displayEndDate.getText().toString());

                term.setTitle(title);
                term.setStartDate(startDate);
                term.setEndDate(endDate);

                dbHelper.updateTerm(term);
                onBackPressed();
            }
        });

        Button cancelButton = findViewById(R.id.btn_add_term_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        displayStartDate = findViewById(R.id.btn_add_term_start_date);
        displayStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(term.getStartDate());
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateTermActivity.this, startDateListener, year, month, day);

                datePickerDialog.show();

            }
        });

        displayEndDate = findViewById(R.id.btn_add_term_end_date);
        displayEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(term.getEndDate());
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateTermActivity.this, endDateListener, year, month, day);
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

    private void getDB() {
        dbHelper = new DBHelper(UpdateTermActivity.this);
        dbHelper.getWritableDatabase();
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

        return isValid;
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }
}
