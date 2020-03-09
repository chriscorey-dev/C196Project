package com.chris_corey.c196project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CourseNotesActivity extends AppCompatActivity {
    DBHelper dbHelper;
    String selectedCourseId;
    EditText notesText;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        saveNotes();

        switch (item.getItemId()) {
            case R.id.share_notes:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, notesText.getText().toString());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

                return true;
//            case R.id.share_messages:
//                return true;
//            case R.id.share_email:
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.course_notes_menu, menu);

        return true;
    }

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
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        saveNotes();

        super.onBackPressed();
    }

    private void saveNotes() {
        dbHelper.setCourseNotes(notesText.getText().toString(), selectedCourseId);
    }

    private void getDB() {
        dbHelper = new DBHelper(CourseNotesActivity.this);
        dbHelper.getWritableDatabase();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }
}
