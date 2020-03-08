package com.chris_corey.c196project;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.util.ArrayList;

public class CourseDetailsActivity extends AppCompatActivity {
    DBHelper dbHelper;
    String selectedCourseId;
    ArrayList<Assessment> assessmentList;
    ArrayAdapter<String> assessmentListAdapter;

    public static final String SELECTED_ASSESSMENT_ID = "com.chris_corey.c196project";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_course:
                // TODO: Update course
                return true;
            case R.id.delete_course:

                ArrayList<String> associatedAssessments = dbHelper.getAssociatedAssessments(selectedCourseId);

                if (associatedAssessments.size() > 0) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Confirmation").setMessage("This course has " + associatedAssessments.size() + " associated assessments.\nStill Delete?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbHelper.deleteCourse(selectedCourseId);
                            onBackPressed();
                        }
                    }).setNegativeButton("No", null).show();
                } else {
                    dbHelper.deleteCourse(selectedCourseId);
                    onBackPressed();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.course_menu, menu);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        getDB();
        getAssessmentList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        Intent intent = getIntent();
        selectedCourseId = intent.getStringExtra(TermDetailsActivity.SELECTED_COURSE_ID);

//        Button buttonAddNewAssessment = findViewById(R.id.btn_course_details_add_assessment);
        Button buttonCourseNotes = findViewById(R.id.btn_course_details_notes);

        TextView courseTitle = findViewById(R.id.text_view_course_details_title);
        TextView courseDates = findViewById(R.id.text_view_course_details_dates);
        TextView courseStatus = findViewById(R.id.text_view_course_details_status);
        TextView mentorName = findViewById(R.id.text_view_course_details_mentor_name);
        TextView mentorPhone = findViewById(R.id.text_view_course_details_mentor_phone);
        TextView mentorEmail = findViewById(R.id.text_view_course_details_mentor_email);

        ListView listViewAssessmentList = findViewById(R.id.list_view_course_details_assessment_list);
        assessmentListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1); // Database method can't be run before this line
        listViewAssessmentList.setAdapter(assessmentListAdapter);

        Button buttonAddNewAssessment = new Button(this);
        buttonAddNewAssessment.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
        buttonAddNewAssessment.setText("Add Assessment");
        listViewAssessmentList.addFooterView(buttonAddNewAssessment);

        listViewAssessmentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CourseDetailsActivity.this, AssessmentDetailsActivity.class);
                int assessmentId = assessmentList.get(i).getId();
                intent.putExtra(SELECTED_ASSESSMENT_ID, Integer.toString(assessmentId));
                startActivity(intent);
            }
        });

        // Getting course info
        getDB();
        Course course = getCourseInfo();

        courseTitle.setText(course.getTitle());
        courseDates.setText(course.getStartDate() + " - " + course.getEndDate());
        courseStatus.setText(course.getStatus());
        mentorName.setText(course.getMentorName());
        mentorPhone.setText(course.getMentorPhone());
        mentorEmail.setText(course.getMentorEmail());

        // Getting associated course info
        getAssessmentList();

        buttonAddNewAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseDetailsActivity.this, AddAssessmentActivity.class);
                intent.putExtra("SELECTED_COURSE_ID", selectedCourseId);
                startActivity(intent);
            }
        });

        buttonCourseNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseDetailsActivity.this, CourseNotesActivity.class);
                intent.putExtra("SELECTED_COURSE_ID", selectedCourseId);
                startActivity(intent);
            }
        });
    }

    private void getDB() {
        dbHelper = new DBHelper(CourseDetailsActivity.this);
        dbHelper.getWritableDatabase();
    }

    private Course getCourseInfo() {
        Cursor cursor = dbHelper.getWritableDatabase().rawQuery("SELECT * FROM courses WHERE id = " + selectedCourseId + ";", null);
        Course course = null;

        // Gets data from DB and convert into objects
        while (cursor.moveToNext()) {
            Date startDate = Date.valueOf(cursor.getString(cursor.getColumnIndex("start_date")));
            Date endDate = Date.valueOf(cursor.getString(cursor.getColumnIndex("end_date")));

            course = new Course(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), startDate, endDate, cursor.getString(cursor.getColumnIndex("notes")), cursor.getString(cursor.getColumnIndex("status")), cursor.getString(cursor.getColumnIndex("mentor_name")), cursor.getString(cursor.getColumnIndex("mentor_phone")), cursor.getString(cursor.getColumnIndex("mentor_email")), cursor.getInt(cursor.getColumnIndex("parent_term")));
        }
        return course;
    }

    private void getAssessmentList() {
        Cursor cursor = dbHelper.getWritableDatabase().rawQuery("SELECT * FROM assessments WHERE parent_course = " + selectedCourseId, null);
        assessmentList = new ArrayList<>();

        assessmentListAdapter.clear();

        // Gets data from DB and convert into objects
        while (cursor.moveToNext()) {
            Date dueDate = Date.valueOf(cursor.getString(cursor.getColumnIndex("due_date")));

            Assessment assessment = new Assessment(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), dueDate, cursor.getString(cursor.getColumnIndex("type")), cursor.getInt(cursor.getColumnIndex("parent_course")));
            assessmentList.add(assessment);

            // Adds data to courseAssessmentAdapter in onCreate method
            assessmentListAdapter.add(assessment.getTitle());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }
}
