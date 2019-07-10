package pl.wat.nutpromobile.features.service;

import android.content.Context;
import android.content.Intent;

public class ServiceUtil {

    public static void startForegroundService(Context context, Class<?> clas){
        Intent startIntent = new Intent(context, clas);
        startIntent.setAction(MyNotification.Action.START.toString());
        context.startService(startIntent);
    }

    public static void stopForegroundService(Context context, Class<?> clas){
        Intent startIntent = new Intent(context, clas);
        startIntent.setAction(MyNotification.Action.END.toString());
        context.startService(startIntent);
    }

}
