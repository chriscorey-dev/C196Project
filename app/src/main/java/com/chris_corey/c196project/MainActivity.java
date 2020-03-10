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

public class MainActivity extends AppCompatActivity {
    public DBHelper dbHelper;

    //TODO LIST:
    //TODO: Validate dates so start can't be after end?
    //TODO: Ability to change assessment type?
    //TODO: Notification on start & end date for courses
    //TODO: Notification on due dates for assessments
    //TODO: Home screen ('scheduling and progress tracking elements'
    //TODO: Essay
    //TODO: Pictures of storyboard
    //TODO: Screenshots of deployment package
    //TODO: Deleting term and backing out of prompts still deletes lol
    //TODO: Notifications that are created when DB is reset have seemingly random parent IDs.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initiateDatabase();

//        // DEBUG: Populates database if first time the app runs
//        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
//        boolean firstStart = prefs.getBoolean("firstStart", true);
//        if (firstStart) {
//            dbHelper.populateDatabase();
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putBoolean("firstStart", false);
//            editor.apply();
//        }

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
                alertDialogBuilder.setTitle("Confirmation").setMessage("Are you sure you want to reset the database to its original state?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
