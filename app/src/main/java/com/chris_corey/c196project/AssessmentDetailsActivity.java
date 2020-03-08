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

    Assessment assessment;

    TextView assessmentTitle, assessmentDates, assessmentType;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_assessment:
                Intent intent = new Intent(AssessmentDetailsActivity.this, UpdateAssessmentActivity.class);
                intent.putExtra("SELECTED_ASSESSMENT_ID", selectedAssessmentId);
                startActivity(intent);
                return true;
            case R.id.delete_assessment:
                dbHelper.deleteAssessment(selectedAssessmentId);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

        assessmentTitle = findViewById(R.id.text_view_assessment_details_title);
        assessmentDates = findViewById(R.id.text_view_assessment_details_due_date);
        assessmentType = findViewById(R.id.text_view_assessment_details_type);

        getDB();
        assessment = dbHelper.getAssessmentFromId(selectedAssessmentId);
        updateAssessmentInfo();
    }

    private void updateAssessmentInfo() {

        assessmentTitle.setText(assessment.getTitle());
        assessmentDates.setText("Due: " + assessment.getDueDate());
        assessmentType.setText(assessment.getType());
    }

    private void getDB() {
        dbHelper = new DBHelper(AssessmentDetailsActivity.this);
        dbHelper.getWritableDatabase();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getDB();
        assessment = dbHelper.getAssessmentFromId(selectedAssessmentId);
        updateAssessmentInfo();
    }
}
