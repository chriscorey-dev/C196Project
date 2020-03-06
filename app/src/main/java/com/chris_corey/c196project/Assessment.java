package com.chris_corey.c196project;

import java.sql.Date;

public class Assessment {
    private int id, parentCourse;
    private String title, type;
    private Date dueDate;

    public Assessment(int id, String title, Date dueDate, String type, int parentCourse) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.type = type;
        this.parentCourse = parentCourse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentCourse() {
        return parentCourse;
    }

    public void setParentCourse(int parentCourse) {
        this.parentCourse = parentCourse;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
