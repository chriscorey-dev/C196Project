package com.chris_corey.c196project;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class App extends Application {
    public static final String CHANNEL_COURSE_ID = "courseChannel";
    public static final String CHANNEL_ASSESSMENT_ID = "assessmentChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel courseChannel = new NotificationChannel(CHANNEL_COURSE_ID, "Course Channel", NotificationManager.IMPORTANCE_DEFAULT);
            courseChannel.setDescription("Channel for course notifications");

            NotificationChannel assessmentChannel = new NotificationChannel(CHANNEL_ASSESSMENT_ID, "Assessment Channel", NotificationManager.IMPORTANCE_DEFAULT);
            assessmentChannel.setDescription("Channel for assessment notifications");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(courseChannel);
            manager.createNotificationChannel(assessmentChannel);
        }
    }
}
