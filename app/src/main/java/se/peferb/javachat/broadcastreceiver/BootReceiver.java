package se.peferb.javachat.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import se.peferb.javachat.firebase.FirebaseEventListener;

import static se.peferb.javachat.util.SystemUtil.isServiceRunning;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isServiceRunning(FirebaseEventListener.class, context)) {
            context.startService(new Intent(context, FirebaseEventListener.class));
        }
    }

}
