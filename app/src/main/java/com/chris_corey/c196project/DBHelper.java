package com.chris_corey.c196project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.ArrayList;

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
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS assessments (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, due_date DATE, type TEXT, complete BOOLEAN, parent_course INTEGER)");
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS notifications (request_code INTEGER PRIMARY KEY AUTOINCREMENT, date DATE, type TEXT, parent_id INTEGER)");
    }

    public void addNotification(Date date, String type, int parentId) {
        this.getWritableDatabase().execSQL("INSERT INTO notifications (date, type, parent_id) VALUES ('"+ date +"', '"+ type +"', "+ parentId +")");
    }

    public void deleteNotification(String request_code) {
        this.getWritableDatabase().delete("notifications", "request_code = " + request_code, null);
    }

//    public ArrayList<Notification> getNotificationsFromParentId(String parentId) {
public ArrayList<Notification> getCourseNotificationsFromParentId(String parentId) {
    ArrayList<Notification> notifications = new ArrayList<>();

    Cursor cursor = this.getWritableDatabase().rawQuery("SELECT * FROM notifications WHERE parent_id = "+ parentId +" AND (type = 'course_start' OR type = 'course_end')", null);
    Notification notification = null;

    while(cursor.moveToNext()) {
        Date date = Date.valueOf(cursor.getString(cursor.getColumnIndex("date")));

        notification = new Notification(cursor.getInt(cursor.getColumnIndex("request_code")), date, cursor.getString(cursor.getColumnIndex("type")), cursor.getInt(cursor.getColumnIndex("parent_id")));
        notifications.add(notification);
    }

    return notifications;
}
    public ArrayList<Notification> getAssessmentNotificationsFromParentId(String parentId) {
        ArrayList<Notification> notifications = new ArrayList<>();

        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT * FROM notifications WHERE parent_id = "+ parentId +" AND type = 'assessment_due'", null);
        Notification notification = null;

        while(cursor.moveToNext()) {
            Date date = Date.valueOf(cursor.getString(cursor.getColumnIndex("date")));

            notification = new Notification(cursor.getInt(cursor.getColumnIndex("request_code")), date, cursor.getString(cursor.getColumnIndex("type")), cursor.getInt(cursor.getColumnIndex("parent_id")));
            notifications.add(notification);
        }

        return notifications;
    }

    public Notification getLatestNotification() {
        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT * FROM notifications ORDER BY request_code DESC LIMIT 1", null);
        Notification notification = null;

        while(cursor.moveToNext()) {
            Date date = Date.valueOf(cursor.getString(cursor.getColumnIndex("date")));

            notification = new Notification(cursor.getInt(cursor.getColumnIndex("request_code")), date, cursor.getString(cursor.getColumnIndex("type")), cursor.getInt(cursor.getColumnIndex("parent_id")));
        }
        return notification;
    }

    public Term getTermFromId(String id) {
        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT * FROM terms WHERE id = "+ id, null);
        Term term = null;

        // Gets data from DB and convert into objects
        while(cursor.moveToNext()) {
            Date startDate = Date.valueOf(cursor.getString(cursor.getColumnIndex("start_date")));
            Date endDate = Date.valueOf(cursor.getString(cursor.getColumnIndex("end_date")));

            term = new Term(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), startDate, endDate);
        }
        return term;
    }

    public Course getCourseFromId(String id) {
        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT * FROM courses WHERE id = "+ id, null);
        Course course = null;

        // Gets data from DB and convert into objects
        while(cursor.moveToNext()) {
            Date startDate = Date.valueOf(cursor.getString(cursor.getColumnIndex("start_date")));
            Date endDate = Date.valueOf(cursor.getString(cursor.getColumnIndex("end_date")));

            course = new Course(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), startDate, endDate, cursor.getString(cursor.getColumnIndex("notes")), cursor.getString(cursor.getColumnIndex("status")), cursor.getString(cursor.getColumnIndex("mentor_name")), cursor.getString(cursor.getColumnIndex("mentor_phone")), cursor.getString(cursor.getColumnIndex("mentor_email")), cursor.getInt(cursor.getColumnIndex("parent_term")));
        }
        return course;
    }

    public Assessment getAssessmentFromId(String id) {
        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT * FROM assessments WHERE id = "+ id, null);
        Assessment assessment = null;

        // Gets data from DB and convert into objects
        while(cursor.moveToNext()) {
            Date dueDate = Date.valueOf(cursor.getString(cursor.getColumnIndex("due_date")));

            assessment = new Assessment(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), dueDate, cursor.getString(cursor.getColumnIndex("type")), cursor.getInt(cursor.getColumnIndex("parent_course")));
        }
        return assessment;
    }

    public Course getLatestCourse() {
        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT * FROM courses ORDER BY ID DESC LIMIT 1", null);
        Course course = null;

        while (cursor.moveToNext()) {
            course = new Course(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), Date.valueOf(cursor.getString(cursor.getColumnIndex("start_date"))), Date.valueOf(cursor.getString(cursor.getColumnIndex("end_date"))), cursor.getString(cursor.getColumnIndex("notes")), cursor.getString(cursor.getColumnIndex("status")), cursor.getString(cursor.getColumnIndex("mentor_name")), cursor.getString(cursor.getColumnIndex("mentor_phone")), cursor.getString(cursor.getColumnIndex("mentor_email")), cursor.getInt(cursor.getColumnIndex("parent_term")));
        }
        return course;
    }

    public Assessment getLatestAssessment() {
        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT * FROM assessments ORDER BY ID DESC LIMIT 1", null);
        Assessment assessment = null;
        while (cursor.moveToNext()) {
            assessment = new Assessment(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")), Date.valueOf(cursor.getString(cursor.getColumnIndex("due_date"))), cursor.getString(cursor.getColumnIndex("type")), cursor.getInt(cursor.getColumnIndex("parent_course")));
        }
        return assessment;
    }


    public void addTerm(String title, Date startDate, Date endDate) {
        this.getWritableDatabase().execSQL("INSERT INTO terms (title, start_date, end_date) VALUES ('"+ title +"', '"+ startDate +"', '"+ endDate +"')");
    }

    public void updateTerm(Term term) {
        this.getWritableDatabase().execSQL("UPDATE terms SET title = '"+ term.getTitle() +"', start_date = '"+ term.getStartDate() +"', end_date = '"+ term.getEndDate() +"' WHERE id = "+ term.getId());
    }

    public void deleteTerm(String id) {
        this.getWritableDatabase().delete("terms", "id = " + id, null);

        ArrayList<String> associatedIds = getAssociatedCourses(id);
        for (String courseId : associatedIds) {
            deleteCourse(courseId);
        }
    }


    public void addCourse(String title, Date startDate, Date endDate, String notes, String status, String mentorName, String mentorEmail, String mentorPhone, int parentTerm) {
        this.getWritableDatabase().execSQL("INSERT INTO courses (title, start_date, end_date, notes, status, mentor_name, mentor_email, mentor_phone, parent_term) VALUES ('"+ title +"', '"+ startDate +"', '"+ endDate +"', '"+ notes +"', '"+ status +"', '"+ mentorName +"', '"+ mentorEmail +"', '"+ mentorPhone +"', "+ parentTerm +")");
        this.addNotification(startDate, "course_start", getLatestCourse().getId());
        this.addNotification(endDate, "course_end", getLatestCourse().getId());

    }

    public void updateCourse(Course course) {
        this.getWritableDatabase().execSQL("UPDATE courses SET title = '"+ course.getTitle() +"', start_date = '"+ course.getStartDate() +"', end_date = '"+ course.getEndDate() +"', status = '"+ course.getStatus() +"', mentor_name = '"+ course.getMentorName() +"', mentor_phone = '"+ course.getMentorPhone() +"', mentor_email = '"+ course.getMentorEmail() +"' WHERE id = "+ course.getId());
    }

    public void deleteCourse(String id) {
        this.getWritableDatabase().delete("courses", "id = " + id, null);

        ArrayList<String> associatedIds = getAssociatedAssessments(id);
        for (String assessmentId : associatedIds) {
            deleteAssessment(assessmentId);
        }

        ArrayList<Notification> associatedNotifications = getCourseNotificationsFromParentId(id);
        for (Notification notification : associatedNotifications) {
            deleteNotification(String.valueOf(notification.getRequestCode()));
        }
    }


    public void addAssessment(String title, Date dueDate, String type, int parentCourse) {
        this.getWritableDatabase().execSQL("INSERT INTO assessments (title, due_date, type, parent_course) VALUES ('"+ title +"', '"+ dueDate +"', '"+ type +"', "+ parentCourse +")");
        this.addNotification(dueDate, "assessment_due", getLatestAssessment().getId());
    }

    public void updateAssessment(Assessment assessment) {
        this.getWritableDatabase().execSQL("UPDATE assessments SET title = '"+ assessment.getTitle() +"', due_date = '"+ assessment.getDueDate() +"', type = '"+ assessment.getType() +"' WHERE id = "+ assessment.getId());
    }

    public void deleteAssessment(String id) {
        this.getWritableDatabase().delete("assessments", "id = " + id, null);

//        ArrayList<Notification> associatedNotifications = getNotificationsFromParentId(id);
        ArrayList<Notification> associatedNotifications = getAssessmentNotificationsFromParentId(id);
        for (Notification notification : associatedNotifications) {
            deleteNotification(String.valueOf(notification.getRequestCode()));
        }
    }


    public String getCourseNotes(String courseId) {
        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT notes FROM courses WHERE id = " + courseId, null);
        String notes = "";

        while (cursor.moveToNext()) {
            notes = cursor.getString(cursor.getColumnIndex("notes"));
        }

        return notes;
    }

    public ArrayList<String> getAssociatedAssessments(String parentId) {
        ArrayList<String> associatedIds = new ArrayList<>();
        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT * FROM assessments WHERE parent_course = "+ parentId, null);

        while (cursor.moveToNext()) {
            associatedIds.add(cursor.getString(cursor.getColumnIndex("id")));
        }

        return associatedIds;
    }

    public ArrayList<String> getAssociatedCourses(String parentId) {
        ArrayList<String> associatedIds = new ArrayList<>();
        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT * FROM courses WHERE parent_term = "+ parentId, null);

        while (cursor.moveToNext()) {
            associatedIds.add(cursor.getString(cursor.getColumnIndex("id")));
        }

        return associatedIds;
    }

    public void setCourseNotes(String notes, String courseId) {
        this.getWritableDatabase().execSQL("UPDATE courses SET notes = '"+ notes +"' WHERE id = "+ courseId);
    }

    // DEBUG
    public void populateDatabase() {
        addTerm("Science Term", Date.valueOf("2020-01-01"), Date.valueOf("2020-06-30"));
        addTerm("Arts Term", Date.valueOf("2020-07-01"), Date.valueOf("2021-12-31"));

        addCourse("Math", Date.valueOf("2020-01-01"), Date.valueOf("2020-02-28"), "My Notes", "In Progress", "Bob Roberts", "bob@roberts.com", "555-555-5555", 1);
        addCourse("Science", Date.valueOf("2020-03-01"), Date.valueOf("2020-04-30"), "My Notes", "Plan To Take", "Bill Ribbets", "bill@ribbits.com", "555-555-5556", 1);
        addCourse("Reading", Date.valueOf("2020-07-01"), Date.valueOf("2020-07-31"), "My Notes", "Plan To Take", "Dick Richardson", "dick@richardson.com", "555-555-5557", 2);
        addCourse("Writing", Date.valueOf("2020-08-01"), Date.valueOf("2020-10-31"), "My Notes", "Plan To Take", "Bill Williamson", "bill@williamson.com", "555-555-5558", 2);

        addAssessment("Algebra", Date.valueOf("2020-01-31"), "OA", 1);
        addAssessment("Geometry", Date.valueOf("2020-02-28"), "OA", 1);
        addAssessment("Physics", Date.valueOf("2020-03-31"), "PA", 2);
        addAssessment("Chemistry", Date.valueOf("2020-04-30"), "OA", 2);

        addAssessment("Reading 101", Date.valueOf("2020-07-31"), "PA", 3);
        addAssessment("Writing 101", Date.valueOf("2020-08-31"), "PA", 4);
        addAssessment("Capstone Project", Date.valueOf("2020-10-31"), "PA", 4);
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