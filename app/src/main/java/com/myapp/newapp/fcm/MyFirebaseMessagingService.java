package com.myapp.newapp.fcm;

/**
 * Created by MOSMI on 9/21/2017.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.myapp.newapp.R;
import com.myapp.newapp.helper.ApiConstants;
import com.myapp.newapp.helper.AppConstants;
import com.myapp.newapp.helper.Functions;
import com.myapp.newapp.helper.PrefUtils;
import com.myapp.newapp.ui.DashboardActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
Log.e("message","message");
        if (PrefUtils.isUserLoggedIn(this)) {
            sendNotification();
        }

    }

    private void sendNotification() {

        String title = "New Post Published";

        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra(ApiConstants.NOTIFICATION_CALL, ApiConstants.NOTIFICATION_CLICK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

/*
            NotificationCompat.BigTextStyle s = new NotificationCompat.BigTextStyle();
            s.setBigContentTitle(title);
            s.setSummaryText(desc);

            NotificationCompat.InboxStyle s2 = new NotificationCompat.InboxStyle();
            s.setBigContentTitle(title);
            s.setSummaryText(desc);
*/

        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(title)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .build();

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(m, notification);

    }

}
