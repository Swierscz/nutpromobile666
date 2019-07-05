package pl.wat.nutpromobile.training;

import android.app.Notification;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import pl.wat.nutpromobile.R;

public class TrainingServiceHelper {
    public static final String NOTIFICATION_CHANNEL_ID = "training_service_channel";

    private Context context;

    public TrainingServiceHelper(Context context) {
        this.context = context;
    }

    public Notification buildNotification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Nutpro Training")
                .setContentText("Trening trwa")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return notificationBuilder.build();
    }
}
