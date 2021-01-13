package com.example.notificationexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {

    public final String CHANNEL_ID = "personal_notifications";
    public static final int NOTIFICATION_ID = 101;
    public static final String TXT_REPLY = "text_reply";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void displayNotification(View view) {

        createNotificationChannel();

        //Create Tap Action on Notification
        Intent secondIntent = new Intent(this,SecondActivity.class);
        secondIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingSecondIntent = PendingIntent.getActivity(this,0,secondIntent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_sms_notification);
        builder.setContentTitle("Simple Notification");
        builder.setContentText("This is a simple notification..");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);    //This is used to cancel the notification once it is tapped
        builder.setContentIntent(pendingSecondIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());

    }

    private void createNotificationChannel() {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "Personal Notifications";
            String description = "All personal Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name,importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void dispayWithActionButton(View view) {
        createNotificationChannel();

        //Create Tap Action on Notification
        Intent secondIntent = new Intent(this,SecondActivity.class);
        secondIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingSecondIntent = PendingIntent.getActivity(this,0,secondIntent,PendingIntent.FLAG_ONE_SHOT);

        Intent yesIntent = new Intent(this,YesActivity.class);
        secondIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingYesIntent = PendingIntent.getActivity(this,0,yesIntent,PendingIntent.FLAG_ONE_SHOT);

        Intent noIntent = new Intent(this,NoActivity.class);
        secondIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingNoIntent = PendingIntent.getActivity(this,0,noIntent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_sms_notification);
        builder.setContentTitle("Simple Notification");
        builder.setContentText("This is a simple notification..");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);    //This is used to cancel the notification once it is tapped
        builder.setContentIntent(pendingSecondIntent);
        builder.addAction(R.drawable.ic_sms_notification,"Yes",pendingYesIntent);
        builder.addAction(R.drawable.ic_sms_notification,"No",pendingNoIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
    }

    public void dispayWithReplyButton(View view) {
        createNotificationChannel();

        //Create Tap Action on Notification
        Intent secondIntent = new Intent(this,SecondActivity.class);
        secondIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingSecondIntent = PendingIntent.getActivity(this,0,secondIntent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_sms_notification);
        builder.setContentTitle("Simple Notification");
        builder.setContentText("This is a simple notification..");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);    //This is used to cancel the notification once it is tapped
        builder.setContentIntent(pendingSecondIntent);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            RemoteInput remoteInput = new RemoteInput.Builder(TXT_REPLY).setLabel("Reply").build();

            Intent remoteIntent = new Intent(this,RemoteReceiver.class);
            remoteIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingRemoteIntent = PendingIntent.getActivity(this,0,remoteIntent,PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Action action= new NotificationCompat.Action.Builder(R.drawable.ic_sms_notification,"Reply",pendingRemoteIntent)
                    .addRemoteInput(remoteInput)
                    .setAllowGeneratedReplies(true)
                    .build();
            builder.addAction(action);
        }

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
    }

    public void displayWithProgressBar(View view) {
        createNotificationChannel();

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_download);
        builder.setContentTitle("Image Download");
        builder.setContentText("Download in Progress");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        final NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        final int max_progress =100;
        int current_progress = 0;
        //builder.setProgress(max_progress,current_progress,false);  //Here indeterminate is false means showing progress percentage
        builder.setProgress(0,current_progress,true);
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());

        Thread thread = new Thread(){
            @Override
            public void run() {
                int count = 0;
                try{
                    while(count<=100){
                        count+=10;
                        sleep(1000);
                        //builder.setProgress(max_progress,count,false); // this is done to update the progress bar when the indeterminate is false
                        //notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
                    }

                    builder.setContentText("Download Completed..");
                    builder.setProgress(0,0,false);
                    notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
                }catch(InterruptedException e){}
            }
        };
        thread.start();

    }

    public void displayWithExpanable(View view) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_sms_notification);
        builder.setContentTitle("Simple Notification");
        builder.setContentText("This is a simple notification..");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //This is for Image Expandable View
       /* Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.cover);
        builder.setLargeIcon(bitmap);
        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigPicture(null));*/

       builder.setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.text)));

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());

    }

    public void displayWithCustomLayout(View view) {
        RemoteViews normal_layout = new RemoteViews(getPackageName(),R.layout.custom_normal);
        RemoteViews expanded_layout = new RemoteViews(getPackageName(),R.layout.custom_expanded);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_sms_notification);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        builder.setCustomContentView(normal_layout);
        builder.setCustomBigContentView(expanded_layout);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
    }
}
