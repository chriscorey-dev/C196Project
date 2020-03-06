package com.chris_corey.c196project;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Date;
import java.util.ArrayList;

public class TermListActivity extends AppCompatActivity {
    DBHelper dbHelper;
    ArrayList<Term> termList;
    ArrayAdapter<String> adapter;

    public static final String SELECTED_TERM_ID = "com.chris_corey.c196project";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

//        Intent intent = getIntent();
//        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
//        Toast.makeText(this, "Message: " + message, Toast.LENGTH_SHORT).show();

        ListView listView = findViewById(R.id.list_view_term_list);

        // List View Stuff
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1); // Database method must be run any time after this line

        initiateDatabase();
        populateTermList();

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TermListActivity.this, TermDetailsActivity.class);
                int termId = termList.get(i).getId();
                intent.putExtra(SELECTED_TERM_ID, Integer.toString(termId));
                startActivity(intent);
            }
        });
    }

    private void initiateDatabase() {

        dbHelper = new DBHelper(TermListActivity.this);
        dbHelper.getWritableDatabase();

        // DEBUG - Resets the database each time the app restarts
        dbHelper.destroyDatabase();

        dbHelper.createDatabase();

        // DEBUG - Resets the database each time the app restarts
        dbHelper.populateDatabase();
    }

    private void populateTermList() {
        Cursor cursor = dbHelper.getWritableDatabase().rawQuery("SELECT * FROM terms", null);
        termList = new ArrayList<>();

        // Gets data from DB and convert into objects
        while(cursor.moveToNext()) {
            Date startDate = Date.valueOf(cursor.getString(cursor.getColumnIndex("start_date")));
            Date endDate = Date.valueOf(cursor.getString(cursor.getColumnIndex("end_date")));

            Term term = new Term(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), startDate, endDate);
            termList.add(term);

            // Adds data to adapter
            adapter.add(term.getTitle());
        }
    }
}
