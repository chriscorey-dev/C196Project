package com.chris_corey.c196project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.sql.Date;

public class MainActivity extends AppCompatActivity {
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initiateDatabase();

        Button btnTermList = findViewById(R.id.btn_term_list);
        btnTermList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TermListActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initiateDatabase() {

        dbHelper = new DBHelper(MainActivity.this);
        dbHelper.getWritableDatabase();

        // DEBUG - Resets the database each time the app restarts
        dbHelper.destroyDatabase();

        dbHelper.createDatabase();

        // DEBUG - Resets the database each time the app restarts
        dbHelper.populateDatabase();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }
}
