package com.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.activities.Activity_Chat;
import com.activities.R;
import com.data.ApplicationUser;
import data.Message;
import data.User;
import data.contents.ChatContent;
import java.util.Date;

import static java.lang.Thread.sleep;

/**
 *
 */
public class MessageService extends Service {
    private static NotificationManager nm; // the notification manager to throw notifications

    private static int nr = 0;

    private ApplicationUser me;

    private static Context activityContext;
    private static Uri alarmSound;
    private static Intent intent;

    @Override
    public void onCreate() {
        nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        activityContext = this;

        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }

    private class ReceiveMessages extends AsyncTask<Object, Object, Object> {

        @Override
        protected Object doInBackground(Object... objects) {
            try {
                /*
                sleep(5000);
                throwNotification(new Message<ChatContent>(new Date(), "Alice", "Bob", new ChatContent("This is a Test.")));
                sleep(5000);
                throwNotification(new Message<ChatContent>(new Date(), "Alice", "Bob", new ChatContent("This is a Test.")));
                */

                me = ApplicationUser.getInstance();
                me.connect();
                me.registerToServer();
                me.sendMessage(new ChatContent("Hallo"), "Alice");

            } catch (Exception e) {
                Log.d("?", e.getMessage());
            }

            stopSelf();

            return new Object();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new ReceiveMessages().execute(null);

        return START_STICKY;
    }

    public static void throwNotification(Message<ChatContent> message) {
        intent = new Intent(activityContext, Activity_Chat.class);
        intent.putExtra("notyou", new User(message.getSender()));
        PendingIntent pIntent = PendingIntent.getActivity(activityContext, 0, intent, 0);

        Notification n = new Notification.Builder(activityContext)
                .setContentTitle("SChat Message from: " + message.getSender())
                .setContentText(message.getContent().getMessage())
                .setSmallIcon(R.drawable.ic_launcher)
                .setSound(alarmSound)
                .setContentIntent(pIntent)
                .setAutoCancel(false).build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;

        nm.notify(nr, n);
        nr++;
    }

    /* we probably don't even need it, added it anyway because it was in the example */
    public class MyBinder extends Binder {
        MessageService getService() {
            return MessageService.this;
        }
    }

    private final IBinder mBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
