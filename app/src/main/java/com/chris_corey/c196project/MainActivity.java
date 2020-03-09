package com.chris_corey.c196project;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.sql.Date;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public DBHelper dbHelper;

    //TODO LIST:
    //TODO: Ability to change course status
    //TODO: Ability to change assessment status?
    //TODO: 'as many as 5 assessments to each course'
    //TODO: Notification on start & end date for courses
    //TODO: Notification on due dates for assessments
    //TODO: Custom color scheme & icon
    //TODO: Home screen ('scheduling and progress tracking elements'
    //TODO: Essay
    //TODO: Pictures of storyboard
    //TODO: Screenshots of deployment package

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
        dbHelper.createDatabase();
    }

    private void resetDatabase() {
        // DEBUG
        dbHelper.destroyDatabase();
        dbHelper.createDatabase();
        dbHelper.populateDatabase();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset_database:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Confirmation").setMessage("Are you sure you want to completely reset the database?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetDatabase();
                    }
                }).setNegativeButton("No", null).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }
}
