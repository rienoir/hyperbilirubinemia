package com.example.kevin.hyperbilirubinemia;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;
import android.os.Process;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;


public class ImageProcessingService extends Service {

    static {
        System.loadLibrary("native-lib");
    }

    private double result;
    private Handler processingHandler;
    private HandlerThread processingThread;



    private static String NOTIFICATION_CHANNEL_ID = "Notif";
    private static int NOTIFICATION_ID = 2;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel(){
        //IF build version is higher than android 8.0 (SDK 26+ or OREO)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createNotification(){
        /*
        Intent finishIntent = new Intent(this, InfoActivity.class);
        finishIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, finishIntent, 0);
*/
        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.round_button)
                .setContentTitle("Processing Image")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                //.setContentIntent(pendingIntent)
                .setProgress(0,0,true)
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        //notificationManagerCompat.notify(NOTIFICATION_ID, notification);

        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service starting", Toast.LENGTH_LONG).show();

        processingThread = new HandlerThread("processing thread",Process.THREAD_PRIORITY_BACKGROUND);
        processingThread.start();

        processingHandler = new Handler(processingThread.getLooper());

        String filePath = intent.getStringExtra("IMAGE_FILE_PATH");

        Log.d(MainActivity.DEBUGTAG, "file path  :  " + filePath);

        Mat rawImage = Imgcodecs.imread(filePath);

        /*File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String filename = "temp.jpg";
        File file = new File(path, filename);
        filename = file.toString();
        Imgcodecs.imwrite(filename, rawImage);
        */
        createNotification();
        Log.d(MainActivity.DEBUGTAG, "onto c++");
        result = ImageProcessing(rawImage.getNativeObjAddr());

        Log.d(MainActivity.DEBUGTAG, "Kadar : " + result);

        stopForeground(STOP_FOREGROUND_REMOVE);
        createFinishNotification();
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service done", Toast.LENGTH_LONG).show();
        closeBackgroundThread();
        super.onDestroy();
    }


    private void createFinishNotification(){

        Intent finishIntent = new Intent(this, MainActivity.class);
        //finishIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finishIntent.putExtra("ShowResult", 1);
        finishIntent.putExtra("Result", result);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, finishIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.round_button)
                .setContentTitle("Image have been processed")
                .setContentInfo("Click here to see the result")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        //Intent mainIntent = new Intent(this, MainActivity.class);
        //mainIntent.putExtra("ThrowResult", )

        startActivity(finishIntent);

        notificationManagerCompat.notify(NOTIFICATION_ID, notification);
    }

    private void closeBackgroundThread() {
        if (processingHandler != null) {
            processingThread.quitSafely();
            processingThread = null;
            processingHandler = null;
        }
    }


    public native double ImageProcessing(long rawImage);
}
