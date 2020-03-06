package com.chris_corey.c196project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QCOM.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void createDatabase() {
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS terms (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, start_date DATE, end_date DATE)");
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS courses (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, start_date DATE, end_date DATE, notes TEXT, status TEXT, mentor_name TEXT, mentor_phone TEXT, mentor_email TEXT, parent_term INTEGER)");
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS assessments (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, due_date DATE, type TEXT, parent_course INTEGER)");
    }

    public void addTerm(String title, Date startDate, Date endDate) {
        this.getWritableDatabase().execSQL("INSERT INTO terms (title, start_date, end_date) VALUES ('"+ title +"', '"+ startDate +"', '"+ endDate +"')");
    }

    public void addCourse(String title, Date startDate, Date endDate, String notes, String status, String mentorName, String mentorEmail, String mentorPhone, int parentTerm) {
        this.getWritableDatabase().execSQL("INSERT INTO courses (title, start_date, end_date, notes, status, mentor_name, mentor_email, mentor_phone, parent_term) VALUES ('"+ title +"', '"+ startDate +"', '"+ endDate +"', '"+ notes +"', '"+ status +"', '"+ mentorName +"', '"+ mentorEmail +"', '"+ mentorPhone +"', "+ parentTerm +")");
    }

    public void addAssessment(String title, Date dueDate, String type, int parentCourse) {
        this.getWritableDatabase().execSQL("INSERT INTO assessments (title, due_date, type, parent_course) VALUES ('"+ title +"', '"+ dueDate +"', '"+ type +"', "+ parentCourse +")");
    }

    // DEBUG
    public void populateDatabase() {
        addTerm("Term 1", Date.valueOf("2020-01-01"), Date.valueOf("2020-06-30"));
        addTerm("Term 2", Date.valueOf("2020-07-01"), Date.valueOf("2021-12-31"));
        addCourse("Course 1", Date.valueOf("2020-01-01"), Date.valueOf("2020-01-31"), "My Notes", "In Progress", "Bob Roberts", "bob@roberts.com", "555-555-5555", 1);
        addCourse("Course 2", Date.valueOf("2020-02-01"), Date.valueOf("2020-02-28"), "My Notes", "Plan To Take", "Bill Ribbets", "bill@ribbits.com", "555-555-5556", 1);
        addAssessment("Assessment 1", Date.valueOf("2020-01-31"), "OA", 1);
        addAssessment("Assessment 2", Date.valueOf("2020-02-28"), "PA", 2);
    }

    // DEBUG
    public void destroyDatabase() {
        this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS terms;");
        this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS courses;");
        this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS assessments;");
    }
}



// execSQL(); - Used for SQL statements that don't return data, like creating a table
// insert(); - Inserts a row into a table. Uses ContentValues, which are key value pairs that represent a column name & column value. Returns long
// query(); - SQL query. Returns Cursor object for iteration
// update(); - SQL update. returns int representing # of rows affected
// delete(); - SQL delete. returns int representing # of rows affected

// Cursor Object - Like an array list, contains any values retrieved from DB table