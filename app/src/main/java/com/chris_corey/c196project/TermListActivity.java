package com.chris_corey.c196project;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Date;
import java.util.ArrayList;

public class TermListActivity extends AppCompatActivity {
    DBHelper dbHelper;
    ArrayList<Term> termList;
    ArrayAdapter<String> termListAdapter;

    public static final String SELECTED_TERM_ID = "com.chris_corey.c196project";

    @Override
    protected void onResume() {
        super.onResume();

        getDB();
        getTermList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        ListView listViewTermList = findViewById(R.id.list_view_term_list);
        termListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1); // Database method can't be run before this line

        getDB();
        getTermList();

        listViewTermList.setAdapter(termListAdapter);

        Button buttonAddNewTerm= new Button(this);
        buttonAddNewTerm.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
        buttonAddNewTerm.setText("Add Term");
        listViewTermList.addFooterView(buttonAddNewTerm);

        listViewTermList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TermListActivity.this, TermDetailsActivity.class);
                int termId = termList.get(i).getId();
                intent.putExtra(SELECTED_TERM_ID, Integer.toString(termId));
                startActivity(intent);
            }
        });

        buttonAddNewTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermListActivity.this, AddTermActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDB() {
        dbHelper = new DBHelper(TermListActivity.this);
        dbHelper.getWritableDatabase();
    }

    private void getTermList() {
        Cursor cursor = dbHelper.getWritableDatabase().rawQuery("SELECT * FROM terms", null);
        termList = new ArrayList<>();

        termListAdapter.clear();

        // Gets data from DB and convert into objects
        while(cursor.moveToNext()) {
            Date startDate = Date.valueOf(cursor.getString(cursor.getColumnIndex("start_date")));
            Date endDate = Date.valueOf(cursor.getString(cursor.getColumnIndex("end_date")));

            Term term = new Term(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), startDate, endDate);
            termList.add(term);

            // Adds data to courseListAdapter in onCreate method
            termListAdapter.add(term.getTitle());
        }
    }
}
