package com.chris_corey.c196project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Date;

public class CourseNotesActivity extends AppCompatActivity {
    DBHelper dbHelper;
    String selectedCourseId;
    EditText notesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_notes);

        Intent intent = getIntent();
        selectedCourseId = intent.getStringExtra("SELECTED_COURSE_ID");

        getDB();

        notesText = findViewById(R.id.edit_text_course_notes);
        notesText.setText(dbHelper.getCourseNotes(selectedCourseId));

        Button submitButton = findViewById(R.id.btn_course_notes_save);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: Validation

                dbHelper.setCourseNotes(notesText.getText().toString(), selectedCourseId);
                onBackPressed();
            }
        });
    }

    private void getDB() {
        dbHelper = new DBHelper(CourseNotesActivity.this);
        dbHelper.getWritableDatabase();
    }
}
