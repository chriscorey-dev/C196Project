package com.chris_corey.c196project;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.sql.Date;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DBHelper dbHelper;
    ArrayList<Term> termList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);

        dbHelper = new DBHelper(MainActivity.this);

        dbHelper.getWritableDatabase();

        // DEBUG
        dbHelper.destroyDatabase();

        dbHelper.createDatabase();

        // DEBUG
        dbHelper.populateDatabase();

//        ArrayList<String> valuesIdk = new ArrayList<>();
//        dbHelper.getWritableDatabase().query("termList", {"id", "title", "start_date", "end_date"}, )

        Cursor cursor = dbHelper.getWritableDatabase().rawQuery("SELECT * FROM terms", null);

        termList = new ArrayList<>();

        // Gets data from DB and converts into objects
        while(cursor.moveToNext()) {
            Date startDate = Date.valueOf(cursor.getString(2));
            Date endDate = Date.valueOf(cursor.getString(3));

            Term term = new Term(cursor.getInt(0), cursor.getString(1), startDate, endDate);
            termList.add(term);
        }

        // List View Stuff
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "Clicked: " + termList.get(i).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        for (Term term : termList) {
            adapter.add(term.getTitle());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }
}
