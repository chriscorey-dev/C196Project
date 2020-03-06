package com.chris_corey.c196project;

import java.sql.Date;

public class Term {
    private int key;
    private String title;
    private Date startDate;
    private Date endDate;

    public Term(int key, String title, Date startDate, Date endDate) {
        this.key = key;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
