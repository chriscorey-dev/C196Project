package com.chris_corey.c196project;

import java.sql.Date;

public class Notification {
    private int requestCode;
    private Date date;
    private String type;
    private int parentId;

    public Notification(int requestCode, Date date, String type, int parentId) {
        this.requestCode = requestCode;
        this.date = date;
        this.type = type;
        this.parentId = parentId;
    }

    public int  getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
