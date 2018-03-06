package demo_notification_reply.pulkit;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;

/**
 * Created by pulkit on 4/11/17.
 */

public class NotificationDisplayService extends Service {

    final int NOTIFICATION_ID = 16;
    public NotificationDisplayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        displayNotification("Testing","Notification 1");
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    private void displayNotification(String title, String text){

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra(getString(R.string.NOTIFICATION_ID_KEY), NOTIFICATION_ID);

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 1, notificationIntent, 0);

        Intent showToastIntent = new Intent(this, ShowToastService.class);
        showToastIntent.putExtra(getString(R.string.NOTIFICATION_ID_KEY), NOTIFICATION_ID);
        PendingIntent showToastPendingIntent = PendingIntent.getService(this, 2, showToastIntent, 0);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_android_white_36dp)
                //.setLargeIcon(BitmapFactory.decodeResource(R.drawable.xyz))
                .setColor(getResources().getColor(R.color.colorAccent))
                .setVibrate(new long[]{0, 300, 300, 300})
                //.setSound()
                .setLights(Color.WHITE, 1000, 5000)
                //.setWhen(System.currentTimeMillis())
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                //.addAction(R.drawable.ic_action_open ,"MainActivity öffnen", notificationPendingIntent)
                .addAction(R.drawable.ic_action_toast, "new Toast", showToastPendingIntent)
                .addAction(generateDirectReplyAction());

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification.build());
    }

    private NotificationCompat.Action generateDirectReplyAction() {

        if(Build.VERSION.SDK_INT >= 24){

            Intent directReplyIntent = new Intent(this, DirectReplyReceiveService.class);
            directReplyIntent.putExtra(getString(R.string.NOTIFICATION_ID_KEY), NOTIFICATION_ID);
            PendingIntent directReplyPendingIntent  = PendingIntent.getService(this, 5, directReplyIntent, 0);

            RemoteInput remoteInput = new RemoteInput.Builder(getString(R.string.DIRECT_REPLY_RESULT_KEY))
                    .setLabel("waiting...")
                    .build();

            NotificationCompat.Action directReplyAction = new NotificationCompat.Action.Builder(
                    R.drawable.ic_action_reply, "Reply Nougat", directReplyPendingIntent)
                    .addRemoteInput(remoteInput)
                    .build();
            return directReplyAction;

        }
        else {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.putExtra(getString(R.string.NOTIFICATION_ID_KEY), NOTIFICATION_ID);
            PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 1, notificationIntent, 0);

            NotificationCompat.Action directReplyAction = new NotificationCompat.Action.Builder(
                    R.drawable.ic_action_reply, "Reply Marshmallow",notificationPendingIntent )
                    .build();
            return directReplyAction;
        }

    }


}
