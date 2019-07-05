package pl.wat.nutpromobile;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import pl.wat.nutpromobile.ble.BluetoothLeService;

public class NotificationCreator {

    private static final int NOTIFICATION_ID = 1094;

    private static Notification notification;

    public static Notification getNotification(Context context) {
        Intent deleteIntent = new Intent(context, BluetoothLeService.class);
        deleteIntent.setAction("stop");
        PendingIntent deletePendingIntent =
                PendingIntent.getService(context,
                        1111,
                        deleteIntent
                , PendingIntent.FLAG_CANCEL_CURRENT);


        if(notification == null) {
            notification = new NotificationCompat.Builder(context, "12322")
                    .setContentTitle("Try Foreground Service")
                    .setContentText("Press below button to stop")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .addAction(R.drawable.ic_launcher_background, "Stop", deletePendingIntent)
                    .build();
        }

        return notification;
    }

    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "chanel_name";
            String description = "chanel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("12322", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public static int getNotificationId() {
        return NOTIFICATION_ID;
    }

}

