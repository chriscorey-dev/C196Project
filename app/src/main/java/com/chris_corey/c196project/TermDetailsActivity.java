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
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;

public class TermDetailsActivity extends AppCompatActivity {
    DBHelper dbHelper;
    String selectedTermId;
    ArrayList<Course> courseList;
    ArrayAdapter<String> courseListAdapter;

    Term term;

    TextView termTitle, termDates;

    public static final String SELECTED_COURSE_ID = "com.chris_corey.c196project";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_term:
                Intent intent = new Intent(TermDetailsActivity.this, UpdateTermActivity.class);
                intent.putExtra("SELECTED_TERM_ID", selectedTermId);
                startActivity(intent);
                return true;
            case R.id.delete_term:
                ArrayList<String> associatedCourses = dbHelper.getAssociatedCourses(selectedTermId);

                if (associatedCourses.size() > 0) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Confirmation").setMessage("This term has " + associatedCourses.size() + " associated courses.\nStill Delete?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbHelper.deleteTerm(selectedTermId);
                            onBackPressed();
                        }
                    }).setNegativeButton("No", null).show();
                } else {
                    dbHelper.deleteTerm(selectedTermId);
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
        inflater.inflate(R.menu.term_menu, menu);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        getDB();
        term = dbHelper.getTermFromId(selectedTermId);
        getCourseList();
        updateTermInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        Intent intent = getIntent();
        selectedTermId = intent.getStringExtra(TermListActivity.SELECTED_TERM_ID);

        termTitle = findViewById(R.id.text_view_term_details_title);
        termDates = findViewById(R.id.text_view_term_details_dates);

        ListView listViewCourseList = findViewById(R.id.list_view_term_details_course_list);
        courseListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1); // Database method can't be run before this line
        listViewCourseList.setAdapter(courseListAdapter);

        Button buttonAddNewCourse= new Button(this);
        buttonAddNewCourse.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
        buttonAddNewCourse.setText("Add Course");
        listViewCourseList.addFooterView(buttonAddNewCourse);

        listViewCourseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TermDetailsActivity.this, CourseDetailsActivity.class);
                int courseId = courseList.get(i).getId();
                intent.putExtra(SELECTED_COURSE_ID, Integer.toString(courseId));
                startActivity(intent);
            }
        });


        // Getting term info
        getDB();
        term = dbHelper.getTermFromId(selectedTermId);
        getCourseList();
        updateTermInfo();

        buttonAddNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermDetailsActivity.this, AddCourseActivity.class);
                intent.putExtra("SELECTED_TERM_ID", selectedTermId);
                startActivity(intent);
            }
        });
    }

    private void updateTermInfo() {
        termTitle.setText(term.getTitle());
        termDates.setText(term.getStartDate() + " - " + term.getEndDate());
    }

    private void getDB() {
        dbHelper = new DBHelper(TermDetailsActivity.this);
        dbHelper.getWritableDatabase();
    }

    private void getCourseList() {
        Cursor cursor = dbHelper.getWritableDatabase().rawQuery("SELECT * FROM courses WHERE parent_term = " + selectedTermId, null);
        courseList = new ArrayList<>();

        courseListAdapter.clear();

        // Gets data from DB and convert into objects
        while(cursor.moveToNext()) {
            Date startDate = Date.valueOf(cursor.getString(cursor.getColumnIndex("start_date")));
            Date endDate = Date.valueOf(cursor.getString(cursor.getColumnIndex("end_date")));

            Course course = new Course(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), startDate, endDate, cursor.getString(cursor.getColumnIndex("notes")), cursor.getString(cursor.getColumnIndex("status")), cursor.getString(cursor.getColumnIndex("mentor_name")), cursor.getString(cursor.getColumnIndex("mentor_phone")), cursor.getString(cursor.getColumnIndex("mentor_email")), cursor.getInt(cursor.getColumnIndex("parent_term")));
            courseList.add(course);

            // Adds data to courseListAdapter in onCreate method
            courseListAdapter.add(course.getTitle());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }
}
