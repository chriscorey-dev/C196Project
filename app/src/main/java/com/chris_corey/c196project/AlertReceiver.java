package com.chris_corey.c196project;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        String startOrEnd = intent.getStringExtra("START_OR_END");
//        String channel = intent.getStringExtra("NOTIFICATION_CHANNEL");
        String type = intent.getStringExtra("NOTIFICATION_TYPE");
        String channelId = intent.getStringExtra("CHANNEL_ID");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = null;

        if (type.contains("course")) {
            String content = (type.contains("start")) ? "Your course starts today!" : "Your course ends today!";
            notification = new NotificationCompat.Builder(context, App.CHANNEL_COURSE_ID).setSmallIcon(R.drawable.ic_notifications_white_24dp).setContentTitle("New Course Information").setContentText(content).build();
        } else {
            notification = new NotificationCompat.Builder(context, App.CHANNEL_ASSESSMENT_ID).setSmallIcon(R.drawable.ic_notifications_white_24dp).setContentTitle("New Assessment Information").setContentText("Your assessment is due today!").build();
        }

        notificationManager.notify(Integer.parseInt(channelId), notification);
    }
}
