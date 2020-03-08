package com.chris_corey.c196project;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;

public class AssessmentDetailsActivity extends AppCompatActivity {
    DBHelper dbHelper;
    String selectedAssessmentId;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_assessment:
                // TODO: Update term
                return true;
            case R.id.delete_assessment:
                // TODO: Delete term
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.assessment_menu, menu);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);

        Intent intent = getIntent();
        selectedAssessmentId = intent.getStringExtra(CourseDetailsActivity.SELECTED_ASSESSMENT_ID);

        TextView assessmentTitle = findViewById(R.id.text_view_assessment_details_title);
        TextView assessmentDates = findViewById(R.id.text_view_assessment_details_due_date);
        TextView assessmentType = findViewById(R.id.text_view_assessment_details_type);

        getDB();
        Assessment assessment = getAssessmentInfo();

        assessmentTitle.setText(assessment.getTitle());
        assessmentDates.setText("Due: " + assessment.getDueDate());
        assessmentType.setText(assessment.getType());
    }

    private void getDB() {
        dbHelper = new DBHelper(AssessmentDetailsActivity.this);
        dbHelper.getWritableDatabase();
    }

    private Assessment getAssessmentInfo() {
        Cursor cursor = dbHelper.getWritableDatabase().rawQuery("SELECT * FROM assessments WHERE id = "+ selectedAssessmentId +";", null);
        Assessment assessment = null;

        // Gets data from DB and convert into objects
        while(cursor.moveToNext()) {
            Date dueDate = Date.valueOf(cursor.getString(cursor.getColumnIndex("due_date")));

            assessment = new Assessment(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), dueDate, cursor.getString(cursor.getColumnIndex("type")), cursor.getInt(cursor.getColumnIndex("parent_course")));
        }
        return assessment;
    }
}
