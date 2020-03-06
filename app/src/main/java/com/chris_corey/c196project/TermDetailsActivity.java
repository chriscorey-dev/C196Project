package com.chris_corey.c196project;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.sql.Date;
import java.util.ArrayList;

public class TermDetailsActivity extends AppCompatActivity {
    DBHelper dbHelper;
    String selectedTermId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);


        Intent intent = getIntent();
        selectedTermId = intent.getStringExtra(TermListActivity.SELECTED_TERM_ID);

        initiateDatabase();

        Term term = getTerm();
        Toast.makeText(this, "Term title: " + term.getTitle(), Toast.LENGTH_SHORT).show();

    }

    private void initiateDatabase() {

        dbHelper = new DBHelper(TermDetailsActivity.this);
        dbHelper.getWritableDatabase();

        // DEBUG - Resets the database each time the app restarts
        dbHelper.destroyDatabase();

        dbHelper.createDatabase();

        // DEBUG - Resets the database each time the app restarts
        dbHelper.populateDatabase();
    }

    private Term getTerm() {
        Cursor cursor = dbHelper.getWritableDatabase().rawQuery("SELECT * FROM terms WHERE id = "+ selectedTermId +";", null);
        Term term = null;

        // Gets data from DB and convert into objects
        while(cursor.moveToNext()) {
            Date startDate = Date.valueOf(cursor.getString(2));
            Date endDate = Date.valueOf(cursor.getString(3));

            term = new Term(cursor.getInt(0), cursor.getString(1), startDate, endDate);
        }
        return term;
    }
}
