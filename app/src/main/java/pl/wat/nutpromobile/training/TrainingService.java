package pl.wat.nutpromobile.training;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TrainingService extends Service {

    public static String NOTIFICATION = "pl_wat_nutpro_mobile_notification_icon";

    private static final int TRAINING_SERVICE_ID = 1;

    private IBinder mBinder;

    private Context context;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mBinder = new LocalBinder();
        context = getBaseContext();

        Notification notification = intent.getExtras().getParcelable(NOTIFICATION);
        startForeground(TRAINING_SERVICE_ID, notification);
        return START_STICKY;
    }

    private void copyInputStreamToFile(InputStream in, File file) {
        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if (out != null) {
                    out.close();
                }

                // If you want to close the "in" InputStream yourself then remove this
                // from here but ensure that you close it yourself eventually.
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class LocalBinder extends Binder {
        public TrainingService getService() {
            //zwracamy instancje serwisu, przez nią odwołamy się następnie do metod.
            return TrainingService.this;
        }
    }
}
