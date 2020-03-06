package com.chris_corey.c196project;

import java.sql.Date;

public class Assessmnet {
    private int id, parentCourse;
    private String title, type;
    private Date dueDate;

    public Assessmnet(int id, int parentCourse, String title, String type, Date dueDate) {
        this.id = id;
        this.parentCourse = parentCourse;
        this.title = title;
        this.type = type;
        this.dueDate = dueDate;
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
