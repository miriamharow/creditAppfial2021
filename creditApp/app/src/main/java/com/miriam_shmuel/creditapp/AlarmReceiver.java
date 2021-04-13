package com.miriam_shmuel.creditapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;

public class AlarmReceiver extends BroadcastReceiver
{
    private static final String NOTIFICATION_CHANNEL_ID = "Alarm Notification Chanel";
    private String key, type;
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel Credit App";
    private  int  notificationID ;
    private NotificationManager notificationManager;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private TaskStackBuilder stackBuilder;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // get intent extra info
        String title = intent.getStringExtra("title");
        String msg = intent.getStringExtra("msg");
        key = intent.getStringExtra("key");
        type = intent.getStringExtra("type");
        stackBuilder = TaskStackBuilder.create(context);


        // create the Notification Channel
        createNotificationChannel(context);

        showOneTimeAlarmNotification(context, title, msg);
    }

    private void createNotificationChannel(Context context)
    {
        // Create a notification manager object.
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=  android.os.Build.VERSION_CODES.O)
        {
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null)
            {
                NotificationChannel notificationChannel = new NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT); // NotificationManager.IMPORTANCE_HIGH

                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }

    private void showOneTimeAlarmNotification(final Context context, final String title, final String msg)
    {
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        DocumentReference docRef = db.collection("user").document(email).collection("list of "+type).document(key);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Gift_Credit gift_credit = document.toObject(Gift_Credit.class);
                        notificationID = gift_credit.getNotificationID();

                        // Create the content intent for the notification, which launches MainActivity activity
                        Intent tapIntent = new Intent(context, ShowItemActivity.class);
                        tapIntent.putExtra("type", type);
                        tapIntent.putExtra("obj", gift_credit);
                       // tapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        stackBuilder.addNextIntentWithParentStack(tapIntent);
                        tapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent tapPendingIntent = PendingIntent.getActivity(context, notificationID, tapIntent,  PendingIntent.FLAG_UPDATE_CURRENT);


                        //PendingIntent tapPendingIntent = PendingIntent.getActivity(context, 0, tapIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

                        // create the notification
                        Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                                .setContentTitle(title)
                                .setContentText(msg)
                                .setSmallIcon(R.drawable.ic_receipt)
                                .setContentIntent(tapPendingIntent)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true)
                                .setOngoing(true)
                                .build();

                        // Deliver the notification
                        Log.d(TAG, "notificationID "+notificationID);
                        notificationManager.notify(notificationID, notification);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
