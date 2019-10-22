package com.mumble.mburger.relation_sample.Push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mumble.mburger.relation_sample.Activities.Act_splash;
import com.mumble.mburger.relation_sample.Controllers.CustomComponents;
import com.mumble.mburger.relation_sample.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

public class FCMReceiver extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Map<String, String> map = remoteMessage.getData();
        String msg = map.get("body");
        String title = getApplicationContext().getString(R.string.app_name);
        if (map.containsKey("title")) {
            title = map.get("title");
            if (title != null) {
                if (title.isEmpty()) {
                    title = getApplicationContext().getString(R.string.app_name);
                }
            } else {
                title = getApplicationContext().getString(R.string.app_name);
            }
        }

        if ((msg != null)) {
            Intent i = new Intent(getApplicationContext(), Act_splash.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("onNotificationStart", true);

            int notifId = new Random().nextInt(Integer.MAX_VALUE);
            long block_id = -1;
            long section_id = -1;
            if (map.containsKey("custom")) {
                String custom = map.get("custom");
                try {
                    JSONObject jCustom = new JSONObject(custom);
                    if (CustomComponents.Companion.isJSONOk(jCustom, "block_id")) {
                        block_id = jCustom.getLong("block_id");
                        i.putExtra("block_id", block_id);
                    }

                    if (CustomComponents.Companion.isJSONOk(jCustom, "section_id")) {
                        section_id = jCustom.getLong("section_id");
                        i.putExtra("section_id", section_id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            i.putExtra("notification_id", notifId);
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), notifId,
                    i, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),
                    getApplicationContext().getString(R.string.notif_channel_id));
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                    .setAutoCancel(true)
                    .setContentText(msg)
                    .setContentIntent(contentIntent);

            mNotificationManager.notify(notifId, notificationBuilder.build());
        }
    }
}