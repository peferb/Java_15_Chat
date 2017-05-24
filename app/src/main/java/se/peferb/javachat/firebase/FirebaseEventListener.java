package se.peferb.javachat.firebase;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import se.peferb.javachat.util.LocalStorage;
import se.peferb.javachat.MainActivity;
import se.peferb.javachat.util.Parser;

import com.google.firebase.codelab.javachat.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FirebaseEventListener extends Service {

    public static final String TAG = "FirebaseEventListener";
    public static final String MESSAGES_CHILD = "messages";
    private static final String JAVA_CHAT = "se.peferb.javachat";
    private Parser parser;
    private LocalStorage localStorage;
    private static boolean firstMessage = true;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        parser = new Parser();
        localStorage = new LocalStorage(getSharedPreferences(MainActivity.APP_PREFS, MODE_PRIVATE));

        DatabaseReference myFirebaseRef = FirebaseDatabase.getInstance().getReference();
        Query lastQuery = myFirebaseRef.child(MESSAGES_CHILD).orderByKey().limitToLast(1);
        lastQuery.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String data = snapshot.toString();
                String id = parser.parseId(data);

                // When registering listener we always get the last message directly after
                // registration, so just ignore the first message received
                if (firstMessage) {
                    localStorage.add(id); // by adding it to storage we will know its an old message
                    firstMessage = false;
                }

                // Only show notifications when app closed and the message id is new/unknown
                if (shouldShowNotification(id)) {
                    String message = parser.parseMessage(data);
                    String name = parser.parseName(data);

                    // TODO fetch image thumbnail if sent and present in notification
                    // TODO make notification vibrate

                    Intent appIntent = new Intent(getApplicationContext(), MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Notification notification = new Notification.Builder(getApplicationContext())
                            .setContentTitle(name + ':')
                            .setContentText(message)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .build();
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(1, notification);
                    localStorage.add(id);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError);
            }
        });

        // TODO notify about messages sent during "service reboot"

        return START_STICKY;
    }

    private boolean shouldShowNotification(String id) {
        // TODO should also show notification when in foreground and screen is locked/off
        return !firstMessage && !isAppInForeground() && !localStorage.contains(id);
    }

    private boolean isAppInForeground() {
        ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService( Context.ACTIVITY_SERVICE );
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo appProcess : appProcesses){
            if(appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                if (JAVA_CHAT.equals(appProcess.processName)) return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: shot down!");
    }
}