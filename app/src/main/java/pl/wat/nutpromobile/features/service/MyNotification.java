package pl.wat.nutpromobile.features.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.activity.main.MainActivity;
import pl.wat.nutpromobile.features.training.TrainingService;

public class MyNotification {

    private static final int NOTIFICATION_ID = 1094;
    private static final int INTENT_REQUEST_CODE = 1095;
    private static final String CHANELL_ID = "chanel_id";
    private static MyNotification INSTANCE;

    public enum Action {
        START, PAUSE, RESUME, END
    }

    private static Notification notification;
    private NotificationCompat.Builder currentBuild;



    public static MyNotification getInstance(){
        if(INSTANCE == null)
            synchronized (MyNotification.class){
                if(INSTANCE == null)
                    INSTANCE = new MyNotification();
            }
        return INSTANCE;
    }



    public Notification getNotification(Context context, boolean isPause) {
        if (notification == null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANELL_ID);
            builder.setContentTitle("Nutpro");
            builder.setContentText("Manage your training");
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setOnlyAlertOnce(true);
            builder = createBuilderWithActions(context, builder, true);
            builder.setContentIntent(createContentIntent(context));
            builder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("Czas trwania\n dystans"));
            currentBuild = builder;
            notification = builder.build();
        }
        return notification;
    }

    private static NotificationCompat.Builder createBuilderWithActions(Context context, NotificationCompat.Builder builder, boolean isPause) {
        if (builder != null) {
            builder.mActions.clear();
            if (isPause) {
                builder.addAction(R.drawable.ic_bottom_nav_history_more, "Pause", createNotificationIntent(context, Action.PAUSE));
            } else {
                builder.addAction(R.drawable.ic_bottom_nav_history_more, "Resume", createNotificationIntent(context, Action.RESUME));
            }
            builder.addAction(R.drawable.ic_bottom_nav_history_more, "End", createNotificationIntent(context, Action.END));
        }
        return builder;
    }

    public void changeNotificationToPauseButton(Context context) {
        if (currentBuild != null) {
            currentBuild = createBuilderWithActions(context, currentBuild, true);
        }
        updateNotification(context, currentBuild.build());
    }

    public void changeNotificationToResumeButton(Context context) {
        if (currentBuild != null) {
            currentBuild = createBuilderWithActions(context, currentBuild, false);
        }
        updateNotification(context, currentBuild.build());
    }

    public void updateText(Context context, String time) {
        currentBuild = currentBuild.setStyle(new NotificationCompat.BigTextStyle().bigText("Distance: " + time + "m"));
        updateNotification(context, currentBuild.build());
    }


    private static PendingIntent createContentIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    private static PendingIntent createNotificationIntent(Context context, Action action) {
        Intent intent = new Intent(context, TrainingService.class);
        intent.setAction(action.toString());
        return PendingIntent.getService(context,
                INTENT_REQUEST_CODE,
                intent
                , PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "chanel_name";
            String description = "chanel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANELL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public static void updateNotification(Context context, Notification notification) {
        final NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(getNotificationId(), notification);
    }



    public static int getNotificationId() {
        return NOTIFICATION_ID;
    }

}

